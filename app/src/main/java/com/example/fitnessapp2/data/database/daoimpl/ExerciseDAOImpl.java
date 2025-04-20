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
        String[] columns = {"name", "type", "muscle", "equipment", "difficulty", "instructions"};
        try (Cursor cursor = db.query("exercises", columns, "muscle = ?", new String[]{muscleGroup}, null, null, null)) {
            if (cursor.moveToFirst()) {
                do {
                    int nameIndex = cursor.getColumnIndex("name");
                    int typeIndex = cursor.getColumnIndex("type");
                    int muscleIndex = cursor.getColumnIndex("muscle");
                    int equipmentIndex = cursor.getColumnIndex("equipment");
                    int difficultyIndex = cursor.getColumnIndex("difficulty");
                    int instructionsIndex = cursor.getColumnIndex("instructions");

                    // Check for -1 to avoid errors (in a real scenario you might handle differently)
                    String name = (nameIndex != -1) ? cursor.getString(nameIndex) : null;
                    String type = (typeIndex != -1) ? cursor.getString(typeIndex) : null;
                    String muscle = (muscleIndex != -1) ? cursor.getString(muscleIndex) : null;
                    String equipment = (equipmentIndex != -1) ? cursor.getString(equipmentIndex) : null;
                    String difficulty = (difficultyIndex != -1) ? cursor.getString(difficultyIndex) : null;
                    String instructions = (instructionsIndex != -1) ? cursor.getString(instructionsIndex) : null;

                    Exercise exercise = new Exercise(name);
                    exercise.setType(type);
                    exercise.setMuscle(muscle);
                    exercise.setEquipment(equipment);
                    exercise.setDifficulty(difficulty);
                    exercise.setInstructions(instructions);

                    exercises.add(exercise);
                } while (cursor.moveToNext());
            }
        }

        db.close();
        return exercises;
    }

}
