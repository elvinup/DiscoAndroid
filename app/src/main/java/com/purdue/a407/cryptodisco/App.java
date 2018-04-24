package com.purdue.a407.cryptodisco;

import android.app.Activity;
import android.app.Application;

import com.github.omadahealth.lollipin.lib.managers.LockManager;
import com.purdue.a407.cryptodisco.Activities.PinActivity;
import com.purdue.a407.cryptodisco.DependencyInjection.Components.DaggerNetComponent;
import com.purdue.a407.cryptodisco.DependencyInjection.Components.NetComponent;
import com.purdue.a407.cryptodisco.DependencyInjection.Modules.AppModule;

import javax.inject.Inject;

public class App extends Application {

    private NetComponent mNetComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mNetComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .build();
        LockManager<PinActivity> lockManager = LockManager.getInstance();
        lockManager.enableAppLock(this, PinActivity.class);
    }

    public NetComponent getNetComponent() {
        return mNetComponent;
    }
}
