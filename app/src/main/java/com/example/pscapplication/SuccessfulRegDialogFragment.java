package com.example.pscapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class SuccessfulRegDialogFragment extends DialogFragment {
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setTitle("Успешная регистрация").setMessage("Вы успешно зарегистрировались. Пожалуйста, войдите в учётную запись, используя указанные в ходе регистрации логин и пароль.").setPositiveButton(getResources().getString(R.string.ok_button_title), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent goToLoginActivity = new Intent(getActivity(), LoginActivity.class);
                startActivity(goToLoginActivity);
            }
        }).create();
    }
}
