package io.github.toberocat.improvedfactions.commands.data

import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.permission.PermissionConfigurations
import io.github.toberocat.improvedfactions.utils.camlCaseToSnakeCase
import io.github.toberocat.improvedfactions.utils.labelToSnakeCase
import io.github.toberocat.improvedfactions.utils.toKotlinString

data class CommandData(
    val targetPackage: String,
    val targetName: String,
    val label: String,
    val module: String,
    val category: String,
    val responses: List<CommandResponse>,
    val processFunctions: List<CommandProcessFunction>,
    val needsConfirmation: Boolean,
    val permissionConfig: PermissionConfigurations,
    val addPluginAsParameter: Boolean,
) {
    val localizedCommandLabel = "$module.commands.${label.labelToSnakeCase("-")}"
    val descriptionKey = "$localizedCommandLabel.description"
    val permission = "factions.commands.${label.labelToSnakeCase("-")}"

    override fun toString(): String {
        return """CommandData(
            |targetPackage="$targetPackage",
            |targetName="$targetName", 
            |label="$label", 
            |module="$module", 
            |category="$category",
            |responses=${responses.map { it.toKotlinString() }.toKotlinString()}, 
            |processFunctions=${processFunctions.toKotlinString()}, 
            |needsConfirmation=$needsConfirmation, 
            |permissionConfig=PermissionConfigurations.$permissionConfig, 
            |addPluginAsParameter=$addPluginAsParameter, 
            |)
         """.trimMargin()
    }


}