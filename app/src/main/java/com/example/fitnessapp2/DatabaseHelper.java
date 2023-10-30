package com.example.fitnessapp2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "fitnessapp2.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //called when the database is first created
    @Override
    public void onCreate(SQLiteDatabase db) {
        //create the "users" table with columns for id, name, last_name, email, password, phone, dob, and remember_me
        db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, last_name TEXT, email TEXT, password TEXT, phone TEXT, dob TEXT, age INTEGER, gender TEXT, height INTEGER, weight REAL, profile_picture TEXT, remember_me INTEGER DEFAULT 0)");

        // Create the "workout_logs" table
        db.execSQL("CREATE TABLE IF NOT EXISTS workout_logs (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, exercise TEXT, sets INTEGER, reps INTEGER, weight REAL, date TEXT, FOREIGN KEY(user_id) REFERENCES users(id))");

        // Create the "meal_logs" table
        db.execSQL("CREATE TABLE IF NOT EXISTS meal_logs (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, meal TEXT, fats DOUBLE, carbs DOUBLE, protein DOUBLE, date TEXT, FOREIGN KEY(user_id) REFERENCES users(id))");

    }

    //called when the database needs to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //if the old version is less than 2 add a column called "remember_me" to the "users" table with a default value of 0
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + "users" + " ADD COLUMN remember_me INTEGER DEFAULT 0");
        }
    }

    //inserts a new row into the "users" table with the given values
    public long insertData(String name, String lastName, String email, String password, String phone, String dob) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("last_name", lastName);
        contentValues.put("email", email);
        contentValues.put("password", password);
        contentValues.put("phone", phone);
        contentValues.put("dob", dob);
        long result = db.insert("users", null, contentValues);
        db.close();
        return result;
    }


    //retrieve all rows from the "users" table
    //used this for development cycle
    public Cursor retrieveData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users", null);
        return cursor;
    }

    //update the row in the "users" table with the given id with the given values
    //probably won't be using this because I don't plan to allow this data to be changed after initial process
    public boolean updateData(String id, String name, String last_name, String email, String password, String phone, String dob) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("name", name);
        contentValues.put("last_name", last_name);
        contentValues.put("email", email);
        contentValues.put("password", password);
        contentValues.put("phone", phone);
        contentValues.put("dob", dob);
        db.update("users", contentValues, "id = ?", new String[]{id});
        return true;
    }

    //deletes the row in the "users" table with the given id
    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("users", "id = ?", new String[]{id});
    }

    //method for getting user for future operations, e.g. login process, setting user info within nav menu,
    // setting user name, last name, and age in AccountActivity, and future progress tracking
    public User getUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                "id",
                "name",
                "last_name",
                "email",
                "password",
                "phone",
                "dob",
                "gender",
                "height",
                "weight",
                "profile_picture"
        };

        //define selection and selectionArgs for the query
        String selection = "email" + " = ? AND " + "password" + " = ?";
        String[] selectionArgs = {email, password};

        //execute the query and get a Cursor object
        Cursor cursor = db.query(
                "users",    // Table name
                columns,            // Columns to retrieve
                selection,          // Selection criteria
                selectionArgs,      // Selection arguments
                null,
                null,
                null
        );

        User user = null;

        //check if the query returned any results
        if (cursor.moveToFirst()) {
            //get the indexes of the columns we're interested in
            int idIndex = cursor.getColumnIndex("id");
            int nameIndex = cursor.getColumnIndex("name");
            int lastNameIndex = cursor.getColumnIndex("last_name");
            int emailIndex = cursor.getColumnIndex("email");
            int passwordIndex = cursor.getColumnIndex("password");
            int phoneIndex = cursor.getColumnIndex("phone");
            int dobIndex = cursor.getColumnIndex("dob");
            int genderIndex = cursor.getColumnIndex("gender");
            int heightIndex = cursor.getColumnIndex("height");
            int weightIndex = cursor.getColumnIndex("weight");
            int profilePictureIndex = cursor.getColumnIndex("profile_picture");

            //check if all the necessary columns were retrieved
            if (idIndex != -1 && nameIndex != -1 && lastNameIndex != -1 && emailIndex != -1 && passwordIndex != -1 && phoneIndex != -1 && dobIndex != -1 && genderIndex != -1 && heightIndex != -1 && weightIndex != -1 && profilePictureIndex != -1) {
                int id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                String lastName = cursor.getString(lastNameIndex);
                String emailFromDB = cursor.getString(emailIndex);
                String passwordFromDB = cursor.getString(passwordIndex);
                String phone = cursor.getString(phoneIndex);
                String dob = cursor.getString(dobIndex);
                String gender = cursor.getString(genderIndex);

                //use the retrieved height and weight values
                int height = cursor.getInt(heightIndex);
                int weight = cursor.getInt(weightIndex);
                byte[] profilePicture = cursor.getBlob(profilePictureIndex);

                //create a new UserProfile object with the retrieved values
                user = new User(id, name, lastName, emailFromDB, passwordFromDB, phone, dob, gender, height, weight, profilePicture);
                Log.d("DBHelper", "User found: " + user.toString()); // Ensure your User class has a proper toString method.
            }

        } else {
            Log.d("DBHelper", "No user found with the provided credentials.");
        }

        //close cursor and the database connection
        cursor.close();
        db.close();

        //return the User object or null if no results were found
        return user;
    }

    /*
    //print data for debugging purposes
    public void printData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users", null);

        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    int idIndex = cursor.getColumnIndex("id");
                    int nameIndex = cursor.getColumnIndex("name");
                    int lastNameIndex = cursor.getColumnIndex("last_name");
                    int emailIndex = cursor.getColumnIndex("email");
                    int passwordIndex = cursor.getColumnIndex("password");
                    int phoneIndex = cursor.getColumnIndex("phone");
                    int dobIndex = cursor.getColumnIndex("dob");

                    String id = idIndex != -1 ? cursor.getString(idIndex) : "";
                    String name = nameIndex != -1 ? cursor.getString(nameIndex) : "";
                    String lastName = lastNameIndex != -1 ? cursor.getString(lastNameIndex) : "";
                    String email = emailIndex != -1 ? cursor.getString(emailIndex) : "";
                    String password = passwordIndex != -1 ? cursor.getString(passwordIndex) : "";
                    String phone = phoneIndex != -1 ? cursor.getString(phoneIndex) : "";
                    String dob = dobIndex != -1 ? cursor.getString(dobIndex) : "";

                    Log.d("Database", "User: " + id + " - " + name + " " + lastName + ", " + email + ", " + password + ", " + phone + ", " + dob);
                } while (cursor.moveToNext());
            }
        } else {
            Log.d("Database", "No data found.");
        }

        cursor.close();
        db.close();
    }

    */

    public boolean updateUserProfile(User user, String gender, int height, double weight, String profile_picture) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("gender", gender);
        contentValues.put("height", height);
        contentValues.put("weight", weight);
        contentValues.put("profile_picture", profile_picture);
        db.update("users", contentValues, "id = ?", new String[]{String.valueOf(user.getId())});
        return true;
    }


    //doesn't clear auto increment sequence of ID, need separate SQL statement
    public void clearTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("users", null, null);
        db.close();
    }

    public void recreateTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    //innserts a new row into the "workout_logs" table with the given values
    public boolean insertWorkoutLog(int userId, String exercise, int sets, int reps, double weight, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id", userId);
        contentValues.put("exercise", exercise);
        contentValues.put("sets", sets);
        contentValues.put("reps", reps);
        contentValues.put("weight", weight);
        contentValues.put("date", date);
        long result = db.insert("workout_logs", null, contentValues);

        // If row ID returned is not -1, insertion is successful
        if (result == -1)
            return false;
        else
            return true;
    }

    //retrieves workout logs for the specified user
    public Cursor getWorkoutLogsByUserId(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM workout_logs WHERE user_id = ?", new String[]{String.valueOf(userId)});
        return cursor;
    }

    //updates the specified workout log with the given values
    public boolean updateWorkoutLog(int id, String exercise, int sets, int reps, double weight, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("exercise", exercise);
        contentValues.put("sets", sets);
        contentValues.put("reps", reps);
        contentValues.put("weight", weight);
        contentValues.put("date", date);
        int result = db.update("workout_logs", contentValues, "id = ?", new String[]{String.valueOf(id)});

        // If the number of affected rows is greater than 0, the update is successful
        return result > 0;
    }

    //method to delete a workout log by ID
    public void deleteWorkoutLog(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("workout_logs", "id = ?", new String[]{String.valueOf(id)});
    }


    public boolean insertMealLog(int userId, String meal, double fats, double carbs, double protein, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id", userId);
        contentValues.put("meal", meal);
        contentValues.put("fats", fats);
        contentValues.put("carbs", carbs);
        contentValues.put("protein", protein);
        contentValues.put("date", date);
        long result = db.insert("meal_logs", null, contentValues);

        // If row ID returned is not -1, insertion is successful
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getMealLogsByUserId(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM meal_logs WHERE user_id = ?", new String[]{String.valueOf(userId)});
        return cursor;
    }

    public boolean updateMealLog(int id, String meal, double fats, double carbs, double protein, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("meal", meal);
        contentValues.put("fats", fats);
        contentValues.put("carbs", carbs);
        contentValues.put("protein", protein);
        contentValues.put("date", date);
        int result = db.update("meal_logs", contentValues, "id = ?", new String[]{String.valueOf(id)});

        return result > 0;
    }

    public void deleteMealLog(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("meal_logs", "id = ?", new String[]{String.valueOf(id)});
    }
}