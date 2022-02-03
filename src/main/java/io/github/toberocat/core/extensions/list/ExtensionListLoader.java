package io.github.toberocat.core.extensions.list;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.toberocat.core.extensions.ExtensionObject;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.async.AsyncCore;
import io.github.toberocat.core.utility.callbacks.ResultCallback;
import io.github.toberocat.core.utility.version.Version;

import java.net.URL;

public class ExtensionListLoader {

    private static ExtensionObject[] extensionList;

    public static ExtensionObject[] readList() {
        if (extensionList == null) loadList((e) -> {});
        return extensionList;
    }

    public static void loadList(ResultCallback<ExtensionObject[]> callback) {
        Utility.run(() -> {
            ObjectMapper mapper = new ObjectMapper();
            extensionList = mapper.readValue(new URL("https://raw.githubusercontent.com/ToberoCat/ImprovedFaction/master/extensions.json"),
                    ExtensionObject[].class);

            callback.call(extensionList);
        });
    }

    public static void unloadList() {
        extensionList = null;
    }

    public static AsyncCore<Version> getExtensionVersion(String filename) {
        return AsyncCore.Run(() -> {
            boolean unload = false;
            if (extensionList == null) {
                unload = true;
                loadList((e) -> {});
            }

            for (ExtensionObject obj : extensionList) {
                if (obj.getFileName().equals(filename)){
                    return obj.getNewestVersion();
                }
            }

            if (unload) unloadList();

            return null;
        });

    }
}
