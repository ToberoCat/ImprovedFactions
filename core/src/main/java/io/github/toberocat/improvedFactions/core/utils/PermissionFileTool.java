package io.github.toberocat.improvedFactions.core.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PermissionFileTool {
    private static final Map<String, Node> permissions = new HashMap<>();

    public static void addPermission(@NotNull String permission, boolean admin) {
        permissions.put(permission, new Node(admin ? "op" : "not op", null));
    }

    public static void bakeToFile(@NotNull File file) throws IOException {
        Map<String, Boolean> children = new HashMap<>();
        permissions.keySet().forEach(x -> children.put(x, true));

        permissions.put("faction.commands.*", new Node(null, children));

        Permissions perms = new Permissions(permissions);
        new ObjectMapper(new YAMLFactory()
                .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES))
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .writeValue(file, perms);
    }

    protected record Permissions(@NotNull Map<String, Node> permissions) {

    }

    protected record Node(@JsonProperty("default") String defaultItem, Map<String, Boolean> children) {

    }
}
