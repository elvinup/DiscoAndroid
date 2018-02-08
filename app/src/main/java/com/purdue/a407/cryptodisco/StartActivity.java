package com.purdue.a407.cryptodisco;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.purdue.a407.cryptodisco.Api.ApiBuilder;
import com.purdue.a407.cryptodisco.Api.CDApi;
import com.purdue.a407.cryptodisco.Dependencies.Modules.App;
import com.purdue.a407.cryptodisco.Models.Responses.LoginResponse;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartActivity extends AppCompatActivity {

    @Inject
    CDApi cdApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ((App) getApplication()).getNetComponent().inject(this);
        cdApi.userExists("").enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.code() != 200) {
                    // Error
                }
                else {
                    // Success
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Failure
            }
        });
    }
}
