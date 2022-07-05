package io.github.toberocat.improvedfactions.extentions.list;

import io.github.toberocat.improvedfactions.extentions.ExtensionObject;

import java.util.Arrays;

public class ExtensionList {
    private ExtensionObject[] extensionObjects;

    @Override
    public String toString() {
        return "ExtensionList{" +
                "extensionObjects=" + Arrays.toString(extensionObjects) +
                '}';
    }

    public ExtensionObject[] getExtensionObjects() {
        return extensionObjects;
    }

    public void setExtensionObjects(ExtensionObject[] extensionObjects) {
        this.extensionObjects = extensionObjects;
    }
}
