package io.github.toberocat.improvedfactions.utility.configs;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;

import javax.sql.DataSource;
import java.sql.Connection;

public class Config {

    private Object defaultObject;
    private DataManager manager;
    private String path;

    public Config(DataManager manager, String path, Object defaultObject) {
        this.defaultObject = defaultObject;
        this.manager = manager;
        this.path = path;
    }

    public Config(String path, Object defaultObject) {
        this.defaultObject = defaultObject;
        this.path = path;
    }

    public void saveDefault() {
        if (manager == null) { //Use config.yml
            ImprovedFactionsMain.getPlugin().getConfig().addDefault(path, defaultObject.toString());
            ImprovedFactionsMain.getPlugin().getConfig().options().copyDefaults(false);
            ImprovedFactionsMain.getPlugin().saveConfig();
        } else {
            manager.getConfig().addDefault(path, defaultObject.toString());
            manager.getConfig().options().copyDefaults(false);
            manager.saveConfig();
        }
    }

    public void save(Object object) {
        if (manager == null) { //Use config.yml
            ImprovedFactionsMain.getPlugin().getConfig().set(path, defaultObject.toString());
            ImprovedFactionsMain.getPlugin().saveConfig();
        } else {
            manager.getConfig().set(path, defaultObject.toString());
            manager.saveConfig();
        }
    }

    public String read() {
        if (manager == null) { //Use config.yml
            return ImprovedFactionsMain.getPlugin().getConfig().get(path).toString();
        } else {
            return manager.getConfig().get(path).toString();
        }
    }

}
