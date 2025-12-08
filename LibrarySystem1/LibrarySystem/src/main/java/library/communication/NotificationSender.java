package library.communication;

/**
 * General interface for sending notifications (Sprint 3).
 */
public interface NotificationSender {

    /**
     * Sends a notification message to a user email.
     *
     * @param email recipient email
     * @param message message content
     */
    void send(String email, String message);
}
