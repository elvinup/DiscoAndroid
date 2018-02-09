package com.purdue.a407.cryptodisco.Helpers;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import com.afollestad.materialdialogs.MaterialDialog;

public class LoadingDialog extends DialogFragment {

    private static final String TAG = LoadingDialog.class.getSimpleName();

    @NonNull
    public static LoadingDialog create() {
        Bundle bundle = new Bundle();
        LoadingDialog dialog = new LoadingDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    public void show(@NonNull FragmentManager manager) {
        if (manager.findFragmentByTag(TAG) != null) {
            return;
        }
        show(manager, TAG);
    }

    public void cancel() {
        if (isAdded()) {
            dismissAllowingStateLoss();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String text = "Loading";
        return new MaterialDialog.Builder(getActivity())
                .progress(true, 0)
                .title(text)
                .content(text)
                .build();
    }
}
