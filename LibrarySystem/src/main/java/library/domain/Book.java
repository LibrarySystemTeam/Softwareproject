package library.domain;

/**
 * Represents a physical book in the library system.
 * <p>
 * A {@code Book} contains basic bibliographic information and a flag
 * indicating whether it is currently borrowed. This class is part of
 * the Domain Layer and is used by multiple sprints including:
 * <ul>
 *     <li>Sprint 1 – Book management and search functionality</li>
 *     <li>Sprint 2 – Borrowing and overdue detection</li>
 *     <li>Sprint 5 – Mixed media handling</li>
 * </ul>
 *
 * Instances of this class are persisted using {@link library.repository.BookRepository}.
 *
 * author Lojain
 *
 * @version 1.0
 */
public class Book {

    /** Title of the book. */
    private String title;

    /** Author of the book. */
    private String author;

    /** Unique ISBN of the book. */
    private String isbn;

    /** Indicates whether the book is currently borrowed. */
    // NOTE: kept for compatibility with comment, but logic replaced by quantity system
    private boolean isBorrowed = false;

    // ================================
    // NEW FIELDS FOR MULTIPLE COPIES
    // ================================

    private int quantity = 1;       // total number of copies
    private int borrowedCount = 0;  // how many are borrowed

    /**
     * Constructs a new Book with title, author, and ISBN.
     *
     * @param title  the title of the book
     * @param author the author of the book
     * @param isbn   the ISBN identifier for the book
     */
    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    /**
     * Returns the book's title.
     *
     * @return the title of the book
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the book's author.
     *
     * @return the name of the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Returns the book's ISBN.
     *
     * @return the ISBN identifier
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Indicates whether the book is currently borrowed.
     *
     * @return true if the book is borrowed, otherwise false
     */
    public boolean isBorrowed() {
        return borrowedCount > 0;
    }

    /**
     * Sets the borrowed status of the book.
     *
     * @param b true if the book is being borrowed, false if returned
     */
    public void setBorrowed(boolean b) {
        // kept for backward compatibility
        if (b) borrowOneCopy();
        else returnOneCopy();
        this.isBorrowed = b;
    }

    // ================================
    // QUANTITY LOGIC (NEW)
    // ================================

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int q) {
        if (q < 1) throw new IllegalArgumentException("Quantity must be >= 1");
        this.quantity = q;
    }

    public int getBorrowedCount() {
        return borrowedCount;
    }

    public boolean hasAvailableCopy() {
        return borrowedCount < quantity;
    }

    public void borrowOneCopy() {
        if (!hasAvailableCopy())
            throw new IllegalStateException("No available copies!");
        borrowedCount++;
    }

    public void returnOneCopy() {
        if (borrowedCount > 0)
            borrowedCount--;
    }
}
