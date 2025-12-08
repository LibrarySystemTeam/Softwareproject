package library.domain;

import java.time.LocalDate;

/**
 * Represents a loan record for a borrowed book in the library system.
 * <p>
 * A {@code Loan} stores all information related to a book borrowing action,
 * including:
 * <ul>
 *     <li>The user who borrowed the book</li>
 *     <li>The book being borrowed</li>
 *     <li>The date the book was borrowed</li>
 *     <li>The due date of the loan</li>
 * </ul>
 *
 * This class is used throughout:
 * <ul>
 *     <li>Sprint 2 – Borrowing logic and overdue detection</li>
 *     <li>Sprint 3 – Reminder notifications</li>
 *     <li>Sprint 4 – Borrow restrictions</li>
 *     <li>Sprint 5 – Mixed media reporting</li>
 * </ul>
 *
 * For CD loans, see {@link LoanCD}.
 *
 *@author Lojain
 * @version 1.0
 */
public class Loan {

    /** The user who borrowed the book. */
    private User user;

    /** The borrowed book. */
    private Book book;

    /** The date the book was borrowed. */
    private LocalDate borrowDate;

    /** The due date when the book must be returned. */
    private LocalDate dueDate;

    /**
     * Constructs a new Loan for a specific book and user.
     *
     * @param user        the user borrowing the book
     * @param book        the book being borrowed
     * @param borrowDate  the date when the borrowing occurred
     * @param dueDate     the due date for returning the book
     */
    public Loan(User user, Book book, LocalDate borrowDate, LocalDate dueDate) {
        this.user = user;
        this.book = book;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
    }

    /**
     * Returns the user associated with this loan.
     *
     * @return the borrowing user
     */
    public User getUser() {
        return user;
    }

    /**
     * Returns the book associated with this loan.
     *
     * @return the borrowed book
     */
    public Book getBook() {
        return book;
    }

    /**
     * Returns the borrowing date.
     *
     * @return date when the book was borrowed
     */
    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    /**
     * Returns the due date for the loan.
     *
     * @return due date for return
     */
    public LocalDate getDueDate() {
        return dueDate;
    }
}
