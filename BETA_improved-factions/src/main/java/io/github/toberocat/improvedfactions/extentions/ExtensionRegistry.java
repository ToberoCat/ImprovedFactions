package io.github.toberocat.improvedfactions.extentions;

public class ExtensionRegistry {

    private final String name;
    private final String version;
    private final String[] pluginVersions;

    public ExtensionRegistry(String name, String version, String[] pluginVersion) {
        this.name = name;
        this.version = version;
        this.pluginVersions = pluginVersion;
    }

    public String[] getPluginVersions() {
        return pluginVersions;
    }

    public String getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }
}
