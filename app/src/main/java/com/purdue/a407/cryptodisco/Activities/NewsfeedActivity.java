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
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

public class NewsfeedActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_newsfeeds);

        Log.d("um", "hello?");

        final ConstraintLayout myLayout
                = (ConstraintLayout) findViewById(R.id.my_tweet_layout);

        final long tweetId = 510908133917487104L;
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
    }
}
