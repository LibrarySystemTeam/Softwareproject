package library.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import library.strategy.BookFineStrategy;

class FineStrategyTest {

    @Test
    void testBookFineCalculation() {
        BookFineStrategy strategy = new BookFineStrategy();
        assertEquals(30, strategy.calculateFine(3)); // 3 days * 10 NIS
    }
}

