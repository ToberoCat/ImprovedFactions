package io.github.toberocat.improvedfactions.exceptions

import io.github.toberocat.improvedfactions.translation.LocalizedException
import org.bukkit.Chunk

class NotEnoughPowerForClaimException(chunk: Chunk) : LocalizedException("base.exceptions.not-enough-power-for-claim", mapOf(
    "x" to chunk.x.toString(),
    "z" to chunk.z.toString()
))
