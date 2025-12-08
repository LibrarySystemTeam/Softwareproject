package library.test;

import java.util.ArrayList;
import java.util.List;
import library.communication.NotificationSender;

public class MockEmailServer implements NotificationSender {

    private List<String> sentMessages = new ArrayList<>();

    @Override
    public void send(String email, String message) {
        sentMessages.add(email + ": " + message);
    }

    public List<String> getSentMessages() {
        return sentMessages;
    }
}
