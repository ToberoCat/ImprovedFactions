package io.github.toberocat.improvedfactions.commands;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionModule;
import io.github.toberocat.core.factions.FactionUtility;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.improvedfactions.HomeExtension;
import io.github.toberocat.improvedfactions.module.HomeModule;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.List;

public class SetHomeSubCommand extends SubCommand {
    public SetHomeSubCommand() {
        super("sethome", "command.faction.sethome", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings()
                .setArgLength(0)
                .setNeedsFaction(SubCommandSettings.NYI.Yes)
                .setFactionPermission(HomeExtension.SET_HOME_PERMISSION);
    }

    @Override
    protected void CommandExecute(Player player, String[] strings) {
        Faction faction = FactionUtility.getPlayerFaction(player);

        Chunk chunk = player.getLocation().getChunk();

        String registry = MainIF.getIF().getClaimManager().getFactionRegistry(chunk);
        if (registry == null || !registry.equals(faction.getRegistryName())) {
            Language.sendMessage("command.faction.sethome.not-in-faction-claim", player);
            return;
        }


        if (!faction.getModules().containsKey(HomeExtension.HOME_MODULE_REGISTRY)) faction
                .getModules().put(HomeExtension.HOME_MODULE_REGISTRY,
                        new HomeModule(faction));

        FactionModule module = faction.getModules().get(HomeExtension.HOME_MODULE_REGISTRY);


        if (module instanceof HomeModule homeModule) {
            homeModule.setHome(player.getLocation());
            Language.sendMessage("command.faction.sethome.success", player);
        } else {
            Language.sendMessage("command.faction.sethome.module-not-set", player);
        }

    }

    @Override
    protected List<String> CommandTab(Player player, String[] strings) {
        return null;
    }
}
