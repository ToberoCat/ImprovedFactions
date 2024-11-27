package io.github.toberocat.improvedfactions.annotations

@Target(AnnotationTarget.FUNCTION)
annotation class CommandResponse(
    val responseName: String,
    val key: LocalizationKey = "",
)