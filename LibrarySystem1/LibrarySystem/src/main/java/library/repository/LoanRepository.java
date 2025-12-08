package library.repository;

import library.domain.Loan;
import library.domain.LoanCD;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository responsible for storing and managing all loan records
 * in the library system. This includes:
 * <ul>
 *     <li>Book loans (Sprint 2)</li>
 *     <li>CD loans (Sprint 5)</li>
 * </ul>
 *
 * The repository maintains two separate collections:
 * <ul>
 *     <li>{@code bookLoans}: stores {@link Loan} objects for books</li>
 *     <li>{@code cdLoans}: stores {@link LoanCD} objects for CDs</li>
 * </ul>
 *
 * This design keeps backward compatibility with Sprint 2 tests
 * (which depend on {@link #addLoan(Loan)} and {@link #getAllLoans()}),
 * while still supporting the extended media system introduced in Sprint 5.
 *
 * @author Lojain
 
 * @version 1.0
 */
public class LoanRepository {

    /** List containing all book loan records. */
    private List<Loan> bookLoans = new ArrayList<>();

    /** List containing all CD loan records. */
    private List<LoanCD> cdLoans = new ArrayList<>();

    // ============================================================
    //                   BOOK LOAN OPERATIONS
    // ============================================================

    /**
     * Adds a book loan to the repository.
     * <p>
     * This method exists primarily for Sprint 2 compatibility
     * because early tests call {@code addLoan()} instead of {@code addBookLoan()}.
     *
     * @param loan The loan object representing a borrowed book.
     */
    public void addLoan(Loan loan) {
        bookLoans.add(loan);
    }

    /**
     * Adds a book loan to the repository.
     *
     * @param loan Book loan object to store.
     */
    public void addBookLoan(Loan loan) {
        bookLoans.add(loan);
    }

    /**
     * Returns all book loans.
     * <p>
     * This method is required to maintain compatibility with
     * early (Sprint 2) tests and logic.
     *
     * @return List of all book loans.
     */
    public List<Loan> getAllLoans() {
        return bookLoans;
    }

    /**
     * Returns all book loans (preferred method for newer sprints).
     *
     * @return List of Loan objects representing book loans.
     */
    public List<Loan> getAllBookLoans() {
        return bookLoans;
    }

    // ============================================================
    //                    CD LOAN OPERATIONS
    // ============================================================

    /**
     * Adds a CD loan to the repository.
     *
     * @param loan LoanCD instance representing the borrowed CD.
     */
    public void addCDLoan(LoanCD loan) {
        cdLoans.add(loan);
    }

    /**
     * Returns all CD loans stored in the repository.
     *
     * @return List of LoanCD objects.
     */
    public List<LoanCD> getAllCDLoans() {
        return cdLoans;
    }
}
