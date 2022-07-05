package io.github.toberocat.improvedfactions.commands;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionModule;
import io.github.toberocat.core.factions.FactionUtility;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parseable;
import io.github.toberocat.improvedfactions.HomeExtension;
import io.github.toberocat.improvedfactions.module.HomeModule;
import io.github.toberocat.improvedfactions.teleport.HomeTeleport;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class HomeSubCommand extends SubCommand {
    public HomeSubCommand() {
        super("home", "command.faction.home", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings()
                .setArgLength(0)
                .setNeedsFaction(SubCommandSettings.NYI.Yes)
                .setFactionPermission(HomeExtension.HOME_PERMISSION);
    }

    @Override
    protected void CommandExecute(Player player, String[] strings) {
        Faction faction = FactionUtility.getPlayerFaction(player);

        if (!faction.getModules().containsKey(HomeExtension.HOME_MODULE_REGISTRY)) faction
                .getModules().put(HomeExtension.HOME_MODULE_REGISTRY,
                        new HomeModule(faction));

        FactionModule module = faction.getModules().get(HomeExtension.HOME_MODULE_REGISTRY);

        if (module instanceof HomeModule homeModule) {
            Location home = homeModule.getHome();
            if (home == null) {
                Language.sendMessage("command.faction.home.no-home", player);
                return;
            }

            int seconds = HomeExtension.SECONDS_TILL_TELEPORT;
            if (seconds <= 0) HomeTeleport.teleport(player, home);

            Parseable defaultSecondsParse = new Parseable("{seconds}", ""+seconds);
            if (HomeExtension.RESET_ON_MOVE)
                Language.sendMessage("command.faction.home.teleport-scheduled.no-move", player,
                        defaultSecondsParse);
            else
                Language.sendMessage("command.faction.home.teleport-scheduled.default", player,
                        defaultSecondsParse);

            new HomeTeleport(player, home, seconds);
        } else {
            Language.sendMessage("command.faction.home.module-not-set", player);
        }


    }

    @Override
    protected List<String> CommandTab(Player player, String[] strings) {
        return null;
    }
}
