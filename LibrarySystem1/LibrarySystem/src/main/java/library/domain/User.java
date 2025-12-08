package library.domain;

/**
 * Represents a library user who can borrow books and CDs.
 * <p>
 * A {@code User} has:
 * <ul>
 *     <li>A name</li>
 *     <li>A unique ID</li>
 *     <li>A fine balance (introduced in Sprint 2)</li>
 *     <li>An email address (added for Sprint 3 real notifications)</li>
 * </ul>
 *
 * This class is used throughout multiple sprints:
 * <ul>
 *     <li><strong>Sprint 1:</strong> Basic search and user creation during borrowing</li>
 *     <li><strong>Sprint 2:</strong> Borrowing rules and fine payments</li>
 *     <li><strong>Sprint 3:</strong> Reminder notifications (grouped by user)</li>
 *     <li><strong>Sprint 4:</strong> Unregister user rules (no fines, no active loans)</li>
 *     <li><strong>Sprint 5:</strong> Mixed media borrowing (Books + CDs)</li>
 * </ul>
 *
 * Users accumulate fines for overdue items, and they must pay all fines
 * before being allowed to borrow again.
 *
 * @see library.domain.Loan
 * @see library.domain.LoanCD
 * 
 * @author Lojain
 * @version 1.0
 */
public class User {

    /** Name of the user. */
    private String name;

    /** Unique user identifier. */
    private String id;

    /** Outstanding fine balance for the user (Sprint 2 feature). */
    private int fineBalance = 0;

    /** Email address used for real notifications (Sprint 3). */
    private String email;

    /**
     * Constructs a new user with a name and unique ID.
     *
     * @param name user's name
     * @param id   user's unique identifier
     * @param email user's email address for notifications
     */
    public User(String name, String id, String email) {
        this.name = name;
        this.id = id;
        this.email = email;
    }

    /** Returns the user's name. */
    public String getName() { 
        return name; 
    }

    /** Returns the user's unique ID. */
    public String getId() { 
        return id; 
    }

    /** Returns the user's email. */
    public String getEmail() { 
        return email; 
    }

    /** Returns the user's outstanding fine balance. */
    public int getFineBalance() { 
        return fineBalance; 
    }

    /** Adds a fine amount to the user's balance. */
    public void addFine(int amount) { 
        fineBalance += amount; 
    }

    /** Reduces the user's fine balance by a payment. */
    public void payFine(int amount) {
        if (amount > fineBalance) {
            fineBalance = 0;
        } else {
            fineBalance -= amount;
        }
        
    }
    public void clearFine() {
        this.fineBalance = 0;
    }

}
