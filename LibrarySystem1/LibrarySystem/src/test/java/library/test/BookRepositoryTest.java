package library.test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import org.junit.jupiter.api.Test;
import library.domain.Book;
import library.repository.BookRepository;

class BookRepositoryTest {

    @Test
    void testAddBook() {
        BookRepository repo = new BookRepository();
        repo.add(new Book("Java", "James", "111"));
        assertEquals(1, repo.getAll().size());
    }

    @Test
    void testSearchBook() {
        BookRepository repo = new BookRepository();

        repo.add(new Book("Java Programming", "James", "111"));
        repo.add(new Book("Python", "Mark", "222"));

        List<Book> result = repo.search("Java");
        assertEquals(1, result.size());
        assertEquals("Java Programming", result.get(0).getTitle());
    }
}

