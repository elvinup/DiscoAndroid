package com.purdue.a407.cryptodisco.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.purdue.a407.cryptodisco.CacheData.CDResource;
import com.purdue.a407.cryptodisco.Data.Entities.CoinPairingEntity;
import com.purdue.a407.cryptodisco.Data.Entities.ExchangeEntity;
import com.purdue.a407.cryptodisco.Repos.CoinPairingRepository;
import com.purdue.a407.cryptodisco.Repos.ExchangeRepository;

import java.util.List;

import javax.inject.Inject;

public class ExchangeViewModel extends ViewModel {
    @NonNull
    private final CoinPairingRepository repository;


    private final LiveData<CDResource<List<CoinPairingEntity>>> pairings;

    @Inject
    public ExchangeViewModel(@NonNull CoinPairingRepository repository) {
        this.repository = repository;
        pairings = repository.coinPairings();
    }

    @MainThread
    @NonNull
    public LiveData<CDResource<List<CoinPairingEntity>>> getCoinPairings() {
        return pairings;
    }
}
