package io.github.toberocat.improvedfactions.commands.contract

import io.github.toberocat.improvedfactions.annotations.permission.PermissionConfigurations
import io.github.toberocat.improvedfactions.commands.data.CommandData
import io.github.toberocat.improvedfactions.utils.localization

/**
 * Stable, test-facing description of a command emitted by the command KSP processor.
 *
 * It intentionally contains only facts available to code generation. Runtime state such as
 * enabled modules, faction permissions, or parser results belongs in integration tests.
 */
data class GeneratedCommandContract(
    val sourceClass: String,
    val label: String,
    val module: String,
    val categoryLocalizationKey: String,
    val descriptionLocalizationKey: String,
    val permission: GeneratedCommandPermission,
    val responses: List<GeneratedCommandResponse>,
    val routes: List<GeneratedCommandRoute>,
    val needsConfirmation: Boolean,
)

data class GeneratedCommandPermission(
    val node: String,
    val default: PermissionConfigurations,
)

data class GeneratedCommandResponse(
    val name: String,
    val localizationKey: String,
)

data class GeneratedCommandRoute(
    val senderType: String,
    val arguments: List<GeneratedCommandArgument>,
)

data class GeneratedCommandArgument(
    val index: Int,
    val name: String,
    val type: String,
    val required: Boolean,
    val manualParser: Boolean,
    val usageLocalizationKey: String,
    val descriptionLocalizationKey: String,
)

/** Converts the generator's internal model without reflection or runtime plugin state. */
fun CommandData.toGeneratedCommandContract() = GeneratedCommandContract(
    sourceClass = "$targetPackage.$targetName",
    label = label,
    module = module,
    categoryLocalizationKey = category,
    descriptionLocalizationKey = descriptionKey,
    permission = GeneratedCommandPermission(permission, permissionConfig),
    responses = responses.map { GeneratedCommandResponse(it.responseName, it.localization(this)) },
    routes = processFunctions.map { function ->
        GeneratedCommandRoute(
            senderType = function.senderClass,
            arguments = function.parameters.map { argument ->
                GeneratedCommandArgument(
                    index = argument.index,
                    name = argument.variableName,
                    type = argument.type,
                    required = argument.isRequired,
                    manualParser = argument.isManual,
                    usageLocalizationKey = argument.getUsage(this),
                    descriptionLocalizationKey = argument.getDescription(this),
                )
            },
        )
    },
    needsConfirmation = needsConfirmation,
)
