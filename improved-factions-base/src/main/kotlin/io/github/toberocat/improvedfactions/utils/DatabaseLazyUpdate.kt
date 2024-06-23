package io.github.toberocat.improvedfactions.utils

class DatabaseLazyUpdate<T>(
    private val getCached: () -> T,
    private val setLazyUpdate: (value: Boolean) -> Unit,
    private val getLazyUpdate: () -> Boolean,
    private val updateCached: () -> Unit
) {
    fun get(): T {
        if (getLazyUpdate()) {
            forceUpdate()
        }
        return getCached()
    }

    fun scheduleUpdate() {
        setLazyUpdate(true)
    }

    private fun forceUpdate() {
        setLazyUpdate(false)
        updateCached()
    }
}