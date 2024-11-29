package io.github.toberocat.improvedfactions.annotations.command

@Target(AnnotationTarget.FUNCTION)
annotation class CommandResponse(
    val responseName: String,
    val key: LocalizationKey = "",
)