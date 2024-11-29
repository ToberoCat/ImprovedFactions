package io.github.toberocat.improvedfactions.commands.executor

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.commands.admin.ByPassCommand
import io.github.toberocat.improvedfactions.commands.admin.ReloadCommand
import io.github.toberocat.improvedfactions.commands.admin.force.ForceCommandRoute
import io.github.toberocat.improvedfactions.commands.admin.zone.ZoneCommandRoute
import io.github.toberocat.improvedfactions.commands.claim.ClaimCommand
import io.github.toberocat.improvedfactions.commands.claim.FactionMap
import io.github.toberocat.improvedfactions.commands.claim.UnclaimCommand
import io.github.toberocat.improvedfactions.commands.general.GenerateWikiSourcesCommand
import io.github.toberocat.improvedfactions.commands.general.HelpCommand
import io.github.toberocat.improvedfactions.commands.general.InfoCommand
import io.github.toberocat.improvedfactions.commands.general.ListFactionsCommand
import io.github.toberocat.improvedfactions.commands.invite.InviteAcceptCommand
import io.github.toberocat.improvedfactions.commands.invite.InviteCommand
import io.github.toberocat.improvedfactions.commands.invite.InviteDiscardCommand
import io.github.toberocat.improvedfactions.commands.invite.ListInvitesCommand
import io.github.toberocat.improvedfactions.commands.manage.*
import io.github.toberocat.improvedfactions.commands.member.*
import io.github.toberocat.improvedfactions.commands.rank.RankCommandRoute
import io.github.toberocat.improvedfactions.translation.localizeUnformatted
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.toberocore.command.CommandExecutor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

/**
 * Created: 04.08.2023
 * @author Tobias Madlberger (Tobias)
 */
class FactionCommandExecutor(plugin: ImprovedFactionsPlugin) {
    init {
        val executor = CommandExecutor.createExecutor("factions")
        executor.setSendException { sender, exception ->
            if (sender !is Player)
                sender.sendMessage(Locale.ENGLISH.localizeUnformatted(exception.message ?: "base.exceptions.unknown", exception.placeholders))
            else
                exception.message?.let { sender.sendLocalized(it, exception.placeholders) }
        }
        executor.addChild(InfoCommand(plugin))

        //executor.addChild(CreateCommand(plugin))
        //executor.addChild(DeleteCommand(plugin))
        executor.addChild(IconCommand(plugin))
        executor.addChild(RenameCommand(plugin))
        executor.addChild(RankCommandRoute(plugin))
        executor.addChild(JoinTypeCommand(plugin))

        executor.addChild(JoinCommand(plugin))
        executor.addChild(LeaveCommand(plugin))
        executor.addChild(KickCommand(plugin))
        executor.addChild(BanCommand(plugin))
        executor.addChild(UnBanCommand(plugin))
        executor.addChild(MembersCommand(plugin))
        executor.addChild(TransferOwnershipCommand(plugin))

        executor.addChild(ClaimCommand(plugin))
        executor.addChild(UnclaimCommand(plugin))
        executor.addChild(FactionMap(plugin))

        executor.addChild(InviteCommand(plugin))
        executor.addChild(ListInvitesCommand(plugin))
        executor.addChild(InviteAcceptCommand(plugin))
        executor.addChild(InviteDiscardCommand(plugin))

        executor.addChild(ListFactionsCommand(plugin))
        executor.addChild(ReloadCommand(plugin))
        executor.addChild(ZoneCommandRoute(plugin))
        executor.addChild(ByPassCommand(plugin))
        executor.addChild(ForceCommandRoute(plugin))

        plugin.moduleManager.addModuleCommands(executor)

        executor.addChild(HelpCommand(plugin, executor))
        executor.addChild(GenerateWikiSourcesCommand(plugin, executor))

        executor.setNothing { sender, _ ->
            Bukkit.dispatchCommand(sender, "factions help")
        }
    }
}