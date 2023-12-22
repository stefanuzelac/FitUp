package com.example.fitnessapp2.activities;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp2.data.database.DatabaseHelper;
import com.example.fitnessapp2.adapters.MacroTrackerAdapter;
import com.example.fitnessapp2.data.database.dao.MealDAO;
import com.example.fitnessapp2.data.database.daoimpl.MealDAOImpl;
import com.example.fitnessapp2.data.model.MealLog;
import com.example.fitnessapp2.R;
import com.example.fitnessapp2.data.model.User;
import com.example.fitnessapp2.data.viewmodel.MealLogViewModel;
import com.example.fitnessapp2.fragments.MealLogBottomSheetFragment;
import com.example.fitnessapp2.utils.UserSessionManager;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MacroTrackerActivity extends BaseActivity {
    private RecyclerView mealLogsRecyclerView;
    private MacroTrackerAdapter mealLogsAdapter;
    private MealDAO mealDAO;
    private int userId;
    private Button addMealLogButton;
    private MaterialCalendarView calendarView;
    private MealLogViewModel mealLogViewModel;
    private org.threeten.bp.LocalDate selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_macro_tracker);
        setupToolbarAndDrawer();

        // Get the currentUser from UserSessionManager
        User currentUser = UserSessionManager.getInstance(this).getCurrentUser();
        if (currentUser == null) {
            // Handle this case, maybe finish the activity or redirect to login
            finish();
            return;
        }
        userId = currentUser.getId(); // Assuming you have a getId method in the User model

        mealLogsRecyclerView = findViewById(R.id.meal_logs_recycler_view);
        mealLogsRecyclerView.setVisibility(View.GONE);

        mealDAO = new MealDAOImpl(new DatabaseHelper(this));
        mealLogsAdapter = new MacroTrackerAdapter(this, new ArrayList<>());
        mealLogsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mealLogsRecyclerView.setAdapter(mealLogsAdapter);

        mealLogViewModel = new ViewModelProvider(this).get(MealLogViewModel.class);
        mealLogViewModel.getMealLog().observe(this, mealLog -> {
            if (mealDAO.insertMealLog(mealLog)) {
                // Convert the selectedDate (org.threeten.bp.LocalDate) to String for comparison
                String selectedDateString = (selectedDate != null) ? selectedDate.format(org.threeten.bp.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null;

                if (selectedDateString != null && selectedDateString.equals(mealLog.getDate())) {
                    // If the new log's date matches the selected date, update the RecyclerView
                    updateMealLogsForDate(selectedDate);
                }
                Toast.makeText(this, "Meal log added successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to add meal log. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });


        addMealLogButton = findViewById(R.id.add_meal_log_button);
        addMealLogButton.setOnClickListener(view -> {
            MealLogBottomSheetFragment bottomSheet = MealLogBottomSheetFragment.newInstance(userId);
            bottomSheet.show(getSupportFragmentManager(), "MealLogBottomSheet");
        });

        mealLogsAdapter.setOnItemClickListener(new MacroTrackerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                MealLog clickedMealLog = mealLogsAdapter.getMealLogAtPosition(position); // Your adapter should have a method like this
                if (clickedMealLog != null) {
                    int logId = clickedMealLog.getId();

                    AlertDialog.Builder builder = new AlertDialog.Builder(MacroTrackerActivity.this);
                    builder.setMessage("Do you want to delete this meal log?")
                            .setPositiveButton("Yes", (dialog, id) -> {
                                mealDAO.deleteMealLog(logId);
                                // Remove from the adapter's dataset and notify it
                                mealLogsAdapter.removeMealLogAtPosition(position);
                            })
                            .setNegativeButton("No", (dialog, id) -> {
                                // User canceled the dialog
                            });
                    builder.create().show();
                } else {
                    Toast.makeText(MacroTrackerActivity.this, "Error: Invalid meal log.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            if (selected) {
                selectedDate = org.threeten.bp.LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
                updateMealLogsForDate(selectedDate);
                mealLogsRecyclerView.setVisibility(View.VISIBLE);
            } else {
                selectedDate = null;
                mealLogsRecyclerView.setVisibility(View.GONE);
                mealLogsAdapter.updateData(new ArrayList<>());
            }
        });
    }

    private void updateMealLogsForDate(org.threeten.bp.LocalDate date) {
        org.threeten.bp.format.DateTimeFormatter formatter = org.threeten.bp.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = date.format(formatter);
        List<MealLog> mealLogsForDate = mealDAO.getMealLogsByUserIdAndDate(userId, formattedDate);
        mealLogsAdapter.updateData(mealLogsForDate);
    }
}