package io.github.toberocat.improvedfactions.commands.arguments.primitives.enums

import io.github.toberocat.improvedfactions.annotations.localization.Localization
import io.github.toberocat.improvedfactions.commands.arguments.primitives.EnumArgumentParser
import io.github.toberocat.improvedfactions.factions.FactionJoinType

class JoinTypeEnumArgumentParser : EnumArgumentParser<FactionJoinType>(FactionJoinType::class.java)