package io.github.toberocat.improvedfactions.extentions;

import java.net.URL;

public class ExtensionObject {
    private String name;
    private String description;
    private URL download;
    private String version;
    private String author;
    private String info;
    private String[] requiresVersion;
    private String[] dependencies;
    private String displayName;

    public ExtensionObject() {}

    public ExtensionObject(String name, String description, URL download) {
        this.name = name;
        this.description = description;
        this.download = download;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDependencies(String[] dependencies) {
        this.dependencies = dependencies;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setRequiresVersion(String[] requiresVersion) {
        this.requiresVersion = requiresVersion;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getAuthor() {
        return author;
    }

    public String getVersion() {
        return version;
    }

    public String getInfo() {
        return info;
    }

    public String[] getDependencies() {
        return dependencies;
    }

    public String[] getRequiresVersion() {
        return requiresVersion;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public URL getDownload() {
        return download;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDownload(URL download) {
        this.download = download;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ExtensionObject{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", download=" + download +
                '}';
    }
}
