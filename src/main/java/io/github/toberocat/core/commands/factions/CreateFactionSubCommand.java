package io.github.toberocat.core.commands.factions;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.Result;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.factions.Faction;
import io.github.toberocat.core.utility.factions.FactionUtility;
import io.github.toberocat.core.utility.language.LangMessage;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parseable;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreateFactionSubCommand extends SubCommand {

    private ArrayList<UUID> PLAYER_WARNINGS;

    public CreateFactionSubCommand() {
        super("create", LangMessage.COMMAND_FACTION_CREATE_DESCRIPTION, false);
        PLAYER_WARNINGS = new ArrayList<>();
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setNeedsFaction(SubCommandSettings.NYI.No).setArgLength(1);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {

        if(!FactionUtility.doesFactionExist(FactionUtility.factionDisplayToRegistry(args[0]))) {
            CreateFaction(player, args[0]);
        } else {
            Language.sendMessage(LangMessage.COMMAND_FACTION_CREATE_FAILED, player,
                    new Parseable("{error}", "The faction already exists"));
        }

        PLAYER_WARNINGS.remove(player.getUniqueId());
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        if (args.length <= 1) {
            UUID id = player.getUniqueId();
            int maxLen = MainIF.getConfigManager().getValue("faction.maxNameLen");
            if (!PLAYER_WARNINGS.contains(id) && args[0].length() >= maxLen) {
                Language.sendRawMessage("&cYou have reached the maximum name length of &e" + maxLen, player);
                PLAYER_WARNINGS.add(id);
            } else if (PLAYER_WARNINGS.contains(id) && args[0].length() < maxLen) {
                Language.sendRawMessage("&aYou are back in the limitation", player);
                PLAYER_WARNINGS.remove(id);
            }
        }

        if (args.length >= 2) {
            Language.sendRawMessage("&cYou can't use spaces in a faction name", player);
        }
        return List.of("Name");
    }

    private void CreateFaction(Player player, String _name) {
        String name = player.hasPermission("faction.colors.colorInFactionName")
                ? Language.format(_name) : _name;
        Result<Faction> factionResult = Faction.CreateFaction(name, player);
        if (!factionResult.isSuccess()) {
            sendCommandExecuteError(factionResult.getPlayerMessage(), player);
            return;
        }
        Language.sendMessage(LangMessage.COMMAND_FACTION_CREATE_SUCCESS, player,
                new Parseable("{faction_name}", factionResult.getPaired().getDisplayName()));
    }
}
