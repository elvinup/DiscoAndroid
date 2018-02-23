package com.purdue.a407.cryptodisco.Fragments;


import android.os.Bundle;
import android.support.v14.preference.PreferenceFragment;

import com.purdue.a407.cryptodisco.R;

public class SettingsFragment extends PreferenceFragment {


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_screen);
    }
}
