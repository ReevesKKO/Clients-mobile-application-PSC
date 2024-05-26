package com.example.pscapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class NewRequestActivity extends AppCompatActivity {

    String[] statuses = {"Стандартный", "Повышенный", "Особый"};
    Spinner spinnerStatuses;
    EditText etReqName, etReqAddress, etReqDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request);

        etReqName = findViewById(R.id.etReqName);
        etReqAddress = findViewById(R.id.etReqAddress);
        etReqDescription = findViewById(R.id.etReqDescription);
        spinnerStatuses = findViewById(R.id.spinnerStatus);

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, statuses);
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinnerStatuses.setAdapter(adapter);
    }
}