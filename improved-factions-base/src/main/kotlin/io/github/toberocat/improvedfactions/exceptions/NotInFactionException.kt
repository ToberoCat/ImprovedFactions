package io.github.toberocat.improvedfactions.exceptions

import io.github.toberocat.toberocore.command.exceptions.CommandException

class NotInFactionException : CommandException("base.exceptions.need-faction", emptyMap()) {
}