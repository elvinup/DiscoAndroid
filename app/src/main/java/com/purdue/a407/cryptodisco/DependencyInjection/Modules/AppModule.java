package com.purdue.a407.cryptodisco.DependencyInjection.Modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.preference.PreferenceManager;

import com.purdue.a407.cryptodisco.Helpers.DeviceID;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    Application mApplication;

    public AppModule(Application mApplication) {
        this.mApplication = mApplication;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return mApplication;
    }

    @Singleton
    @Provides
    public DeviceID provideDeviceID(Application application) {
        return new DeviceID(Settings.Secure.getString(application.getContentResolver(),
                Settings.Secure.ANDROID_ID));
    }

    @Singleton
    @Provides
    public SharedPreferences provideSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }
}
