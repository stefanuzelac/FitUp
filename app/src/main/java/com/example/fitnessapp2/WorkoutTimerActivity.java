package com.example.fitnessapp2;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class WorkoutTimerActivity extends BaseActivity {
    private Button startTimerButton, pauseTimerButton, resetTimerButton;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private NumberPicker hoursPicker, minutesPicker, secondsPicker;
    private long initialTimeInMillis;

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


    private void startTimerButtonClicked() {
        if (!isTimerRunning) {
            isTimerRunning = true;
            final long startTimeInMillis = calculateTimeInMillis();
            initialTimeInMillis = startTimeInMillis;
            long interval = 1000; // 1 second in milliseconds

            countDownTimer = new CountDownTimer(startTimeInMillis, interval) {
                @Override
                public void onTick(long millisUntilFinished) {
                    //update timerDisplay with the remaining time
                    updateTimerDisplay(millisUntilFinished);
                }

                @Override
                public void onFinish() {
                    //timer has finished, update timerDisplay
                    isTimerRunning = false;
                    updateTimerDisplay(0);
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
            countDownTimer.cancel();
            isTimerRunning = false;
        }

    }

    private void resetTimerButtonClicked() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            // Reset the number pickers to the initial values set by the user
            hoursPicker.setValue((int) TimeUnit.MILLISECONDS.toHours(initialTimeInMillis));
            minutesPicker.setValue((int) (TimeUnit.MILLISECONDS.toMinutes(initialTimeInMillis) % 60));
            secondsPicker.setValue((int) (TimeUnit.MILLISECONDS.toSeconds(initialTimeInMillis) % 60));
            // Update the timer display with the initial values
            updateTimerDisplay(initialTimeInMillis);
            isTimerRunning = false;
        }
    }

    private void setPickerFocusability(NumberPicker picker) {
        picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
    }

}