package io.github.toberocat.core.commands.factions;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionManager;
import io.github.toberocat.core.utility.Result;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parseable;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreateFactionSubCommand extends SubCommand {

    private final ArrayList<UUID> PLAYER_WARNINGS_LENGTH = new ArrayList<>();
    private final ArrayList<UUID> PLAYER_WARNINGS_COLOR = new ArrayList<>();

    public CreateFactionSubCommand() {
        super("create", "command.faction.create.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setNeedsFaction(SubCommandSettings.NYI.No).setArgLength(1);
    }

    @Override
    protected void commandExecute(Player player, String[] args) {

        if (!FactionManager.doesFactionExist(FactionManager.factionDisplayToRegistry(args[0]))) {
            createFaction(player, args[0]);
        } else {
            Language.sendMessage("command.faction.create.failed", player,
                    new Parseable("{error}", "The faction already exists"));
        }

        PLAYER_WARNINGS_LENGTH.remove(player.getUniqueId());
    }

    @Override
    protected List<String> commandTab(Player player, String[] args) {
        if (args.length <= 1) {
            UUID id = player.getUniqueId();
            String display = args[0];
            int maxLen = MainIF.getConfigManager().getValue("faction.maxNameLen");
            if (!PLAYER_WARNINGS_LENGTH.contains(id) && display.length() >= maxLen) {
                Language.sendRawMessage("&cYou have reached the maximum name length of &e" + maxLen, player);
                PLAYER_WARNINGS_LENGTH.add(id);
            } else if (PLAYER_WARNINGS_LENGTH.contains(id) && display.length() < maxLen) {
                Language.sendRawMessage("&aYou are back in the limitation", player);
                PLAYER_WARNINGS_LENGTH.remove(id);
            }

            if (display.matches("&[0-9a-f]")) {
                if (PLAYER_WARNINGS_COLOR.contains(id)) {
                    Language.sendMessage("command.faction.create.no-color-perms", player);
                    PLAYER_WARNINGS_COLOR.add(id);
                }
            }
        }

        if (args.length >= 2) {
            Language.sendRawMessage("&cYou can't use spaces in a faction name", player);
        }
        return List.of("Name");
    }

    private void createFaction(Player player, String _name) {
        String name = player.hasPermission("faction.colors.colorInFactionName")
                ? Language.format(_name) : _name;
        Result<Faction> factionResult = Faction.createFaction(name, player);
        if (!factionResult.isSuccess()) sendCommandExecuteError(factionResult.getPlayerMessage(), player);
    }
}
