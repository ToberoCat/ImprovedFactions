package io.github.toberocat.improvedfactions.database.storage

import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.block.Block

fun Chunk.claimKey() = ClaimKey(world.name, x, z)
fun Location.claimKey() = chunk.claimKey()
fun Block.claimKey() = chunk.claimKey()
