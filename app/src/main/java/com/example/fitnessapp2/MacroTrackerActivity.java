package com.example.fitnessapp2;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MacroTrackerActivity extends BaseActivity {
    private RecyclerView mealLogsRecyclerView;
    private MealLogsAdapter mealLogsAdapter;
    private int userId;
    private Button addMealLogButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_macro_tracker);

        setupToolbarAndDrawer();

        userId = getIntent().getIntExtra("loggedInUserId", -1);
        if (userId == -1) {
            //handle the error by finishing activity
            finish();
            return;
        }

        mealLogsRecyclerView = findViewById(R.id.meal_logs_recycler_view);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Cursor mealLogsCursor = dbHelper.getMealLogsByUserId(userId); // Replace userId with the actual user ID
        mealLogsAdapter = new MealLogsAdapter(this, mealLogsCursor);
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
                double fats = Integer.parseInt(fatsInput.getText().toString());
                double carbs = Integer.parseInt(carbsInput.getText().toString());
                double protein = Integer.parseInt(proteinInput.getText().toString());

                String date = new SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault()).format(new Date());

                if (dbHelper.insertMealLog(userId, meal, fats, carbs, protein, date)) {
                    //updating RecyclerView
                    Cursor newMealLogsCursor = dbHelper.getMealLogsByUserId(userId);
                    mealLogsAdapter.swapCursor(newMealLogsCursor);

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

        mealLogsAdapter.setOnItemClickListener(new MealLogsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Cursor currentCursor = mealLogsAdapter.getCursor();
                if (position >= 0 && position < currentCursor.getCount()) {
                    currentCursor.moveToPosition(position);
                    int columnIndex = currentCursor.getColumnIndex("id");
                    if (columnIndex != -1) {
                        int logId = currentCursor.getInt(columnIndex);

                        AlertDialog.Builder builder = new AlertDialog.Builder(MacroTrackerActivity.this);
                        builder.setMessage("Do you want to delete this meal log?")
                                .setPositiveButton("Yes", (dialog, id) -> {
                                    dbHelper.deleteMealLog(logId);
                                    //updating recycler view
                                    Cursor newMealLogsCursor = dbHelper.getMealLogsByUserId(userId);
                                    Cursor oldCursor = mealLogsAdapter.swapCursor(newMealLogsCursor);
                                    if (oldCursor != null) {
                                        oldCursor.close();
                                    }
                                })
                                .setNegativeButton("No", (dialog, id) -> {
                                    //when user cancels the dialog
                                });
                        builder.create().show();
                    } else {
                        Toast.makeText(MacroTrackerActivity.this, "Error: Invalid column index.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MacroTrackerActivity.this, "Error: Invalid position.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}