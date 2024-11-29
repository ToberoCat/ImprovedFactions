package io.github.toberocat.improvedfactions.command.data

data class CommandProcessFunction(
    val senderClass: String,
    val functionName: String,
    val parameters: List<CommandProcessFunctionParameter>
) {
    fun totalArgumentCount(needsConfirmation: Boolean = false): Int {
        return parameters.size + if (needsConfirmation) 1 else 0
    }
}