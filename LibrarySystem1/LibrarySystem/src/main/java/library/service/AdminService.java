package library.service;

import library.domain.Admin;

public class AdminService {

    private Admin admin = new Admin();

    public boolean login(String user, String pass) {
        return admin.login(user, pass);
    }

    public void logout() {
        admin.logout();
    }

    public boolean isLoggedIn() {
        return admin.isLoggedIn();
    }
}

