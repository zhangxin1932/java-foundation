package com.zy.foundation.lang.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * https://segmentfault.com/q/1010000040029946
 * https://www.5axxw.com/questions/content/uylwd7
 */
public final class TimeUtils {

    private TimeUtils() {
        throw new RuntimeException("TimeUtils can not instantiated.");
    }

    public static final String TIME_FORMAT_01 = "yyyy-MM-dd HH:mm:ss";

    public static long parseEpochStr2Mills(String epochStr, String format) {
        return LocalDateTime.from(DateTimeFormatter.ofPattern(format).parse(epochStr)).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static String parseMillsTimestamp2Str(long mills, String format) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(mills), ZoneId.systemDefault());
        return DateTimeFormatter.ofPattern(format).format(localDateTime);
    }

    /**
     * ZonedDateTime now = ZonedDateTime.now();
     *
     * 1.DateTimeFormatter.ISO_OFFSET_DATE_TIME
     * 2022-06-06T20:32:34.816+08:00
     * @param now
     * @return
     */
    public static String zonedDateTime2Str(ZonedDateTime now) {
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(now);
    }

    public static ZonedDateTime str2ZonedDateTime(String now) {
        return ZonedDateTime.parse(now, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    /**
     * LocalDateTime now = LocalDateTime.now();
     *
     * 1.DateTimeFormatter.ISO_LOCAL_DATE_TIME
     * 2022-06-06T20:53:32.266
     * @param now
     * @return
     */
    public static String localDateTime2Str(LocalDateTime now) {
        return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(now);
    }

    public static LocalDateTime str2LocalDateTime(String now) {
        return LocalDateTime.parse(now, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    /**
     * LocalDate now = LocalDate.now();
     *
     * 1.DateTimeFormatter.ISO_LOCAL_DATE
     * 2022-06-06
     * @param now
     * @return
     */
    public static String localDate2Str(LocalDate now) {
        return DateTimeFormatter.ISO_LOCAL_DATE.format(now);
    }

    public static LocalDate str2LocalDate(String now) {
        return LocalDate.parse(now, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * ZonedDateTime now = ZonedDateTime.now();
     *
     * 1.DateTimeFormatter.ISO_OFFSET_DATE
     * 2022-06-06+08:00
     * @param now
     * @return
     */
    public static String zonedDateTime2Str1(ZonedDateTime now) {
        return DateTimeFormatter.ISO_OFFSET_DATE.format(now);
    }

    public static LocalDate str2LocalDate1(String now) {
        return LocalDate.parse(now, DateTimeFormatter.ISO_OFFSET_DATE);
    }

}
