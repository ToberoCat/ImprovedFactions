package io.github.toberocat.improvedfactions.placeholder.visitor

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import io.github.toberocat.improvedfactions.annotations.papi.PapiPlaceholder
import io.github.toberocat.improvedfactions.utils.findAnnotations

class PlaceholderVisitor : KSVisitorVoid() {
    val placeholders = mutableListOf<PlaceholderData>()

    override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit) {
        val annotation = function.findAnnotations<PapiPlaceholder>() ?: return
        placeholders.addAll(annotation.map {
            PlaceholderData(it.value, it.module, it.description)
        })
    }
}

data class PlaceholderData(
    val value: String,
    val module: String,
    val description: String,
)
