package io.github.toberocat.improvedfactions.utils.command

import io.github.toberocat.improvedfactions.translation.LocalizationKey

object CommandCategory {
    const val GENERAL_CATEGORY = "base.command.category.general"
    const val MANAGE_CATEGORY = "base.command.category.manage"
    const val MEMBER_CATEGORY = "base.command.category.members"
    const val INVITE_CATEGORY = "base.command.category.invites"
    const val CLAIM_CATEGORY = "base.command.category.claim"
    const val ADMIN_CATEGORY = "base.command.category.admin"
    const val PERMISSION_CATEGORY = "base.command.category.permissions"
    const val POWER_CATEGORY = "power.command.category.power"
    const val RELATIONS_CATEGORY = "relations.command.category"
}

@Target(AnnotationTarget.CLASS)
annotation class CommandMeta(
    val description: LocalizationKey,
    val category: LocalizationKey = CommandCategory.GENERAL_CATEGORY,
    val module: String = "core"
)

