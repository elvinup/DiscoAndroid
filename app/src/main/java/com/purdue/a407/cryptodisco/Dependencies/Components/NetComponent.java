package com.purdue.a407.cryptodisco.Dependencies.Components;

import com.purdue.a407.cryptodisco.Dependencies.Modules.AppModule;
import com.purdue.a407.cryptodisco.Dependencies.Modules.NetModule;
import com.purdue.a407.cryptodisco.HomeActivity;
import com.purdue.a407.cryptodisco.StartActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface NetComponent {
    void inject(HomeActivity activity);
    void inject(StartActivity activity);
}
