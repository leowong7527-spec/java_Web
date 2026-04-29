package ict.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateTimeUtil {
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private DateTimeUtil() {
    }

    public static String format(LocalDateTime value) {
        if (value == null) {
            return "";
        }
        return value.format(DISPLAY_FORMAT);
    }
}


