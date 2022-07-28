package io.github.toberocat.core.utility.command;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.handler.FactionHandler;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.core.utility.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.core.utility.exceptions.setting.SettingNotFoundException;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parseable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public record SubSettingExecutor(boolean message, SubCommand subCommand,
                                 Player player,
                                 SubCommandSettings settings) {

    public boolean allowExecution(@NotNull String[] args)
            throws FactionNotInStorage, PlayerHasNoFactionException, SettingNotFoundException {
        if (settings.disableInNoneFactionWorlds() && Utility.isDisabled(player.getWorld())) return false;
        int required = settings.getArgLength();
        if (args.length < required)
            return sendMessage("command.too-less-args",
                    new Parseable("{required}", required),
                    new Parseable("{given}", args.length));
        if (args.length > required)
            return sendMessage("command.too-many-args",
                    new Parseable("{required}", required),
                    new Parseable("{given}", args.length));

        if (FactionHandler.isInFaction(player)) return playerInFaction();
        return playerNoFaction();
    }

    public boolean allowTab()
            throws FactionNotInStorage, PlayerHasNoFactionException, SettingNotFoundException {
        if (settings.disableInNoneFactionWorlds() && Utility.isDisabled(player.getWorld())) return false;
        if (FactionHandler.isInFaction(player)) return playerInFaction();
        return playerNoFaction();
    }

    private boolean playerInFaction() throws FactionNotInStorage, PlayerHasNoFactionException,
            SettingNotFoundException {
        Faction<?> faction = FactionHandler.getFaction(player);
        if (settings.getNeedsFaction() == SubCommandSettings.NYI.No)
            return sendMessage("command.arent-allowed-faction");

        if (faction.isFrozen() && !settings.isUseWhenFrozen())
            return sendMessage("command.faction-frozen");

        if (settings.getFactionPermission() != null &&
                !faction.hasPermission(player, settings.getFactionPermission()))
            return sendMessage("command.not-enough-faction-permission");
        return true;
    }

    private boolean playerNoFaction() {
        if (settings.getNeedsFaction() == SubCommandSettings.NYI.Yes)
            return sendMessage("command.requires-faction");
        return true;
    }

    private boolean sendMessage(@NotNull String msg, Parseable... parseables) {
        if (!message) return false;
        Language.sendMessage(msg, player, parseables);
        return false;
    }
}
