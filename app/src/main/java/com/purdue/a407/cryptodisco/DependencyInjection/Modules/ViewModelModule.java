package com.purdue.a407.cryptodisco.DependencyInjection.Modules;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.purdue.a407.cryptodisco.Adapters.NotificationsAdapter;
import com.purdue.a407.cryptodisco.Data.AppDatabase;
import com.purdue.a407.cryptodisco.ViewModels.ChatMsgViewModel;
import com.purdue.a407.cryptodisco.ViewModels.ChatRoomsViewModel;
import com.purdue.a407.cryptodisco.ViewModels.ExchangesViewModel;
import com.purdue.a407.cryptodisco.ViewModels.Factories.BaseViewModelFactory;
import com.purdue.a407.cryptodisco.ViewModels.NotificationsViewModel;
import com.purdue.a407.cryptodisco.ViewModels.UserExchangesViewModel;

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
    @IntoMap
    @ViewModelKey(UserExchangesViewModel.class)
    public abstract ViewModel bindUserExchangeViewModel(UserExchangesViewModel postsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ChatRoomsViewModel.class)
    public abstract ViewModel bindChatRoomsViewModel(ChatRoomsViewModel chatRoomsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ChatMsgViewModel.class)
    public abstract ViewModel bindChatMsgViewModel(ChatMsgViewModel chatMsgViewModel);

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(BaseViewModelFactory factory);
}
