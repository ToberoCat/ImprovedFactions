package io.github.toberocat.core.commands.factions;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionManager;
import io.github.toberocat.core.factions.handler.FactionHandler;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parseable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WhoSubCommand extends SubCommand {
    public WhoSubCommand() {
        super("who", "command.who.descriptions", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setUseWhenFrozen(true);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        String factionRegistry = getRegistry(player, args);
        if (factionRegistry == null) return;

        try {
            displayWho(FactionManager.getFactionByRegistry(factionRegistry), player);
        } catch (FactionNotInStorage e) {
            Language.sendMessage("command.who.faction-not-found", player);
        }
    }

    private void displayWho(@NotNull Faction<?> faction, @NotNull Player player) {
        player.sendMessage(Language.getLore("command.who.info", player,
                Parseable.of("display", faction.getDisplay()),
                Parseable.of("registry", faction.getRegistry()),
                Parseable.of("motd", faction.getMotd()),
                Parseable.of("owner", Bukkit.getOfflinePlayer(faction.getOwner()).getName()),
                Parseable.of("created-at", faction.getCreatedAt()),
                Parseable.of("members-online", String.valueOf(FactionManager.onlineMembers(faction).count())),
                Parseable.of("members-total", String.valueOf(faction.getMembers().count())),
                Parseable.of("power", String.valueOf(faction.getPower())),
                Parseable.of("max-power", String.valueOf(faction.getMaxPower())),
                Parseable.of("claims", String.valueOf(faction.getClaims().getTotal())),
                Parseable.of("allies", String.valueOf(faction.getAllies().count())),
                Parseable.of("enemies", String.valueOf(faction.getEnemies().count())),
                Parseable.of("description", String.join("\n", faction.getDescription().getLines().toList()))
        ));
    }

    private @Nullable String getRegistry(@NotNull Player player, @NotNull String[] args) {
        if (args.length == 1) return args[0];

        if (FactionHandler.isInFaction(player))
            return FactionHandler.getPlayerFactionRegistry(player);

        Language.sendMessage("command.who.no-faction", player);
        return null;
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return FactionHandler.getAllFactions().toList();
    }
}
