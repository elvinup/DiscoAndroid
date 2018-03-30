package com.purdue.a407.cryptodisco.Repos;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.purdue.a407.cryptodisco.Api.CDApi;
import com.purdue.a407.cryptodisco.Data.DAOs.CoinDao;
import com.purdue.a407.cryptodisco.Data.Entities.ChatMessageEntity;
import com.purdue.a407.cryptodisco.Data.Entities.ChatRoomEntity;
import com.purdue.a407.cryptodisco.Data.Entities.CoinEntity;
import com.purdue.a407.cryptodisco.Data.Entities.ExchangeEntity;
import com.purdue.a407.cryptodisco.CacheData.CDResource;
import com.purdue.a407.cryptodisco.CacheData.NetworkBoundResource;

import java.util.List;

import retrofit2.Call;

public class CoinRepository {

    CDApi cdApi;
    CoinDao coinDao;


    public CoinRepository(CDApi cdApi, CoinDao coinDao) {
        this.cdApi = cdApi;
        this.coinDao = coinDao;
    }

    @NonNull
    public LiveData<CDResource<List<CoinEntity>>> coins() {
        return new NetworkBoundResource<List<CoinEntity>, List<CoinEntity>>() {

            @Override
            protected void saveCallResult(@NonNull List<CoinEntity> items) {
                coinDao.saveAll(items);
            }

            @NonNull
            @Override
            protected LiveData<List<CoinEntity>> loadFromDb() {
                return coinDao.getCoins();
            }

            @NonNull
            @Override
            protected Call<List<CoinEntity>> createCall() {
                return cdApi.getCoins();
            }
        }.getAsLiveData();
    }

}
