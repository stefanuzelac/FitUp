package com.example.fitnessapp2;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class ProgressTrackerActivity extends BaseActivity {
    private RecyclerView workoutLogsRecyclerView;
    private ProgressTrackerAdapter workoutLogsAdapter;
    private int userId;
    private Button addWorkoutLogButton;
    private WorkoutDAO workoutDAO;

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

        // Initialize the DAO implementation with the database helper
        workoutDAO = new WorkoutDAOImpl(new DatabaseHelper(this));


        // Use DAO to get workout logs for the user
        List<WorkoutLog> workoutLogs = workoutDAO.getWorkoutLogsByUserId(userId);
        workoutLogsAdapter = new ProgressTrackerAdapter(this, workoutLogs);
        workoutLogsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        workoutLogsRecyclerView.setAdapter(workoutLogsAdapter);

        workoutLogsAdapter.setOnItemClickListener(new ProgressTrackerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Instead of working with a cursor, we get the log directly from the list
                WorkoutLog log = workoutLogs.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(ProgressTrackerActivity.this);
                builder.setMessage("Do you want to delete this workout log?")
                        .setPositiveButton("Yes", (dialog, id) -> {
                            workoutDAO.deleteWorkoutLog(log.getId());
                            // Update the list and notify the adapter
                            workoutLogsAdapter.removeWorkoutLogAtPosition(position);
                        })
                        .setNegativeButton("No", (dialog, id) -> {
                            // User cancelled the dialog
                        });
                builder.create().show();
            }
        });

        // Setup click listeners for workout type selections
        findViewById(R.id.weightlifting_card).setOnClickListener(v -> showWorkoutTypeFragment(new WeightliftingFragment()));
        findViewById(R.id.running_card).setOnClickListener(v -> showWorkoutTypeFragment(new RunningFragment()));
        findViewById(R.id.cycling_card).setOnClickListener(v -> showWorkoutTypeFragment(new CyclingFragment()));
        findViewById(R.id.swimming_card).setOnClickListener(v -> showWorkoutTypeFragment(new SwimmingFragment()));
    }

    // A method to update the adapter's data
    private void updateAdapterData() {
        List<WorkoutLog> newLogs = workoutDAO.getWorkoutLogsByUserId(userId);
        workoutLogsAdapter.updateData(newLogs); // You'll need to implement this method in your adapter
    }

    private void showWorkoutTypeFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.workout_specific_input_frame, fragment);
        transaction.commit();
        // Make the FrameLayout visible if it's not already
        findViewById(R.id.workout_specific_input_frame).setVisibility(View.VISIBLE);
    }
}