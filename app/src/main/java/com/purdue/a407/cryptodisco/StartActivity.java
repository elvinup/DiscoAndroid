package com.purdue.a407.cryptodisco;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.purdue.a407.cryptodisco.Api.ApiBuilder;
import com.purdue.a407.cryptodisco.Models.Responses.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.provider.Settings.Secure;
import android.util.Log;


public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        String android_id = Secure.getString(getContentResolver(),
                Secure.ANDROID_ID);

        Log.d("UID", android_id);




        ApiBuilder.getInstance().userExists(android_id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() != 200) {
                    // Error
                    Log.d("Result", String.valueOf(response.code()));
                }
                else {
                    // Success
                    Log.d("Result", "Works");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Failure
                Log.d("Failure", "Exception");
            }
        });
    }
}
