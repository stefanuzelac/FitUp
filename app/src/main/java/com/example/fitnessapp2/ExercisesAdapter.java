package com.example.fitnessapp2;

import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class ExercisesAdapter extends RecyclerView.Adapter<ExercisesAdapter.ViewHolder> {

    private List<Exercise> exercises;

    public ExercisesAdapter(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    //creating a new ViewHolder instance by inflating the layout for each item
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
        return new ViewHolder(view);
    }

    //bind the data for each Exercise object to the corresponding views in the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);

        String videoFileName = getVideoFileNameForExercise(exercise.getName());
        if (videoFileName != null) {
            int videoResId = holder.itemView.getContext().getResources().getIdentifier(videoFileName, "raw", holder.itemView.getContext().getPackageName());
            holder.exerciseVideo.setVideoURI(Uri.parse("android.resource://" + holder.itemView.getContext().getPackageName() + "/" + videoResId));

            //set an OnCompletionListener to make the video loop
            holder.exerciseVideo.setOnCompletionListener(mp -> holder.exerciseVideo.start());

            holder.exerciseVideo.start();
            holder.exerciseVideo.setVisibility(View.VISIBLE);

            //save the current playback position when the view is detached
            holder.itemView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {
                    //no action needed for onViewAttachedToWindow
                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                    holder.currentPlaybackPosition = holder.exerciseVideo.getCurrentPosition();
                    holder.exerciseVideo.pause();
                }
            });
        } else {
            holder.exerciseVideo.setVisibility(View.GONE);
        }

        holder.exerciseNameTextView.setText(toTitleCase(exercise.getName()));
        holder.exerciseDifficultyTextView.setText(exercise.getDifficulty().toUpperCase(Locale.ROOT));
        holder.exerciseInstructionsTextView.setText(exercise.getInstructions());
    }

    private String getVideoFileNameForExercise(String exerciseName) {
        String formattedExerciseName = exerciseName.trim().toLowerCase().replace(" ", "_").replace("-", "_").replace(".", "");
        switch (formattedExerciseName) {
            case "barbell_curl":
                return "barbell_curl";
            case "barbell_deadlift":
                return "barbell_deadlift";
            case "barbell_deficit_deadlift":
                return "barbell_deficit_deadlift";
            case "barbell_glute_bridge":
                return "barbell_glute_bridge";
            case "barbell_hip_thrust":
                return "barbell_hip_thrust";
            case "cable_v_bar_push_down":
                return "cable_v_bar_push_down";
            case "close_grip_bench_press":
                return "close_grip_bench_press";
            case "close_grip_pull_down":
                return "close_grip_pull_down";
            case "concentration_curl":
                return "concentration_curl";
            case "deadlift_with_bands":
                return "deadlift_with_bands";
            case "dumbbell_bench_press":
                return "dumbbell_bench_press";
            case "dumbbell_floor_press":
                return "dumbbell_floor_press";
            case "dumbbell_flyes":
                return "dumbbell_flyes";
            case "glute_bridge":
                return "glute_bridge";
            case "hammer_curls":
                return "hammer_curls";
            case "incline_hammer_curls":
                return "incline_hammer_curls";
            case "one_arm_dumbbell_row":
                return "one_arm_dumbbell_row";
            case "pullups":
                return "pullups";
            case "pushups":
                return "pushups";
            case "reverse_grip_bent_over_row":
                return "reverse_grip_bent_over_row";
            case "romanian_deadlift_with_dumbbells":
                return "romanian_deadlift_with_dumbbells";
            case "single_leg_cable_hip_extension":
                return "single_leg_cable_hip_extension";
            case "sumo_deadlift":
                return "sumo_deadlift";
            case "triceps_dip":
                return "triceps_dip";
            default:
                return null;
        }
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder.exerciseVideo != null) {
            holder.exerciseVideo.stopPlayback();
        }
    }

    //return the number of items in the RecyclerView
    @Override
    public int getItemCount() {
        return exercises.size();
    }

    //class that contains the views for each item in the RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView exerciseNameTextView;
        TextView exerciseDifficultyTextView;
        TextView exerciseInstructionsTextView;
        VideoView exerciseVideo;

        // Add this variable to save the current playback position
        int currentPlaybackPosition = 0;

        public ViewHolder(View view) {
            super(view);
            exerciseNameTextView = view.findViewById(R.id.exercise_name);
            exerciseDifficultyTextView = view.findViewById(R.id.exercise_difficulty);
            exerciseInstructionsTextView = view.findViewById(R.id.exercise_instructions);
            exerciseVideo = itemView.findViewById(R.id.exercise_video);

            // Add an OnPreparedListener to seek to the saved position and start the video
            exerciseVideo.setOnPreparedListener(mp -> {
                mp.seekTo(currentPlaybackPosition);
                mp.start();
            });
        }
    }

    //method to update the list of Exercise objects displayed in the RecyclerView
    public void updateExercises(List<Exercise> newExercises) {
        exercises.clear();
        exercises.addAll(newExercises);
        notifyDataSetChanged();
    }

    //my method for capitalizing each word in the name
    //converting String to title case
    private String toTitleCase(String stringOfName) {
        if (stringOfName == null || stringOfName.isEmpty()) {
            return stringOfName;
        }

        StringBuilder sb = new StringBuilder(stringOfName.length());
        boolean capitalizeNext = true;
        for (char c : stringOfName.toCharArray()) {
            //if the character is a whitespace, set capitalizeNext to true to capitalize the next character
            //and append the whitespace character to the StringBuilder object
            if (Character.isWhitespace(c)) {
                capitalizeNext = true;
                sb.append(c);
            }

            //if capitalizeNext is true, capitalize the current character and set capitalizeNext to false
            //to avoid capitalizing subsequent characters that are not whitespace
            else if (capitalizeNext) {
                sb.append(Character.toUpperCase(c));
                capitalizeNext = false;
            }

            //if capitalizeNext is false and the character is not whitespace, append the character to the StringBuilder
            //object in lower case
            else {
                sb.append(Character.toLowerCase(c));
            }

        }
        return sb.toString();
    }


}