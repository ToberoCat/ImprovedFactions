package io.github.toberocat.improvedfactions.gui;

import org.bukkit.Material;

import java.util.List;

public class FlagUtils {

    public static <T extends Enum<T>> boolean CompareEnum(Flag flag, T should) {
        return flag.getSelected() == should.ordinal();
    }
}
