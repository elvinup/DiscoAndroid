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
import com.github.omadahealth.lollipin.lib.managers.LockManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.purdue.a407.cryptodisco.Api.CDApi;
import com.purdue.a407.cryptodisco.App;
import com.purdue.a407.cryptodisco.Data.AppDatabase;
import com.purdue.a407.cryptodisco.Data.Entities.CoinEntity;
import com.purdue.a407.cryptodisco.Helpers.DeviceID;
import com.purdue.a407.cryptodisco.R;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.util.List;


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
