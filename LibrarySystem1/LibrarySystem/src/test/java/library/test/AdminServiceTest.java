package library.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import library.service.AdminService;

class AdminServiceTest {

    @Test
    void testLoginSuccess() {
        AdminService service = new AdminService();
        assertTrue(service.login("admin", "1234"));
        assertTrue(service.isLoggedIn());
    }

    @Test
    void testLoginFailure() {
        AdminService service = new AdminService();
        assertFalse(service.login("wrong", "pass"));
        assertFalse(service.isLoggedIn());
    }

    @Test
    void testLogout() {
        AdminService service = new AdminService();
        service.login("admin", "1234");
        service.logout();
        assertFalse(service.isLoggedIn());
    }
}
