package com.purdue.a407.cryptodisco.Api;

import com.purdue.a407.cryptodisco.Models.Responses.LoginResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CDApi {

    @GET("/user/exists")
    Call<LoginResponse> userExists(@Query("uid") String uid);

}
