package com.example.pscapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class RestorePasswordActivity extends AppCompatActivity {

    String login, password, passwordConfirmation;
    EditText etRestoreLogin, etRestorePassword, etRestoreSecPassword;
    Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_password);

        etRestoreLogin = findViewById(R.id.etRestoreLogin);
        etRestorePassword = findViewById(R.id.etRestorePassword);
        etRestoreSecPassword = findViewById(R.id.etRestoreSecPassword);

        btnConfirm = findViewById(R.id.btnRestorePassword);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login = String.valueOf(etRestoreLogin.getText());
                password = String.valueOf(etRestorePassword.getText());
                passwordConfirmation = String.valueOf(etRestoreSecPassword.getText());

                if (!login.isEmpty() && !password.isEmpty() && !passwordConfirmation.isEmpty()) {
                    if (password.equals(passwordConfirmation)) {
                        RestorePasswordSuccessFragment dialog = new RestorePasswordSuccessFragment();
                        dialog.show(getSupportFragmentManager(), "custom");
                    }
                    else {
                        // TODO: 25.05.2024 toast пароли не совпадают
                    }
                }
                else {
                    // TODO: 25.05.2024 toast не все поля заполнены
                }
            }
        });
    }
}