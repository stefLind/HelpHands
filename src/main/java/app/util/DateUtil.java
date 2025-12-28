package app.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@UtilityClass
public class DateUtil {

    public static long getDateDiffFromNowInDays(LocalDate startDate) {
        if (startDate.isBefore(LocalDate.now())) {
            return 0;
        }
        return ChronoUnit.DAYS.between(LocalDate.now(), startDate);
    }

    public static long getDateTimeDiffInHours(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        if (startDate == null || startTime == null || endDate == null || endTime == null || startDate.isAfter(endDate)) {
            return 0;
        }

        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);

        return ChronoUnit.HOURS.between(startDateTime, endDateTime);
    }

    public long getDays(long hours) {
        return TimeUnit.HOURS.toDays(hours);
    }
}
