package com.zhaoyss.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerUtil {
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String getTimestampedMessage(String message) {
        LocalDateTime now = LocalDateTime.now();
        return "[" + dtf.format(now) + "] " + message;
    }

    public static void logError(String message) {
        System.err.println(getTimestampedMessage(message));
    }

    public static void logInfo(String message) {
        System.out.println(getTimestampedMessage(message));
    }
}
