package com.example.pscapplication;

import static com.example.pscapplication.R.drawable.*;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ObjectDetailedActivity extends AppCompatActivity {

    String id, user_id;
    String name, address, description;
    TextView tvObjNumber, tvObjName, tvObjAddress, tvObjDescription;
    Button btnObjectArm, btnObjectDisarm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_detailed);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        user_id = intent.getStringExtra("user_id");
        name = intent.getStringExtra("name");
        address = intent.getStringExtra("address");
        description = intent.getStringExtra("description");

        tvObjAddress = findViewById(R.id.tvObjectDetailedAddressVar);
        tvObjName = findViewById(R.id.tvObjectDetailedNameVar);
        tvObjNumber = findViewById(R.id.tvObjectDetailedNumber);
        tvObjDescription = findViewById(R.id.tvObjectDetailedDescriptionVar);
        btnObjectArm = findViewById(R.id.btnObjectArm);
        btnObjectDisarm = findViewById(R.id.btnObjectDisarm);
        btnObjectDisarm.setBackgroundResource(button_border);

        btnObjectArm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                btnObjectArm.setBackgroundResource(button_border);
                btnObjectDisarm.setBackgroundResource(button_inactive_border);
            }
        });

        btnObjectDisarm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                btnObjectDisarm.setBackgroundResource(button_border);
                btnObjectArm.setBackgroundResource(button_inactive_border);
            }
        });

        tvObjName.setText(name);
        tvObjNumber.setText(id);
        tvObjDescription.setText(description);
        tvObjAddress.setText(address);
    }
}