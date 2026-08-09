package io.github.toberocat.improvedfactions.database.storage

import com.zaxxer.hikari.HikariDataSource
import io.github.toberocat.improvedfactions.database.DatabaseSettings
import io.github.toberocat.improvedfactions.modules.power.config.PowerManagementConfig
import java.util.UUID
import java.util.concurrent.CompletionStage
import java.util.concurrent.TimeUnit
import java.sql.Connection
import java.util.logging.Level
import java.util.logging.Logger
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.RejectedExecutionException

object StorageManager {
    val cache = ClaimStateCache()

    private var dataSource: HikariDataSource? = null
    private var dispatcher: StorageDispatcher? = null
    private var repository: ClaimRepository? = null
    private var knownPlayerRepository: KnownPlayerRepository? = null
    private var jdbcRepository: JdbcStorageRepository? = null
    private var mainThreadContinuation: MainThreadContinuation? = null
    private var logger: Logger? = null
    private val closing = AtomicBoolean(true)
    private val snapshotListeners = java.util.concurrent.CopyOnWriteArrayList<() -> Unit>()

    fun start(
        settings: DatabaseSettings,
        dataSource: HikariDataSource,
        logger: Logger,
        scheduleOnMain: (Runnable) -> Unit
    ) {
        close()
        closing.set(false)
        this.dataSource = dataSource
        this.dispatcher = StorageDispatcher.create(settings.type, settings.mysqlMaximumPoolSize)
        this.knownPlayerRepository = KnownPlayerRepository(dataSource, settings.type)
        this.mainThreadContinuation = MainThreadContinuation(scheduleOnMain)
        this.logger = logger
    }

    fun initializeSnapshotRepository(
        powerConfig: PowerManagementConfig,
        guestRankName: String
    ): CompletionStage<GameStateSnapshot> {
        val currentDataSource = checkNotNull(dataSource) { "Storage has not been started" }
        val currentDispatcher = checkNotNull(dispatcher) { "Storage has not been started" }
        val loader = JdbcGameStateLoader(
            currentDataSource,
            RaidSnapshotConfig(powerConfig.allowOverclaim, powerConfig.claimPowerKeep, guestRankName)
        )
        repository = ClaimRepository(currentDispatcher, cache, loader::load)
        val currentRepository = checkNotNull(repository)
        jdbcRepository = JdbcStorageRepository(currentDataSource, currentDispatcher, currentRepository)
        return StorageBootstrap(currentRepository).loadAsync().also { bootstrap ->
            bootstrap.whenComplete { _, failure ->
                if (failure == null && !closing.get()) {
                    mainThreadContinuation?.execute(::notifySnapshotListeners)
                } else if (failure != null && !closing.get()) {
                    logger?.log(Level.SEVERE, "Unable to load initial storage snapshot", failure)
                }
            }
        }
    }

    fun requestRefresh(): CompletionStage<GameStateSnapshot>? = runCatching {
        repository?.refresh()?.also { refresh ->
            refresh.whenComplete { _, failure ->
                if (failure != null && !closing.get() &&
                    failure !is RejectedExecutionException && failure.cause !is RejectedExecutionException) {
                    logger?.log(Level.WARNING, "Storage cache refresh failed", failure)
                }
            }
        }
    }.onFailure { failure ->
        logger?.log(Level.WARNING, "Unable to schedule storage cache refresh", failure)
    }.getOrNull()

    fun invalidateAndRefresh() {
        requestRefresh()
    }

    fun persistKnownPlayer(uniqueId: UUID, name: String) {
        val currentDispatcher = dispatcher ?: return
        val currentRepository = knownPlayerRepository ?: return
        runCatching {
            currentDispatcher.submit { currentRepository.upsert(uniqueId, name) }
                .whenComplete { _, failure ->
                    if (failure == null) {
                        requestRefresh()
                    } else {
                        logger?.log(Level.WARNING, "Unable to persist known player $uniqueId", failure)
                    }
                }
        }.onFailure { failure ->
            logger?.log(Level.WARNING, "Unable to schedule known player persistence", failure)
        }
    }

    fun <T> read(query: (Connection) -> T): CompletionStage<T> =
        checkNotNull(jdbcRepository) { "Storage snapshot repository has not been initialized" }.read(query)

    fun <T> write(command: (Connection) -> T): CompletionStage<T> =
        checkNotNull(jdbcRepository) { "Storage snapshot repository has not been initialized" }.write(command).also { stage ->
            stage.whenComplete { _, failure ->
                if (failure == null && !closing.get()) {
                    mainThreadContinuation?.execute(::notifySnapshotListeners)
                }
            }
        }

    fun addSnapshotListener(listener: () -> Unit) {
        snapshotListeners += listener
    }

    private fun notifySnapshotListeners() = snapshotListeners.forEach { listener ->
        runCatching(listener).onFailure { logger?.log(Level.WARNING, "Snapshot listener failed", it) }
    }

    fun <T> continueOnMain(
        stage: CompletionStage<T>,
        success: (T) -> Unit,
        failure: (Throwable) -> Unit = { logger?.log(Level.WARNING, "Storage workflow failed", it) }
    ) = checkNotNull(mainThreadContinuation) { "Storage has not been started" }.resume(stage, success, failure)

    fun close() {
        closing.set(true)
        repository = null
        knownPlayerRepository = null
        jdbcRepository = null
        mainThreadContinuation = null
        snapshotListeners.clear()
        val currentDispatcher = dispatcher
        dispatcher = null
        if (currentDispatcher != null && !currentDispatcher.close(SHUTDOWN_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
            logger?.warning(
                "Timed out waiting for storage writes to finish during shutdown; remaining work was cancelled"
            )
        }
        dataSource?.close()
        dataSource = null
        cache.clear()
    }

    private const val SHUTDOWN_TIMEOUT_SECONDS = 10L
}
