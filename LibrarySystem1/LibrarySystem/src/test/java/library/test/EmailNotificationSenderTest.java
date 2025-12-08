package library.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import library.communication.EmailNotificationSender;

public class EmailNotificationSenderTest {

    @Test
    public void testSendDoesNotThrow() {
        EmailNotificationSender sender = new EmailNotificationSender();

        // نستخدم إيميل وهمي حتى ما يصير إرسال فعلي
        String fakeEmail = "test@example.com";
        String fakeMessage = "Testing message";

        // المهم: أن الدالة تعمل بدون أخطاء
        assertDoesNotThrow(() -> sender.send(fakeEmail, fakeMessage));
    }
}
