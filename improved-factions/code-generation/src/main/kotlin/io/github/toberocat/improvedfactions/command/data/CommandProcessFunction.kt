package io.github.toberocat.improvedfactions.command.data

data class CommandProcessFunction(
    val senderClass: String,
    val functionName: String,
    val parameters: List<CommandProcessFunctionParameter>,
)

data class CommandProcessFunctionParameter(
    val simpleName: String,
    val type: String,
    val index: Int,
    val isRequired: Boolean,
)