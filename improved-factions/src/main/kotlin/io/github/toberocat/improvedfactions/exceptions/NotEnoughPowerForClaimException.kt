package io.github.toberocat.improvedfactions.exceptions

import io.github.toberocat.toberocore.command.exceptions.CommandException
import org.bukkit.Chunk

class NotEnoughPowerForClaimException(chunk: Chunk) : CommandException("base.exceptions.not-enough-power-for-claim", mapOf(
    "x" to chunk.x.toString(),
    "z" to chunk.z.toString()
))