package io.github.toberocat.improvedfactions.exceptions

import io.github.toberocat.improvedfactions.translation.LocalizedException
import org.bukkit.Chunk

class CantClaimThisChunkException(chunk: Chunk) : LocalizedException("base.exceptions.cant-claim-this-chunk", mapOf(
    "x" to chunk.x.toString(),
    "z" to chunk.z.toString()
))