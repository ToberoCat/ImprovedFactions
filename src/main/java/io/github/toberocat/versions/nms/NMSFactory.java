package io.github.toberocat.versions.nms;

import io.github.toberocat.versions.v1_13.NMSLoader_1_13;
import io.github.toberocat.versions.v1_14.NMSLoader_1_14;
import io.github.toberocat.versions.v1_15.NMSLoader_1_15;
import io.github.toberocat.versions.v1_16.NMSLoader_1_16;
import io.github.toberocat.versions.v1_17.NMSLoader_1_17;
import io.github.toberocat.versions.v1_18.NMSLoader_1_18;

/**
 * This allows the plugin to load different version for different minecraft versions
 */
public class NMSFactory {
    /**
     * A list of all mc versions that are represented in this version
     */
    public static String[] versions = new String[] {"1.18", "1.17", "1.16", "1.15", "1.14", "1.13"};

    public static NMSInterface create_1_18() { return new NMSLoader_1_18(); }
    public static NMSInterface create_1_17() {
        return new NMSLoader_1_17();
    }
    public static  NMSInterface create_1_16() {
        return new NMSLoader_1_16();
    }
    public static NMSInterface create_1_15() { return new NMSLoader_1_15(); }
    public static NMSInterface create_1_14() { return new NMSLoader_1_14(); }
    public static NMSInterface create_1_13() { return new NMSLoader_1_13(); }
}
