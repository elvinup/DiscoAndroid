package com.purdue.a407.cryptodisco.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.purdue.a407.cryptodisco.CacheData.CDResource;
import com.purdue.a407.cryptodisco.Data.Entities.ChatRoomEntity;
import com.purdue.a407.cryptodisco.Data.Entities.CoinEntity;
import com.purdue.a407.cryptodisco.Repos.ChatRoomRepository;
import com.purdue.a407.cryptodisco.Repos.CoinRepository;

import java.util.List;

import javax.inject.Inject;

public class CoinViewModel extends ViewModel{
    @NonNull
    private final CoinRepository repository;


    private final LiveData<CDResource<List<CoinEntity>>> coins;

    @Inject
    public CoinViewModel(@NonNull CoinRepository repository) {
        this.repository = repository;
        coins = repository.coins();
    }

    @MainThread
    @NonNull
    public LiveData<CDResource<List<CoinEntity>>> getCoins() {
        return coins;
    }
}
