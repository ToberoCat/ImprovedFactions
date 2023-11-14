package io.github.toberocat.improvedfactions.claims

import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.block.Block
import org.jetbrains.exposed.sql.and

fun Block.getFactionClaim(): FactionClaim? = chunk.getFactionClaim()
fun Location.getFactionClaim(): FactionClaim? = chunk.getFactionClaim()

fun Chunk.getFactionClaim(): FactionClaim? =
    FactionClaim.find {
        FactionClaims.chunkX eq x and
                (FactionClaims.chunkZ eq z) and
                (FactionClaims.world eq world.name)
    }
        .firstOrNull()