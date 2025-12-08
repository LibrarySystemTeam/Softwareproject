package library.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import library.domain.CD;

class CDDomainTest {

    @Test
    void testCdBorrowFlag() {
        CD cd = new CD("CD1", "Artist", "55");

        assertFalse(cd.isBorrowed());

        cd.setBorrowed(true);
        assertTrue(cd.isBorrowed());

        cd.setBorrowed(false);
        assertFalse(cd.isBorrowed());
    }
}
