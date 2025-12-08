package library.domain;

/**
 * Abstract base class representing a generic media item in the library.
 * <p>
 * This class provides shared attributes and behaviors for all media types such as:
 * <ul>
 *     <li>Title</li>
 *     <li>Creator (author, artist, etc.)</li>
 *     <li>Unique ID</li>
 *     <li>Borrowed state</li>
 * </ul>
 * <br>
 * Subclasses such as {@link Book} and {@link CD} implement specific borrowing
 * rules by overriding:
 * <ul>
 *     <li>{@link #getBorrowDays()}</li>
 *     <li>{@link #getDailyFine()}</li>
 * </ul>
 *
 * This abstraction is part of the Sprint 5 extension (Mixed Media Handling),
 * enabling polymorphism when calculating fines and due dates.
 *
 * @author Lojain

 * @version 1.0
 */
public abstract class Media {

    /** The title of the media item. */
    protected String title;

    /** The creator (author, artist, producer… depending on media type). */
    protected String creator;

    /** Unique identifier for the media (ISBN, CD-ID, etc.). */
    protected String id;

    /** Indicates whether the media item is currently borrowed. */
    protected boolean borrowed = false;

    /**
     * Constructs a media object with shared fields.
     *
     * @param title   media title
     * @param creator creator name (author, artist, etc.)
     * @param id      unique media identifier
     */
    public Media(String title, String creator, String id) {
        this.title = title;
        this.creator = creator;
        this.id = id;
    }

    /**
     * Returns the media title.
     *
     * @return title of the media
     */
    public String getTitle() { 
        return title; 
    }

    /**
     * Returns the creator's name (author, artist, etc.).
     *
     * @return creator name
     */
    public String getCreator() { 
        return creator; 
    }

    /**
     * Returns the unique media identifier.
     *
     * @return ID string
     */
    public String getId() { 
        return id; 
    }

    /**
     * Indicates whether the media is currently borrowed.
     *
     * @return true if borrowed, false otherwise
     */
    public boolean isBorrowed() { 
        return borrowed; 
    }

    /**
     * Sets the borrowed status of the media.
     *
     * @param b true for borrowed, false for returned
     */
    public void setBorrowed(boolean b) { 
        borrowed = b; 
    }

    /**
     * Returns the number of days this media can be borrowed.
     * <br>
     * Implemented differently for each media type:
     * <ul>
     *     <li>Books → 28 days</li>
     *     <li>CDs → 7 days</li>
     * </ul>
     *
     * @return borrowing duration in days
     */
    public abstract int getBorrowDays();

    /**
     * Returns the daily overdue fine for this media type.
     * <br>
     * Implemented differently for each media type:
     * <ul>
     *     <li>Books → 10 NIS/day</li>
     *     <li>CDs → 20 NIS/day</li>
     * </ul>
     *
     * @return fine amount per overdue day
     */
    public abstract int getDailyFine();
}
