package io.github.toberocat.core.utility.command.radius;

import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.regex.Pattern;

public abstract class RadiusSubCommand extends SubCommand {

    private static final Pattern pattern = Pattern.compile("[0-9]");

    public RadiusSubCommand(String subCommand, String permission, String descriptionKey, boolean manager) {
        super(subCommand, permission, descriptionKey, manager);
    }

    public RadiusSubCommand(String subCommand, String descriptionKey, boolean manager) {
        super(subCommand, descriptionKey, manager);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings()
                .setArgLength(1);
    }

    public abstract List<String> suggestedRadius(@NotNull Player player, @NotNull String[] args);

    public abstract void radius(@NotNull Player player, int radius);

    @Override
    protected void commandExecute(Player player, String[] args) {
        String s = args[0];
        if (!pattern.matcher(s).matches()) return;

        radius(player, Integer.parseInt(s));
    }

    @Override
    protected List<String> commandTab(Player player, String[] args) {
        return suggestedRadius(player, args);
    }
}
