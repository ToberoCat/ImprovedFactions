# Testing

Run commands from `improved-factions/`:

```shell
./gradlew unitTest
./gradlew integrationTest
./gradlew test
./gradlew databaseTest
```

If a regular `build/` directory is not writable, redirect every project's outputs to isolated subdirectories:

```shell
./gradlew test -PtestingBuildDir=/tmp/improved-factions-build
```

- `unitTest` runs tests tagged `unit` and does not start MockBukkit or Docker.
- `integrationTest` runs tests tagged `integration`, including MockBukkit and embedded H2/SQLite tests, but excludes external databases.
- `test` is the regular local/CI suite: all unit and integration tests except tests tagged `database`.
- `databaseTest` is the explicit MariaDB suite. The migration test starts `mariadb:10.3.39` with Testcontainers. JUnit reports it as skipped when no Docker-compatible runtime is available. The two legacy repository/loader checks run only when `MARIADB_TEST_URL` is set.

The tasks all use the same `src/test` output. A test is therefore compiled once; task selection is done with JUnit tags rather than duplicate source sets. Use the composed annotations in `io.github.toberocat.improvedfactions.testing`: `@UnitTest`, `@IntegrationTest`, and `@DatabaseTest`.

## Command testing

Command tests are deliberately split into three levels. A processor's `process` function is an implementation detail; command behavior should normally be tested through the generated contract or the real Bukkit entry point.

1. **Contract/unit:** KSP emits `generatedCommandContracts` and `generatedCommandContractsByLabel` beside `FactionCommandProcessors`. These typed production objects need no plugin boot and contain only compile-time facts: source class, label, module, category/description localization keys, Bukkit permission/default, declared and generated-default responses, sender routes, argument name/type/position/required/manual-parser metadata, argument localization keys, and confirmation. `GeneratedCommandContractTest` checks uniqueness, route shape, canonical localization, and `plugin.yml` permission consistency.
2. **Executor integration:** `command("/f ...")` dispatches through MockBukkit, the bound `CommandExecutor`, permission checks, route selection, generated argument parsers, and localization. The DSL can assert exact localized components and use Bukkit tab completion, including plugin aliases.
3. **End to end:** create players/factions/world state, dispatch the Bukkit command, call `awaitStorage()`, and assert the published storage state or eventual world effect. These tests prove the async persistence boundary as well as command routing.

The contract registry is generated, not reconstructed with reflection. When a command annotation or `process...` signature changes, its contract changes in the same KSP run as its processor. Do not add fields for runtime facts that KSP cannot know; test active modules, parser results, rank permissions, and storage effects at the integration level.

```kotlin
class BanRoutingTest : FactionsIntegrationTest() {
    @Test
    fun `permission is checked before parsing`() {
        val owner = player("Owner")

        command("/f ban Target")
            .asPlayer(owner)
            .withoutPermission() // inferred from the generated `ban` contract
            .run()
            .expectHandled(false)
            .expectLocalizedResponse("base.commands.cant-execute")
    }

    @Test
    fun `invite is persisted`() {
        val owner = player("Owner")
        val target = player("Target")
        faction(owner.uniqueId)

        command("/fac invite ${target.name} Member")
            .asPlayer(owner)
            .run()
            .expectHandled()
            .awaitStorage()

        assertTrue(StorageManager.cache.invites(target.uniqueId).isNotEmpty())
    }
}
```

Other useful assertions are `expectDeclaredResponse(commandLabel, responseName)`, `expectMessageContaining`, and `complete().expectContains(...)`. Call `withLocale(Locale(...))` after `asPlayer` to exercise locale selection and fallback.

The baseline command matrix does not execute every route with empty arguments: some valid empty routes claim chunks, teleport players, or mutate configuration. Instead it compares all active generated contracts with the processors registered in the real executor, while explicit safe tests cover permission denial, invalid arguments, responses, aliases/completion, and representative `ban`, `home`, `claim`, and `invite` flows.

## MockBukkit fixture

Extend `FactionsIntegrationTest` for a loaded plugin, isolated MockBukkit server, and storage lifecycle cleanup:

```kotlin
class InviteTest : FactionsIntegrationTest() {
    @Test
    fun `owner can invite a player`() = scenario {
        val owner = player("Owner")
        val guest = player("Guest")
        faction(owner.uniqueId)

        execute(owner, "/f invite ${guest.name} Member")
        awaitStorage()
        ticks(2)

        // assertions against StorageManager.cache or player output
    }
}
```

`player`, `world`, `faction`, `execute`, `awaitStorage`, and `ticks` keep asynchronous storage and scheduler handling consistent. Completion stages can use the fixture's bounded `await()` extension. Avoid unbounded `Future.get()` and fixed sleeps; use a bounded await that asserts the observable result.

## MariaDB

Docker is intentionally never touched by `test`, `unitTest`, or `integrationTest`. Run `databaseTest` explicitly on a host with a Docker-compatible runtime. The container test uses a fresh database and applies the MySQL migration twice to verify idempotency against MariaDB 10.3.

For the remaining externally managed MariaDB checks:

```shell
MARIADB_TEST_URL='jdbc:mariadb://localhost:3306/improved_factions_test' \
MARIADB_TEST_USER='root' \
MARIADB_TEST_PASSWORD='secret' \
./gradlew databaseTest
```
