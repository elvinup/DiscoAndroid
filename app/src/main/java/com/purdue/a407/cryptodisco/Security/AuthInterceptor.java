package com.purdue.a407.cryptodisco.Security;
//
//import android.util.Log;
//
//import com.google.gson.Gson;
//import com.purdue.a407.cryptodisco.HttpModels.GateIO.GateIOOrderRequest;
//import com.purdue.a407.cryptodisco.HttpModels.ThirdPartyRequest;
//
//import java.io.IOException;
//import java.util.Iterator;
//import java.util.Map;
//
//import okhttp3.HttpUrl;
//import okhttp3.Interceptor;
//import okhttp3.MediaType;
//import okhttp3.Request;
//import okhttp3.Response;
//
public class AuthInterceptor {
//    private final String apiKey;
//
//    private final String secret;
//
//    private ThirdPartyRequest object;
//
//    public AuthInterceptor(String apiKey, String secret, ThirdPartyRequest object) {
//        this.object = object;
//        this.apiKey = apiKey;
//        this.secret = secret;
//    }
//
//    @Override
//    public Response intercept(Chain chain) throws IOException {
//        Request original = chain.request();
//        Request.Builder newRequestBuilder = original.newBuilder();
//        newRequestBuilder.addHeader(object.apiKeyHeader(), apiKey);
//
//        HttpUrl.Builder urlBuilder = original.url().newBuilder();
//        Log.d("ObjectIn Interceptor", new Gson().toJson(object));
//        Iterator it = object.serializeToQuery().entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry pair = (Map.Entry)it.next();
//            urlBuilder.addQueryParameter(pair.getKey().toString(), pair.getValue().toString());
//            it.remove();
//        }
//
//
//        String urlStr = urlBuilder.build().url().getQuery();
//        if (!urlStr.isEmpty()) {
//            String signature = object.getSecretSigner().sign(urlStr, secret);
//            Log.d("SIGNATURE", signature);
//            if(object instanceof GateIOOrderRequest) {
//                newRequestBuilder.addHeader(object.secretHeader(),signature);
//            }
//            else
//                urlBuilder.addQueryParameter(object.secretHeader(), signature).build();
//            newRequestBuilder.url(urlBuilder.build());
//        }
//        Request newRequest = newRequestBuilder.build();
//        Log.d("URL STRING", newRequest.url().toString());
//        Log.d("URL HEADERS", newRequest.headers().toString());
//        return chain.proceed(newRequest);
//    }
}
