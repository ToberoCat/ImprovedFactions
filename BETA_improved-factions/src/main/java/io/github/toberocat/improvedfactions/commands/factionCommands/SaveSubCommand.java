package io.github.toberocat.improvedfactions.commands.factionCommands;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.language.LangMessage;
import io.github.toberocat.improvedfactions.language.Language;
import io.github.toberocat.improvedfactions.utility.ChunkUtils;
import org.bukkit.entity.Player;

import java.util.List;

public class SaveSubCommand extends SubCommand {
    public SaveSubCommand() {
        super("save", "save", LangMessage.SAVE_DESCRIPTION);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        Faction.SaveFactions(ImprovedFactionsMain.getPlugin());
        ChunkUtils.Save();
        player.sendMessage(Language.getPrefix() + "§fSaved faction data");
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
