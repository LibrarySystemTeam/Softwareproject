package library.test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import library.domain.Book;
import library.domain.Loan;
import library.domain.User;
import library.repository.LoanRepository;
import library.repository.UserRepository;
import library.service.AdminService;
import library.service.UserService;

class UserServiceTest {

    @Test
    void testUnregisterFailsIfNotAdmin() {

        UserRepository ur = new UserRepository();
        LoanRepository lr = new LoanRepository();
        AdminService admin = new AdminService();
        UserService us = new UserService(ur, lr, admin);

        // user now requires email
        User u = new User("Ali", "1", "ali@test.com");
        ur.addUser(u);

        assertThrows(IllegalStateException.class, () -> {
            us.unregisterUser("1");
        });
    }

    @Test
    void testUnregisterFailsIfUserHasActiveLoan() {

        UserRepository ur = new UserRepository();
        LoanRepository lr = new LoanRepository();
        AdminService admin = new AdminService();
        UserService us = new UserService(ur, lr, admin);

        User u = new User("Ali", "1", "ali@test.com");
        ur.addUser(u);

        lr.addLoan(new Loan(
                u,
                new Book("Java", "James", "111"),
                LocalDate.now(),
                LocalDate.now().plusDays(10)
        ));

        admin.login("admin", "1234");

        assertThrows(IllegalStateException.class, () -> {
            us.unregisterUser("1");
        });
    }

    @Test
    void testUnregisterFailsIfUserHasUnpaidFines() {

        UserRepository ur = new UserRepository();
        LoanRepository lr = new LoanRepository();
        AdminService admin = new AdminService();
        UserService us = new UserService(ur, lr, admin);

        User u = new User("Ali", "1", "ali@test.com");
        u.addFine(50);
        ur.addUser(u);

        admin.login("admin", "1234");

        assertThrows(IllegalStateException.class, () -> {
            us.unregisterUser("1");
        });
    }

    @Test
    void testUnregisterSuccess() {

        UserRepository ur = new UserRepository();
        LoanRepository lr = new LoanRepository();
        AdminService admin = new AdminService();
        UserService us = new UserService(ur, lr, admin);

        User u = new User("Ali", "1", "ali@test.com");
        ur.addUser(u);

        admin.login("admin", "1234");

        assertDoesNotThrow(() -> us.unregisterUser("1"));

        assertNull(ur.findById("1")); // user removed
    }
}
