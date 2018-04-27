package com.purdue.a407.cryptodisco.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.omadahealth.lollipin.lib.managers.AppLock;
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


public class StartActivity extends AppCompatActivity {

    private static final int REQUEST_LOCK = 1000;

    private static final int CONFIRM_PIN = 2000;

    private static final int FIRST_TIME = 3000;

    @Inject
    DeviceID deviceID;

    @Inject
    CDApi cdApi;

    @Inject
    AppDatabase appDatabase;

    @Inject
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ((App)getApplication()).getNetComponent().inject(this);

        Log.d("UUID", deviceID.getDeviceID());
        String UID = deviceID.getDeviceID();
        String FCMToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("UUID", UID);
        if (!LockManager.getInstance().getAppLock().isPasscodeSet()) {
            Intent intent = new Intent(this, PinActivity.class);
            intent.putExtra(AppLock.EXTRA_TYPE, AppLock.ENABLE_PINLOCK);
            startActivityForResult(intent, REQUEST_LOCK);
        }


        else {
            Intent intent = new Intent(this, PinActivity.class);
            intent.putExtra(AppLock.EXTRA_TYPE, AppLock.UNLOCK_PIN);
            startActivityForResult(intent, CONFIRM_PIN);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {
            case REQUEST_LOCK:
                firstTimeCheck();
                break;
            case CONFIRM_PIN:
                firstTimeCheck();
                break;
            case FIRST_TIME:
                firstTimeCheck();
                break;
        }
    }

    private void firstTimeCheck() {
        if(!sharedPreferences.getBoolean(
                "firstTime", false)) {
            sharedPreferences.edit().putBoolean("firstTime", true).apply();
            Intent intent = new Intent(this, FirstActivity.class);
            startActivityForResult(intent, FIRST_TIME);
        }
        else {
            continueToHome();
        }
    }


    private void continueToHome() {
        String UID = deviceID.getDeviceID();
        String FCMToken = FirebaseInstanceId.getInstance().getToken();
        cdApi.getCoins().enqueue(new Callback<List<CoinEntity>>() {
            @Override
            public void onResponse(Call<List<CoinEntity>> call, Response<List<CoinEntity>> response) {
                if(response.code() != 200) {
                    // Error
                    Log.d("Coin Result", String.valueOf(response.code()));

                }
                else {
                    // Success
                    Log.d("Coin Result", "Success");
                }
            }

            @Override
            public void onFailure(Call<List<CoinEntity>> call, Throwable t) {
                Log.d("Coin Result", "Failure");

            }
        });

        cdApi.userExists(UID, FCMToken).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() != 200) {
                    // Error
                    Log.d("Result", String.valueOf(response.code()));
                    Intent myIntent = new Intent(StartActivity.this, HomeActivity.class);
                    startActivity(myIntent);
                }
                else {
                    // Success
                    Log.d("Result", "Success");
                    Intent myIntent = new Intent(StartActivity.this, HomeActivity.class);
                    startActivity(myIntent);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Failure
                Log.d("Result", "Failure");
                Intent myIntent = new Intent(StartActivity.this, HomeActivity.class);
                startActivity(myIntent);
            }
        });
    }
}
