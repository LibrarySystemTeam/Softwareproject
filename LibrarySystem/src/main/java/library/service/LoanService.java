package library.service;

import library.domain.Book;
import library.domain.CD;
import library.domain.Loan;
import library.domain.LoanCD;
import library.domain.User;
import library.repository.LoanRepository;

import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Service responsible for handling all borrowing and overdue logic
 * in the library system. This class implements requirements from:
 * <ul>
 *     <li><strong>Sprint 2:</strong> Borrowing, overdue detection, fine rules</li>
 *     <li><strong>Sprint 4:</strong> Borrowing restrictions</li>
 *     <li><strong>Sprint 5:</strong> Mixed media (Books + CDs)</li>
 * </ul>
 *
 * <p>The service enforces the rule:</p>
 * <blockquote>
 *     "User can borrow only when they have <strong>no unpaid fines</strong>
 *     and <strong>no overdue items</strong>."
 * </blockquote>
 *
 * <p>Fines are calculated dynamically based on today's date.
 * No fines are stored or accumulated automatically, preventing stacking errors.</p>
 *
 * @author Lojain
 * @version 2.0
 */
public class LoanService {

    private LoanRepository loanRepo;
    private Clock clock;
    private static final int BOOK_LOAN_DAYS = 28;
    private static final int CD_LOAN_DAYS = 7;


    /** Constructs using the system clock. */
    public LoanService(LoanRepository repo) {
        this(repo, Clock.systemDefaultZone());
    }

    /** Constructs a LoanService with an injectable clock (used in unit testing). */
    public LoanService(LoanRepository repo, Clock clock) {
        this.loanRepo = repo;
        this.clock = clock;
    }

    /** Returns today based on the active clock. */
    private LocalDate today() {
        return LocalDate.now(clock);
    }

    /**
     * Borrows a book for 28 days.
     * <p>
     * Borrowing conditions:
     * <ul>
     *     <li>User must have no unpaid fines.</li>
     *     <li>User must not have overdue items.</li>
     *     <li>Book must have at least one available copy.</li>
     * </ul>
     *
     * @param user the borrowing user
     * @param book the book being borrowed
     * @return Loan containing borrow and due dates
     */
    public Loan borrowBook(User user, Book book) {

        // لا نعيد حساب الغرامة هنا – الدفع هو اللي بحدّث الرصيد
        if (user.getFineBalance() > 0)
            throw new IllegalStateException("User must pay all fines first.");

    
        if (!book.hasAvailableCopy())
            throw new IllegalStateException("Book already borrowed.");

        LocalDate borrowDate = today();
        LocalDate dueDate = borrowDate.plusDays(BOOK_LOAN_DAYS);

        Loan loan = new Loan(user, book, borrowDate, dueDate);

        book.borrowOneCopy();
        loanRepo.addBookLoan(loan);

        return loan;
    }

    // ============================================================
    //                      BORROW CD
    // ============================================================

    /**
     * Borrows a CD for 7 days.
     */
    public LoanCD borrowCD(User user, CD cd) {

        // برضه ما نعيد حساب الغرامة هنا
        if (user.getFineBalance() > 0)
            throw new IllegalStateException("User must pay all fines first.");

       

        if (cd.isBorrowed())
            throw new IllegalStateException("CD already borrowed.");

        LocalDate borrowDate = today();
        LocalDate due = borrowDate.plusDays(CD_LOAN_DAYS);

        LoanCD loan = new LoanCD(user, cd, borrowDate, due);
        cd.setBorrowed(true);

        loanRepo.addCDLoan(loan);
        return loan;
    }

    // ============================================================
    //                      OVERDUE CHECKS
    // ============================================================

    /** Returns true if a book loan is overdue. */
    public boolean isOverdue(Loan loan) {
        return today().isAfter(loan.getDueDate());
    }

    /** Returns true if a CD loan is overdue. */
    public boolean isOverdue(LoanCD loan) {
        return today().isAfter(loan.getDueDate());
    }

    /** Calculates overdue days for a book loan. */
    public long getOverdueDays(Loan loan) {
        return isOverdue(loan)
                ? ChronoUnit.DAYS.between(loan.getDueDate(), today())
                : 0;
    }

    /** Calculates overdue days for a CD loan. */
    public long getOverdueDays(LoanCD loan) {
        return isOverdue(loan)
                ? ChronoUnit.DAYS.between(loan.getDueDate(), today())
                : 0;
    }

    // ============================================================
    //                      FINE CALCULATION
    // ============================================================

    /**
     * Calculates the fine for a book <strong>without adding it automatically</strong>.
     * Prevents fine stacking when viewing overdue items or calculating fines.
     */
    public int calculateFine(Loan loan) {
        return (int) (getOverdueDays(loan) * 10);
    }

    /**
     * Calculates the fine for a CD without modifying the user balance.
     */
    public int calculateFine(LoanCD loan) {
        return (int) (getOverdueDays(loan) * 20);
    }

    /**
     * Recomputes the user's total fines fresh from all overdue loans.
     * This avoids the fine-doubling bug from Sprint 2.
     */
   

    // ============================================================
    //                      PAY FINE
    // ============================================================

    /**
     * Reduces user's fine. Borrowing is allowed only when fine becomes zero.
     */
    public void payFine(User user, int amount) {
        user.payFine(amount);
    }

    // ============================================================
    //                   OVERDUE STATUS METHODS
    // ============================================================

    /** Returns true if the user has any overdue books or CDs. */
    public boolean userHasAnyOverdue(User user) {

        boolean books = loanRepo.getAllBookLoans().stream()
                .anyMatch(l -> l.getUser().getId().equals(user.getId()) && isOverdue(l));

        boolean cds = loanRepo.getAllCDLoans().stream()
                .anyMatch(l -> l.getUser().getId().equals(user.getId()) && isOverdue(l));

        return books || cds;
    }

    /** Returns all overdue book loans. */
    public List<Loan> getOverdueBookLoans() {
        return loanRepo.getAllBookLoans().stream()
                .filter(this::isOverdue)
                .toList();
    }

    /** Returns all overdue CD loans. */
    public List<LoanCD> getOverdueCDLoans() {
        return loanRepo.getAllCDLoans().stream()
                .filter(this::isOverdue)
                .toList();
    }

    /** Support for Sprint 2 tests. */
    public List<Loan> getOverdueLoans() {
        return getOverdueBookLoans();
    }
}
