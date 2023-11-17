package com.example.fitnessapp2.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.fitnessapp2.R;
import com.example.fitnessapp2.data.User;
import com.example.fitnessapp2.utils.UserSessionManager;
import com.google.android.material.navigation.NavigationView;

public class BaseActivity extends AppCompatActivity {
    protected Toolbar toolbar;
    protected DrawerLayout drawer;
    protected NavigationView navigationView;
    private SharedPreferences sharedPref;
    protected User currentUser;
    protected UserSessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply the theme change here, before super.onCreate()
        SharedPreferences sharedPref = getSharedPreferences("app_settings", MODE_PRIVATE);
        boolean isDarkTheme = sharedPref.getBoolean("dark_theme", false);

        if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        sessionManager = UserSessionManager.getInstance(BaseActivity.this);

    }

    protected void checkSession() {
        currentUser = sessionManager.getCurrentUser();
        if (currentUser == null) {
            logout();
        }
    }

    protected void setupToolbarAndDrawer() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.primary));
        toolbar.setTitleTextColor(getResources().getColor(R.color.on_primary));
        drawer = findViewById(R.id.drawer_layout);

        sharedPref = getSharedPreferences("app_pref", MODE_PRIVATE);

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
                Intent intent;
                // Get the current user from the session manager
                User currentUser = sessionManager.getCurrentUser();
                if (currentUser == null) {
                    Toast.makeText(BaseActivity.this, "User not logged in. Please log in again.",
                            Toast.LENGTH_SHORT).show();
                    logout();
                    return true;
                }

                int currentUserId = currentUser.getId();

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        intent = new Intent(BaseActivity.this, AppMainPageActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_account:
                        intent = new Intent(BaseActivity.this, AccountActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_progress_tracker:
                        intent = new Intent(BaseActivity.this, ProgressTrackerActivity.class);
                        intent.putExtra("currentUserId", currentUserId);
                        startActivity(intent);
                        break;
                    case R.id.nav_macro_tracker:
                        intent = new Intent(BaseActivity.this, MacroTrackerActivity.class);
                        intent.putExtra("currentUserId", currentUserId);
                        startActivity(intent);
                        break;
                    case R.id.nav_settings:
                        intent = new Intent(BaseActivity.this, SettingsActivity.class);
                        intent.putExtra("currentUserId", currentUserId);
                        startActivity(intent);
                        break;
                    case R.id.nav_logout:
                        logout();
                        break;
                }
                return true;
            }
        });
    }

    // This method is new. It will be called when the activity starts interacting with the user.
    @Override
    protected void onResume() {
        super.onResume();
        checkSession();
        updateNavigationHeaderWithUserData();
    }

    // updates the UI elements in the Navigation Drawer header.
    protected void updateNavigationHeaderWithUserData() {
        View headerView = navigationView.getHeaderView(0);
        TextView nameTextView = headerView.findViewById(R.id.nav_header_name);
        TextView emailTextView = headerView.findViewById(R.id.nav_header_email);

        if (currentUser != null) {
            // We have a valid user, so update the name and email in the Navigation Drawer
            nameTextView.setText(currentUser.getName());
            emailTextView.setText(currentUser.getEmail());
        } else {
            // No user is logged in, so show a generic message
            nameTextView.setText("Guest");
            emailTextView.setText("Please sign in");
        }
    }

    //override the onBackPressed method to handle when the back button is pressed
    @Override
    public void onBackPressed() {
        if (getClass().equals(MainActivity.class)) {
            finishAffinity(); // Closes the app if we're on the main activity.
        } else if (getClass().equals(RegistrationActivity.class) || getClass().equals(AppMainPageActivity.class)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish(); // Returns to the main activity (login screen).
        } else {
            // If we're in any other activity, we want to return to the AppMainPageActivity.
            Intent intent = new Intent(this, AppMainPageActivity.class);
            startActivity(intent);
            finish(); // Finishes the current activity and returns to the main page of the app.
        }
    }

    private void logout() {
        // Clear the current user in UserSessionManager
        UserSessionManager.getInstance(this).setCurrentUser(null);

        // Clear shared preferences related to login details
        SharedPreferences sharedPref = getSharedPreferences("remember_me_pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("email");
        editor.putBoolean("rememberMeCheckboxState", false);
        editor.apply();

        // Navigate to MainActivity (Login screen)
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Close the current activity
    }
}