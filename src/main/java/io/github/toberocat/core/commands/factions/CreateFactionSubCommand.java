package io.github.toberocat.core.commands.factions;

import io.github.toberocat.core.utility.Result;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.factions.Faction;
import io.github.toberocat.core.utility.factions.FactionUtility;
import io.github.toberocat.core.utility.language.LangMessage;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parseable;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CreateFactionSubCommand extends SubCommand {
    public CreateFactionSubCommand() {
        super("create", LangMessage.COMMAND_FACTION_CREATE_DESCRIPTION, false);
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
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return List.of("Name");
    }

    private void CreateFaction(Player player, String _name) {
        String name = player.hasPermission("faction.colors.colorInFactionName")
                ? Language.format(_name) : _name;
        Result<Faction> factionResult = Faction.CreateFaction(name, player);
        if (!factionResult.isSuccess()) {
            SendCommandExecuteError(player, factionResult.getPlayerMessage());
            return;
        }
        Language.sendMessage(LangMessage.COMMAND_FACTION_CREATE_SUCCESS, player,
                new Parseable("{faction_name}", factionResult.getPaired().getDisplayName()));
    }
}
