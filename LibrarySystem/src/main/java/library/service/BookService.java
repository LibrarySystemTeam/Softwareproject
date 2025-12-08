package library.service;

import library.domain.Book;
import library.repository.BookRepository;

import java.util.List;

/**
 * Service layer responsible for managing book-related operations.
 * <p>
 * This class provides core use cases for:
 * <ul>
 *     <li>Adding books to the library collection</li>
 *     <li>Searching books based on keywords</li>
 * </ul>
 * It represents the business logic of Sprint 1.
 *
 * The persistence details are handled by {@link BookRepository}.
 * This class focuses only on application-level operations.
 *
 * @author Lojain
 * @version 1.0
 */
public class BookService {

    private BookRepository repo;

    /**
     * Constructs the service with a BookRepository instance.
     *
     * @param repo Repository used for storing and retrieving books.
     */
    public BookService(BookRepository repo) {
        this.repo = repo;
    }

    /**
     * Adds a new book to the system.
     *
     * @param title  Title of the book.
     * @param author Author of the book.
     * @param isbn   ISBN identifier of the book.
     */
    public void addBook(String title, String author, String isbn) {
        Book book = new Book(title, author, isbn);
        book.setQuantity(1);  // default quantity = 1
        repo.add(book);
    }

    /**
     * NEW — Adds a book with a specific quantity.
     *
     * @param title     Title of the book.
     * @param author    Author name.
     * @param isbn      ISBN identifier.
     * @param quantity  Number of copies available.
     */
    public void addBook(String title, String author, String isbn, int quantity) {
        Book book = new Book(title, author, isbn);
        book.setQuantity(quantity);
        repo.add(book);
    }

    /**
     * Searches for books whose title, author, or ISBN match the given keyword.
     *
     * @param keyword Keyword used for matching books.
     * @return List of books matching the search term.
     */
    public List<Book> search(String keyword) {
        return repo.search(keyword);
    }
}
