package com.purdue.a407.cryptodisco.Repos;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.purdue.a407.cryptodisco.Api.CDApi;
import com.purdue.a407.cryptodisco.CacheData.CDResource;
import com.purdue.a407.cryptodisco.CacheData.NetworkBoundResource;
import com.purdue.a407.cryptodisco.Data.DAOs.CoinDao;
import com.purdue.a407.cryptodisco.Data.DAOs.WatchlistDao;
import com.purdue.a407.cryptodisco.Data.Entities.CoinEntity;
import com.purdue.a407.cryptodisco.Data.Entities.WatchListEntity;

import java.util.List;

import retrofit2.Call;

/**
 * Created by KennyZheng on 3/30/18.
 */

public class WatchlistRepository {

    CDApi cdApi;
    WatchlistDao wlDao;


    public WatchlistRepository(CDApi cdApi, WatchlistDao wlDao) {
        this.cdApi = cdApi;
        this.wlDao = wlDao;
    }

    @NonNull
    public LiveData<CDResource<List<WatchListEntity>>> coins() {
        return new NetworkBoundResource<List<WatchListEntity>, List<WatchListEntity>>() {

            @Override
            protected void saveCallResult(@NonNull List<WatchListEntity> items) {
                wlDao.saveAll(items);
            }

            @NonNull
            @Override
            protected LiveData<List<WatchListEntity>> loadFromDb() {
                return wlDao.getWatchLists();
            }

            @NonNull
            @Override
            protected Call<List<WatchListEntity>> createCall() {
                return cdApi.getWatchListEntities();
            }
        }.getAsLiveData();
    }
}
