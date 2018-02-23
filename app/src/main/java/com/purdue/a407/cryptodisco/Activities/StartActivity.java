package com.purdue.a407.cryptodisco.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.purdue.a407.cryptodisco.Api.CDApi;
import com.purdue.a407.cryptodisco.App;
import com.purdue.a407.cryptodisco.Data.AppDatabase;
import com.purdue.a407.cryptodisco.Helpers.DeviceID;
import com.purdue.a407.cryptodisco.R;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;


public class StartActivity extends AppCompatActivity {

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

        new Handler().postDelayed(() -> {
            if(!sharedPreferences.getBoolean(
                    "firstTime", false)) {
                sharedPreferences.edit().putBoolean("firstTime", true).apply();
                Intent intent = new Intent(this, FirstActivity.class);
                startActivity(intent);
            }
            else {
                Intent myIntent = new Intent(StartActivity.this, HomeActivity.class);
                Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(getApplication(),
                        android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
                StartActivity.this.startActivity(myIntent, bundle);
            }
        }, 1000);
        String UID = deviceID.getDeviceID();
        Log.d("UUID", UID);
        cdApi.userExists(UID).enqueue(new Callback<Void>() {
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
