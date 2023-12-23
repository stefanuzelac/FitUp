package com.example.fitnessapp2.activities;

import android.os.Bundle;
import android.os.CountDownTimer;
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

        initializeNumberPickers();
        setupButtons();
    }

    private void initializeNumberPickers() {
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

        // Update listeners for NumberPickers
        hoursPicker.setOnValueChangedListener((picker, oldVal, newVal) -> updateInitialTime());
        minutesPicker.setOnValueChangedListener((picker, oldVal, newVal) -> updateInitialTime());
        secondsPicker.setOnValueChangedListener((picker, oldVal, newVal) -> updateInitialTime());
    }

    private void setupButtons() {
        // Setup buttons and their onClickListeners
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
        if (isTimerRunning || timeRemaining > 0) {
            resumeTimer();
        } else {
            startScaleUpAnimation();
        }
    }

    private void startScaleUpAnimation() {
        LinearLayout timePickerLayout = findViewById(R.id.timePickerLayout);
        Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale);

        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                resumeTimer();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        updateControlsState(false);
        timePickerLayout.startAnimation(scaleAnimation);
    }

    private void resumeTimer() {
        if (!isTimerRunning) {
            updateControlsState(false);
            isTimerRunning = true;

            long startTimeInMillis = (timeRemaining > 0) ? timeRemaining : initialTimeInMillis;
            long interval = 1000; // 1 second

            countDownTimer = new CountDownTimer(startTimeInMillis, interval) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeRemaining = millisUntilFinished;
                    updateTimerDisplay(millisUntilFinished);
                }

                @Override
                public void onFinish() {
                    completeTimer();
                }
            }.start();
        }
    }

    private void completeTimer() {
        isTimerRunning = false;
        timeRemaining = 0;
        updateTimerDisplay(0);
        startScaleDownAnimation();
    }

    private void startScaleDownAnimation() {
        LinearLayout timePickerLayout = findViewById(R.id.timePickerLayout);
        Animation scaleDownAnimation = AnimationUtils.loadAnimation(WorkoutTimerActivity.this, R.anim.scale_down);

        scaleDownAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                resetTimerValues();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        timePickerLayout.startAnimation(scaleDownAnimation);
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

    private void updateInitialTime() {
        initialTimeInMillis = calculateTimeInMillis();
        // You can also update the display here if needed
    }

    private void resetTimerButtonClicked() {
        if (countDownTimer != null) {
            countDownTimer.cancel();

            // Check if the timer was running or had remaining time
            if (isTimerRunning || timeRemaining > 0) {
                // Timer was active, so start the scale-down animation
                startScaleDownAnimation();
            } else {
                // Timer was not active, reset directly without animation
                resetTimerValues();
            }

            isTimerRunning = false;
            timeRemaining = 0;
            wasTimerReset = true;
        }
    }

    private void resetTimerValues() {
        // Reset timer display to initial time and re-enable controls
        updateTimerDisplay(initialTimeInMillis);
        updateControlsState(true);
    }

    private void setPickerFocusability(NumberPicker picker) {
        picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
    }
}