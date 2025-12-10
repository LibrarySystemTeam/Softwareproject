package library.repository;

import library.domain.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Repository responsible for storing and retrieving {@link Book} objects.
 * <p>
 * This class represents the data persistence layer for books within the system.
 * It provides operations for:
 * <ul>
 *     <li>Adding books to the collection</li>
 *     <li>Searching for books by various fields</li>
 *     <li>Retrieving all stored books</li>
 * </ul>
 *
 * Part of Sprint 1 (Book Management).
 *
 * author Lojain
 *
 * @version 1.0
 */
public class BookRepository {

    /** Internal list holding all stored book records. */
    private List<Book> books = new ArrayList<>();

    /**
     * Adds a new book to the repository.
     *
     * @param book the {@link Book} object to store.
     */
    public void add(Book book) {
        // ============================================
        // NEW LOGIC: merge books with same ISBN
        // ============================================
        Book existing = findByISBN(book.getIsbn());
        if (existing != null) {
            // Increase quantity instead of adding duplicate record
            existing.setQuantity(existing.getQuantity() + book.getQuantity());
            return;
        }

        books.add(book);
    }

    /**
     * Searches for books that match the given keyword.
     * <p>
     * A match occurs if the keyword appears in:
     * <ul>
     *     <li>Book title</li>
     *     <li>Author name</li>
     *     <li>ISBN</li>
     * </ul>
     *
     * @param keyword the search term to filter books by.
     * @return a list of books matching the given keyword.
     */
    public List<Book> search(String keyword) {
        return books.stream()
                .filter(b ->
                        b.getTitle().contains(keyword) ||
                        b.getAuthor().contains(keyword) ||
                        b.getIsbn().contains(keyword)
                )
                .collect(Collectors.toList());
    }

    /**
     * Returns all books currently stored in the repository.
     *
     * @return list of all books in the system.
     */
    public List<Book> getAll() {
        return books;
    }

    // ===============================================
    // NEW HELPER METHOD — DOES NOT AFFECT ANY TEST
    // ===============================================
    public Book findByISBN(String isbn) {
        for (Book b : books) {
            if (b.getIsbn().equals(isbn)) return b;
        }
        return null;
    }
}
