package com.example.fitnessapp2;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MealLogsAdapter extends RecyclerView.Adapter<MealLogsAdapter.MealLogViewHolder> {
    private Cursor mealCursor;
    private Context mealContext;
    private OnItemClickListener mClickListener;

    public MealLogsAdapter(Context context, Cursor cursor) {
        mealContext = context;
        mealCursor = cursor;
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
    public MealLogsAdapter.MealLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mealContext);
        View view = inflater.inflate(R.layout.meal_log_item, parent, false);
        return new MealLogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealLogsAdapter.MealLogViewHolder holder, int position) {
        if (!mealCursor.moveToPosition(position)) {
            return;
        }

        int dateColumnIndex = mealCursor.getColumnIndex("date");
        int mealColumnIndex = mealCursor.getColumnIndex("meal");
        int fatsColumnIndex = mealCursor.getColumnIndex("fats");
        int carbsColumnIndex = mealCursor.getColumnIndex("carbs");
        int proteinColumnIndex = mealCursor.getColumnIndex("protein");

        if (mealColumnIndex != -1 && fatsColumnIndex != -1 && carbsColumnIndex != -1 &&
                proteinColumnIndex != -1 && dateColumnIndex != -1) {

            String date = mealCursor.getString(dateColumnIndex);
            String meal = mealCursor.getString(mealColumnIndex);
            double fats = mealCursor.getInt(fatsColumnIndex);
            double carbs = mealCursor.getInt(carbsColumnIndex);
            double protein = mealCursor.getDouble(proteinColumnIndex);


            holder.dateTextView.setText(date);
            holder.mealTextView.setText(meal);
            holder.fatsTextView.setText(String.valueOf(fats) + " g");
            holder.carbsTextView.setText(String.valueOf(carbs) + " g");
            holder.proteinTextView.setText(String.valueOf(protein) + " g");
        }
    }


    @Override
    public int getItemCount() {
        return mealCursor.getCount();
    }

    public Cursor swapCursor(Cursor newCursor) {
        if (mealCursor == newCursor) {
            return null;
        }

        Cursor oldCursor = mealCursor;
        mealCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }

        return oldCursor;
    }

    public Cursor getCursor() {
        return mealCursor;
    }


    //interface for making items clickable
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(MealLogsAdapter.OnItemClickListener listener) {
        mClickListener = listener;
    }

}
