package com.nimvb.app.database.util;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

public class DateTime {

    private static final long TICKS_AT_EPOCH = 621355968000000000L;
    private static final long TICKS_PER_MILLISECOND = 10000;



    public static long ticks(Instant instant) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Date.from(instant));
        return (calendar.getTimeInMillis() * TICKS_PER_MILLISECOND) + TICKS_AT_EPOCH;
    }

    public static final class UtcNow {
        public static long ticks() {
            return DateTime.ticks(Instant.now());
        }
    }
}
