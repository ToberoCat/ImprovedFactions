package io.github.toberocat.improvedfactions.utils

import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.commands.data.CommandData

fun CommandResponse.localization(commandData: CommandData) = key.ifBlank {
    "${commandData.localizedCommandLabel}.${responseName.camlCaseToSnakeCase("-")}"
}