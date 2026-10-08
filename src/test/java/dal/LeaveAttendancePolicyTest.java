package dal;

import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LeaveAttendancePolicyTest {
    @Test
    void createsWeekdayAttendanceAndSkipsWeekendDays() {
        assertEquals(Arrays.asList(
                        LocalDate.of(2026, 10, 9),
                        LocalDate.of(2026, 10, 12)),
                LeaveAttendancePolicy.weekdays(
                        LocalDate.of(2026, 10, 9), LocalDate.of(2026, 10, 12)));
    }

    @Test
    void weekendAttendanceDoesNotConflictWithApprovedLeave() {
        assertEquals(Arrays.asList(Date.valueOf("2026-10-09")),
                LeaveAttendancePolicy.weekdayConflicts(Arrays.asList(
                        Date.valueOf("2026-10-09"),
                        Date.valueOf("2026-10-10"),
                        Date.valueOf("2026-10-11"))));
    }
}
