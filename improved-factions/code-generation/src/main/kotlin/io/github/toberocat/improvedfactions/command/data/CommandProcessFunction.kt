package io.github.toberocat.improvedfactions.command.data

data class CommandProcessFunction(
    val senderClass: String,
    val functionName: String,
    val parameters: List<CommandProcessFunctionParameter>
) {
    fun requiredParametersCount() = parameters.count { it.isRequired }

    fun getMinParametersCount(needsConfirmation: Boolean = false): Int {
        return requiredParametersCount() + if (needsConfirmation) 1 else 0
    }

    fun getMaxParametersCount(needsConfirmation: Boolean = false): Int {
        return parameters.size + if (needsConfirmation) 1 else 0
    }
}