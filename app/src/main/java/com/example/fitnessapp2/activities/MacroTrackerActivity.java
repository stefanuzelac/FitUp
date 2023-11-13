package com.example.fitnessapp2.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp2.data.DatabaseHelper;
import com.example.fitnessapp2.adapters.MacroTrackerAdapter;
import com.example.fitnessapp2.data.MealDAO;
import com.example.fitnessapp2.data.MealDAOImpl;
import com.example.fitnessapp2.data.MealLog;
import com.example.fitnessapp2.R;
import com.example.fitnessapp2.data.User;
import com.example.fitnessapp2.utils.UserSessionManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MacroTrackerActivity extends BaseActivity {
    private RecyclerView mealLogsRecyclerView;
    private MacroTrackerAdapter mealLogsAdapter;
    private MealDAO mealDAO;
    private int userId;
    private Button addMealLogButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_macro_tracker);
        setupToolbarAndDrawer();

        // Get the currentUser from UserSessionManager
        User currentUser = UserSessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            // Handle this case, maybe finish the activity or redirect to login
            finish();
            return;
        }
        userId = currentUser.getId(); // Assuming you have a getId method in the User model

        mealLogsRecyclerView = findViewById(R.id.meal_logs_recycler_view);

        // Fetching meal logs as a List instead of Cursor
        mealDAO = new MealDAOImpl(new DatabaseHelper(this));

        List<MealLog> mealLogs = mealDAO.getMealLogsByUserId(userId); // Make sure you have this method in your DAO
        mealLogsAdapter = new MacroTrackerAdapter(this, mealLogs);
        mealLogsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mealLogsRecyclerView.setAdapter(mealLogsAdapter);


        addMealLogButton = findViewById(R.id.add_meal_log_button);
        addMealLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText mealInput = findViewById(R.id.meal_input);
                EditText fatsInput = findViewById(R.id.fats_input);
                EditText carbsInput = findViewById(R.id.carbs_input);
                EditText proteinInput = findViewById(R.id.protein_input);

                String meal = mealInput.getText().toString();
                String fatsStr = fatsInput.getText().toString();
                String carbsStr = carbsInput.getText().toString();
                String proteinStr = proteinInput.getText().toString();

                // Check if any field is empty
                if (meal.isEmpty() || fatsStr.isEmpty() || carbsStr.isEmpty() || proteinStr.isEmpty()) {
                    Toast.makeText(MacroTrackerActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                double fats = Double.parseDouble(fatsStr);
                double carbs = Double.parseDouble(carbsStr);
                double protein = Double.parseDouble(proteinStr);

                String date = new SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault()).format(new Date());

                MealLog mealLog = new MealLog(-1, userId, meal, fats, carbs, protein, date); // -1 for ID as it will be auto-generated

                if (mealDAO.insertMealLog(mealLog)) {
                    // Update RecyclerView with new data
                    List<MealLog> newMealLogs = mealDAO.getMealLogsByUserId(userId);
                    mealLogsAdapter.updateData(newMealLogs);

                    //clearing input fields
                    mealInput.setText("");
                    fatsInput.setText("");
                    carbsInput.setText("");
                    proteinInput.setText("");

                    Toast.makeText(MacroTrackerActivity.this, "Meal log added successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MacroTrackerActivity.this, "Failed to add meal log. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
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
    }
}