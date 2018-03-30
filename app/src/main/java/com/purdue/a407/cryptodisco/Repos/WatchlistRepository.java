package com.purdue.a407.cryptodisco.Repos;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.purdue.a407.cryptodisco.Api.CDApi;
import com.purdue.a407.cryptodisco.CacheData.CDResource;
import com.purdue.a407.cryptodisco.CacheData.NetworkBoundResource;
import com.purdue.a407.cryptodisco.Data.DAOs.CoinDao;
import com.purdue.a407.cryptodisco.Data.Entities.CoinEntity;
import com.purdue.a407.cryptodisco.Data.Entities.WatchListEntity;

import java.util.List;

import retrofit2.Call;

/**
 * Created by KennyZheng on 3/30/18.
 */

public class WatchlistRepository {

    CDApi cdApi;
    CoinDao coinDao;


    public WatchlistRepository(CDApi cdApi, CoinDao coinDao) {
        this.cdApi = cdApi;
        this.coinDao = coinDao;
    }

    @NonNull
    public LiveData<CDResource<List<WatchListEntity>>> coins() {
        return new NetworkBoundResource<List<WatchListEntity>, List<WatchListEntity>>() {

            @Override
            protected void saveCallResult(@NonNull List<WatchListEntity> items) {
                coinDao.saveAll(items);
            }

            @NonNull
            @Override
            protected LiveData<List<WatchListEntity>> loadFromDb() {
                return coinDao.getCoins();
            }

            @NonNull
            @Override
            protected Call<List<WatchListEntity>> createCall() {
                return cdApi.getCoins();
            }
        }.getAsLiveData();
    }
}
