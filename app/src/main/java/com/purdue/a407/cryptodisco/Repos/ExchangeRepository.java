package com.purdue.a407.cryptodisco.Repos;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.purdue.a407.cryptodisco.Api.CDApi;
import com.purdue.a407.cryptodisco.Data.DAOs.ExchangeDao;
import com.purdue.a407.cryptodisco.Data.Entities.ExchangeEntity;
import com.purdue.a407.cryptodisco.CacheData.CDResource;
import com.purdue.a407.cryptodisco.CacheData.NetworkBoundResource;

import java.util.List;

import retrofit2.Call;

public class ExchangeRepository {

    CDApi cdApi;
    ExchangeDao exchangeDao;


    public ExchangeRepository(CDApi cdApi, ExchangeDao exchangeDao) {
        this.cdApi = cdApi;
        this.exchangeDao = exchangeDao;
    }

    @NonNull
    public LiveData<CDResource<List<ExchangeEntity>>> exchanges() {
        return new NetworkBoundResource<List<ExchangeEntity>, List<ExchangeEntity>>() {

            @Override
            protected void saveCallResult(@NonNull List<ExchangeEntity> items) {
                exchangeDao.saveAll(items);
            }

            @NonNull
            @Override
            protected LiveData<List<ExchangeEntity>> loadFromDb() {
                return exchangeDao.exchanges();
            }

            @NonNull
            @Override
            protected Call<List<ExchangeEntity>> createCall() {
                return cdApi.getExchanges();
            }
        }.getAsLiveData();
    }

}
