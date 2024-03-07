package com.example.pscapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class NotAllFieldsAreFilledFragment extends DialogFragment {
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getActivity().getString(R.string.not_all_fields_are_filled_title);
        String text = getString(R.string.not_all_fields_are_filled);
        String ok = getString(R.string.ok_button_title);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setTitle(title).setMessage(text).setPositiveButton(ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create();
    }
}