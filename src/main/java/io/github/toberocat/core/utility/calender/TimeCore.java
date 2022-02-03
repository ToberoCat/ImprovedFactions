package io.github.toberocat.core.utility.calender;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class TimeCore {
    private static SimpleDateFormat format;

    public static boolean init() {
        format = new SimpleDateFormat("ss:mm:hh-dd/MM/yyyy");
        return true;
    }

    public static LocalDateTime currentDate() {
        LocalDateTime now = LocalDateTime.now();
        return now;
    }
}
