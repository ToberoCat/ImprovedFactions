package io.github.toberocat.improvedFactions.core.utils.command;

import io.github.toberocat.improvedFactions.core.player.CommandSender;
import io.github.toberocat.improvedFactions.core.utils.command.exceptions.CommandHasParentException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class Command {

    protected final Map<String, SubCommand> children = new HashMap<>();
    protected @Nullable Command parent;
    private final @NotNull String permission;
    protected @Nullable String prefix;
    protected String label;

    public Command(@NotNull String permission, String label) {
        this.permission = permission;
        this.label = label;
    }

    //<editor-fold desc="Children Handling">
    public void addChild(@NotNull SubCommand command) {
        if (command.parent != null)
            throw new CommandHasParentException(command.label);

        command.parent = this;
        command.prefix = prefix;
        children.put(command.label, command);
    }

    public @NotNull Map<String, SubCommand> getChildren() {
        return children;
    }

    public abstract boolean showInTab(@NotNull CommandSender sender, @NotNull String[] args);

    //</editor-fold>

    //<editor-fold desc="Getters and Setters">

    public @Nullable Command getParent() {
        return parent;
    }

    public @NotNull String getPermission() {
        if (parent == null) return permission;
        return parent.getPermission() + "." + permission;
    }

    public @NotNull String getLabel() {
        return label;
    }

    public @NotNull List<String> childrenTabList(@NotNull CommandSender sender, @NotNull String[] args) {
        return children
                .values()
                .stream()
                .filter(x -> x.showInTab(sender, args))
                .map(x -> x.label)
                .toList();
    }

    //</editor-fold>
}
