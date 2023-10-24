package com.example.fitnessapp2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AccountActivity extends BaseActivity {
    private TextView profileName, textViewAge, textViewAgeLabel;
    private RadioGroup radioGroupGender;
    private RadioButton radioButtonMale, radioButtonFemale;
    private EditText profileHeightInput, profileWeightInput;
    private Button saveButton;
    private User user;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        setupToolbarAndDrawer();

        // find the views
        profileName = findViewById(R.id.profileName);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        radioButtonMale = findViewById(R.id.radioButtonMale);
        radioButtonFemale = findViewById(R.id.radioButtonFemale);
        profileHeightInput = findViewById(R.id.profileHeightInput);
        profileWeightInput = findViewById(R.id.profileWeightInput);
        textViewAgeLabel = findViewById(R.id.profileAgeLabel);
        textViewAge = findViewById(R.id.profileAgeDisplay);
        saveButton = findViewById(R.id.saveButton);

        // get the email and password of the currently logged in user from SharedPreferences
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String email = sharedPref.getString("email", "");
        String password = sharedPref.getString("password", "");

        // get the user data from the database
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        user = databaseHelper.getUser(email, password);

        // set the full name in the profileName TextView
        if (user != null) {
            String fullName = user.getName() + " " + user.getLastName();
            profileName.setText(fullName);
        }

        // set the gender selection in the radioGroupGender
        String gender = user.getGender();
        if (gender != null) {
            switch (gender) {
                case "Male":
                    radioButtonMale.setChecked(true);
                    break;
                case "Female":
                    radioButtonFemale.setChecked(true);
                    break;
            }
        }

        // set the height and weight in the editTextHeight and editTextWeight
        int height = user.getHeight();
        if (height != 0) {
            profileHeightInput.setText(String.valueOf(height));
        }

        int weight = user.getWeight();
        if (weight != 0) {
            profileWeightInput.setText(String.valueOf(weight));
        }

        // set the age in the textViewAge
        String dob = user.getDob();
        Log.d("AccountActivity", "dob = " + dob);
        if (dob != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date birthDate = sdf.parse(dob);
                Calendar birthDay = Calendar.getInstance();
                birthDay.setTimeInMillis(birthDate.getTime());
                Calendar today = Calendar.getInstance();
                int age = today.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
                if (today.get(Calendar.DAY_OF_YEAR) < birthDay.get(Calendar.DAY_OF_YEAR)) {
                    age--;
                }
                String ageString = String.valueOf(age);
                textViewAge.setText(ageString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();
            }
        });


    }

    private void saveUserData() {
        //get the selected gender from the radio buttons
        String gender = "";
        int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
        if (selectedGenderId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedGenderId);
            gender = selectedRadioButton.getText().toString();
        }

        int height = 0;
        int weight = 0;
        if (!profileHeightInput.getText().toString().isEmpty()) {
            height = Integer.parseInt(profileHeightInput.getText().toString());
        }
        if (!profileWeightInput.getText().toString().isEmpty()) {
            weight = Integer.parseInt(profileWeightInput.getText().toString());
        }

        user.setGender(gender);
        user.setHeight(height);
        user.setWeight(weight);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        boolean success = databaseHelper.updateUserProfile(user, gender, height, weight, null);

        if (success) {
            //save updated user data in SharedPreferences
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("height", user.getHeight());
            editor.putInt("weight", user.getWeight());
            editor.apply();

            Toast.makeText(this, "User data saved successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error saving user data", Toast.LENGTH_SHORT).show();
        }
    }


}