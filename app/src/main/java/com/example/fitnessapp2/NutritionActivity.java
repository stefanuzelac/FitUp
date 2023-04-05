package com.example.fitnessapp2;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NutritionActivity extends BaseActivity implements NutritionCallback {
    private NutritionApi nutritionApi;
    private RecyclerView nutritionInfoRecyclerView;
    private NutritionAdapter nutritionAdapter;
    private EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);
        setupToolbarAndDrawer();

        //set up the recyclerView and adapter
        nutritionInfoRecyclerView = findViewById(R.id.nutrition_info_recycler_view);
        nutritionInfoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        nutritionAdapter = new NutritionAdapter(new ArrayList<>());
        nutritionInfoRecyclerView.setAdapter(nutritionAdapter);

        //set up nutrition api
        nutritionApi = new NutritionApiImpl();
        nutritionApi.setNutritionCallback(this);

        //set up search bar and search button
        searchBar = findViewById(R.id.search_bar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                //calling api with the search term as we type
                String searchTerm = s.toString().trim();
                if (!searchTerm.isEmpty()) {
                    nutritionApi.getNutritionInfo(searchTerm, NutritionActivity.this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    //if api request succeed
    @Override
    public void onSuccess(List<NutritionInfo> nutritionInfoList) {
        runOnUiThread(() -> {
            //update the adapter with the new nutritionInfo recieved from the api
            nutritionAdapter.updateNutritionInfo(nutritionInfoList);
            //notify the adapter that the data has changed
            nutritionAdapter.notifyDataSetChanged();
            // Log a message to indicate that the nutrition info list was updated
            Log.d("NutritionActivity", "Updated nutrition info list with " + nutritionInfoList.size() + " items");
        });
    }

    @Override
    public void onError(Exception e) {
        runOnUiThread(() -> Toast.makeText(this, "Error while getting nutrition info: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}