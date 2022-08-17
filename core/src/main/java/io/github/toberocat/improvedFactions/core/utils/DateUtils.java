package io.github.toberocat.improvedFactions.core.utils;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class DateUtils {
    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    public static final PeriodFormatter PERIOD_FORMAT = new PeriodFormatterBuilder()
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
}
