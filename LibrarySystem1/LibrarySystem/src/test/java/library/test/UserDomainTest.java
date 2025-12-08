package library.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import library.domain.User;

class UserDomainTest {

    @Test
    void testUserFineBehavior() {
        User u = new User("Ali", "1", "a@test.com");

        u.addFine(50);
        assertEquals(50, u.getFineBalance());

        u.payFine(20);
        assertEquals(30, u.getFineBalance());

        u.payFine(100);  // overpay
        assertEquals(0, u.getFineBalance());
    }
}
