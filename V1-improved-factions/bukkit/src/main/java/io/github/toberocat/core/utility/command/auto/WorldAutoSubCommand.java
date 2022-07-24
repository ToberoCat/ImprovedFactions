package io.github.toberocat.core.utility.command.auto;

import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.regex.Pattern;

public abstract class WorldAutoSubCommand extends SubCommand {

    private static final Pattern pattern = Pattern.compile("[0-9]");

    public WorldAutoSubCommand(String subCommand, String permission, String descriptionKey, boolean manager) {
        super(subCommand, permission, descriptionKey, manager);
    }

    public WorldAutoSubCommand(String subCommand, String descriptionKey, boolean manager) {
        super(subCommand, descriptionKey, manager);
    }

    public abstract String getEnabledKey();

    public abstract String getDisabledKey();

    public abstract List<String> suggestedRadius(@NotNull Player player, @NotNull String[] args);

    public abstract void single(@NotNull Player player, @NotNull Chunk action);

    @Override
    protected void commandExecute(Player player, String[] args) {
        if (args.length == 0) single(player, player.getLocation().getChunk());
        else if (args.length == 1) {
            String arg = args[0];
            switch (arg) {
                case "one": single(player, player.getLocation().getChunk());
                case "auto": autoCommand(player);
                default: radiusOperation(player, arg);
            }
        } else {
            sendCommandExecuteError(CommandExecuteError.ToManyArgs, player);
        }
    }

    private void autoCommand(@NotNull Player player) {

    }

    private void radiusOperation(@NotNull Player player, @NotNull String s) {
        if (!pattern.matcher(s).matches()) {
            Language.sendMessage("command.auto-radius.no-valid-radius", player);
            return;
        }

        int radius = Integer.parseInt(s);

        int centerX = player.getLocation().getBlockX();
        int centerZ = player.getLocation().getBlockZ();

        for (int i = -radius; i < radius; i++)
            for (int j = -radius; j < radius; j++)
                single(player, player.getWorld().getChunkAt(centerX + i, centerZ + j));
    }

    @Override
    protected List<String> commandTab(Player player, String[] args) {
        List<String> tab = suggestedRadius(player, args);
        tab.add("auto");
        tab.add("one");

        return tab;
    }
}
