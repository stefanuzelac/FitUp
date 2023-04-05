package com.example.fitnessapp2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class BaseActivity extends AppCompatActivity {
    protected Toolbar toolbar;
    protected DrawerLayout drawer;
    protected NavigationView navigationView;
    protected int loggedInUserId = -1;

    //set logged in user
    public void setLoggedInUserId(int userId) {
        loggedInUserId = userId;
    }

    protected void setupToolbarAndDrawer() {
        //find the toolbar and drawer layout in the activity layout file
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);

        //set the toolbar as the actionbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        //finding navigation view and adding listener to handle my item clicks
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Switch case to handle different menu items
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intent = new Intent(BaseActivity.this, AppMainPageActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_account:
                        Intent intent2 = new Intent(BaseActivity.this, AccountActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.nav_progress_tracker:
                        if (loggedInUserId == -1) {
                            Toast.makeText(BaseActivity.this, "User ID not found. Please log in again.", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                        Intent intent3 = new Intent(BaseActivity.this, ProgressTrackerActivity.class);
                        intent3.putExtra("loggedInUserId", loggedInUserId);
                        startActivity(intent3);
                        break;
                    case R.id.nav_macro_tracker:
                        Intent intent4 = new Intent(BaseActivity.this, MacroTrackerActivity.class);
                        intent4.putExtra("loggedInUserId", loggedInUserId);
                        startActivity(intent4);
                        break;
                }
                return true;
            }
        });


        //getting header view and the TextViews to set the name and email
        View headerView = navigationView.getHeaderView(0);
        TextView nameTextView = headerView.findViewById(R.id.nav_header_name);
        TextView emailTextView = headerView.findViewById(R.id.nav_header_email);

        //getting the email from SharedPreferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String email = sharedPref.getString("email", "");

        //getting the user data from the database
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        String password = sharedPref.getString("password", "");
        User user = databaseHelper.getUser(email, password);

        //setting name and email in the TextViews
        if (user != null) {
            String name = user.getName();
            String lastName = user.getLastName();
            String fullName = name + " " + lastName;
            String userEmail = user.getEmail();
            nameTextView.setText(fullName);
            emailTextView.setText(userEmail);

            //setting loggedInUserId with the user's ID
            loggedInUserId = user.getId();
        }


    }

    //override the onBackPressed method to handle when the back button is pressed
    @Override
    public void onBackPressed() {
        if (getClass().equals(MainActivity.class)) {
            finishAffinity();
        } else if (getClass().equals(RegistrationActivity.class)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (getClass().equals(AppMainPageActivity.class)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, AppMainPageActivity.class);
            intent.putExtra("loggedInUserId", loggedInUserId);
            startActivity(intent);
            finish();
        }
    }


}