package library.domain;

public class Admin {

    private static final String username = "admin";
    private static final String password = "1234";
    private boolean loggedIn = false;

    public boolean login(String user, String pass) {
        if (user.equals(username) && pass.equals(password)) {
            loggedIn = true;
        }
        return loggedIn;
    }

    public void logout() {
        loggedIn = false;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
}

