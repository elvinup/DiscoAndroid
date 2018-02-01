package com.purdue.a407.cryptodisco.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiBuilder {

    public static CDApi cdApi;

    public static CDApi getInstance() {
        if(cdApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.github.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            cdApi = retrofit.create(CDApi.class);
        }
        return cdApi;
    }
}
