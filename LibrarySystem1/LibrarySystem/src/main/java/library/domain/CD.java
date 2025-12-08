package library.domain;

/**
 * Represents a CD media item in the library system.
 * <p>
 * A {@code CD} is a type of {@link Media} with its own borrowing rules
 * introduced in Sprint 5 as part of the media extension feature.
 * <br><br>
 * Borrowing rules for CDs:
 * <ul>
 *     <li>Borrow duration: 7 days</li>
 *     <li>Daily overdue fine: 20 NIS</li>
 * </ul>
 *
 * This class overrides the borrowing and fine logic from {@link Media},
 * demonstrating the use of polymorphism in the project.
 *
 * @author Lojain
 * @version 1.0
 */
public class CD extends Media {

    /**
     * Constructs a new CD with title, artist name, and a unique ID.
     *
     * @param title  the CD title
     * @param artist the primary creator (artist)
     * @param id     the unique identifier for the CD
     */
    public CD(String title, String artist, String id) {
        super(title, artist, id);
    }

    /**
     * Returns the name of the CD's artist.
     * <p>
     * This method simply maps to {@link Media#getCreator()}.
     *
     * @return the artist name
     */
    public String getArtist() {
        return getCreator();
    }

    /**
     * Returns the borrowing duration for a CD.
     *
     * @return 7 days (CD borrowing period)
     */
    @Override
    public int getBorrowDays() {
        return 7;
    }

    /**
     * Returns the daily fine for overdue CDs.
     *
     * @return 20 NIS per overdue day
     */
    @Override
    public int getDailyFine() {
        return 20;
    }
}
