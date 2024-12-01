package io.github.toberocat.improvedfactions.annotations.permission

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Permission(
    val value: String,
    val config: PermissionConfigurations = PermissionConfigurations.ALL
)