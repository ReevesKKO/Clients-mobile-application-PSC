package com.example.pscapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainClientActivity extends AppCompatActivity {

    static String login;
    BottomNavigationView bottomNavigationView;
    MyObjectsFragment myObjectsFragment = new MyObjectsFragment();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_client);

        Intent intent = getIntent();
        login = intent.getStringExtra("login");
        Log.e("LOGIN", login);

        bottomNavigationView = findViewById(R.id.bnvClientMain);
        getSupportFragmentManager().beginTransaction().replace(R.id.flMainClientFrameLayout, myObjectsFragment);
    }
}