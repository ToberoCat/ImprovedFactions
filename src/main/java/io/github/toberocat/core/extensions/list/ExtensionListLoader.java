package io.github.toberocat.core.extensions.list;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.extensions.ExtensionObject;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.data.PluginInfo;
import io.github.toberocat.core.utility.jackson.JsonUtility;
import io.github.toberocat.core.utility.version.Version;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;

public class ExtensionListLoader {

    public static AsyncTask<HashMap<String, ExtensionObject>> getMap() {
        return AsyncTask.run(() -> {
            ExtensionList list = readList();
            if (list == null) return null;

            return list.map();
        });
    }

    public static AsyncTask<ExtensionObject[]> readExtensions() {
        return AsyncTask.run(() -> {
            ExtensionList list = readList();
            if (list == null) return null;

            return list.getExtensionObject();
        });
    }

    public static ExtensionList readList() {
        String path = MainIF.getIF().getDataFolder().getPath() + "/.temp/extension.json";
        File file = new File(path);

        ExtensionList list = null;
        try {
            if (!file.exists()) list = downloadExtensionList(file);
            else list = readFromFile(file);
        } catch (IOException e) {
            Utility.except(e);
        }

        if (list == null) return null;
        Version current = list.version();
        Version latest = Version.from(PluginInfo.fetch().getLatestExtensionRegistry());

        if (latest.versionToInteger() > current.versionToInteger()) {
            MainIF.logMessage(Level.INFO, "&aUpdating extension registry...");
            try {
                list = downloadExtensionList(file);
            } catch (IOException e) {
                Utility.except(e);
            }
        }

        return list;
    }

    private static ExtensionList readFromFile(File path) throws IOException {
        return JsonUtility.readObject(path, ExtensionList.class);
    }

    private static ExtensionList downloadExtensionList(File path) throws IOException {
        ExtensionObject[] array = JsonUtility.readObjectFromURL(
                new URL("https://raw.githubusercontent.com/ToberoCat/ImprovedFaction/master/v1_extensions.json"),
                ExtensionObject[].class);
        if (array == null) return null;

        ExtensionList list = new ExtensionList();
        list.setExtensionObject(array);
        list.setVersion(PluginInfo.fetch().getLatestExtensionRegistry());
        JsonUtility.saveObject(path, list);

        return list;
    }
}
