package com.example.fitnessapp2;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProgressTrackerActivity extends BaseActivity {
    private RecyclerView workoutLogsRecyclerView;
    private WorkoutLogsAdapter workoutLogsAdapter;
    private int userId;
    private Button addWorkoutLogButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_tracker);
        setupToolbarAndDrawer();

        // Get the currentUser from UserSessionManager
        User currentUser = UserSessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            // Handle this case, maybe finish the activity or redirect to login
            finish();
            return;
        }
        userId = currentUser.getId(); // Assuming you have a getId method in the User model

        workoutLogsRecyclerView = findViewById(R.id.workout_logs_recycler_view);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Cursor workoutLogsCursor = dbHelper.getWorkoutLogsByUserId(userId); // Replace userId with the actual user ID
        workoutLogsAdapter = new WorkoutLogsAdapter(this, workoutLogsCursor);
        workoutLogsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        workoutLogsRecyclerView.setAdapter(workoutLogsAdapter);

/*
        addWorkoutLogButton = findViewById(R.id.add_workout_log_button);
        addWorkoutLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText exerciseInput = findViewById(R.id.exercise_input);
                EditText setsInput = findViewById(R.id.sets_input);
                EditText repsInput = findViewById(R.id.reps_input);
                EditText weightInput = findViewById(R.id.weight_input);

                String exercise = exerciseInput.getText().toString();
                int sets = Integer.parseInt(setsInput.getText().toString());
                int reps = Integer.parseInt(repsInput.getText().toString());

                //because some exercises can be done with just weight check if the weight input is empty
                // and if it is use 0 as default weight
                String weightString = weightInput.getText().toString();
                double weight = weightString.isEmpty() ? 0.0 : Double.parseDouble(weightString);

                String date = new SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault()).format(new Date());

                if (dbHelper.insertWorkoutLog(userId, exercise, sets, reps, weight, date)) {
                    //updating RecyclerView
                    Cursor newWorkoutLogsCursor = dbHelper.getWorkoutLogsByUserId(userId);
                    workoutLogsAdapter.swapCursor(newWorkoutLogsCursor);

                    //clearing input fields
                    exerciseInput.setText("");
                    setsInput.setText("");
                    repsInput.setText("");
                    weightInput.setText("");

                    Toast.makeText(ProgressTrackerActivity.this, "Workout log added successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProgressTrackerActivity.this, "Failed to add workout log. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
*/
        workoutLogsAdapter.setOnItemClickListener(new WorkoutLogsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Cursor currentCursor = workoutLogsAdapter.getCursor();
                if (position >= 0 && position < currentCursor.getCount()) {
                    currentCursor.moveToPosition(position);
                    int columnIndex = currentCursor.getColumnIndex("id");
                    if (columnIndex != -1) {
                        int logId = currentCursor.getInt(columnIndex);

                        AlertDialog.Builder builder = new AlertDialog.Builder(ProgressTrackerActivity.this);
                        builder.setMessage("Do you want to delete this workout log?")
                                .setPositiveButton("Yes", (dialog, id) -> {
                                    dbHelper.deleteWorkoutLog(logId);
                                    //updating recycler view
                                    Cursor newWorkoutLogsCursor = dbHelper.getWorkoutLogsByUserId(userId);
                                    Cursor oldCursor = workoutLogsAdapter.swapCursor(newWorkoutLogsCursor);
                                    if (oldCursor != null) {
                                        oldCursor.close();
                                    }
                                })
                                .setNegativeButton("No", (dialog, id) -> {
                                    //when user cancels the dialog
                                });
                        builder.create().show();
                    } else {
                        Toast.makeText(ProgressTrackerActivity.this, "Error: Invalid column index.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ProgressTrackerActivity.this, "Error: Invalid position.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}