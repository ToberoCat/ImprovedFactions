package io.github.toberocat.improvedfactions.annotations

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@Repeatable
@Retention(AnnotationRetention.SOURCE)
annotation class Localization(val key: String)
