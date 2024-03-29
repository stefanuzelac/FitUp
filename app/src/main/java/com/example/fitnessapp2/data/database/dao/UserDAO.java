package com.example.fitnessapp2.data.database.dao;

import com.example.fitnessapp2.data.model.User;

import java.util.List;

public interface UserDAO {
    boolean addUser(User user);
    User getUser(int id);
    User getUserByEmailAndPassword(String email, String password); // Added method
    List<User> getAllUsers();
    void updateUser(User user);
    void deleteUser(User user);
}
