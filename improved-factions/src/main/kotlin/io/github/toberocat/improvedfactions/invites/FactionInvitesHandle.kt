package io.github.toberocat.improvedfactions.invites

import io.github.toberocat.improvedfactions.user.FactionUser

fun FactionUser.invites() = FactionInvite.find { FactionInvites.invitedId eq this@invites.id.value }