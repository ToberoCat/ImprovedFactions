package io.github.toberocat.core.extensions;

import io.github.toberocat.core.utility.version.Version;

public record ExtensionRegistry(String displayName, Version version, Version minVersion,
                                Version[] testedVersions, String[] dependencies) {
    @Override
    public String displayName() {
        return displayName;
    }

    @Override
    public Version version() {
        return version;
    }

    @Override
    public Version minVersion() {
        return minVersion;
    }

    @Override
    public Version[] testedVersions() {
        return testedVersions;
    }

    @Override
    public String[] dependencies() {
        return dependencies;
    }
}
