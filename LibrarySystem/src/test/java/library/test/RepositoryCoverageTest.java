package library.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import library.domain.Book;
import library.domain.CD;
import library.domain.User;

import library.repository.BookRepository;
import library.repository.CDRepository;
import library.repository.UserRepository;

public class RepositoryCoverageTest {

    // ==============================
    // 📘 TEST: BookRepository
    // ==============================
    @Test
    void testBookRepository() {
        BookRepository repo = new BookRepository();

        Book b1 = new Book("Java", "James", "111");
        b1.setQuantity(1);

        Book b2 = new Book("Python", "Mark", "222");
        b2.setQuantity(1);

        repo.add(b1);
        repo.add(b2);

        // ✓ search()
        assertEquals(1, repo.search("Java").size());
        assertEquals(1, repo.search("Python").size());
        assertEquals(0, repo.search("Ruby").size());

        // ✓ getAll()
        assertEquals(2, repo.getAll().size());

        // ✓ findByISBN()
        assertNotNull(repo.findByISBN("111"));
        assertNull(repo.findByISBN("999"));
    }

    // ==============================
    // 💿 TEST: CDRepository
    // ==============================
    @Test
    void testCDRepository() {
        CDRepository repo = new CDRepository();

        CD cd1 = new CD("Hits", "Artist1", "CD1");
        CD cd2 = new CD("Soft", "Artist2", "CD2");

        repo.add(cd1);
        repo.add(cd2);

        // ✓ getAll()
        assertEquals(2, repo.getAll().size());

        // ✓ findById()
        assertNotNull(repo.findById("CD1"));
        assertNull(repo.findById("999"));
    }

    // ==============================
    // 👤 TEST: UserRepository
    // ==============================
    @Test
    void testUserRepository() {
        UserRepository repo = new UserRepository();

        User u1 = new User("Lojain", "1", "a@mail");
        User u2 = new User("Sara", "2", "b@mail");

        repo.addUser(u1);
        repo.addUser(u2);

        // ✓ getAllUsers()
        assertEquals(2, repo.getAllUsers().size());

        // ✓ findById()
        assertNotNull(repo.findById("1"));
        assertNull(repo.findById("999"));
    }
}
