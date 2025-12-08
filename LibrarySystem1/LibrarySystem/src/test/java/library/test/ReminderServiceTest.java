package library.test;

import static org.junit.jupiter.api.Assertions.*;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;

import org.junit.jupiter.api.Test;

import library.domain.Book;
import library.domain.Loan;
import library.domain.User;
import library.repository.LoanRepository;
import library.service.LoanService;
import library.service.ReminderService;

class ReminderServiceTest {

    private Clock fixedClock(LocalDate date) {
        return Clock.fixed(
                date.atStartOfDay(ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault()
        );
    }

    @Test
    void testSendOverdueReminders() {

        LoanRepository loanRepo = new LoanRepository();
        LoanService loanService = new LoanService(loanRepo, fixedClock(LocalDate.of(2023, 1, 20)));

        MockEmailServer mockServer = new MockEmailServer();

        ReminderService reminderService = new ReminderService(loanRepo, loanService, mockServer);

        User user1 = new User("Ali", "1", "ali@test.com");

        loanRepo.addLoan(new Loan(
                user1,
                new Book("Java", "James", "111"),
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 10)
        ));

        loanRepo.addLoan(new Loan(
                user1,
                new Book("Python", "Mark", "222"),
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 5)
        ));

        reminderService.sendOverdueReminders();

        // ONE email expected
        assertEquals(1, mockServer.getSentMessages().size());

        String msg = mockServer.getSentMessages().get(0);

        // CHECK IMPORTANT PARTS ONLY
        assertTrue(msg.contains("ali@test.com"));
        assertTrue(msg.contains("Dear Ali"));
        assertTrue(msg.contains("You have 2 overdue book(s)"));
        assertTrue(msg.contains("Java"));
        assertTrue(msg.contains("Python"));
    }
    @Test
    void testSendOverdueReminders_ignoresNonOverdueAndOtherUsers() {

        LoanRepository loanRepo = new LoanRepository();
        LoanService loanService = new LoanService(loanRepo, fixedClock(LocalDate.of(2023, 1, 20)));

        MockEmailServer mock = new MockEmailServer();
        ReminderService reminder = new ReminderService(loanRepo, loanService, mock);

        User u1 = new User("Ali", "1", "ali@test.com");
        User u2 = new User("Sara", "2", "sara@test.com");

        // overdue loan → يجب إرسال إيميل
        loanRepo.addLoan(new Loan(
                u1,
                new Book("Java", "James", "111"),
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 5)
        ));

        // NOT overdue → يجب تجاهله
        loanRepo.addLoan(new Loan(
                u2,
                new Book("Python", "Mark", "222"),
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 30)
        ));

        reminder.sendOverdueReminders();

        // فقط إيميل واحد يجب أن يُرسل
        assertEquals(1, mock.getSentMessages().size());

        String msg = mock.getSentMessages().get(0);
        assertTrue(msg.contains("Ali"));
        assertFalse(msg.contains("Sara"));
    }



}
