package library.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import library.domain.Book;

class BookDomainTest {

    @Test
    void testBorrowAndReturnBookBehavior() {
        Book book = new Book("Test", "Author", "123");
        book.setQuantity(2);

        // initially available
        assertTrue(book.hasAvailableCopy());

        // borrow 1
        book.borrowOneCopy();
        assertEquals(1, book.getBorrowedCount());
        assertTrue(book.hasAvailableCopy());

        // borrow 2nd
        book.borrowOneCopy();
        assertEquals(2, book.getBorrowedCount());
        assertFalse(book.hasAvailableCopy());

        // return 1
        book.returnOneCopy();
        assertEquals(1, book.getBorrowedCount());
        assertTrue(book.hasAvailableCopy());
    }

    @Test
    void testReturnWhenNothingBorrowed() {
        Book book = new Book("X", "Y", "999");
        book.setQuantity(1);

        book.returnOneCopy(); 
        assertEquals(0, book.getBorrowedCount());
    }
    @Test
    void testSetBorrowedTrueAndFalse() {
        Book b = new Book("Java", "James", "111");

        b.setBorrowed(true);
        assertTrue(b.isBorrowed());

        b.setBorrowed(false);
        assertFalse(b.isBorrowed());
    }

    @Test
    void testSetQuantityPositive() {
        Book b = new Book("Java", "James", "111");

        b.setQuantity(5);
        assertEquals(5, b.getQuantity());
    }

    @Test
    void testSetQuantityNegativeThrowsException() {
        Book b = new Book("Java", "James", "111");
        b.setQuantity(3);

        assertThrows(IllegalArgumentException.class, () -> {
            b.setQuantity(-1);
        });
    }


    @Test
    void testIsBorrowedStates() {
        Book b = new Book("Java", "James", "111");

        b.setBorrowed(false);
        assertFalse(b.isBorrowed());

        b.setBorrowed(true);
        assertTrue(b.isBorrowed());
    }
    @Test
    void testBorrowOneCopySuccess() {
        Book book = new Book("Java", "James", "111");
        book.setQuantity(2); // two copies available

        book.borrowOneCopy(); // should NOT throw

        assertEquals(1, book.getBorrowedCount());
        assertTrue(book.hasAvailableCopy());
    }
    @Test
    void testBorrowOneCopyFailsWhenNoCopyAvailable() {
        Book book = new Book("Java", "James", "111");
        book.setQuantity(1);

        // borrow the only copy
        book.borrowOneCopy();

        // now no copies left → expect exception
        assertThrows(IllegalStateException.class, () -> {
            book.borrowOneCopy();
        });
    }

}
