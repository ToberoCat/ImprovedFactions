package io.github.toberocat.improvedfactions.messages

import io.github.toberocat.improvedfactions.factions.LocalizedMessage

// ToDo: Implement a new message broker strategy
object MessageBroker {
    private val listeners = mutableMapOf<Int, MutableSet<(message: String) -> Unit>>()
    private val listenersLocalized = mutableMapOf<Int, MutableSet<(message: LocalizedMessage) -> Unit>>()

    fun send(id: Int, message: String) {
        listeners[id]?.forEach { it.invoke(message) }
    }

    fun send(id: Int, message: LocalizedMessage) {
        listenersLocalized[id]?.forEach { it.invoke(message) }
    }

    fun listenMsg(id: Int, callback: (message: String) -> Unit) {
        listeners.computeIfAbsent(id) { HashSet() }.add(callback);
    }

    fun listenLocalized(id: Int, callback: (message: LocalizedMessage) -> Unit) {
        listenersLocalized.computeIfAbsent(id) { HashSet() }.add(callback);
    }
}