package com.example.fitnessapp2.data.database.daoimpl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.fitnessapp2.data.database.DatabaseHelper;
import com.example.fitnessapp2.data.database.dao.ExerciseDAO;
import com.example.fitnessapp2.data.model.Exercise;

import java.util.ArrayList;
import java.util.List;

public class ExerciseDAOImpl implements ExerciseDAO {
    private DatabaseHelper dbHelper;

    public ExerciseDAOImpl(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    @Override
    public List<Exercise> getExercisesByMuscle(String muscleGroup) {
        List<Exercise> exercises = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Explicitly define columns (make sure these match the actual table columns)
        String[] columns = {"name", "muscle", "equipment", "difficulty", "instructions", "media_path"};
        try (Cursor cursor = db.query("exercises", columns, "muscle = ?", new String[]{muscleGroup}, null, null, null)) {
            if (cursor.moveToFirst()) {
                do {
                    int nameIndex = cursor.getColumnIndex("name");
                    int muscleIndex = cursor.getColumnIndex("muscle");
                    int equipmentIndex = cursor.getColumnIndex("equipment");
                    int difficultyIndex = cursor.getColumnIndex("difficulty");
                    int instructionsIndex = cursor.getColumnIndex("instructions");
                    int mediaPathIndex = cursor.getColumnIndex("media_path");

                    // Check for -1 to avoid errors (in a real scenario you might handle differently)
                    String name = (nameIndex != -1) ? cursor.getString(nameIndex) : null;
                    String muscle = (muscleIndex != -1) ? cursor.getString(muscleIndex) : null;
                    String equipment = (equipmentIndex != -1) ? cursor.getString(equipmentIndex) : null;
                    String difficulty = (difficultyIndex != -1) ? cursor.getString(difficultyIndex) : null;
                    String instructions = (instructionsIndex != -1) ? cursor.getString(instructionsIndex) : null;
                    String mediaPath = (mediaPathIndex != -1) ? cursor.getString(mediaPathIndex) : null;

                    Exercise exercise = new Exercise(name);
                    exercise.setMuscle(muscle);
                    exercise.setEquipment(equipment);
                    exercise.setDifficulty(difficulty);
                    exercise.setInstructions(instructions);
                    exercise.setMediaPath(mediaPath);

                    exercises.add(exercise);
                } while (cursor.moveToNext());
            }
        }

        db.close();
        return exercises;
    }

}
