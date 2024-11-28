package io.github.toberocat.improvedfactions.translation

class LocalizedException(
    val key: String,
    val placeholders: Map<String, String> = emptyMap(),
) : Exception() {
    override val message: String
        get() = "LocalizedException(key='$key', placeholders=$placeholders)"
}