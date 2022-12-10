package com.backend.kmsproject.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DatetimeUtils {
    public static String formatLocalDateTimeNow() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }

    public static String formatLocalTime(LocalTime localTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return localTime.format(formatter);
    }

    public static String formatLocalDate(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return localDate.format(formatter);
    }
}
