package library.test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;

import org.junit.jupiter.api.Test;

import library.domain.Book;
import library.domain.CD;
import library.domain.User;
import library.repository.LoanRepository;
import library.service.LoanService;

public class MediaBorrowTest {

    private Clock fixedClock(LocalDate date) {
        return Clock.fixed(
                date.atStartOfDay(ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault()
        );
    }

    @Test
    void testBorrowCD_7days() {

        LoanRepository lr = new LoanRepository();
        LoanService ls = new LoanService(lr, fixedClock(LocalDate.of(2025, 1, 1)));

        User u = new User("Ali", "1", "ali@example.com");   // <-- تعديل مهم
        CD cd = new CD("Hits", "DJ", "CD1");

        var loan = ls.borrowCD(u, cd);

        assertEquals(LocalDate.of(2025, 1, 8), loan.getDueDate());
    }

    @Test
    void testBorrowBook_28days() {

        LoanRepository lr = new LoanRepository();
        LoanService ls = new LoanService(lr, fixedClock(LocalDate.of(2025, 1, 1)));

        User u = new User("Ali", "1", "ali@example.com");  // <-- تعديل مهم
        Book b = new Book("Java", "James", "111");

        var loan = ls.borrowBook(u, b);

        assertEquals(LocalDate.of(2025, 1, 29), loan.getDueDate());
    }
}
