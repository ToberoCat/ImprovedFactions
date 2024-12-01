package io.github.toberocat.improvedfactions.commands.data

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

    fun getUsage(commandData: CommandData) =
        "${commandData.localizedCommandLabel}.arguments.${variableName}.usage"

    fun getDescription(commandData: CommandData) =
        "${commandData.localizedCommandLabel}.arguments.${variableName}.description"

    override fun toString(): String {
        return """CommandProcessFunctionParameter(index=$index, isManual=$isManual, isRequired=$isRequired, type="$type", variableName="$variableName", simpleName="$simpleName")"""
    }


}