package com.example.pscapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class RestorePasswordSuccessFragment extends DialogFragment {
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setTitle("Успешное восстановление пароля").setMessage("Проверьте адрес электронной почты, указанный при регистрации.").setPositiveButton("Ок", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent goToLoginActivity = new Intent(getActivity(), LoginActivity.class);
                startActivity(goToLoginActivity);
            }
        }).create();
    }
}
