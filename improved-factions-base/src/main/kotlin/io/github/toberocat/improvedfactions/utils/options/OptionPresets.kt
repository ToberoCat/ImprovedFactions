package io.github.toberocat.improvedfactions.utils.options

import io.github.toberocat.improvedfactions.factions.Factions
import io.github.toberocat.toberocore.command.options.MaxArgLengthOption
import io.github.toberocat.toberocore.command.options.Options

fun Options.addFactionNameOption(index: Int): Options {
    opt(JoinedTextOption(index, Factions.maxSpacesInName))
    opt(MaxArgLengthOption(index, Factions.maxNameLength))
    opt(ArgRegexMatcherOption(index, Factions.nameRegex))
    return this
}