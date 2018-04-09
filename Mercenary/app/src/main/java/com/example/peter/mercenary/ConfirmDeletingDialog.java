package com.example.peter.mercenary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by minci on 2018/4/8.
 */


//haven't used
public class ConfirmDeletingDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to delete the task?")
                .setPositiveButton("YES", (dialog, id) -> {
                    // user gonna delete

                })
                .setNegativeButton("NO", (dialog, id) -> {
                    // User cancelled the dialog

                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
