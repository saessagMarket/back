package com.market.saessag.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TimeUtils {
    public static String getRelativeTime(LocalDateTime addedDate) {
        LocalDateTime now = LocalDateTime.now();

        long seconds = ChronoUnit.SECONDS.between(addedDate, now);
        long minutes = ChronoUnit.MINUTES.between(addedDate, now);
        long hours = ChronoUnit.HOURS.between(addedDate, now);
        long days = ChronoUnit.DAYS.between(addedDate, now);
        long weeks = ChronoUnit.WEEKS.between(addedDate, now);
        long months = ChronoUnit.MONTHS.between(addedDate, now);
        long years = ChronoUnit.YEARS.between(addedDate, now);

        if (seconds < 60) {
            return seconds + "초 전";
        } else if (minutes < 60) {
            return minutes + "분 전";
        } else if (hours < 24) {
            return hours + "시간 전";
        } else if (days < 7) {
            return days + "일 전";
        } else if (weeks < 5) {
            return weeks + "주 전";
        } else if (months < 12) {
            return months + "개월 전";
        } else {
            return years + "년 전";
        }
    }
}
