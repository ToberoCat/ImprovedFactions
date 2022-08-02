package io.github.toberocat.core.extensions.list;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.toberocat.core.extensions.ExtensionObject;
import io.github.toberocat.core.utility.version.Version;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ExtensionList {
    private Version version;
    private LinkedHashMap<String, ExtensionObject> extensionObject;

    public String getVersion() {
        return version.getVersion();
    }

    public void setVersion(String version) {
        this.version = Version.from(version);
    }

    public ExtensionObject[] getExtensionObject() {
        return extensionObject.values().toArray(ExtensionObject[]::new);
    }

    public void setExtensionObject(ExtensionObject[] objs) {
        this.extensionObject = new LinkedHashMap<>();
        for (ExtensionObject object : objs) extensionObject.put(object.getRegistryName(), object);
    }

    @JsonIgnore
    public LinkedHashMap<String, ExtensionObject> map() {
        return extensionObject;
    }

    @JsonIgnore
    public Version version() {
        return version;
    }
}
