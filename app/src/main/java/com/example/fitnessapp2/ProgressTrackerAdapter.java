package com.example.fitnessapp2;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProgressTrackerAdapter extends RecyclerView.Adapter<ProgressTrackerAdapter.WorkoutLogViewHolder> {
    private Cursor workoutCursor;
    private Context workoutContext;
    private OnItemClickListener mClickListener;

    public ProgressTrackerAdapter(Context context, Cursor cursor) {
        workoutContext = context;
        workoutCursor = cursor;
    }

    public class WorkoutLogViewHolder extends RecyclerView.ViewHolder {
        public TextView exerciseTextView, setsRepsTextView, weightTextView, dateTextView;

        public WorkoutLogViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.date_text_view);
            exerciseTextView = itemView.findViewById(R.id.exercise_text_view);
            setsRepsTextView = itemView.findViewById(R.id.sets_reps_text_view);
            weightTextView = itemView.findViewById(R.id.weight_text_view);

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
    public WorkoutLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(workoutContext);
        View view = inflater.inflate(R.layout.workout_log_item, parent, false);
        return new WorkoutLogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutLogViewHolder holder, int position) {
        if (!workoutCursor.moveToPosition(position)) {
            return;
        }

        int dateColumnIndex = workoutCursor.getColumnIndex("date");
        int exerciseColumnIndex = workoutCursor.getColumnIndex("exercise");
        int setsColumnIndex = workoutCursor.getColumnIndex("sets");
        int repsColumnIndex = workoutCursor.getColumnIndex("reps");
        int weightColumnIndex = workoutCursor.getColumnIndex("weight");

        if (exerciseColumnIndex != -1 && setsColumnIndex != -1 && repsColumnIndex != -1 &&
                weightColumnIndex != -1 && dateColumnIndex != -1) {

            String date = workoutCursor.getString(dateColumnIndex);
            String exercise = workoutCursor.getString(exerciseColumnIndex);
            int sets = workoutCursor.getInt(setsColumnIndex);
            int reps = workoutCursor.getInt(repsColumnIndex);
            double weight = workoutCursor.getDouble(weightColumnIndex);


            holder.dateTextView.setText(date);
            holder.exerciseTextView.setText(exercise);
            holder.setsRepsTextView.setText(sets + "x" + reps); //displaying sets and reps together
            holder.weightTextView.setText(String.valueOf(weight) + " kg");
        }
    }

    @Override
    public int getItemCount() {
        return workoutCursor.getCount();
    }

    public Cursor swapCursor(Cursor newCursor) {
        if (workoutCursor == newCursor) {
            return null;
        }

        Cursor oldCursor = workoutCursor;
        workoutCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }

        return oldCursor;
    }

    public Cursor getCursor() {
        return workoutCursor;
    }

    //interface for making items clickable
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }
}