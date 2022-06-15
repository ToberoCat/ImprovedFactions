package io.github.toberocat.core.utility.version;

public class UpdateChecker {
    private final Version version;
    private final Version newestVersion;

    public UpdateChecker(Version version, Version newestVersion) {
        this.version = version;
        this.newestVersion = newestVersion;
    }

    public Version getVersion() {
        return version;
    }

    public Version getNewestVersion() {
        return newestVersion;
    }

    public boolean isNewestVersion() {
        return version.versionToInteger() >= newestVersion.versionToInteger();
    }
}
