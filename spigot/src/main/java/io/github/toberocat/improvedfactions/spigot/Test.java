package io.github.toberocat.improvedfactions.spigot;

import io.github.toberocat.improvedFactions.core.utils.PermissionFileTool;

import java.io.File;

public class Test {
    public static void main(String[] args) throws Exception {
        PermissionFileTool.addPermission("faction.command.create", true);
        PermissionFileTool.bakeToFile(new File("C:\\Users\\Tobias\\Downloads\\permissions.yml"));
    }
}
