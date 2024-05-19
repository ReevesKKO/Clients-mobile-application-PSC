package com.example.pscapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainClientActivity extends AppCompatActivity {

    static String user_id, username;
    BottomNavigationView bottomNavigationView;
    MyObjectsFragment myObjectsFragment = new MyObjectsFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    MyLogsFragment myLogsFragment = new MyLogsFragment();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_client);

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        Log.e("USER-ID", user_id);
        username = intent.getStringExtra("username");

        /*Button btnGetLogs = findViewById(R.id.btnGetLogs);
        btnGetLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.flMainClientFrameLayout, myLogsFragment).commit();
            }
        });*/

        bottomNavigationView = findViewById(R.id.bnvClientMain);
        getSupportFragmentManager().beginTransaction().replace(R.id.flMainClientFrameLayout, myObjectsFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.myObjectsMenu) {
                getSupportFragmentManager().beginTransaction().replace(R.id.flMainClientFrameLayout, myObjectsFragment).commit();
            }
            if(item.getItemId() == R.id.myRequestsMenu) {
                //getSupportFragmentManager().beginTransaction().replace(R.id.adminActivityFragment, adminStocksFragment).commit();
            }
            if(item.getItemId() == R.id.myProfileMenu) {
                getSupportFragmentManager().beginTransaction().replace(R.id.flMainClientFrameLayout, profileFragment).commit();
            }
            return false;
        });
    }

    @Override
    public void onBackPressed() {
        showLogOutDialog();
    }

    public void showLogOutDialog() {
        LogOutFragment dialog = new LogOutFragment();
        dialog.show(getSupportFragmentManager(), "custom");
    }

    public static String getUsername() {
        return username;
    }
    public static String getUserId() {
        return user_id;
    }
}