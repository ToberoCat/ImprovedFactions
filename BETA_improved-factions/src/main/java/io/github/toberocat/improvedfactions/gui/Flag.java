package io.github.toberocat.improvedfactions.gui;

import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.factions.FactionSettings;
import io.github.toberocat.improvedfactions.language.Language;
import io.github.toberocat.improvedfactions.utility.Utils;
import io.github.toberocat.improvedfactions.utility.Callback;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Flag<T> {
    public enum FlagType { Enum, Boolean, Function };

    private final String flagName;
    private final Material material;

    protected FlagType type;

    //Enum
    private List<T> enumValues;
    private int selected;

    //Boolean
    private boolean currentBool;

    //Function
    private Callback callback;
    private String callBackDescription;

    public Flag(FlagType type, Material material, String flagName, List<T> values, int selected) {
        this(flagName, type, material);
        enumValues = values;
        this.selected = selected;
    }

    public Flag(FlagType type, Material material, String flagName, String callBackDescription, Callback callback) {
        this(flagName, type, material);
        this.callback = callback;
        this.callBackDescription = callBackDescription;
    }

    public Flag(FlagType type, Material material, String flagName, boolean defaultValue) {
        this(flagName, type, material);
        currentBool = defaultValue;
    }

    private Flag(String flagName, FlagType type, Material material) {
        this.flagName = Language.format(flagName);
        this.material = material;
        this.type = type;
    }

    public ItemStack ToggleSetting(Faction faction, Player player) {
        switch (type) {
            case Enum:
                selected++;
                if (selected >= enumValues.size())
                    selected = 0;
                return getItem();
            case Boolean:
                currentBool = !currentBool;
                return getItem();
            case Function:
                callback.CallBack(faction, player, null);
                return getItem();
            default:
                return getItem();
        }
    }

    public ItemStack getItem() {
        switch (type) {
            case Enum:
                return Utils.createItem(material, flagName + ": " + enumValues.get(selected).toString(),
                        (String[]) ArrayUtils.addAll(new String[]{Language.format("&8Click to switch objects")}, getEnumData()));
            case Boolean:
                return Utils.createItem(material, flagName + ": " + (currentBool ? "§a" : "§c") + currentBool,
                        new String[]{Language.format("&8Click to toggle")});
            case Function:
                return Utils.createItem(material, flagName,
                        new String[]{Language.format(callBackDescription)});
            default:
                return new ItemStack(material);
        }
    }

    public int getSelected() {
        return selected;
    }

    public boolean getCurrentBool() {
        return currentBool;
    }

    private String[] getEnumData() {
        String[] data = new String[enumValues.size()];
        for (int i = 0; i < enumValues.size(); i++) {
            data[i] = Language.format("&7-"+(i == selected ? "&n" : "") + ChatColor.stripColor(enumValues.get(i).toString()));
        }
        return data;
    }

    @Override
    public String toString() {
        return "Flag{" +
                "type=" + type +
                "; selected=" + selected +
                "; currentBool=" + currentBool +
                '}';
    }

    public static Flag fromString(String str) {
        String name = str.split("::")[0];
        FlagType type = null;
        int selected = 0;
        boolean currentBoolean = false;

        Material material = null;
        List<String> enumValues = null;
        Callback callback;
        String callBackDesc;

        str = str.replace(name + "::Flag{", "").replaceAll("'", "").replace("}", "");
        String[] parms = str.split("[=;]");
        for (int i = 0; i < parms.length; i++) {
            String parm = parms[i];
            parm = parm.trim();
            switch (parm) {
                case "flagName":
                    name = parms[i + 1];
                    break;
                case "type":
                    type = FlagType.valueOf(parms[i + 1]);
                    break;
                case "selected":
                    selected = Integer.parseInt(parms[i + 1]);
                    break;
                case "currentBool":
                    currentBoolean = Boolean.parseBoolean(parms[i + 1]);
                    break;
            }
        }
        Flag flag = getFlag(name);
        return flag;
    }

    private static Flag getFlag(String name) {
        for (String flag : FactionSettings.FLAGS.keySet()) {
            if (flag.equals(name)) return FactionSettings.FLAGS.get(flag);
        }
        return null;
    }

    public FlagType getType() {
        return type;
    }

    public List<T> getEnumValues() {
        return enumValues;
    }

    public Material getMaterial() {
        return material;
    }

    public Callback getCallback() {
        return callback;
    }

    public String getCallBackDescription() {
        return callBackDescription;
    }

    public String getFlagName() {
        return flagName;
    }
}
