package ru.chernov.prosto.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Pavel Chernov
 */
public class DateUtils {

    private static final String DOT_FORMAT = "dd.MM.yyyy";
    private static final String SPACE_FORMAT = "dd MM yyyy";

    public static String formatDate(LocalDate localDate) {
        var dateFormatter = DateTimeFormatter.ofPattern(DOT_FORMAT);
        return dateFormatter.format(localDate);
    }

    public static String formatDate(LocalDateTime localDateTime) {
        var dateFormatter = DateTimeFormatter.ofPattern(DOT_FORMAT);
        return dateFormatter.format(localDateTime);
    }
}
