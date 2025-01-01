package io.github.toberocat.improvedfactions.permission.visitor

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import io.github.toberocat.improvedfactions.annotations.permission.Permission
import io.github.toberocat.improvedfactions.utils.findAnnotation

class PermissionVisitor : KSVisitorVoid() {
    val permissions = mutableListOf<Permission>()

    override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit) {
        val permissionAnnotation = function.findAnnotation<Permission>() ?: return
        permissions.add(permissionAnnotation)
    }
}