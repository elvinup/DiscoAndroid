package com.purdue.a407.cryptodisco.DependencyInjection.Modules;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.purdue.a407.cryptodisco.ViewModels.ExchangesViewModel;
import com.purdue.a407.cryptodisco.ViewModels.Factories.BaseViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ExchangesViewModel.class)
    public abstract ViewModel bindExchangeViewModel(ExchangesViewModel postsViewModel);

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(BaseViewModelFactory factory);
}
