package library.test;
import library.domain.Book;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import library.repository.BookRepository;
import library.service.BookService;

class BookServiceTest {

    @Test
    void testAddBookThroughService() {
        BookRepository repo = new BookRepository();
        BookService service = new BookService(repo);

        service.addBook("Java", "James", "111");

        assertEquals(1, repo.getAll().size());
    }

    @Test
    void testSearchThroughService() {
        BookRepository repo = new BookRepository();
        BookService service = new BookService(repo);

        service.addBook("Java Programming", "James", "111");
        service.addBook("Python", "Mark", "222");

        assertEquals(1, service.search("Java").size());
    }
    @Test
    void testSearchByTitle() {
        BookRepository repo = new BookRepository();
        BookService service = new BookService(repo);

        Book b1 = new Book("Java Programming", "James", "111");
        repo.add(b1);

        var result = service.search("Java");

        assertEquals(1, result.size());
        assertEquals("Java Programming", result.get(0).getTitle());
    }
    @Test
    void testSearchByAuthor() {
        BookRepository repo = new BookRepository();
        BookService service = new BookService(repo);

        Book b1 = new Book("OOP", "Martin Fowler", "222");
        repo.add(b1);

        var result = service.search("Fowler");

        assertEquals(1, result.size());
    }
    @Test
    void testSearchByISBN() {
        BookRepository repo = new BookRepository();
        BookService service = new BookService(repo);

        Book b1 = new Book("OOP", "James", "999");
        repo.add(b1);

        var result = service.search("999");

        assertEquals(1, result.size());
    }
    @Test
    void testSearchNoResults() {
        BookRepository repo = new BookRepository();
        BookService service = new BookService(repo);

        repo.add(new Book("Java", "James", "111"));

        var result = service.search("Python");

        assertTrue(result.isEmpty());
    }
    @Test
    void testSearchEmptyKeyword() {
        BookRepository repo = new BookRepository();
        BookService service = new BookService(repo);

        repo.add(new Book("Java", "James", "111"));
        repo.add(new Book("Python", "Mark", "222"));

        var result = service.search("");

        assertEquals(2, result.size());  // إذا كانت الدالة ترجع كل الكتب
    }

}


