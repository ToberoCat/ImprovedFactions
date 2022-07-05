package io.github.toberocat.improvedfactions.command;

import io.github.toberocat.core.utility.claim.ClaimManager;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class MassSubCommand extends SubCommand {

    public MassSubCommand(ClaimManager manager) {
        super("mass", "claim.mass", "command.claim-mass.description", true);
        subCommands.addAll(Arrays.asList(
                new MassClaimClaim(manager),
                new MassClaimUnclaim(manager),
                new MassClaimPos1(),
                new MassClaimPos2()
        ));
    }

    @Override
    protected void CommandExecute(Player player, String[] strings) {

    }

    @Override
    protected List<String> CommandTab(Player player, String[] strings) {
        return null;
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setNeedsFaction(SubCommandSettings.NYI.Yes);
    }
}
