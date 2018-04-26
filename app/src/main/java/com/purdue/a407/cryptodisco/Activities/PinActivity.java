package com.purdue.a407.cryptodisco.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.omadahealth.lollipin.lib.managers.AppLockActivity;
import com.purdue.a407.cryptodisco.R;


public class PinActivity extends AppLockActivity {


    @Override
    public void showForgotDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Positive",
                (dialogInterface, i) -> dialogInterface.dismiss());
        builder.setNegativeButton("Negative",
                (dialogInterface, i) -> dialogInterface.dismiss());
        builder.create().show();
    }

    @Override
    public void onPinFailure(int attempts) {

    }

    @Override
    public void onPinSuccess(int attempts) {
    }


}
