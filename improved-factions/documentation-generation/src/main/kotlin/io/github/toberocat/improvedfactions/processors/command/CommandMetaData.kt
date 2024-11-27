package io.github.toberocat.improvedfactions.processors.command

data class CommandMetaData(
    val qualifiedName: String,
    val permission: String,
    val description: String,
    val category: String,
    val module: String
)