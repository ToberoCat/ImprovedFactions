package io.github.toberocat.improvedfactions.spigot;

import io.github.toberocat.improvedFactions.core.utils.CUtils;

public class Test {
    public static void main(String[] args) throws Exception {
        CUtils.copyResource("/lang/en_us.xml",
                "C:\\Users\\Tobias\\Desktop\\Development\\Minecraft\\" +
                        "Spigot\\Server1.16.5\\plugins\\ImprovedFactions\\lang\\en_us.xml", Test.class);
    }
}
