package app.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@UtilityClass
public class DateUtil {

    public static long getDateDiffFromNowInDays(LocalDateTime startDate) {
        if (startDate.isBefore(LocalDateTime.now())) {
            return 0;
        }
        return ChronoUnit.DAYS.between(LocalDateTime.now(), startDate);
    }

    public static long getDateTimeDiffInHours(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            return 0;
        }

        return ChronoUnit.HOURS.between(startDate, endDate);
    }

    public long getDays(long hours) {
        return TimeUnit.HOURS.toDays(hours);
    }
}
