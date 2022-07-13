package io.github.toberocat.core.utility.date;

import io.github.toberocat.core.player.PlayerSettings;
import org.bukkit.entity.Player;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.*;

public class DateCore {
    public static DateTimeFormatter TIME_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    public static PeriodFormatter PERIOD_FORMAT = new PeriodFormatterBuilder()
            .appendSeparator(":").printZeroAlways()
            .appendDays().appendSeparator(":")
            .appendHours().appendSeparator(":")
            .appendMinutes().appendSeparator(":")
            .appendSeconds().appendSeparator(":")
            .toFormatter();

    public static String formatPeriod(Period period) {
        return period.toString(PERIOD_FORMAT);
    }

    public static String formatTime(DateTime time) {
        return time.toString(TIME_FORMAT);
    }

    public static boolean hasTimeout(Player player) {
        String timeout = PlayerSettings.getSettings(player.getUniqueId()).getSetting("factionJoinTimeout").getSelected().toString();
        if (!timeout.equals("-1")) {
            DateTimeFormatter fmt = DateCore.TIME_FORMAT;
            DateTime until = fmt.parseDateTime(timeout);

            //Language.sendRawMessage("Can't join. You are in timeout until " + until.toString(fmt) + "&f. Please wait &6" + diff.toString(DateCore.PERIOD_FORMAT) + "&f until you can join again", player);
            return DateTime.now().isAfter(until);
        }

        return true;
    }

    public static Period leftTimeDifference(Player player) {
        String timeout = PlayerSettings.getSettings(player.getUniqueId()).getSetting("factionJoinTimeout").getSelected().toString();
        if (!timeout.equals("-1")) {
            DateTimeFormatter fmt = DateCore.TIME_FORMAT;
            DateTime until = fmt.parseDateTime(timeout);

            if (!DateTime.now().isAfter(until)) return new Period(DateTime.now(), until);

        }

        return new Period();
    }

    public static DateTime getTimeout(Player player) {
        String timeout = PlayerSettings.getSettings(player.getUniqueId()).getSetting("factionJoinTimeout").getSelected().toString();
        if (!timeout.equals("-1")) {
            DateTimeFormatter fmt = DateCore.TIME_FORMAT;
            DateTime until = fmt.parseDateTime(timeout);

            if (!DateTime.now().isAfter(until)) return until;
        }

        return DateTime.now();
    }
}
