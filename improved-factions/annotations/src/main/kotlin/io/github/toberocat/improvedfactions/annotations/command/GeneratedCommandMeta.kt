package io.github.toberocat.improvedfactions.annotations.command


@Target(AnnotationTarget.CLASS)
annotation class GeneratedCommandMeta(
    val label: String,
    val responses: Array<CommandResponse>,
    val category: LocalizationKey,
    val module: String,
)