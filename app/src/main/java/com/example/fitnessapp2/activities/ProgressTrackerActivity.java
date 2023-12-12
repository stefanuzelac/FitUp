package com.example.fitnessapp2.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp2.R;
import com.example.fitnessapp2.adapters.ProgressTrackerAdapter;
import com.example.fitnessapp2.data.database.DatabaseHelper;
import com.example.fitnessapp2.data.viewmodel.ProgressTrackerViewModel;
import com.example.fitnessapp2.data.viewmodel.ProgressTrackerViewModelFactory;
import com.example.fitnessapp2.data.model.User;
import com.example.fitnessapp2.data.database.dao.WorkoutDAO;
import com.example.fitnessapp2.data.database.daoimpl.WorkoutDAOImpl;
import com.example.fitnessapp2.data.WorkoutDataPass;
import com.example.fitnessapp2.data.model.WorkoutLog;
import com.example.fitnessapp2.fragments.CyclingFragment;
import com.example.fitnessapp2.fragments.RunningFragment;
import com.example.fitnessapp2.fragments.SwimmingFragment;
import com.example.fitnessapp2.fragments.WeightliftingFragment;
import com.example.fitnessapp2.utils.UserSessionManager;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ProgressTrackerActivity extends BaseActivity implements WorkoutDataPass {
    private RecyclerView workoutLogsRecyclerView;
    private ProgressTrackerAdapter workoutLogsAdapter;
    private int userId;
    private Button addWorkoutLogButton;
    private WorkoutDAO workoutDAO;
    private Fragment currentFragment;
    private String selectedDate;
    private ProgressTrackerViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_tracker);
        setupToolbarAndDrawer();

        // Initialize DatabaseHelper and WorkoutDAO
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        workoutDAO = new WorkoutDAOImpl(databaseHelper);

        // Initialize ViewModel with Factory
        ProgressTrackerViewModelFactory factory = new ProgressTrackerViewModelFactory(workoutDAO);
        viewModel = new ViewModelProvider(this, factory).get(ProgressTrackerViewModel.class);

        workoutLogsRecyclerView = findViewById(R.id.workout_logs_recycler_view);
        workoutLogsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        workoutLogsAdapter = new ProgressTrackerAdapter(this, new ArrayList<>());
        workoutLogsRecyclerView.setAdapter(workoutLogsAdapter);

        // Set user ID in ViewModel
        User currentUser = UserSessionManager.getInstance(this).getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getId(); // Make sure this is the correct user ID
            viewModel.setUserId(userId);
        } else {
            // Handle the case where there is no logged-in user
            finish(); // or redirect to login screen
            return;
        }

        // Observe LiveData for workout logs
        viewModel.getWorkoutLogs().observe(this, logs -> {
            if (logs != null && !logs.isEmpty()) {
                workoutLogsAdapter.updateData(logs);
            } else {
                // Show message if no logs are available for the selected date
                Toast.makeText(this, "No logs available for this date.", Toast.LENGTH_SHORT).show();
                workoutLogsAdapter.updateData(new ArrayList<>()); // Clear existing data
            }
        });

        // Initialize MaterialCalendarView and set a Date Selection Listener
        com.prolificinteractive.materialcalendarview.MaterialCalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            LocalDate localDate = date.getDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            selectedDate = localDate.format(formatter);

            viewModel.setSelectedDate(selectedDate); // Trigger LiveData update in ViewModel
        });

        // Use DAO to get workout logs for the user
        List<WorkoutLog> workoutLogs = workoutDAO.getWorkoutLogsByUserId(userId);
        workoutLogsAdapter = new ProgressTrackerAdapter(this, workoutLogs);
        workoutLogsAdapter.setOnItemClickListener(new ProgressTrackerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("ProgressTrackerActivity", "List size: " + workoutLogs.size() + ", Position: " + position);
                if (position >= 0 && position < workoutLogs.size()) {
                    WorkoutLog clickedLog = workoutLogs.get(position);

                    AlertDialog.Builder builder = new AlertDialog.Builder(ProgressTrackerActivity.this);
                    builder.setMessage("Do you want to delete this workout log?")
                            .setPositiveButton("Yes", (dialog, id) -> {
                                workoutDAO.deleteWorkoutLog(clickedLog.getId());
                                workoutLogsAdapter.removeWorkoutLogAtPosition(position);
                            })
                            .setNegativeButton("No", (dialog, id) -> {
                                // User cancelled the dialog
                            });
                    builder.create().show();
                } else {
                    Log.d("ProgressTrackerActivity", "Invalid position: " + position);
                }
            }
        });

        workoutLogsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        workoutLogsRecyclerView.setAdapter(workoutLogsAdapter);

        // Setup click listeners for workout type selections
        findViewById(R.id.weightlifting_card).setOnClickListener(v -> showWorkoutTypeFragment("Weightlifting"));
        findViewById(R.id.running_card).setOnClickListener(v -> showWorkoutTypeFragment("Running"));
        findViewById(R.id.cycling_card).setOnClickListener(v -> showWorkoutTypeFragment("Cycling"));
        findViewById(R.id.swimming_card).setOnClickListener(v -> showWorkoutTypeFragment("Swimming"));

        addWorkoutLogButton = findViewById(R.id.add_workout_log_button);
        addWorkoutLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener dateSetListener = (view, year1, monthOfYear, dayOfMonth) -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            calendar.set(year1, monthOfYear, dayOfMonth);
            selectedDate = dateFormat.format(calendar.getTime());
            Log.d("ProgressTrackerActivity", "Date selected: " + selectedDate);

            saveCurrentWorkoutData();
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
        datePickerDialog.show();
    }

    private String getFragmentType(Fragment fragment) {
        if (fragment instanceof WeightliftingFragment) {
            return "Weightlifting";
        } else if (fragment instanceof CyclingFragment) {
            return "Cycling";
        } else if (fragment instanceof RunningFragment) {
            return "Running";
        } else if (fragment instanceof SwimmingFragment) {
            return "Swimming";
        }
        return "";
    }


    // A method to update the adapter's data
    private void updateAdapterData() {
        List<WorkoutLog> updatedLogs = workoutDAO.getWorkoutLogsByUserId(userId);
        workoutLogsAdapter.updateData(updatedLogs);
    }

    private void showWorkoutTypeFragment(String workoutType) {
        Fragment fragment;
        switch (workoutType) {
            case "Weightlifting":
                fragment = WeightliftingFragment.newInstance(userId, selectedDate);
                break;
            case "Cycling":
                fragment = CyclingFragment.newInstance(userId, selectedDate);
                break;
            case "Running":
                fragment = RunningFragment.newInstance(userId, selectedDate);
                break;
            case "Swimming":
                fragment = SwimmingFragment.newInstance(userId, selectedDate);
                break;
            default:
                return;
        }
        currentFragment = fragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.workout_specific_input_frame, fragment);
        transaction.commitNow();
        Log.d("ProgressTrackerActivity", "Fragment shown: " + workoutType + ", Date: " + selectedDate);
    }

    private void saveCurrentWorkoutData() {
        if (currentFragment == null || selectedDate == null || selectedDate.isEmpty()) {
            Toast.makeText(this, "Please select a workout type and date", Toast.LENGTH_SHORT).show();
            return;
        }

        WorkoutLog workoutLog = new WorkoutLog();
        workoutLog.setUserId(userId);
        workoutLog.setDate(selectedDate);

        // Logic for each fragment type
        if (currentFragment instanceof CyclingFragment) {
            CyclingFragment cyclingFragment = (CyclingFragment) currentFragment;
            workoutLog.setWorkoutType("Cycling");
            workoutLog.setWorkoutDetails(cyclingFragment.getWorkoutDetails());
        } else if (currentFragment instanceof RunningFragment) {
            RunningFragment runningFragment = (RunningFragment) currentFragment;
            workoutLog.setWorkoutType("Running");
            workoutLog.setWorkoutDetails(runningFragment.getWorkoutDetails());
        } else if (currentFragment instanceof SwimmingFragment) {
            SwimmingFragment swimmingFragment = (SwimmingFragment) currentFragment;
            workoutLog.setWorkoutType("Swimming");
            workoutLog.setWorkoutDetails(swimmingFragment.getWorkoutDetails());
        } else if (currentFragment instanceof WeightliftingFragment) {
            WeightliftingFragment weightliftingFragment = (WeightliftingFragment) currentFragment;
            weightliftingFragment.setCurrentDate(selectedDate); // Update the date in the fragment
            workoutLog.setWorkoutType("Weightlifting");
            workoutLog.setWorkoutDetails(weightliftingFragment.getWorkoutDetails());
        } else {
            Toast.makeText(this, "Please select a workout type", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if workout details are empty
        if (workoutLog.getWorkoutDetails() == null || workoutLog.getWorkoutDetails().isEmpty()) {
            Toast.makeText(this, "Please enter workout details", Toast.LENGTH_SHORT).show();
            return;
        }

        // Pass the complete workout log to be saved
        OnWorkoutDataPass(workoutLog);
    }


    @Override
    public void OnWorkoutDataPass(WorkoutLog workoutLog) {
        if (workoutDAO.insertWorkoutLog(workoutLog)) {
            Toast.makeText(this, "Workout log saved successfully!", Toast.LENGTH_SHORT).show();
            updateAdapterData(); // Refresh the RecyclerView with new data
        } else {
            Toast.makeText(this, "Error saving workout log.", Toast.LENGTH_SHORT).show();
        }
    }
}