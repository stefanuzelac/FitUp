package com.example.fitnessapp2;

import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

        String videoFileName = ExerciseVideoUtility.getVideoFileNameForExercise(exercise.getName()); // Moved to a separate utility
        if (videoFileName != null) {
            int videoResId = holder.itemView.getContext().getResources()
                    .getIdentifier(videoFileName, "raw", holder.itemView.getContext().getPackageName());
            holder.exerciseVideo.setVideoURI(Uri.parse("android.resource://"
                    + holder.itemView.getContext().getPackageName() + "/" + videoResId));

            //set an OnCompletionListener to make the video loop
            holder.exerciseVideo.setOnCompletionListener(mp -> holder.exerciseVideo.start());

            holder.exerciseVideo.start();
            holder.exerciseVideo.setVisibility(View.VISIBLE);

            //save the current playback position when the view is detached
            holder.itemView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {

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

        holder.exerciseNameTextView.setText(ExerciseVideoUtility.toTitleCase(exercise.getName()));
        holder.exerciseDifficultyTextView.setText(exercise.getDifficulty().toUpperCase(Locale.ROOT));
        holder.exerciseInstructionsTextView.setText(exercise.getInstructions());

        // Possibly add an animation here, for example a fade-in effect for the card
        setEnterAnimation(holder.itemView, position);
    }

    private void setEnterAnimation(View view, int position) {
        Animation animation = AnimationUtils.loadAnimation(view.getContext(), android.R.anim.fade_in);
        animation.setDuration(300);
        animation.setStartOffset(position * 50); // slight delay between items
        view.startAnimation(animation);
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
}