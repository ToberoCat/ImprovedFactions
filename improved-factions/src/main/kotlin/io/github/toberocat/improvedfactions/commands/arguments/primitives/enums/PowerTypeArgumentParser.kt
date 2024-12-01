package io.github.toberocat.improvedfactions.commands.arguments.primitives.enums

import io.github.toberocat.improvedfactions.commands.arguments.primitives.EnumArgumentParser
import io.github.toberocat.improvedfactions.modules.power.PowerType

class PowerTypeArgumentParser : EnumArgumentParser<PowerType>(PowerType::class.java)