package io.github.toberocat.improvedfactions.localization.visitor

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.*
import io.github.toberocat.improvedfactions.annotations.Localization

class LocalizationVisitor(
    private val logger: KSPLogger,
    private val resolver: Resolver,
) : KSVisitorVoid() {

    val localizations = mutableListOf<String>()

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        if (!classDeclaration.annotations.any { it.shortName.asString() == Localization::class.simpleName }) return

        localizations.addAll(classDeclaration.annotations
            .filter { it.shortName.asString() == Localization::class.simpleName }
            .map { it.arguments.first().value as String })
    }

    override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit) {
        if (!function.annotations.any { it.shortName.asString() == Localization::class.simpleName }) return

        localizations.addAll(function.annotations
            .filter { it.shortName.asString() == Localization::class.simpleName }
            .map { it.arguments.first().value as String })
    }
}
