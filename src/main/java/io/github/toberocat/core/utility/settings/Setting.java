package io.github.toberocat.core.utility.settings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bukkit.Material;

public class Setting<T> {
    public enum SettingsType { BOOL, ENUM, NOT_LISTED }

    private String[] enumValues;
    private T selected;
    private SettingsType type;
    private Material material;

    public Setting() {
    }

    public Setting(T t, SettingsType type) {
        this.selected = t;
        this.type = type;
    }

    @JsonIgnore
    public Material getMaterial() {
        return material;
    }
    @JsonIgnore
    public SettingsType getType() {
        return type;
    }

    public T getSelected() {
        return selected;
    }
    public Setting<T> setSelected(T selected) {
        this.selected = selected;
        return this;
    }
    @JsonIgnore
    public Setting<T> setType(SettingsType type) {
        this.type = type;
        return this;
    }
    @JsonIgnore
    public Setting<T> setMaterial(Material material) {
        this.material = material;
        return this;
    }

    @JsonIgnore
    public String[] getEnumValues() {
        return enumValues;
    }

    @JsonIgnore
    public Setting<T> setEnumValues(String[] enumValues) {
        this.enumValues = enumValues;
        return this;
    }
}
