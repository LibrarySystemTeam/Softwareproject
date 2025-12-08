package library.test;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import library.domain.Book;
import library.domain.CD;
import library.domain.Loan;
import library.domain.LoanCD;
import library.domain.User;

import library.repository.BookRepository;
import library.repository.CDRepository;
import library.repository.LoanRepository;
import library.repository.UserRepository;

import library.service.AdminService;
import library.service.BookService;
import library.service.LoanService;
import library.service.UserService;

public class LibraryAppTest {

    AdminService adminService;
    BookRepository bookRepo;
    BookService bookService;

    LoanRepository loanRepo;
    LoanService loanService;

    UserRepository userRepo;
    UserService userService;

    CDRepository cdRepo;

    @BeforeEach
    void setup() {
        adminService = new AdminService();
        bookRepo = new BookRepository();
        bookService = new BookService(bookRepo);

        loanRepo = new LoanRepository();
        loanService = new LoanService(loanRepo);

        userRepo = new UserRepository();
        userService = new UserService(userRepo, loanRepo, adminService);

        cdRepo = new CDRepository();
    }

    // ----------------------------
    // Sprint 1
    // ----------------------------

    @Test
    void testAdminLogin() {
        assertTrue(adminService.login("admin", "1234"));
        adminService.logout();
    }

    @Test
    void testAddAndSearchBook() {
        Book b = new Book("Java", "Author", "111");
        b.setQuantity(3);
        bookRepo.add(b);

        assertEquals(1, bookService.search("Java").size());
    }

    // ----------------------------
    // Sprint 2
    // ----------------------------

    @Test
    void testBorrowBook() {
        User u = new User("Ali", "10", "mail@mail");
        userRepo.addUser(u);

        Book b = new Book("DS", "Auth", "222");
        b.setQuantity(1);
        bookRepo.add(b);

        Loan loan = loanService.borrowBook(u, b);
        assertNotNull(loan.getDueDate());
    }

    @Test
    void testFineCalculationBook() throws Exception {
        User u = new User("Sara", "20", "s@mail");
        userRepo.addUser(u);

        Book b = new Book("Math", "Auth", "333");
        b.setQuantity(1);
        bookRepo.add(b);

        Loan loan = loanService.borrowBook(u, b);

        // Force overdue
        Field due = Loan.class.getDeclaredField("dueDate");
        due.setAccessible(true);
        due.set(loan, LocalDate.now().minusDays(5)); // 5 days late

        int fine = loanService.calculateFine(loan);
        assertTrue(fine > 0);
    }

    // ----------------------------
    // Sprint 3 & 4
    // ----------------------------

    @Test
    void testUnregisterUser() {
        adminService.login("admin", "1234");

        User u = new User("Lojain", "55", "l@mail");
        userRepo.addUser(u);

        assertDoesNotThrow(() -> userService.unregisterUser("55"));
    }

    // ----------------------------
    // Sprint 5 – CD Borrow + CD Fine
    // ----------------------------

    @Test
    void testBorrowCD() {
        User u = new User("CDUser", "99", "cd@mail");
        userRepo.addUser(u);

        CD cd = new CD("Hits", "Artist", "CD1");
        cdRepo.add(cd);

        LoanCD loan = loanService.borrowCD(u, cd);
        assertNotNull(loan.getDueDate());
    }

    @Test
    void testFineCalculationCD() throws Exception {
        User u = new User("User", "44", "u@mail");
        userRepo.addUser(u);

        CD cd = new CD("Music", "Artist", "CD50");
        cdRepo.add(cd);

        LoanCD loan = loanService.borrowCD(u, cd);

        // Force overdue correctly
        Field dueField = LoanCD.class.getDeclaredField("dueDate");
        dueField.setAccessible(true);
        dueField.set(loan, LocalDate.now().minusDays(3)); // overdue 3 days

        int fine = loanService.calculateFine(loan);

        // 3 days * 20 = 60
        assertEquals(60, fine);
    }
}
