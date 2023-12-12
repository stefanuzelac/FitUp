package com.example.fitnessapp2.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnessapp2.data.database.DatabaseHelper;
import com.example.fitnessapp2.R;
import com.example.fitnessapp2.data.model.User;
import com.example.fitnessapp2.data.database.daoimpl.UserDAOImpl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegistrationActivity extends AppCompatActivity {
    private EditText registerName, registerLastName, registerEmail, registerPassword, registerPhone;
    private Button registerDOB, registerConfirm;
    private Calendar dobCalendar;
    private SimpleDateFormat dateFormatter;
    private String dateOfBirth;
    String name, lastName, email, password, phone, dob;
    private UserDAOImpl userDaoImpl;
    private RadioGroup registerGender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialize the UserDAOImpl
        DatabaseHelper db = new DatabaseHelper(this);
        userDaoImpl = new UserDAOImpl(db);

        registerName = findViewById(R.id.registerName);
        registerLastName = findViewById(R.id.registerLastName);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        registerPhone = findViewById(R.id.registerPhone);
        registerGender = findViewById(R.id.registerGender);
        registerDOB = findViewById(R.id.registerDOB);
        registerConfirm = findViewById(R.id.registerConfirm);

        dobCalendar = Calendar.getInstance();
        registerDOB.setText(dobCalendar.get(Calendar.DATE)
                + "/" + (dobCalendar.get(Calendar.MONTH) + 1)
                + "/" + dobCalendar.get(Calendar.YEAR));
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        registerDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(RegistrationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dobCalendar.set(Calendar.YEAR, year);
                                dobCalendar.set(Calendar.MONTH, monthOfYear);
                                dobCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                registerDOB.setText(dateFormatter.format(dobCalendar.getTime()));

                                //update dateOfBirth string for database
                                dateOfBirth = dateFormatter.format(dobCalendar.getTime());
                            }
                        },
                        dobCalendar.get(Calendar.YEAR),
                        dobCalendar.get(Calendar.MONTH),
                        dobCalendar.get(Calendar.DAY_OF_MONTH)
                );

                //set max date to today
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();
            }
        });

        //add functionality to register user
        registerConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = registerName.getText().toString().trim();
                lastName = registerLastName.getText().toString().trim();
                email = registerEmail.getText().toString().trim();
                password = registerPassword.getText().toString().trim();
                phone = registerPhone.getText().toString().trim();
                dob = registerDOB.getText().toString().trim();

                if (name.isEmpty()) {
                    //set error message for the name field
                    registerName.setError("Name is required");
                    registerName.requestFocus();
                    return;
                }

                if (lastName.isEmpty()) {
                    registerLastName.setError("Last name is required");
                    registerLastName.requestFocus();
                    return;
                }

                if (email.isEmpty()) {
                    registerEmail.setError("Email is required");
                    registerEmail.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    registerEmail.setError("Enter a valid email");
                    registerEmail.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    registerPassword.setError("Password is required");
                    registerPassword.requestFocus();
                    return;
                }

                if (password.length() < 6) {
                    registerPassword.setError("Password should be at least 6 characters long");
                    registerPassword.requestFocus();
                    return;
                }

                if (phone.isEmpty()) {
                    registerPhone.setError("Phone is required");
                    registerPhone.requestFocus();
                    return;
                } else if (phone.length() != 10 || !phone.matches("[0-9]+")) {
                    registerPhone.setError("Phone must be 10 digits long");
                    registerPhone.requestFocus();
                    return;
                }

                if (dob.isEmpty()) {
                    Toast.makeText(RegistrationActivity.this, "Please select your date of birth", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Get gender selection
                int selectedGenderId = registerGender.getCheckedRadioButtonId();
                String gender = "";
                if (selectedGenderId == R.id.genderMale) {
                    gender = "male";
                } else if (selectedGenderId == R.id.genderFemale) {
                    gender = "female";
                }
                // if all fields are valid, add the user data to the database using DAO
                boolean isInserted = userDaoImpl.addUser(new User(0, name, lastName, email, password, phone, gender, dob, 0, 0, null)); // Now correctly passing dob and gender

                if (isInserted) {
                    // show a toast message indicating that the registration was successful
                    Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();

                    // start MainActivity
                    Intent mainIntent = new Intent(RegistrationActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                } else {
                    // show a toast message indicating that the registration was not successful
                    Toast.makeText(RegistrationActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    };
}