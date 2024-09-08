package io.github.toberocat.improvedfactions.utils.arguments.entity

import io.github.toberocat.improvedfactions.invites.FactionInvite
import io.github.toberocat.improvedfactions.invites.FactionInvites
import io.github.toberocat.improvedfactions.user.factionUser
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder

class InviteIdArgument : EntityIdArgument<FactionInvite>(FactionInvite) {
    override fun collector(player: Player): SqlExpressionBuilder.() -> Op<Boolean> =
        { FactionInvites.invitedId eq player.factionUser().id.value }

    override fun descriptionKey(): String = "base.command.args.invite-id"
}