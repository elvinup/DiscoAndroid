package com.purdue.a407.cryptodisco.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v14.preference.PreferenceFragment;
import android.support.v7.preference.Preference;
import android.util.Log;
import android.widget.Toast;

import com.github.omadahealth.lollipin.lib.managers.AppLock;
import com.github.omadahealth.lollipin.lib.managers.LockManager;
import com.purdue.a407.cryptodisco.Activities.PinActivity;
import com.purdue.a407.cryptodisco.Api.CDApi;
import com.purdue.a407.cryptodisco.App;
import com.purdue.a407.cryptodisco.Data.AppDatabase;
import com.purdue.a407.cryptodisco.Data.Entities.SettingsEntity;
import com.purdue.a407.cryptodisco.Helpers.DeviceID;
import com.purdue.a407.cryptodisco.R;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends PreferenceFragment {


    @Inject
    CDApi cdApi;

    @Inject
    DeviceID deviceID;
    @Inject
    SharedPreferences sharedPreferences;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        ((App) getActivity().getApplication()).getNetComponent().inject(this);
        addPreferencesFromResource(R.xml.settings_screen);
        Preference button = findPreference("Backup");
        Preference setCode = findPreference("Passcode");
        setCode.setOnPreferenceClickListener((preference) ->  {
            Intent intent = new Intent(getActivity(), PinActivity.class);
            intent.putExtra(AppLock.EXTRA_TYPE, AppLock.CHANGE_PIN);
            startActivity(intent);
            return true;
        });
        button.setOnPreferenceClickListener(preference -> {
            //code for what you want it to do
            boolean uuid = sharedPreferences.getBoolean("key1", false);
            boolean arb = sharedPreferences.getBoolean("key2", false);
            if(uuid) {
                cdApi.insertBackup(new SettingsEntity(deviceID.getDeviceID(), arb)).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code() != 200) {
                            Log.d("STATUS CODE", String.valueOf(response.code()));
                            Toast.makeText(getActivity(), "There was an error:", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(getActivity(), "Successfully backed up settings", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getActivity(), "There was a failure", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else
                Toast.makeText(getActivity(), "Must select UUID to be backed up", Toast.LENGTH_SHORT).show();
            return true;
        });
    }
}
