package com.purdue.a407.cryptodisco.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.purdue.a407.cryptodisco.CacheData.CDResource;
import com.purdue.a407.cryptodisco.Data.AppDatabase;
import com.purdue.a407.cryptodisco.Data.Entities.ExchangeEntity;
import com.purdue.a407.cryptodisco.Data.Entities.UserExchangeEntity;
import com.purdue.a407.cryptodisco.Repos.ExchangeRepository;

import java.util.List;

import javax.inject.Inject;

public class UserExchangesViewModel extends ViewModel {

    @NonNull
    private final AppDatabase appDatabase;


    private final LiveData<List<UserExchangeEntity>> exchanges;

    @Inject
    public UserExchangesViewModel(@NonNull AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
        exchanges = appDatabase.userExchangeDao().userExchanges();
    }

    @MainThread
    @NonNull
    public LiveData<List<UserExchangeEntity>> getExchangesList() {
        return exchanges;
    }
}
