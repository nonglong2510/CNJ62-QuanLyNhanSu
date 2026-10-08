package bus;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SalaryCalculatorTest {
    @Test
    void proratesSalaryAndAllowanceByWorkedDays() {
        SalaryCalculator.Salary salary = SalaryCalculator.calculate(1.5, 3_000_000, 11);

        assertEquals(5_000_000 * 1.5 / 22 * 11, salary.getBaseSalary(), 0.0001);
        assertEquals(3_000_000.0 / 22 * 11, salary.getAllowance(), 0.0001);
        assertEquals(salary.getBaseSalary() + salary.getAllowance(), salary.getNetPay(), 0.0001);
    }

    @Test
    void capsAllowanceAtStandardWorkDaysButKeepsSalaryForRecordedDays() {
        SalaryCalculator.Salary salary = SalaryCalculator.calculate(1.0, 2_200_000, 24);

        assertEquals(5_000_000.0 / 22 * 24, salary.getBaseSalary(), 0.0001);
        assertEquals(2_200_000, salary.getAllowance(), 0.0001);
    }

    @Test
    void noWorkDaysMeansNoBaseSalaryOrAllowance() {
        SalaryCalculator.Salary salary = SalaryCalculator.calculate(2.0, 3_000_000, 0);

        assertEquals(0, salary.getBaseSalary());
        assertEquals(0, salary.getAllowance());
        assertEquals(0, salary.getNetPay());
    }

    @Test
    void rejectsInvalidCoefficientsAllowancesAndWorkDays() {
        assertThrows(IllegalArgumentException.class,
                () -> SalaryCalculator.calculate(0, 0, 0));
        assertThrows(IllegalArgumentException.class,
                () -> SalaryCalculator.calculate(Double.NaN, 0, 0));
        assertThrows(IllegalArgumentException.class,
                () -> SalaryCalculator.calculate(1, -1, 0));
        assertThrows(IllegalArgumentException.class,
                () -> SalaryCalculator.calculate(1, 0, -1));
    }
}
