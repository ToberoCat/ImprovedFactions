package io.github.toberocat.improvedfactions.commands.arguments.primitives.enums

import io.github.toberocat.improvedfactions.annotations.localization.Localization
import io.github.toberocat.improvedfactions.commands.arguments.primitives.EnumArgumentParser
import io.github.toberocat.improvedfactions.factions.FactionJoinType

@Localization("base.arguments.joinType.usage")
@Localization("base.arguments.joinType.description")
class JoinTypeEnumArgumentParser : EnumArgumentParser<FactionJoinType>(
    FactionJoinType::class.java,
    "base.arguments.joinType.usage",
    "base.arguments.joinType.description",
)