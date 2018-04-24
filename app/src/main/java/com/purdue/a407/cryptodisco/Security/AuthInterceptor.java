package com.purdue.a407.cryptodisco.Security;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    public AuthInterceptor() {
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request original = chain.request();
        HttpUrl url = original.url();
        String encoded = url.encodedQuery();
        if (encoded == null) {
            return chain.proceed(chain.request());
        } else {
            Request.Builder builder = chain.request().newBuilder();
            builder.url(url.newBuilder()
                    .encodedQuery(encoded.replaceAll("%26", "&"))
                    .build());
            return chain.proceed(builder.build());
        }
    }
}
