package com.s22010170.heathtrakerapp;

import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home_container), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        createNotificationChannel();
        // find the bottom navigation view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        // replace home activity with the home fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.home_container, new HomeFragment()).commit();
        // set the home icon as selected
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        // set the on item selected listener
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // check the selected item and replace the fragment
                if(menuItem.getItemId() == R.id.nav_home) {
                    // replace home activity with the home fragment
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_container, new HomeFragment()).commit();
                } else if(menuItem.getItemId() == R.id.nav_list) {
                    // replace home activity with the list fragment
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_container, new ListFragment()).commit();
                } else if(menuItem.getItemId() == R.id.nav_report) {
                    // replace home activity with the report fragment
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_container, new ReportFragment()).commit();
                }
                else if(menuItem.getItemId() == R.id.nav_notification) {
                    // replace home activity with the notification fragment
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_container, new NotificationFragment()).commit();
                } else if(menuItem.getItemId() == R.id.nav_user) {
                    // replace home activity with the user fragment
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_container, new UserFragment()).commit();
                }
                return true;
            }
        });
    }

    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "medicationReminderApp";
            String desc = "Channel for medication Alarm Manager";
            int imp = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("medicationReminder", name, imp);
            channel.setDescription(desc);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}