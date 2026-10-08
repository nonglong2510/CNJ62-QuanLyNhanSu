package utils;

import model.TaiKhoan;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccessPolicyTest {
    @Test
    void recognizesOnlyAdminAsSystemAdministrator() {
        assertTrue(AccessPolicy.isAdmin(account("Admin", null)));
        assertFalse(AccessPolicy.isAdmin(account("User", "NV001")));
        assertFalse(AccessPolicy.isAdmin(account("Unknown", "NV001")));
    }

    @Test
    void limitsUserAccountToItsLinkedEmployee() {
        TaiKhoan user = account("User", " NV001 ");

        assertEquals("NV001", AccessPolicy.requireEmployeeId(user));
        assertThrows(IllegalArgumentException.class,
                () -> AccessPolicy.validateAccount(account("User", " ")));
        assertThrows(IllegalArgumentException.class,
                () -> AccessPolicy.requireEmployeeId(account("Admin", null)));
        assertThrows(IllegalArgumentException.class,
                () -> AccessPolicy.validateAccount(account("Unknown", "NV001")));
    }

    private TaiKhoan account(String role, String employeeId) {
        TaiKhoan account = new TaiKhoan();
        account.setQuyen(role);
        account.setMaNV(employeeId);
        return account;
    }
}
