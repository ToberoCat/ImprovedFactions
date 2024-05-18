package io.github.toberocat.improvedfactions.utils.options

import io.github.toberocat.improvedfactions.factions.Factions
import io.github.toberocat.toberocore.command.options.MaxArgLengthOption
import io.github.toberocat.toberocore.command.options.Options

fun Options.addFactionNameOption(index: Int): Options {
    cmdOpt(JoinedTextOption(index, Factions.maxSpacesInName))
    cmdOpt(MaxArgLengthOption(index, Factions.maxNameLength))
    cmdOpt(ArgRegexMatcherOption(index, Factions.nameRegex))
    return this
}