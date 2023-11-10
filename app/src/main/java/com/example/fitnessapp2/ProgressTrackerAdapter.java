package com.example.fitnessapp2;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProgressTrackerAdapter extends RecyclerView.Adapter<ProgressTrackerAdapter.WorkoutLogViewHolder> {
    private List<WorkoutLog> workoutLogs;
    private Context workoutContext;
    private OnItemClickListener mClickListener;

    public ProgressTrackerAdapter(Context context, List<WorkoutLog> workoutLogs) {
        workoutContext = context;
        this.workoutLogs = workoutLogs;
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
        WorkoutLog log = workoutLogs.get(position);

        holder.dateTextView.setText(log.getDate());
        holder.exerciseTextView.setText(log.getExercise());
        holder.setsRepsTextView.setText(log.getSets() + "x" + log.getReps());
        holder.weightTextView.setText(log.getWeight() + " kg");
    }

    // Method to add a WorkoutLog
    public void addWorkoutLog(WorkoutLog workoutLog) {
        workoutLogs.add(workoutLog);
        notifyItemInserted(workoutLogs.size() - 1);
    }

    // Method to remove a WorkoutLog at a specific position
    public void removeWorkoutLogAtPosition(int position) {
        if (position >= 0 && position < workoutLogs.size()) {
            workoutLogs.remove(position);
            notifyItemRemoved(position);
        }
    }

    // Method to update a WorkoutLog at a specific position
    public void updateWorkoutLogAtPosition(int position, WorkoutLog workoutLog) {
        if (position >= 0 && position < workoutLogs.size()) {
            workoutLogs.set(position, workoutLog);
            notifyItemChanged(position);
        }
    }

    // Method to get a WorkoutLog at a specific position
    public WorkoutLog getWorkoutLogAtPosition(int position) {
        if (position >= 0 && position < workoutLogs.size()) {
            return workoutLogs.get(position);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return workoutLogs.size();
    }

    //interface for making items clickable
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    // Method to update the adapter's dataset
    public void updateData(List<WorkoutLog> newWorkoutLogs) {
        workoutLogs = newWorkoutLogs;
        notifyDataSetChanged(); // This will refresh the RecyclerView
    }

}