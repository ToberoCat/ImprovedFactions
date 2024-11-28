package io.github.toberocat.improvedfactions.command.data

import io.github.toberocat.improvedfactions.annotations.CommandResponse
import io.github.toberocat.improvedfactions.utils.camlCaseToSnakeCase

data class CommandData(
    val targetPackage: String,
    val targetName: String,
    val label: String,
    val module: String,
    val responses: List<CommandResponse>,
    val processFunctions: List<CommandProcessFunction>
) {
    val localizedCommandLabel = "$module.commands.${label.camlCaseToSnakeCase(".")}"
    val descriptionKey = "$localizedCommandLabel.description"
    val permission = "factions.commands.${label.camlCaseToSnakeCase(".")}"
}