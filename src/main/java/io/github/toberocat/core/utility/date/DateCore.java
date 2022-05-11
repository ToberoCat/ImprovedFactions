package io.github.toberocat.core.utility.date;

import org.joda.time.format.*;

public class DateCore {
    public static DateTimeFormatter TIME_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    public static PeriodFormatter PERIOD_FORMAT = new PeriodFormatterBuilder().appendSeparator(":")
            .printZeroAlways().appendDays().appendHours().appendMinutes().appendSeconds().toFormatter();

}
