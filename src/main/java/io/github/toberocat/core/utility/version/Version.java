package io.github.toberocat.core.utility.version;

import io.github.toberocat.core.utility.Utility;

public class Version implements Comparable<Version> {
    private String version;

    public Version() {}
    private Version(String version) {
        this.version = version;
    }

    public static Version from(String version) {
        return new Version(version);
    }

    public int versionToInteger() {
        return Integer.parseInt(Utility.removeNonDigits(version));
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public int compareTo(Version o) {
        int first = versionToInteger();
        int second = o.versionToInteger();

        if (first == second) return 0;
        else if (first < second) return -1;
        return 1;
    }

    @Override
    public String toString() {
        return version;
    }
}
