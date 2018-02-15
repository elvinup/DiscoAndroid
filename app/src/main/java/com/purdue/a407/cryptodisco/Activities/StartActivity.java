package com.purdue.a407.cryptodisco.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.purdue.a407.cryptodisco.Api.CDApi;
import com.purdue.a407.cryptodisco.App;
import com.purdue.a407.cryptodisco.Helpers.DeviceID;
import com.purdue.a407.cryptodisco.R;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;


public class StartActivity extends AppCompatActivity {

    @Inject
    DeviceID deviceID;

    @Inject
    CDApi cdApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ((App)getApplication()).getNetComponent().inject(this);



        cdApi.userExists("jonah").enqueue(new Callback<Void>() {
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
