package com.purdue.a407.cryptodisco.DependencyInjection.Components;

import com.purdue.a407.cryptodisco.DependencyInjection.Modules.AppModule;
import com.purdue.a407.cryptodisco.DependencyInjection.Modules.DBModule;
import com.purdue.a407.cryptodisco.DependencyInjection.Modules.NetModule;
import com.purdue.a407.cryptodisco.Activities.HomeActivity;
import com.purdue.a407.cryptodisco.Activities.StartActivity;
import com.purdue.a407.cryptodisco.Fragments.DummyFragment;
import com.purdue.a407.cryptodisco.Fragments.ExchangeFragment;
import com.purdue.a407.cryptodisco.Fragments.ExchangesFragment;
import com.purdue.a407.cryptodisco.Fragments.MyExchangeFragment;
import com.purdue.a407.cryptodisco.Helpers.BinanceHelper;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetModule.class, DBModule.class})
public interface NetComponent {
    void inject(HomeActivity activity);
    void inject(StartActivity activity);
    void inject(DummyFragment dummyFragment);
    void inject(ExchangesFragment exchangesFragment);
    void inject(ExchangeFragment exchangeFragment);
    void inject(MyExchangeFragment exchangeFragment);
    void inject(BinanceHelper binanceHelper);
}
