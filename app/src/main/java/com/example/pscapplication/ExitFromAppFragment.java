package com.example.pscapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class ExitFromAppFragment extends DialogFragment {
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getActivity().getString(R.string.are_you_sure);
        String text = getActivity().getString(R.string.want_to_exit);
        String yes = getString(R.string.btn_yes);
        String no = getString(R.string.btn_no);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setTitle(title).setMessage(text).setPositiveButton(yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        }).setNegativeButton(no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }
        ).create();
    }
}
