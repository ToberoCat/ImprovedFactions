package io.github.toberocat.core.commands.extension;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.extensions.ExtensionObject;
import io.github.toberocat.core.extensions.list.ExtensionListLoader;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ExtensionRemoveSubCommand extends SubCommand {

    public static final LinkedList<String> PATHS_TO_DELETE = new LinkedList<>();

    public ExtensionRemoveSubCommand() {
        super("remove", "command.extension.remove.description", false);
    }

    @Override
    protected void commandExecute(Player player, String[] args) {
        ExtensionListLoader.getMap().then((map) -> {
            if (!map.containsKey(args[0])) {
                Language.sendRawMessage("Couldn't find extension you where searching for", player);
                return;
            }


            File path = new File(MainIF.getIF().getDataFolder().getPath() + "/Extensions/" + map.get(args[0]).getFileName() + ".jar");
            if (!path.exists()) {
                Language.sendRawMessage("Extension isn't installed", player);
                return;
            }
            PATHS_TO_DELETE.add(path.getPath());
            Language.sendRawMessage("Deleting the extension on server reload", player);
        });
    }

    @Override
    protected List<String> commandTab(Player player, String[] args) {
        return Arrays.stream(ExtensionListLoader.readExtensions().await())
                .map(ExtensionObject::getRegistryName).toList();
    }
}
