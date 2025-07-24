package com.backend.server.util;

import java.time.Duration;

public class DurationUtils {

    /**
     * Duration 객체를 한국어 문자열로 변환합니다.<br>
     * {@code 3605초 => 1시간 5초 }<br>
     * {@code 2400초 => 40분 }<br>
     * {@code 100200초 => 1일 3시간 50분 }
     */
    public static String durationToKorean(Duration duration) {
        long day = duration.toDays();
        long hour = duration.toHours() % 24;
        long minute = duration.toMinutes() % 60;
        long second = duration.toSeconds() % 60;

        StringBuilder sb = new StringBuilder();

        if (day != 0) sb.append(day).append("일 ");
        if (hour != 0) sb.append(hour).append("시간 ");
        if (minute != 0) sb.append(minute).append("분 ");
        if (second != 0) sb.append(second).append("초");

        return sb.toString();
    }

}
