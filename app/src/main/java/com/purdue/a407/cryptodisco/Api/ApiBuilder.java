package com.purdue.a407.cryptodisco.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiBuilder {

    public static CDApi cdApi;

    public static CDApi getInstance() {
        if(cdApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://ec2-54-165-180-155.compute-1.amazonaws.com:3825/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            cdApi = retrofit.create(CDApi.class);
        }
        return cdApi;
    }
}
