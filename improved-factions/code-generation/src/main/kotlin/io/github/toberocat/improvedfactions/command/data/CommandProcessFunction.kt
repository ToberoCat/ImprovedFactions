package io.github.toberocat.improvedfactions.command.data

import java.util.UUID

data class CommandProcessFunction(
    val senderClass: String,
    val functionName: String,
    val parameters: List<CommandProcessFunctionParameter>,
)

data class CommandProcessFunctionParameter(
    val simpleName: String,
    val variableName: String,
    val type: String,
    val isRequired: Boolean,
    val isManual: Boolean,
    val index: Int,
) {
    val uniqueName = "${simpleName}${UUID.randomUUID().toString().replace("-", "")}"
}