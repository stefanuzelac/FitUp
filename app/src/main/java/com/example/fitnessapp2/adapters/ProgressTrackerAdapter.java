package com.example.fitnessapp2.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp2.R;
import com.example.fitnessapp2.data.model.WorkoutLog;

import java.util.List;
import java.util.Map;

public class ProgressTrackerAdapter extends RecyclerView.Adapter<ProgressTrackerAdapter.WorkoutLogViewHolder> {
    private List<WorkoutLog> workoutLogs;
    private Context context;
    private OnItemClickListener mClickListener;

    public ProgressTrackerAdapter(Context context, List<WorkoutLog> workoutLogs) {
        this.context = context;
        this.workoutLogs = workoutLogs;
    }

    @NonNull
    @Override
    public WorkoutLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.workout_log_item, parent, false);
        return new WorkoutLogViewHolder(view, mClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutLogViewHolder holder, int position) {
        WorkoutLog log = workoutLogs.get(position);
        holder.dateTextView.setText(log.getDate());

        Map<String, Object> details = log.getWorkoutDetails();
        String detailText = ""; // This string will hold the detailed text for each workout type
        switch (log.getWorkoutType()) {
            case "Cycling":
                // Format and set cycling-specific details
                detailText = "Route: " + details.get("route") + "\nDuration: " + details.get("duration") +
                        "\nAverage Speed: " + details.get("averageSpeed") + " km/h";
                break;
            case "Running":
                // Format and set running-specific details
                detailText = "Distance: " + details.get("distance") + " km\nTime: " + details.get("time");
                break;
            case "Swimming":
                // Format and set swimming-specific details
                detailText = "Lap Count: " + details.get("lapCount") + "\nPool Size: " + details.get("poolSize") +
                        " meters\nSwim Time: " + details.get("swimTime");
                break;
            case "Weightlifting":
                // Format and set weightlifting-specific details
                detailText = "Exercise: " + details.get("name") + "\nSets: " + details.get("sets") +
                        "\nReps: " + details.get("reps") + "\nWeight: " + details.get("weight") + " kg";
                break;
            // We can add more cases for additional workout types if necessary
        }

        holder.detailTextView.setText(detailText);
        Log.d("ProgressTrackerAdapter", "Binding View: " + log.toString());
    }

    @Override
    public int getItemCount() {
        return workoutLogs.size();
    }

    public static class WorkoutLogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView dateTextView, detailTextView;
        private OnItemClickListener mListener;

        public WorkoutLogViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.date_text_view);
            detailTextView = itemView.findViewById(R.id.workout_detail_text_view);
            this.mListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(v, position);
                }
            }
        }
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

    //interface for making items clickable
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    // Method to update the adapter's dataset
    public void updateData(List<WorkoutLog> newWorkoutLogs) {
        workoutLogs.clear();
        workoutLogs.addAll(newWorkoutLogs);
        notifyDataSetChanged();
    }

}