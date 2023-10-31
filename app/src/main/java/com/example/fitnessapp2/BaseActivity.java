package com.example.fitnessapp2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
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
    private SharedPreferences sharedPref;
    protected User currentUser;

    //set logged in user
    public void setLoggedInUserId(int userId) {
        loggedInUserId = userId;
    }

    protected void setupToolbarAndDrawer() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.primary));
        toolbar.setTitleTextColor(getResources().getColor(R.color.on_primary));
        drawer = findViewById(R.id.drawer_layout);

        sharedPref = getSharedPreferences("app_pref", MODE_PRIVATE);
        loggedInUserId = sharedPref.getInt("loggedInUserId", -1);

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
                            Toast.makeText(BaseActivity.this, "User ID not found. Please log in again.",
                                    Toast.LENGTH_SHORT).show();
                            return true;
                        }
                        Intent intent3 = new Intent(BaseActivity.this, ProgressTrackerActivity.class);
                        intent3.putExtra("loggedInUserId", loggedInUserId);
                        startActivity(intent3);
                        break;
                    case R.id.nav_macro_tracker:
                        if (loggedInUserId == -1) {
                            Toast.makeText(BaseActivity.this, "User ID not found. Please log in again.",
                                    Toast.LENGTH_SHORT).show();
                            return true;
                        }
                        Intent intent4 = new Intent(BaseActivity.this, MacroTrackerActivity.class);
                        intent4.putExtra("loggedInUserId", loggedInUserId);
                        startActivity(intent4);
                        break;
                    case R.id.nav_settings:
                        Intent intent5 = new Intent(BaseActivity.this, SettingsActivity.class);
                        intent5.putExtra("loggedInUserId", loggedInUserId);
                        startActivity(intent5);
                        break;
                    case R.id.nav_logout:
                        logout();
                        break;

                }
                return true;
            }
        });
    }

    protected void checkSession() {
        currentUser = UserSessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            logout();
        }
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
            nameTextView.setText(currentUser.getName()); // Assuming 'getName' is a method from your 'User' model
            emailTextView.setText(currentUser.getEmail()); // Same assumption as above
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

    private void logout() {
        // Clear the current user in UserSessionManager
        UserSessionManager.getInstance().setCurrentUser(null);

        // Clear shared preferences if needed
        SharedPreferences sharedPref = getSharedPreferences("remember_me_pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("email");
        editor.remove("password");
        editor.remove("loggedInUserId");
        editor.putBoolean("rememberMeCheckboxState", false);
        editor.apply();

        // Navigate to MainActivity (Login screen)
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Close the current activity
    }

}