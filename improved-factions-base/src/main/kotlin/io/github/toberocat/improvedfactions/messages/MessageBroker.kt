package io.github.toberocat.improvedfactions.messages

import io.github.toberocat.improvedfactions.factions.LocalizedMessage

// ToDo: Implement a new message broker strategy
object MessageBroker {
    private val listenersLocalized = mutableMapOf<Int, MutableSet<(message: LocalizedMessage) -> Unit>>()

    fun send(id: Int, message: LocalizedMessage) {
        listenersLocalized[id]?.forEach { it.invoke(message) }
    }

    fun listenLocalized(id: Int, callback: (message: LocalizedMessage) -> Unit) {
        listenersLocalized.computeIfAbsent(id) { HashSet() }.add(callback);
    }
}