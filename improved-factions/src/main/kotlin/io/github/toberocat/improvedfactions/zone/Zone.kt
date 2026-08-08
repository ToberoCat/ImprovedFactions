package io.github.toberocat.improvedfactions.zone

data class Zone(val type: String,
                val noFactionTitle: String,
                val announceTitle: Boolean,
                val protectAlways: Boolean,
                val mapColor: Int,
                val allowClaiming: Boolean)
