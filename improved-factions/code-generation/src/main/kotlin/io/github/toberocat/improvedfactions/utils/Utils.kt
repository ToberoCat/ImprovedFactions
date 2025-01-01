package io.github.toberocat.improvedfactions.utils

import com.google.devtools.ksp.KSTypeNotPresentException
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.symbol.KSAnnotated

@OptIn(KspExperimental::class)
inline fun <reified T : Annotation> KSAnnotated.getAnnotation(): T? {
    return try {
        getAnnotationsByType(T::class).firstOrNull()
    } catch (e: KSTypeNotPresentException) {
        null
    }
}

@OptIn(KspExperimental::class)
inline fun <reified T : Annotation> KSAnnotated.getAnnotations(): Sequence<T> {
    return try {
        getAnnotationsByType(T::class)
    } catch (e: KSTypeNotPresentException) {
        emptySequence()
    }
}

inline fun <reified T : Annotation> KSAnnotated.hasAnnotation(): Boolean {
    return annotations.any { it.shortName.asString() == T::class.simpleName }
}

fun String.capitalize() = replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }