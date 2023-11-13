package com.example.fitnessapp2.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    private DatabaseHelper databaseHelper;

    public UserDAOImpl(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public boolean addUser(User user) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", user.getName());
        contentValues.put("last_name", user.getLastName());
        contentValues.put("email", user.getEmail());
        contentValues.put("password", user.getPassword());
        contentValues.put("phone", user.getPhone());
        contentValues.put("dob", user.getDob());
        contentValues.put("gender", user.getGender());
        contentValues.put("height", user.getHeight());
        contentValues.put("weight", user.getWeight());
        contentValues.put("profile_picture", user.getProfilePicture());
        // No need to put the ID, as it's auto-incremented.
        long result = db.insert("users", null, contentValues);
        db.close();
        return result != -1; // return true if insert is successful
    }


    @Override
    public User getUser(int id) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(
                "users",
                new String[]{"id", "name", "last_name", "email", "password", "phone", "dob", "gender", "height", "weight", "profile_picture"},
                "id = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        User user = null;
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            if (idIndex < 0) {
                // Handle error - column not found
            } else {
                user = new User(
                        cursor.getInt(idIndex),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("last_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("email")),
                        cursor.getString(cursor.getColumnIndexOrThrow("password")),
                        cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                        cursor.getString(cursor.getColumnIndexOrThrow("dob")),
                        cursor.getString(cursor.getColumnIndexOrThrow("gender")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("height")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("weight")),
                        cursor.getBlob(cursor.getColumnIndexOrThrow("profile_picture")) // profile_picture should be a Blob if it's storing binary data
                );
            }
        }
        cursor.close();
        db.close();
        return user;
    }


    @Override
    public User getUserByEmailAndPassword(String email, String password) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(
                "users",
                new String[]{"id", "name", "last_name", "email", "password", "phone", "dob", "gender", "height", "weight", "profile_picture"},
                "email = ? AND password = ?",
                new String[]{email, password},
                null,
                null,
                null
        );

        User user = null;
        if (cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("last_name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("email")),
                    cursor.getString(cursor.getColumnIndexOrThrow("password")),
                    cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                    cursor.getString(cursor.getColumnIndexOrThrow("dob")),
                    cursor.getString(cursor.getColumnIndexOrThrow("gender")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("height")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("weight")),
                    cursor.getBlob(cursor.getColumnIndexOrThrow("profile_picture")) // Assuming 'profile_picture' is a BLOB
            );
        }
        cursor.close();
        db.close();
        return user;
    }


    @Override
    public List<User> getAllUsers() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<User> userList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM users", null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("last_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("email")),
                        cursor.getString(cursor.getColumnIndexOrThrow("password")),
                        cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                        cursor.getString(cursor.getColumnIndexOrThrow("dob")),
                        cursor.getString(cursor.getColumnIndexOrThrow("gender")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("height")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("weight")),
                        cursor.getBlob(cursor.getColumnIndexOrThrow("profile_picture")) // Assuming 'profile_picture' is a BLOB
                );
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userList;
    }



    @Override
    public void updateUser(User user) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", user.getName());
        contentValues.put("last_name", user.getLastName());
        contentValues.put("email", user.getEmail());
        contentValues.put("password", user.getPassword());
        contentValues.put("phone", user.getPhone());
        contentValues.put("dob", user.getDob());
        contentValues.put("gender", user.getGender());
        contentValues.put("height", user.getHeight());
        contentValues.put("weight", user.getWeight());
        contentValues.put("profile_picture", user.getProfilePicture());

        int result = db.update("users", contentValues, "id = ?", new String[]{String.valueOf(user.getId())});
        if (result <= 0) {
            db.close(); // Ensure you close the database before throwing an exception
            throw new SQLException("Failed to update user");
        }
        db.close();
    }



    @Override
    public void deleteUser(User user) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int deletedRows = db.delete("users", "id = ?", new String[]{String.valueOf(user.getId())});
        if (deletedRows == 0) {
            throw new SQLException("Failed to delete user");
        }
        db.close();
    }

}