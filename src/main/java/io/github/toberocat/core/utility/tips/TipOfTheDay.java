package io.github.toberocat.core.utility.tips;

import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class TipOfTheDay {
    private static final Random rand = new Random();
    private static final ArrayList<UUID> receivedTip = new ArrayList<>();

    public static void sendTipOfTheDay(Player player) {
        String[] tips = Language.getLore("tip-of-the-day", player);
        Language.sendRawMessage(tips[rand.nextInt(tips.length)], player);
        receivedTip.add(player.getUniqueId());
    }

    public static boolean receivedTip(Player player) {
        return receivedTip.contains(player.getUniqueId());
    }

    public static void resetPlayer(Player player) {
        receivedTip.remove(player.getUniqueId());
    }
}
