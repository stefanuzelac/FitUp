package com.example.fitnessapp2;

public class UserSessionManager {
    private static UserSessionManager instance = null;
    private User currentUser;

    private UserSessionManager() { }

    public static UserSessionManager getInstance() {
        if (instance == null) {
            instance = new UserSessionManager();
        }
        return instance;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // Other session management methods as necessary, like clear session, etc.
}
