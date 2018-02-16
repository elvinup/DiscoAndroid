package com.purdue.a407.cryptodisco.Api;

import com.purdue.a407.cryptodisco.Data.Entities.ExchangeEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CDApi {

    @GET("/user/")
    Call<Void> userExists(@Query("uuid") String uid);

    @GET("/exchanges")
    Call<List<ExchangeEntity>> getExchanges();
}
