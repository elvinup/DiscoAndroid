package com.purdue.a407.cryptodisco.Dependencies.Modules;

import android.app.Application;

import com.purdue.a407.cryptodisco.Dependencies.Components.NetComponent;

public class App extends Application {

    private NetComponent mNetComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mNetComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule("https://jsonplaceholder.typicode.com/"))
                .build();
    }

    public NetComponent getNetComponent() {
        return mNetComponent;
    }
}
