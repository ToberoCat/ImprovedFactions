package io.github.toberocat.core.extensions;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.toberocat.core.utility.version.Version;

import java.util.Objects;

public final class ExtensionRegistry {
    private String main;
    private String displayName;
    private String registry;
    private String jarFile;
    private String author;
    private Version version;
    private Version minVersion;
    private Version[] testedVersions;
    private String[] dependencies;
    private String[] extensionDependencies;

    public ExtensionRegistry() {

    }

    @JsonProperty("extension-dependencies")
    public String[] extensionDependencies() {
        return extensionDependencies;
    }

    public String main() {
        return main;
    }

    public String displayName() {
        return displayName;
    }

    public String registry() {
        return registry;
    }

    public Version version() {
        return version;
    }

    public Version minVersion() {
        return minVersion;
    }

    public Version[] testedVersions() {
        return testedVersions;
    }

    public String[] dependencies() {
        return dependencies;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setRegistry(String registry) {
        this.registry = registry;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public void setMinVersion(Version minVersion) {
        this.minVersion = minVersion;
    }

    public void setTestedVersions(Version[] testedVersions) {
        this.testedVersions = testedVersions;
    }

    public void setDependencies(String[] dependencies) {
        this.dependencies = dependencies;
    }

    @JsonProperty("jar-file")
    public String getJarFile() {
        return jarFile;
    }

    @JsonProperty("jar-file")
    public void setJarFile(String jarFile) {
        this.jarFile = jarFile;
    }

    @JsonProperty("extension-dependencies")
    public void setExtensionDependencies(String[] extensionDependencies) {
        this.extensionDependencies = extensionDependencies;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ExtensionRegistry) obj;
        return Objects.equals(this.main, that.main) &&
                Objects.equals(this.displayName, that.displayName) &&
                Objects.equals(this.registry, that.registry) &&
                Objects.equals(this.version, that.version) &&
                Objects.equals(this.minVersion, that.minVersion) &&
                Objects.equals(this.testedVersions, that.testedVersions) &&
                Objects.equals(this.dependencies, that.dependencies) &&
                Objects.equals(this.extensionDependencies, that.extensionDependencies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(main, displayName, registry, version, minVersion, testedVersions, dependencies, extensionDependencies);
    }

    @Override
    public String toString() {
        return "ExtensionRegistry[" +
                "main=" + main + ", " +
                "displayName=" + displayName + ", " +
                "registry=" + registry + ", " +
                "version=" + version + ", " +
                "minVersion=" + minVersion + ", " +
                "testedVersions=" + testedVersions + ", " +
                "dependencies=" + dependencies + ", " +
                "extensionDependencies=" + extensionDependencies + ']';
    }
}
