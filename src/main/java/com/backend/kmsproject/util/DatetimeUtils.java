package com.backend.kmsproject.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatetimeUtils {
    public static String formatLocalDateTimeNow() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }
}
