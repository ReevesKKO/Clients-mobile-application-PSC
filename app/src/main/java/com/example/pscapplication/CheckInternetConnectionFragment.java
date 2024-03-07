package com.example.pscapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class CheckInternetConnectionFragment extends DialogFragment {
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getActivity().getString(R.string.check_internet_connection_title);
        String text = getString(R.string.check_internet_connection);
        String ok = getString(R.string.ok_button_title);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setTitle(title).setMessage(text).setPositiveButton(ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                System.exit(1);
            }
        }).create();
    }
}
