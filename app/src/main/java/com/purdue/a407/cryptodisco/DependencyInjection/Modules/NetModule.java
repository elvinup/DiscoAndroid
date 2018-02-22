package com.purdue.a407.cryptodisco.DependencyInjection.Modules;

import android.app.Application;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.purdue.a407.cryptodisco.Api.CDApi;
import com.purdue.a407.cryptodisco.DependencyInjection.Modules.RetrofitTypes.CryptoDisco;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetModule {
    private static String CRYPTO_DISCO_URL =
            "http://ec2-54-165-180-155.compute-1.amazonaws.com:3825/";

    private static String BINANCE_URL = "https://api.binance.com/api/";

    public NetModule() {}


    @Provides
    @Singleton
    public Cache provideHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    @Provides
    @Singleton
    public Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkhttpClient(Cache cache) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.cache(cache);
        return client.build();
    }

    @Provides
    @Singleton
    @CryptoDisco
    public Retrofit provideRetrofitCryptoDisco(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(CRYPTO_DISCO_URL)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    public CDApi provideApiCryptoDisco(@CryptoDisco Retrofit retrofit) {
        return retrofit.create(CDApi.class);
    }
}
