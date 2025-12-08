package library.test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;

import org.junit.jupiter.api.Test;

import library.domain.Book;
import library.domain.CD;
import library.domain.LoanCD;


import library.domain.Loan;
import library.domain.User;
import library.repository.LoanRepository;
import library.service.LoanService;

class LoanServiceTest {

    private Clock fixedClock(LocalDate date) {
        return Clock.fixed(
                date.atStartOfDay(ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault()
        );
    }

    // =============================
    //   US2.1 – Borrow Book Tests
    // =============================

    @Test
    void testBorrowBookSuccess() {

        LoanRepository repo = new LoanRepository();
        LoanService service = new LoanService(repo, fixedClock(LocalDate.now()));

        User user = new User("Ali", "1", "ali@example.com");
        Book book = new Book("Java", "James", "111");

        Loan loan = service.borrowBook(user, book);

        assertTrue(book.isBorrowed());
        assertEquals(1, repo.getAllLoans().size());
        assertEquals(LocalDate.now().plusDays(28), loan.getDueDate());
    }

    @Test
    void testBorrowAlreadyBorrowedBookFails() {

        LoanRepository repo = new LoanRepository();
        LoanService service = new LoanService(repo, fixedClock(LocalDate.now()));

        User user = new User("Ali", "1", "ali@example.com");
        Book book = new Book("Java", "James", "111");

        service.borrowBook(user, book);

        assertThrows(IllegalStateException.class, () -> {
            service.borrowBook(user, book);
        });
    }

    @Test
    void testBorrowFailsWhenUserHasFine() {

        LoanRepository repo = new LoanRepository();
        LoanService service = new LoanService(repo, fixedClock(LocalDate.now()));

        User user = new User("Ali", "1", "ali@example.com");
        user.addFine(40);

        Book book = new Book("Java", "James", "111");

        assertThrows(IllegalStateException.class, () -> {
            service.borrowBook(user, book);
        });
    }

    // =============================
    //   US2.2 – Overdue Detection
    // =============================

    @Test
    void testLoanNotOverdue() {

        Loan loan = new Loan(
                new User("Ali", "1", "ali@example.com"),
                new Book("Java", "James", "111"),
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 2, 1)
        );

        LoanService service = new LoanService(
                new LoanRepository(),
                fixedClock(LocalDate.of(2023, 1, 15))
        );

        assertFalse(service.isOverdue(loan));
    }

    @Test
    void testLoanIsOverdue() {

        Loan loan = new Loan(
                new User("Ali", "1", "ali@example.com"),
                new Book("Java", "James", "111"),
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 10)
        );

        LoanService service = new LoanService(
                new LoanRepository(),
                fixedClock(LocalDate.of(2023, 1, 20))
        );

        assertTrue(service.isOverdue(loan));
    }
    @Test
    void testBorrowCDSuccess() {
        LoanRepository repo = new LoanRepository();
        LoanService service = new LoanService(repo, fixedClock(LocalDate.now()));

        User user = new User("Sara", "10", "sara@test.com");
        CD cd = new CD("Music", "Artist", "CD1");

        LoanCD loan = service.borrowCD(user, cd);

        assertTrue(cd.isBorrowed());
        assertEquals(1, repo.getAllCDLoans().size());
        assertEquals(LocalDate.now().plusDays(7), loan.getDueDate());
    }
    @Test
    void testBorrowAlreadyBorrowedCDFails() {
        LoanRepository repo = new LoanRepository();
        LoanService service = new LoanService(repo, fixedClock(LocalDate.now()));

        User user = new User("Sara", "10", "sara@test.com");
        CD cd = new CD("Music", "Artist", "CD1");

        service.borrowCD(user, cd);

        assertThrows(IllegalStateException.class, () -> {
            service.borrowCD(user, cd);
        });
    }
    @Test
    void testCDOverdue() {
        LoanRepository repo = new LoanRepository();
        LoanService service = new LoanService(repo, fixedClock(LocalDate.of(2023,1,20)));

        User user = new User("Ali", "1", "ali@test.com");
        CD cd = new CD("Music", "Artist", "CD1");

        LoanCD loan = new LoanCD(user, cd,
                LocalDate.of(2023,1,1),
                LocalDate.of(2023,1,10));

        assertTrue(service.isOverdue(loan));
    }
    @Test
    void testGetOverdueCDLoans() {
        LoanRepository repo = new LoanRepository();
        LoanService service = new LoanService(repo, fixedClock(LocalDate.of(2023,1,20)));

        User user = new User("Ali", "1", "ali@test.com");
        CD cd = new CD("Music", "Artist", "CD1");

        repo.addCDLoan(new LoanCD(user, cd,
                LocalDate.of(2023,1,1),
                LocalDate.of(2023,1,5)));

        assertEquals(1, service.getOverdueCDLoans().size());
    }



    @Test
    void testGetOverdueDays() {

        Loan loan = new Loan(
                new User("Ali", "1", "ali@example.com"),
                new Book("Java", "James", "111"),
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 10)
        );

        LoanService service = new LoanService(
                new LoanRepository(),
                fixedClock(LocalDate.of(2023, 1, 15))
        );

        assertEquals(5, service.getOverdueDays(loan));
    }

    // =============================
    //    US2.3 – Fine Calculation
    // =============================

    @Test
    void testCalculateFine() {

        Loan loan = new Loan(
                new User("Ali", "1", "ali@example.com"),
                new Book("Java", "James", "111"),
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 10)
        );

        LoanService service = new LoanService(
                new LoanRepository(),
                fixedClock(LocalDate.of(2023, 1, 15))
        );

        assertEquals(50, service.calculateFine(loan));
    }

    // =============================
    //    US2.3 – Pay Fine
    // =============================

    @Test
    void testPartialFinePayment() {

        User user = new User("Ali", "1", "ali@example.com");
        user.addFine(100);

        LoanService service = new LoanService(new LoanRepository(), fixedClock(LocalDate.now()));

        service.payFine(user, 40);

        assertEquals(60, user.getFineBalance());
    }

    @Test
    void testFullFinePayment() {

        User user = new User("Ali", "1", "ali@example.com");
        user.addFine(70);

        LoanService service = new LoanService(new LoanRepository(), fixedClock(LocalDate.now()));

        service.payFine(user, 100);

        assertEquals(0, user.getFineBalance());
    }
    @Test
    void testBorrowAllowedEvenIfUserHasOverdueBooks() {

        LoanRepository lr = new LoanRepository();
        LoanService ls = new LoanService(lr, fixedClock(LocalDate.of(2023, 1, 20)));

        User u = new User("Ali", "1", "ali@example.com");
        Book b1 = new Book("Java", "James", "111");
        Book b2 = new Book("Python", "Mark", "222");

        // user has overdue loan
        lr.addLoan(new Loan(
                u,
                b1,
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 5)
        ));

        // allowed because only unpaid fines block borrowing
        assertDoesNotThrow(() -> {
            ls.borrowBook(u, b2);
        });
    }


 @Test
 void testBorrowFailsIfUserHasOverdueBooks() {

	    LoanRepository lr = new LoanRepository();
	    LoanService ls = new LoanService(lr, fixedClock(LocalDate.of(2023, 1, 20)));

	    User u = new User("Ali", "1", "ali@example.com");
	    Book b1 = new Book("Java", "James", "111");
	    Book b2 = new Book("Python", "Mark", "222");

	    // Add overdue loan
	    lr.addLoan(new Loan(
	            u,
	            b1,
	            LocalDate.of(2023, 1, 1),
	            LocalDate.of(2023, 1, 5)  // overdue
	    ));

	    // EXPECT: borrowing should be allowed (because only fines block borrowing now)
	    assertDoesNotThrow(() -> {
	        ls.borrowBook(u, b2);
	    });
	}
 @Test
 void testUserHasOverdueBook() {

     LoanRepository repo = new LoanRepository();
     LoanService service = new LoanService(repo, fixedClock(LocalDate.of(2023, 1, 20)));

     User user = new User("Ali", "1", "ali@example.com");
     Book book = new Book("Java", "James", "111");

     // overdue loan
     repo.addBookLoan(new Loan(
             user,
             book,
             LocalDate.of(2023, 1, 1),
             LocalDate.of(2023, 1, 5)
     ));

     assertTrue(service.userHasAnyOverdue(user));
 }
 @Test
 void testGetOverdueBookLoans() {

     LoanRepository repo = new LoanRepository();
     LoanService service = new LoanService(repo, fixedClock(LocalDate.of(2023, 1, 20)));

     User user = new User("Ali", "1", "ali@example.com");
     Book book = new Book("Java", "James", "111");

     repo.addBookLoan(new Loan(
             user,
             book,
             LocalDate.of(2023, 1, 1),
             LocalDate.of(2023, 1, 5) // overdue
     ));

     assertEquals(1, service.getOverdueBookLoans().size());
 }
 @Test
 void testNoOverdueLoans() {

     LoanRepository repo = new LoanRepository();
     LoanService service = new LoanService(repo, fixedClock(LocalDate.of(2023, 1, 2)));

     User user = new User("Ali", "1", "ali@example.com");
     Book book = new Book("Java", "James", "111");

     repo.addBookLoan(new Loan(
             user,
             book,
             LocalDate.of(2023, 1, 1),
             LocalDate.of(2023, 1, 10) // not overdue
     ));

     assertFalse(service.userHasAnyOverdue(user));
     assertEquals(0, service.getOverdueBookLoans().size());
 }
 @Test
 void testBorrowBookFails_dueToFine() {
     LoanService service = new LoanService(new LoanRepository(), fixedClock(LocalDate.now()));
     User u = new User("Ali", "1", "a@test.com");
     Book b = new Book("Java", "J", "111");

     u.addFine(10);

     assertThrows(IllegalStateException.class, () -> service.borrowBook(u, b));
 }

 @Test
 void testBorrowBookFails_bookNotAvailable() {
     LoanRepository repo = new LoanRepository();
     LoanService service = new LoanService(repo, fixedClock(LocalDate.now()));

     User u = new User("Ali", "1", "a@test.com");
     Book b = new Book("Java", "J", "111");
     b.setQuantity(1);

     service.borrowBook(u, b);

     assertThrows(IllegalStateException.class, () -> service.borrowBook(u, b));
 }

 @Test
 void testBorrowBook_successBranch() {
     LoanRepository repo = new LoanRepository();
     LoanService service = new LoanService(repo, fixedClock(LocalDate.now()));

     User u = new User("Ali", "1", "a@test.com");
     Book b = new Book("Java", "J", "111");
     b.setQuantity(2);

     assertDoesNotThrow(() -> service.borrowBook(u, b));
 }
 @Test
 void testBorrowCDFails_dueToFine() {
     LoanService service = new LoanService(new LoanRepository(), fixedClock(LocalDate.now()));
     User u = new User("Ali", "1", "a@test.com");
     u.addFine(20);

     CD cd = new CD("Music", "Artist", "C1");

     assertThrows(IllegalStateException.class, () -> service.borrowCD(u, cd));
 }

 @Test
 void testBorrowCDFails_cdAlreadyBorrowed() {
     LoanRepository repo = new LoanRepository();
     LoanService service = new LoanService(repo, fixedClock(LocalDate.now()));

     User u = new User("Ali", "1", "a@test.com");
     CD cd = new CD("Music", "Artist", "C1");

     service.borrowCD(u, cd);

     assertThrows(IllegalStateException.class, () -> service.borrowCD(u, cd));
 }

 @Test
 void testBorrowCD_successBranch() {
     LoanRepository repo = new LoanRepository();
     LoanService service = new LoanService(repo, fixedClock(LocalDate.now()));

     User u = new User("Ali", "1", "a@test.com");
     CD cd = new CD("Music", "Artist", "C1");

     assertDoesNotThrow(() -> service.borrowCD(u, cd));
 }@Test
 void testUserHasNoOverdue() {
	    LoanService service = new LoanService(new LoanRepository(), fixedClock(LocalDate.of(2023,1,1)));
	    User u = new User("Ali", "1", "a@test.com");
	    assertFalse(service.userHasAnyOverdue(u));
	}

	@Test
	void testUserHasOverdueBookOnly() {
	    LoanRepository repo = new LoanRepository();
	    LoanService service = new LoanService(repo, fixedClock(LocalDate.of(2023,1,20)));

	    User u = new User("Ali", "1", "a@test.com");
	    Book b = new Book("Java", "J", "111");

	    repo.addLoan(new Loan(
	            u, b,
	            LocalDate.of(2023,1,1),
	            LocalDate.of(2023,1,5)
	    ));

	    assertTrue(service.userHasAnyOverdue(u));
	}

	@Test
	void testUserHasOverdueCDOnly() {
	    LoanRepository repo = new LoanRepository();
	    LoanService service = new LoanService(repo, fixedClock(LocalDate.of(2023,1,20)));

	    User u = new User("Ali", "1", "a@test.com");
	    CD cd = new CD("Music", "Artist", "C1");

	    repo.addCDLoan(new LoanCD(
	            u, cd,
	            LocalDate.of(2023,1,1),
	            LocalDate.of(2023,1,5)
	    ));

	    assertTrue(service.userHasAnyOverdue(u));
	}
	@Test
	void testIsOverdueBookFalse() {
	    Loan loan = new Loan(
	            new User("Ali","1","a@test.com"),
	            new Book("Java","J","111"),
	            LocalDate.of(2023,1,1),
	            LocalDate.of(2023,2,1)
	    );
	    LoanService service = new LoanService(new LoanRepository(),
	            fixedClock(LocalDate.of(2023,1,15)));

	    assertFalse(service.isOverdue(loan));
	}

	@Test
	void testIsOverdueBookTrue() {
	    Loan loan = new Loan(
	            new User("Ali","1","a@test.com"),
	            new Book("Java","J","111"),
	            LocalDate.of(2023,1,1),
	            LocalDate.of(2023,1,5)
	    );
	    LoanService service = new LoanService(new LoanRepository(),
	            fixedClock(LocalDate.of(2023,1,20)));

	    assertTrue(service.isOverdue(loan));
	}

	@Test
	void testIsOverdueCDTrue() {
	    LoanCD loan = new LoanCD(
	            new User("Ali","1","a@test.com"),
	            new CD("Music","Art","C1"),
	            LocalDate.of(2023,1,1),
	            LocalDate.of(2023,1,5)
	    );
	    LoanService service = new LoanService(new LoanRepository(),
	            fixedClock(LocalDate.of(2023,1,20)));

	    assertTrue(service.isOverdue(loan));
	}

	@Test
	void testIsOverdueCDFalse() {
	    LoanCD loan = new LoanCD(
	            new User("Ali","1","a@test.com"),
	            new CD("Music","Art","C1"),
	            LocalDate.of(2023,1,1),
	            LocalDate.of(2023,2,1)
	    );
	    LoanService service = new LoanService(new LoanRepository(),
	            fixedClock(LocalDate.of(2023,1,15)));

	    assertFalse(service.isOverdue(loan));
	}
	@Test
	void testOverdueDaysBookZero() {
	    Loan loan = new Loan(
	            new User("Ali","1","a@test.com"),
	            new Book("Java","J","111"),
	            LocalDate.of(2023,1,1),
	            LocalDate.of(2023,2,1)
	    );

	    LoanService service = new LoanService(new LoanRepository(),
	            fixedClock(LocalDate.of(2023,1,10)));

	    assertEquals(0, service.getOverdueDays(loan));
	}

	@Test
	void testOverdueDaysCDZero() {
	    LoanCD loan = new LoanCD(
	            new User("Ali","1","a@test.com"),
	            new CD("Music","Art","C1"),
	            LocalDate.of(2023,1,1),
	            LocalDate.of(2023,2,1)
	    );

	    LoanService service = new LoanService(new LoanRepository(),
	            fixedClock(LocalDate.of(2023,1,20)));

	    assertEquals(0, service.getOverdueDays(loan));
	}

	@Test
	void testOverdueDaysCDPositive() {
	    LoanCD loan = new LoanCD(
	            new User("Ali","1","a@test.com"),
	            new CD("Music","Art","C1"),
	            LocalDate.of(2023,1,1),
	            LocalDate.of(2023,1,5)
	    );

	    LoanService service = new LoanService(new LoanRepository(),
	            fixedClock(LocalDate.of(2023,1,10)));

	    assertEquals(5, service.getOverdueDays(loan));
	}
	@Test
	void testCalculateFineBookZero() {
	    Loan loan = new Loan(
	            new User("Ali","1","a@test.com"),
	            new Book("Java","J","111"),
	            LocalDate.of(2023,1,1),
	            LocalDate.of(2023,2,1)
	    );

	    LoanService service = new LoanService(new LoanRepository(),
	            fixedClock(LocalDate.of(2023,1,10)));

	    assertEquals(0, service.calculateFine(loan));
	}

	@Test
	void testCalculateFineCDZero() {
	    LoanCD loan = new LoanCD(
	            new User("Ali","1","a@test.com"),
	            new CD("Music","Art","C1"),
	            LocalDate.of(2023,1,1),
	            LocalDate.of(2023,2,1)
	    );

	    LoanService service = new LoanService(new LoanRepository(),
	            fixedClock(LocalDate.of(2023,1,10)));

	    assertEquals(0, service.calculateFine(loan));
	}

	@Test
	void testCalculateFineCDPositive() {
	    LoanCD loan = new LoanCD(
	            new User("Ali","1","a@test.com"),
	            new CD("Music","Art","C1"),
	            LocalDate.of(2023,1,1),
	            LocalDate.of(2023,1,5)
	    );

	    LoanService service = new LoanService(new LoanRepository(),
	            fixedClock(LocalDate.of(2023,1,10)));

	    assertEquals(100, service.calculateFine(loan)); // 5 days * 20
	}
	@Test
	void testGetOverdueBookLoansEmpty() {
	    LoanRepository repo = new LoanRepository();
	    LoanService service = new LoanService(repo, fixedClock(LocalDate.now()));

	    assertEquals(0, service.getOverdueBookLoans().size());
	}

	@Test
	void testGetOverdueCDLoansEmpty() {
	    LoanRepository repo = new LoanRepository();
	    LoanService service = new LoanService(repo, fixedClock(LocalDate.now()));

	    assertEquals(0, service.getOverdueCDLoans().size());
	}

	@Test
	void testGetOverdueBookLoansNonEmpty() {
	    LoanRepository repo = new LoanRepository();
	    LoanService service = new LoanService(repo, fixedClock(LocalDate.of(2023,1,20)));

	    User u = new User("Ali","1","a@test.com");
	    Book b = new Book("Java","J","111");

	    repo.addLoan(new Loan(
	            u, b, LocalDate.of(2023,1,1), LocalDate.of(2023,1,5)
	    ));

	    assertEquals(1, service.getOverdueBookLoans().size());
	}

	@Test
	void testGetOverdueCDLoansNonEmpty() {
	    LoanRepository repo = new LoanRepository();
	    LoanService service = new LoanService(repo, fixedClock(LocalDate.of(2023,1,20)));

	    User u = new User("Ali","1","a@test.com");
	    CD cd = new CD("Music","Art","C1");

	    repo.addCDLoan(new LoanCD(
	            u, cd, LocalDate.of(2023,1,1), LocalDate.of(2023,1,5)
	    ));

	    assertEquals(1, service.getOverdueCDLoans().size());
	}
	
	@Test
	void testUserHasOverdueBooksOnly() {
	    LoanRepository repo = new LoanRepository();
	    LoanService service = new LoanService(repo, fixedClock(LocalDate.of(2023, 1, 20)));

	    User u = new User("Ali", "1", "ali@test.com");
	    Book b = new Book("Java", "James", "111");

	    // Overdue book
	    repo.addLoan(new Loan(
	            u, b,
	            LocalDate.of(2023, 1, 1),
	            LocalDate.of(2023, 1, 5)
	    ));

	    assertTrue(service.userHasAnyOverdue(u));
	}
	@Test
	void testUserHasOverdueCDsOnly() {
	    LoanRepository repo = new LoanRepository();
	    LoanService service = new LoanService(repo, fixedClock(LocalDate.of(2023, 1, 20)));

	    User u = new User("Ali", "1", "ali@test.com");
	    CD cd = new CD("Music", "Artist", "CD1");

	    // Overdue CD
	    repo.addCDLoan(new LoanCD(
	            u, cd,
	            LocalDate.of(2023, 1, 1),
	            LocalDate.of(2023, 1, 5)
	    ));

	    assertTrue(service.userHasAnyOverdue(u));
	}






}
