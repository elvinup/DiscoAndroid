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

import java.util.ArrayList;
import java.util.List;

public class NewsfeedActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeeds);

        TwitterAuthConfig authConfig =  new TwitterAuthConfig(
                "mHWqXrsXR6xPj6Z6v9jFhTDGJ",
                "umFSrYmoxAomzvpr6GmCytIw3IqTFOJpTuiUR6dJROrJAFMkO1");
        TwitterConfig.Builder builder=new TwitterConfig.Builder(this);
        builder.twitterAuthConfig(authConfig);
        Twitter.initialize(builder.build());
        Log.d("um", "hello?");

        LinearLayout myLayout
                = (LinearLayout) findViewById(R.id.newsfeed_layout);

        final long tweetId = 510908133917487104L;
        final long tweetId2 = 982399569899143170L;
        List<Long> tweetIds = new ArrayList<>();
        tweetIds.add(tweetId);
        tweetIds.add(tweetId2);

        TweetUtils.loadTweets(tweetIds, new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                myLayout.addView(new TweetView(NewsfeedActivity.this, result.data.get(0)));
                myLayout.addView(new TweetView(NewsfeedActivity.this, result.data.get(1)));
            }

            @Override
            public void failure(TwitterException exception) {

            }
        });

        /*
        TweetUtils.loadTweet(tweetId, new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                Log.d("Tweet", result.data.text);
                myLayout.addView(new TweetView(NewsfeedActivity.this, result.data));
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("Very useful debug", "yo");
                Toast.makeText(NewsfeedActivity.this, "yo", 5);
            }
        });

        TweetUtils.loadTweet(tweetId2, new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                myLayout.addView(new TweetView(NewsfeedActivity.this, result.data));
            }

            @Override
            public void failure(TwitterException exception) {

            }
        });
        */
    }
}
