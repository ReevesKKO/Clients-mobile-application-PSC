package com.example.pscapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainClientActivity extends AppCompatActivity {

    static String user_id;
    BottomNavigationView bottomNavigationView;
    MyObjectsFragment myObjectsFragment = new MyObjectsFragment();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_client);

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        Log.e("USER-ID", user_id);

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
                //getSupportFragmentManager().beginTransaction().replace(R.id.adminActivityFragment, profileFragment).commit();
            }
            return false;
        });
    }

    public static String getUserId() {
        return user_id;
    }
}