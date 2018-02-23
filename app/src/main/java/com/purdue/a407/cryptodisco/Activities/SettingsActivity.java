package com.purdue.a407.cryptodisco.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.purdue.a407.cryptodisco.Fragments.SettingsFragment;
import com.purdue.a407.cryptodisco.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_settings);

        SettingsFragment fragment = new SettingsFragment();
        getFragmentManager().beginTransaction().
                replace(android.R.id.content,fragment).commit();
    }
}
