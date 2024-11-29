package io.github.toberocat.improvedfactions.commands.executor

import io.github.toberocat.improvedfactions.commands.CommandProcessor

data class CompletionLevelNode(
    val children: MutableMap<String, CompletionLevelNode> = mutableMapOf(),
) {
    fun insert(commandProcessor: CommandProcessor) = insert(commandProcessor.label.split(" "), 0)

    private fun insert(args: List<String>, index: Int) {
        if (index == args.size) return

        val nextNode = children.getOrPut(args[index]) { CompletionLevelNode() }
        nextNode.insert(args, index + 1)
    }

    fun getCompletions(args: List<String>): List<String> {
        if (args.isEmpty()) return children.keys.toList()

        val nextNode = children[args[0]] ?: return children.keys.toList()
        return nextNode.getCompletions(args.drop(1))
    }
}