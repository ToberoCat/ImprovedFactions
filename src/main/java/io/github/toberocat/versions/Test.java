package io.github.toberocat.versions;

import io.github.toberocat.core.extensions.ExtensionObject;
import io.github.toberocat.core.utility.json.JsonUtility;
import io.github.toberocat.core.utility.version.Version;
import org.bukkit.Material;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Test {
    //ToDo: Remove this class
    public static void main(String[] args) throws MalformedURLException {
        Map<Version, URL> map = new HashMap<>();
        map.put(Version.from("1.1"), new URL("https://example.com"));

        JsonUtility.SaveObject(new File("C:\\Users\\Tobias\\Documents\\extension.json"),
                new ExtensionObject("filename", "displayName", "description", map,
                        Version.from("v1.0"), "Tobero", Material.ACACIA_SAPLING));
    }
}
