package io.github.toberocat.improvedfactions.commands.arguments.primitives.enums

import io.github.toberocat.improvedfactions.annotations.localization.Localization
import io.github.toberocat.improvedfactions.commands.arguments.primitives.EnumArgumentParser
import io.github.toberocat.improvedfactions.modules.power.PowerType

@Localization("base.arguments.power-type.description")
class PowerTypeArgumentParser : EnumArgumentParser<PowerType>(
    PowerType::class.java,
    "base.arguments.power-type.description"
)