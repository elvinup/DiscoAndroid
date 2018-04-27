package com.purdue.a407.cryptodisco.Activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;
// EmbeddedTweetActivity
import com.purdue.a407.cryptodisco.Adapters.CoinAdapter;
import com.purdue.a407.cryptodisco.Api.CDApi;
import com.purdue.a407.cryptodisco.App;
import com.purdue.a407.cryptodisco.Data.AppDatabase;
import com.purdue.a407.cryptodisco.Data.Entities.CoinEntity;
import com.purdue.a407.cryptodisco.Data.Entities.TweetId;
import com.purdue.a407.cryptodisco.Data.Entities.WatchListEntity;
import com.purdue.a407.cryptodisco.Helpers.DeviceID;
import com.purdue.a407.cryptodisco.R;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsfeedActivity extends AppCompatActivity {

    @Inject
    CDApi cdApi;

    @Inject
    AppDatabase appDatabase;

    CoinAdapter watchlistAdapter;

    @Inject
    DeviceID deviceID;

    @OnClick(R.id.topBackButton)
    public void onBack() {
        onBackPressed();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeeds);

        ButterKnife.bind(this);
        ((App) getApplication()).getNetComponent().inject(this);

        //Gets user watch list
        List<CoinEntity> coinEntityList = appDatabase.coinDao().coinsNotLive();
        String uuid = deviceID.getDeviceID();
        watchlistAdapter = new CoinAdapter(this, new ArrayList<>(), coinEntityList, uuid);


        TwitterAuthConfig authConfig =  new TwitterAuthConfig(
                "mHWqXrsXR6xPj6Z6v9jFhTDGJ",
                "umFSrYmoxAomzvpr6GmCytIw3IqTFOJpTuiUR6dJROrJAFMkO1");
        TwitterConfig.Builder builder=new TwitterConfig.Builder(this);
        builder.twitterAuthConfig(authConfig);
        Twitter.initialize(builder.build());
        Log.d("um", "hello?");

        LinearLayout myLayout
                = (LinearLayout) findViewById(R.id.newsfeed_layout);

        //final long tweetId = 984001470646444034L;
        //final long tweetId2 = 983915609737121792L;
        List<Long> tweetIds = new ArrayList<>();
        //tweetIds.add(tweetId);
        //tweetIds.add(tweetId2);

        StringBuilder sb = new StringBuilder();

        cdApi.getWatchListEntities().enqueue(new retrofit2.Callback<List<WatchListEntity>>() {
            @Override
            public void onResponse(Call<List<WatchListEntity>> call, Response<List<WatchListEntity>> response) {
                //wl = response.body();
                List<CoinEntity> cl = watchlistAdapter.filterCoinList(response.body());

                Log.d("WatchList Response", Integer.toString(response.body().size()));
                Log.d("Coinlist Size", Integer.toString(cl.size()));
                for (CoinEntity entry: cl) {
                    sb.append(entry.getShort_name() + "&");
                }

                Log.d("Query Str", sb.toString());


                //Interceptor to replace %26 to &'s
                Interceptor interceptor = new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        HttpUrl url = original.url();
                        String encoded = url.encodedQuery();
                        if(encoded == null) {
                            return chain.proceed(original);
                        }
                        Request.Builder reqBuilder = chain.request().newBuilder();
                        reqBuilder.url(url.newBuilder()
                                .encodedQuery(encoded.replaceAll("%26", "&"))
                                .build());
                        return chain.proceed(reqBuilder.build());
                    }
                };
                OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
                CDApi second = new Retrofit.Builder().baseUrl("http://ec2-54-165-180-155.compute-1.amazonaws.com:3825/")
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build().create(CDApi.class);

                second.getTweets(sb.toString(), 4).enqueue(new retrofit2.Callback<List<TweetId>>() {
                    @Override
                    public void onResponse(Call<List<TweetId>> call, Response<List<TweetId>> response) {
                        if(response.code() != 200) {
                            // Error
                            Log.d("Result", String.valueOf(response.code()));
                        }
                        else {
                            Log.d("NumOfTweets", Integer.toString(response.body().size()));

                            for (TweetId id: response.body()) {
                                tweetIds.add(id.getId());
                            }

                            TweetUtils.loadTweets(tweetIds, new Callback<List<Tweet>>() {
                                @Override
                                public void success(Result<List<Tweet>> result) {
                                    int i;
                                    Log.d("tweet list size", Integer.toString(tweetIds.size()));
                                    for (i = 0; i < tweetIds.size(); i++) {
                                        myLayout.addView(new TweetView(NewsfeedActivity.this, result.data.get(i)));
                                        //myLayout.add;
                                    }
                                }

                                @Override
                                public void failure(TwitterException exception) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<List<TweetId>> call, Throwable t) {
                        Log.d("Result", "TweetGrabFailure");

                    }
                });
            }

            @Override
            public void onFailure(Call<List<WatchListEntity>> call, Throwable t) {

            }
        });
    }
}
