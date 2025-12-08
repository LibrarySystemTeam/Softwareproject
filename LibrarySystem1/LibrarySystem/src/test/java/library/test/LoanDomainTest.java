package library.test;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import library.domain.*;

class LoanDomainTest {

    @Test
    void testLoanGetters() {
        User u = new User("Ali", "1", "mail@test.com");
        Book b = new Book("Java", "James", "222");

        LocalDate borrow = LocalDate.of(2023,1,1);
        LocalDate due = LocalDate.of(2023,1,10);

        Loan loan = new Loan(u, b, borrow, due);

        assertEquals(u, loan.getUser());
        assertEquals(b, loan.getBook());
        assertEquals(borrow, loan.getBorrowDate());
        assertEquals(due, loan.getDueDate());
    }
}
