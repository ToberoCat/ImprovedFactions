package io.github.toberocat.improvedfactions.exceptions

import io.github.toberocat.toberocore.command.exceptions.CommandException
import org.bukkit.Chunk

class CantClaimThisChunkException(chunk: Chunk) : CommandException("base.exceptions.cant-claim-this-chunk", mapOf(
    "x" to chunk.x.toString(),
    "z" to chunk.z.toString()
))