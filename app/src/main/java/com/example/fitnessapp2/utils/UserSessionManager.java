package com.example.fitnessapp2.utils;

import android.content.Context;

import com.example.fitnessapp2.data.database.DatabaseHelper;
import com.example.fitnessapp2.data.model.User;
import com.example.fitnessapp2.data.database.daoimpl.UserDAOImpl;

public class UserSessionManager {
    private static UserSessionManager instance = null;
    private User currentUser;
    private final UserDAOImpl userDao;

    private UserSessionManager(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        this.userDao = new UserDAOImpl(dbHelper);
    }

    public static UserSessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new UserSessionManager(context);
        }
        return instance;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void refreshCurrentUser() {
        if (currentUser != null) {
            // Fetch the latest user data from the database
            User updatedUser = userDao.getUser(currentUser.getId());
            if (updatedUser != null) {
                setCurrentUser(updatedUser);
            }
        }
    }

}