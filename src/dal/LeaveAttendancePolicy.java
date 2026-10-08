package dal;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

final class LeaveAttendancePolicy {
    private LeaveAttendancePolicy() {
    }

    static List<LocalDate> weekdays(LocalDate start, LocalDate end) {
        List<LocalDate> days = new ArrayList<>();
        for (LocalDate day = start; !day.isAfter(end); day = day.plusDays(1)) {
            DayOfWeek dayOfWeek = day.getDayOfWeek();
            if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
                days.add(day);
            }
        }
        return days;
    }

    static List<Date> weekdayConflicts(List<Date> attendanceDates) {
        List<Date> conflicts = new ArrayList<>();
        for (Date date : attendanceDates) {
            DayOfWeek day = date.toLocalDate().getDayOfWeek();
            if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {
                conflicts.add(date);
            }
        }
        return conflicts;
    }
}
