package io.github.toberocat.improvedfactions.annotations

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Permission(val value: String, val byDefault: Boolean = false)
