package com.purdue.a407.cryptodisco.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.purdue.a407.cryptodisco.Data.Entities.ExchangeEntity;
import com.purdue.a407.cryptodisco.CacheData.CDResource;
import com.purdue.a407.cryptodisco.Repos.ExchangeRepository;

import java.util.List;

import javax.inject.Inject;

public class ExchangesViewModel extends ViewModel {
//    @NonNull
//    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();

    @NonNull
    private final ExchangeRepository repository;


    private final LiveData<CDResource<List<ExchangeEntity>>> exchanges;

    @Inject
    public ExchangesViewModel(@NonNull ExchangeRepository repository) {
        this.repository = repository;
        exchanges = repository.exchanges();
    }

    @MainThread
    @NonNull
    public LiveData<CDResource<List<ExchangeEntity>>> getExchangesList() {
        return exchanges;
    }
}
