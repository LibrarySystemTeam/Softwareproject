package library.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import library.domain.Admin;

class AdminTest {

    @Test
    void testValidLogin() {
        Admin admin = new Admin();
        assertTrue(admin.login("admin", "1234"));
    }

    @Test
    void testInvalidLogin() {
        Admin admin = new Admin();
        assertFalse(admin.login("wrong", "pass"));
    }

    @Test
    void testLogout() {
        Admin admin = new Admin();
        admin.login("admin", "1234");
        admin.logout();
        assertFalse(admin.isLoggedIn());
    }
    @Test
    void testLoginCorrectUsernameWrongPassword() {
        Admin admin = new Admin();
        assertFalse(admin.login("admin", "wrongpass"));
        assertFalse(admin.isLoggedIn());
    }
    @Test
    void testLoginWrongUsernameCorrectPassword() {
        Admin admin = new Admin();
        assertFalse(admin.login("wrong", "1234"));
        assertFalse(admin.isLoggedIn());
    }
    @Test
    void testLoginCaseSensitivity() {
        Admin admin = new Admin();
        assertFalse(admin.login("Admin", "1234"));  // should fail (case-sensitive)
    }
    @Test
    void testLogoutWithoutLogin() {
        Admin admin = new Admin();
        admin.logout();
        assertFalse(admin.isLoggedIn());
    }




    
}
