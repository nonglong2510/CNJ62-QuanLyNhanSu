package bus;

import model.NhanVien;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NhanVienValidatorTest {
    @Test
    void trimsRequiredEmployeeFieldsAfterValidation() {
        NhanVien employee = validEmployee();
        employee.setMaNV(" NV100 ");
        employee.setHoTen(" Nguyen Van A ");

        NhanVienValidator.validate(employee);

        assertEquals("NV100", employee.getMaNV());
        assertEquals("Nguyen Van A", employee.getHoTen());
    }

    @Test
    void rejectsMissingIdentityAndInvalidContactInformation() {
        NhanVien missingIdentity = validEmployee();
        missingIdentity.setHoTen(" ");
        assertThrows(IllegalArgumentException.class,
                () -> NhanVienValidator.validate(missingIdentity));

        NhanVien invalidEmail = validEmployee();
        invalidEmail.setEmail("not-an-email");
        assertThrows(IllegalArgumentException.class,
                () -> NhanVienValidator.validate(invalidEmail));
    }

    @Test
    void rejectsHireDateBeforeBirthDateAndNonPositiveCoefficient() {
        NhanVien invalidDates = validEmployee();
        invalidDates.setNgayVaoLam(Date.valueOf("1990-01-01"));
        assertThrows(IllegalArgumentException.class,
                () -> NhanVienValidator.validate(invalidDates));

        NhanVien invalidCoefficient = validEmployee();
        invalidCoefficient.setHeSoLuong(Double.POSITIVE_INFINITY);
        assertThrows(IllegalArgumentException.class,
                () -> NhanVienValidator.validate(invalidCoefficient));
    }

    private NhanVien validEmployee() {
        return new NhanVien("NV100", "Nguyen Van A", "Nam",
                Date.valueOf("1990-01-01"), "0901234567", "a@example.com",
                null, null, null, 1.0, Date.valueOf("2020-01-01"), "Đang làm việc");
    }
}
