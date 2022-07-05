package io.github.toberocat.improvedfactions.extentions.list;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.toberocat.improvedfactions.extentions.Extension;
import io.github.toberocat.improvedfactions.extentions.ExtensionObject;
import io.github.toberocat.improvedfactions.language.Language;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ExtensionListLoader {

    private static ExtensionList extensionList = null;

    public static ExtensionList ReadList() {
        if (extensionList == null) {
            try {
                RegenerateExtensionList();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return extensionList;
    }

    public static void RegenerateExtensionList() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        extensionList = mapper.readValue(new URL("https://raw.githubusercontent.com/ToberoCat/ImprovedFaction/master/extensions.json"),
                ExtensionList.class);
    }

    public static ExtensionObject getExtenionObject(String name) {
        for (ExtensionObject obj : ReadList().getExtensionObjects()) {
            if (obj.getName().equals(name)) return obj;
        }
        return null;
    }

}
