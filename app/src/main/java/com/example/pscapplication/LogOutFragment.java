package com.example.pscapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class LogOutFragment extends DialogFragment {
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_log_out, new LinearLayout(getActivity()), false);
        String title = getActivity().getString(R.string.log_out_title);
        String text = getString(R.string.log_out);
        String yes = getString(R.string.yes_button_title);
        String no = getString(R.string.no_button_title);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setTitle(title).setMessage(text).setPositiveButton(yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                LoginActivity loginActivity = new LoginActivity();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        }).setNegativeButton(no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        }).create();
    }
}