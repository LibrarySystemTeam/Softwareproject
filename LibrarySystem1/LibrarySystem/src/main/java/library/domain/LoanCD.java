package library.domain;

import java.time.LocalDate;

/**
 * Represents a loan record for a borrowed CD in the library system.
 * <p>
 * This class is the CD-specific equivalent of {@link Loan},
 * introduced in Sprint 5 as part of the mixed media extension.
 * <br><br>
 * A {@code LoanCD} stores:
 * <ul>
 *     <li>The user borrowing the CD</li>
 *     <li>The CD being borrowed</li>
 *     <li>The date of borrowing</li>
 *     <li>The due date for returning the CD</li>
 * </ul>
 *
 * Borrowing rules for CDs (defined in {@link CD}):
 * <ul>
 *     <li>Borrow duration: 7 days</li>
 *     <li>Daily fine: 20 NIS per overdue day</li>
 * </ul>
 *
 * Used heavily by:
 * <ul>
 *     <li>Sprint 5 – Borrow CD</li>
 *     <li>LoanService – Mixed overdue detection</li>
 *     <li>ReminderService – Notifications</li>
 * </ul>
 *
 * @see Loan for book-based loan records.
 * @see CD for media-specific behavior.
 *@author Lojain

 * @version 1.0
 */
public class LoanCD {

    /** The user borrowing the CD. */
    private User user;

    /** The CD being borrowed. */
    private CD cd;

    /** The date when borrowing happened. */
    private LocalDate borrowDate;

    /** The due date when the CD must be returned. */
    private LocalDate dueDate;

    /**
     * Constructs a CD loan record.
     *
     * @param user       user borrowing the CD
     * @param cd         CD being borrowed
     * @param borrowDate date the CD was borrowed
     * @param dueDate    due date for returning the CD
     */
    public LoanCD(User user, CD cd, LocalDate borrowDate, LocalDate dueDate) {
        this.user = user;
        this.cd = cd;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
    }

    /**
     * Returns the user who borrowed this CD.
     *
     * @return borrowing user
     */
    public User getUser() {
        return user;
    }

    /**
     * Returns the CD being borrowed.
     *
     * @return borrowed CD
     */
    public CD getCD() {
        return cd;
    }

    /**
     * Returns the date when the CD was borrowed.
     *
     * @return borrow date
     */
    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    /**
     * Returns the due date for returning the CD.
     *
     * @return due date
     */
    public LocalDate getDueDate() {
        return dueDate;
    }
}
