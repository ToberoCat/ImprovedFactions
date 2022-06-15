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

    public double versionToInteger() {
        if (version.length() == 0) return 0;
        int n0 = Character.getNumericValue(version.charAt(0));

        return n0 + Integer.parseInt(Utility.removeNonDigits(version.substring(1))) * Math.pow(10, -version.length() - 1);
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public int compareTo(Version o) {
        double first = versionToInteger();
        double second = o.versionToInteger();

        if (first == second) return 0;
        else if (first < second) return -1;
        return 1;
    }

    @Override
    public String toString() {
        return version;
    }
}
