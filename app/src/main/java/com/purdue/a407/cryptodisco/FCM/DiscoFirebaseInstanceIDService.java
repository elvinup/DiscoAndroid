package com.purdue.a407.cryptodisco.FCM;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.purdue.a407.cryptodisco.Activities.HomeActivity;
import com.purdue.a407.cryptodisco.Activities.StartActivity;
import com.purdue.a407.cryptodisco.Api.CDApi;
import com.purdue.a407.cryptodisco.Helpers.DeviceID;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DiscoFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Inject
    CDApi cdApi;

    @Inject
    DeviceID deviceID;

    private static final String TAG = "DiscoFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        String UID = deviceID.getDeviceID();
        String FCMToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("UUID", UID);
        Log.d("FCMToken", FCMToken);
        cdApi.userExists(UID, FCMToken).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() != 200) {
                    // Error
                    Log.d("Result", String.valueOf(response.code()));
                }
                else {
                    // Success
                    Log.d("Result", "Success");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Failure
                Log.d("Result", "Failure");
            }
        });
    }
    // [END refresh_token]
}
