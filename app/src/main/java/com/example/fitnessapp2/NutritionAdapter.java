package com.example.fitnessapp2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class NutritionAdapter extends RecyclerView.Adapter<NutritionAdapter.ViewHolder> {
    private List<NutritionInfo> nutritionInfoList;

    public NutritionAdapter(List<NutritionInfo> nutritionInfoList) {
        this.nutritionInfoList = nutritionInfoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nutrition, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NutritionInfo nutritionInfo = nutritionInfoList.get(position);
        holder.foodNameTextView.setText(capitalizeWords(nutritionInfo.getName()));
        holder.foodCalorieTextView.setText(String.format(Locale.getDefault(), "Calories: %.1f kcal", nutritionInfo.getCalories()));
        holder.foodServingSizeTextView.setText(String.format(Locale.getDefault(), "Serving Size: %.1f g", nutritionInfo.getServing_size_g()));
        holder.foodFatTotalTextView.setText(String.format(Locale.getDefault(), "Total Fat: %.1f g", nutritionInfo.getFat_total_g()));
        holder.foodFatSaturatedTextView.setText(String.format(Locale.getDefault(), "Saturated Fat: %.1f g", nutritionInfo.getFat_saturated_g()));
        holder.foodProteinTextView.setText(String.format(Locale.getDefault(), "Protein: %.1f g", nutritionInfo.getProtein_g()));
        holder.foodSodiumTextView.setText(String.format(Locale.getDefault(), "Sodium: %.1f mg", nutritionInfo.getSodium_mg()));
        holder.foodPotassiumTextView.setText(String.format(Locale.getDefault(), "Potassium: %.1f mg", nutritionInfo.getPotassium_mg()));
        holder.foodCholesterolTextView.setText(String.format(Locale.getDefault(), "Cholesterol: %.1f mg", nutritionInfo.getCholesterol_mg()));
        holder.foodCarbohydratesTextView.setText(String.format(Locale.getDefault(), "Total Carbs: %.1f g", nutritionInfo.getCarbohydrates_total_g()));
        holder.foodFiberTextView.setText(String.format(Locale.getDefault(), "Fiber: %.1f g", nutritionInfo.getFiber_g()));
        holder.foodSugarTextView.setText(String.format(Locale.getDefault(), "Sugar: %.1f g", nutritionInfo.getSugar_g()));

    }

    @Override
    public int getItemCount() {
        return nutritionInfoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //define views for each item in the RecyclerView
        TextView foodNameTextView,
                foodCalorieTextView,
                foodServingSizeTextView,
                foodFatTotalTextView,
                foodFatSaturatedTextView,
                foodProteinTextView,
                foodSodiumTextView,
                foodPotassiumTextView,
                foodCholesterolTextView,
                foodCarbohydratesTextView,
                foodFiberTextView,
                foodSugarTextView;

        public ViewHolder(View view) {
            super(view);
            foodNameTextView = view.findViewById(R.id.nutrition_name);
            foodCalorieTextView = view.findViewById(R.id.nutrition_calories);
            foodServingSizeTextView = view.findViewById(R.id.nutrition_serving_size);
            foodFatTotalTextView = view.findViewById(R.id.nutrition_fat_total);
            foodFatSaturatedTextView = view.findViewById(R.id.nutrition_fat_saturated);
            foodProteinTextView = view.findViewById(R.id.nutrition_protein);
            foodSodiumTextView = view.findViewById(R.id.nutrition_sodium);
            foodPotassiumTextView = view.findViewById(R.id.nutrition_potassium);
            foodCholesterolTextView = view.findViewById(R.id.nutrition_cholesterol);
            foodCarbohydratesTextView = view.findViewById(R.id.nutrition_carbohydrates);
            foodFiberTextView = view.findViewById(R.id.nutrition_fiber);
            foodSugarTextView = view.findViewById(R.id.nutrition_sugar);

        }
    }

    public void updateNutritionInfo(List<NutritionInfo> newNutritionInfoList) {
        nutritionInfoList.clear();
        nutritionInfoList.addAll(newNutritionInfoList);
        notifyDataSetChanged();
    }

    private String capitalizeWords(String input) {
        if (input == null || input.length() == 0) {
            return input;
        }

        String[] words = input.split("\\s+");
        StringBuilder capitalizedWords = new StringBuilder();

        for (String word : words) {
            if (word.length() > 0) {
                String firstLetter = word.substring(0, 1).toUpperCase();
                String remainingLetters = word.substring(1).toLowerCase();
                capitalizedWords.append(firstLetter).append(remainingLetters).append(" ");
            }
        }

        return capitalizedWords.toString().trim();
    }
}
