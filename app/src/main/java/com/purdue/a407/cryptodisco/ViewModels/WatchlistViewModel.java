package com.purdue.a407.cryptodisco.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.purdue.a407.cryptodisco.CacheData.CDResource;
import com.purdue.a407.cryptodisco.Data.Entities.ExchangeEntity;
import com.purdue.a407.cryptodisco.Data.Entities.WatchListEntity;
import com.purdue.a407.cryptodisco.Repos.ExchangeRepository;
import com.purdue.a407.cryptodisco.Repos.WatchlistRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by KennyZheng on 3/30/18.
 */

public class WatchlistViewModel extends ViewModel {
    @NonNull
    private final WatchlistRepository repository;


    private final LiveData<CDResource<List<WatchListEntity>>> watchList;

    @Inject
    public WatchlistViewModel(@NonNull WatchlistRepository repository) {
        this.repository = repository;
        watchList = repository.watchLists();
    }

    @MainThread
    @NonNull
    public LiveData<CDResource<List<WatchListEntity>>> getWatchList() {
        return watchList;
    }
}
