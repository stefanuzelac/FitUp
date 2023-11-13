package com.example.fitnessapp2.activities;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Button;

import com.example.fitnessapp2.R;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class WorkoutTimerActivity extends BaseActivity {
    private Button startTimerButton, pauseTimerButton, resetTimerButton;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private NumberPicker hoursPicker, minutesPicker, secondsPicker;
    private long initialTimeInMillis;
    private long timeRemaining = 0; // New variable
    private int defaultBorderColor;
    private boolean wasTimerReset = false; // New variable to track if the timer was reset

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_timer);
        setupToolbarAndDrawer();

        // initialize the number pickers
        hoursPicker = findViewById(R.id.hoursPicker);
        minutesPicker = findViewById(R.id.minutesPicker);
        secondsPicker = findViewById(R.id.secondsPicker);

        // set the range of values for each picker
        hoursPicker.setMinValue(0);
        hoursPicker.setMaxValue(23);
        minutesPicker.setMinValue(0);
        minutesPicker.setMaxValue(59);
        secondsPicker.setMinValue(0);
        secondsPicker.setMaxValue(59);
        hoursPicker = findViewById(R.id.hoursPicker);
        //preventing user input directly
        setPickerFocusability(hoursPicker);
        minutesPicker = findViewById(R.id.minutesPicker);
        setPickerFocusability(minutesPicker);
        secondsPicker = findViewById(R.id.secondsPicker);
        setPickerFocusability(secondsPicker);

        hoursPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format(Locale.getDefault(), "%02d", value);
            }
        });

        minutesPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format(Locale.getDefault(), "%02d", value);
            }
        });

        secondsPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format(Locale.getDefault(), "%02d", value);
            }
        });

        hoursPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                long timeInMillis = calculateTimeInMillis();
                updateTimerDisplay(timeInMillis);
            }
        });

        minutesPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                long timeInMillis = calculateTimeInMillis();
                updateTimerDisplay(timeInMillis);
            }
        });

        secondsPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                long timeInMillis = calculateTimeInMillis();
                updateTimerDisplay(timeInMillis);
            }
        });

        //set onClickListener for startTimerButton
        startTimerButton = findViewById(R.id.startTimerButton);
        startTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTimerRunning) {
                    startTimerButtonClicked();
                }
            }
        });

        //set onClickListener for pauseTimerButton
        pauseTimerButton = findViewById(R.id.pauseTimerButton);
        pauseTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimerButtonClicked();
            }
        });

        //set onClickListener for resetTimerButton
        resetTimerButton = findViewById(R.id.resetTimerButton);
        resetTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimerButtonClicked();
            }
        });

        // Fetch the color from the resources
        defaultBorderColor = getResources().getColor(R.color.primary_variant);
    }

    //converting my total time to milliseconds
    private long calculateTimeInMillis() {
        int hours = hoursPicker.getValue();
        int minutes = minutesPicker.getValue();
        int seconds = secondsPicker.getValue();

        return TimeUnit.HOURS.toMillis(hours)
                + TimeUnit.MINUTES.toMillis(minutes)
                + TimeUnit.SECONDS.toMillis(seconds);
    }

    private void updateControlsState(boolean enable) {
        // Enable or disable controls
        hoursPicker.setEnabled(enable);
        minutesPicker.setEnabled(enable);
        secondsPicker.setEnabled(enable);
        // Might also want to change the appearance (e.g., opacity) of the pickers here
    }

    private void startTimerButtonClicked() {
        // if the timer is running or it's paused (timeRemaining > 0), we just resume it without animation
        if (isTimerRunning || timeRemaining > 0) {
            resumeTimer();
            return;
        }

        // Find the timePickerLayout and load the scale animation.
        LinearLayout timePickerLayout = findViewById(R.id.timePickerLayout);
        Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale);

        // Set an animation listener to react when the animation ends
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Here, you might want to perform an action at the start of the animation
                // (e.g., change the appearance of a widget)
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Start the countdown only after the scale-up animation completes
                resumeTimer();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // This won't be called as we're not repeating the animation
            }
        });

        // Disabling NumberPickers again during animation
        updateControlsState(false);

        // Start the animation
        timePickerLayout.startAnimation(scaleAnimation);

        // Reset the wasTimerReset flag when the timer starts again
        wasTimerReset = false;
    }

    private void resumeTimer() {
        if (!isTimerRunning) {
            updateControlsState(false);
            isTimerRunning = true;

            long startTimeInMillis = (timeRemaining > 0) ? timeRemaining : calculateTimeInMillis();
            initialTimeInMillis = (initialTimeInMillis > 0) ? initialTimeInMillis : startTimeInMillis;
            long interval = 1000; // 1 second in milliseconds

            // Create the countdown timer and handle its events.
            countDownTimer = new CountDownTimer(startTimeInMillis, interval) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeRemaining = millisUntilFinished; // Update the time remaining on every tick
                    updateTimerDisplay(millisUntilFinished);

                    // Existing color transition and border color update code...
                }

                @Override
                public void onFinish() {
                    // Timer has finished; reset state and update display.
                    isTimerRunning = false;
                    timeRemaining = 0; // Reset the time remaining
                    updateTimerDisplay(0);

                    // Starting scale-down animation
                    LinearLayout timePickerLayout = findViewById(R.id.timePickerLayout);
                    Animation scaleDownAnimation = AnimationUtils.loadAnimation(WorkoutTimerActivity.this, R.anim.scale_down);

                    // Setting listener to re-enable interaction with the timer once the animation ends
                    scaleDownAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            updateControlsState(true);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                    // Start the scale-down animation
                    timePickerLayout.startAnimation(scaleDownAnimation);
                }
            }.start();
        }
    }

    private void updateTimerDisplay(long millisUntilFinished) {
        // Format remaining time in hh:mm:ss format
        int totalSeconds = (int) (millisUntilFinished / 1000);
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        // set the values for the hours, minutes, and seconds pickers
        hoursPicker.setValue(hours);
        minutesPicker.setValue(minutes);
        secondsPicker.setValue(seconds);
    }

    private void pauseTimerButtonClicked() {
        if (isTimerRunning) {
            countDownTimer.cancel(); // Stops the countdown
            isTimerRunning = false; // Mark the timer as not running
        }
    }

    private void resetTimerButtonClicked() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            isTimerRunning = false;
            timeRemaining = 0;

            // Only start the scale-down animation if the timer was previously running
            if (initialTimeInMillis > 0 && !wasTimerReset) {
                final LinearLayout timePickerLayout = findViewById(R.id.timePickerLayout);
                Animation scaleDownAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_down);

                scaleDownAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // Reset timer value and UI elements here, after scale-down animation is completed
                        hoursPicker.setValue((int) TimeUnit.MILLISECONDS.toHours(initialTimeInMillis));
                        minutesPicker.setValue((int) (TimeUnit.MILLISECONDS.toMinutes(initialTimeInMillis) % 60));
                        secondsPicker.setValue((int) (TimeUnit.MILLISECONDS.toSeconds(initialTimeInMillis) % 60));
                        updateTimerDisplay(initialTimeInMillis);

                        timePickerLayout.setScaleX(1.0f); // Reset X Scale
                        timePickerLayout.setScaleY(1.0f); // Reset Y Scale

                        GradientDrawable background = (GradientDrawable) timePickerLayout.getBackground();
                        int borderWidthPx = (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                2,
                                getResources().getDisplayMetrics()
                        );
                        background.setStroke(borderWidthPx, defaultBorderColor);

                        timePickerLayout.invalidate();
                        updateControlsState(true); // Re-enable controls after the reset
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                //Start the scale-down animation
                timePickerLayout.startAnimation(scaleDownAnimation);
                wasTimerReset = true;
            } else {
                // If timer was not running, just reset the values without any animation.
                hoursPicker.setValue((int) TimeUnit.MILLISECONDS.toHours(initialTimeInMillis));
                minutesPicker.setValue((int) (TimeUnit.MILLISECONDS.toMinutes(initialTimeInMillis) % 60));
                secondsPicker.setValue((int) (TimeUnit.MILLISECONDS.toSeconds(initialTimeInMillis) % 60));
                updateTimerDisplay(initialTimeInMillis);
                updateControlsState(true);
            }
        }
    }


    private void setPickerFocusability(NumberPicker picker) {
        picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
    }

}