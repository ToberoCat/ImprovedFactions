package io.github.toberocat.core.utility.version;

public record UpdateChecker(Version version,
                            Version newestVersion) {

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
