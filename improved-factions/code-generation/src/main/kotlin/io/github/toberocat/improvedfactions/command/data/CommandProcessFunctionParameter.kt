package io.github.toberocat.improvedfactions.command.data

import java.util.*


data class CommandProcessFunctionParameter(
    val simpleName: String,
    val variableName: String,
    val type: String,
    val isRequired: Boolean,
    val isManual: Boolean,
    val index: Int,
) {
    val uniqueName = "${simpleName}${UUID.randomUUID().toString().replace("-", "")}"

    fun createVariableKey(commandData: CommandData) =
        "${commandData.localizedCommandLabel}.arguments.${variableName}.usage"
}