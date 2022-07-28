package io.github.toberocat.core.commands.factions;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionManager;
import io.github.toberocat.core.factions.components.rank.members.OwnerRank;
import io.github.toberocat.core.factions.handler.FactionHandler;
import io.github.toberocat.core.player.PlayerSettingHandler;
import io.github.toberocat.core.utility.Result;
import io.github.toberocat.core.utility.command.confirm.ConfirmSubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.date.DateCore;
import io.github.toberocat.core.utility.exceptions.faction.FactionIsFrozenException;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.core.utility.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.core.utility.exceptions.faction.leave.PlayerIsOwnerException;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.player.PlayerSettings;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

public class LeaveFactionSubCommand extends ConfirmSubCommand {
    public LeaveFactionSubCommand() {
        super("leave", "command.leave.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setNeedsFaction(SubCommandSettings.NYI.Yes);
    }

    @Override
    protected String confirmMessage(Player player) {
        return "command.faction.leave.confirm";
    }

    @Override
    protected void confirmExecute(Player player) {
        try {
            Faction<?> faction = FactionHandler.getFaction(player);
            leaveFaction(faction, player);
        } catch (FactionNotInStorage | PlayerHasNoFactionException e) {
            e.printStackTrace();
        } catch (FactionIsFrozenException e) {
            Language.sendMessage("command.faction.leave.frozen", player);
        } catch (PlayerIsOwnerException e) {
            Language.sendMessage("command.faction.leave.owner", player);
        }
    }

    private boolean leaveFaction(@NotNull Faction<?> faction, @NotNull Player player)
            throws FactionIsFrozenException, PlayerIsOwnerException {
        if (faction.isPermanent()) return leaveWithTimeout(faction, player);
        if (faction.getPlayerRank(player).getRegistryName().equals(OwnerRank.registry))
            throw new PlayerIsOwnerException(faction, player);

        return leaveWithTimeout(faction, player);
    }

    private boolean leaveWithTimeout(@NotNull Faction<?> faction, @NotNull Player player) throws FactionIsFrozenException {
        if (!faction.leavePlayer(player)) return false;

        int timeout =  MainIF.config().getInt("faction.joinTimeout");
        DateTime now = DateTime.now();
        now = now.plusHours(timeout);

        DateTimeFormatter fmt = DateCore.TIME_FORMAT;
        PlayerSettingHandler.getSettings(player.getUniqueId()).get("factionJoinTimeout").setSelected(fmt.print(now));
        return true;
    }
}
