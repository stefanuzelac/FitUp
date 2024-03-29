package com.example.fitnessapp2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp2.data.model.MealLog;
import com.example.fitnessapp2.R;

import java.util.List;
import java.util.Locale;

public class MacroTrackerAdapter extends RecyclerView.Adapter<MacroTrackerAdapter.MealLogViewHolder> {
    private List<MealLog> mealLogs;
    private Context mealContext;
    private OnItemClickListener mClickListener;

    public MacroTrackerAdapter(Context context, List<MealLog> mealLogs) {
        mealContext = context;
        this.mealLogs = mealLogs;
    }

    public class MealLogViewHolder extends RecyclerView.ViewHolder {
        public TextView mealTextView, fatsTextView, carbsTextView, proteinTextView, dateTextView;

        public MealLogViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.date_text_view);
            mealTextView = itemView.findViewById(R.id.meal_text_view);
            fatsTextView = itemView.findViewById(R.id.fats_text_view);
            carbsTextView = itemView.findViewById(R.id.carbs_text_view);
            proteinTextView = itemView.findViewById(R.id.protein_text_view);

            itemView.setOnClickListener(v -> {
                if (mClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mClickListener.onItemClick(position);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public MacroTrackerAdapter.MealLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mealContext);
        View view = inflater.inflate(R.layout.item_meal_log, parent, false);
        return new MealLogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MacroTrackerAdapter.MealLogViewHolder holder, int position) {
        MealLog mealLog = mealLogs.get(position);

        holder.dateTextView.setText(mealLog.getDate());
        holder.mealTextView.setText(mealLog.getMeal());

        // Update the text to include labels
        holder.fatsTextView.setText("Fats: " + String.format(Locale.getDefault(), "%.2f g", mealLog.getFats()));
        holder.carbsTextView.setText("Carbs: " + String.format(Locale.getDefault(), "%.2f g", mealLog.getCarbs()));
        holder.proteinTextView.setText("Protein: " + String.format(Locale.getDefault(), "%.2f g", mealLog.getProtein()));
    }



    @Override
    public int getItemCount() {
        return mealLogs.size();
    }

    //interface for making items clickable
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    // Method to get a MealLog at a specific position
    public MealLog getMealLogAtPosition(int position) {
        return mealLogs.get(position);
    }

    // Method to remove a MealLog at a specific position
    public void removeMealLogAtPosition(int position) {
        mealLogs.remove(position);
        notifyItemRemoved(position);
    }

    // Method to update the entire dataset of the adapter
    public void updateData(List<MealLog> newMealLogs) {
        this.mealLogs = newMealLogs;
        notifyDataSetChanged();
    }

    public void addMealLog(MealLog mealLog) {
        mealLogs.add(mealLog);
        notifyItemInserted(mealLogs.size() - 1);
    }

    public void updateMealLogAtPosition(int position, MealLog mealLog) {
        mealLogs.set(position, mealLog);
        notifyItemChanged(position);
    }
}