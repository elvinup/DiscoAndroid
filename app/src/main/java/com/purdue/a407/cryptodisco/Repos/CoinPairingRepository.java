package com.purdue.a407.cryptodisco.Repos;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.purdue.a407.cryptodisco.Api.CDApi;
import com.purdue.a407.cryptodisco.CacheData.CDResource;
import com.purdue.a407.cryptodisco.CacheData.NetworkBoundResource;
import com.purdue.a407.cryptodisco.Data.DAOs.CoinPairingDao;
import com.purdue.a407.cryptodisco.Data.DAOs.ExchangeDao;
import com.purdue.a407.cryptodisco.Data.Entities.CoinPairingEntity;
import com.purdue.a407.cryptodisco.Data.Entities.ExchangeEntity;

import java.util.List;

import retrofit2.Call;

public class CoinPairingRepository {

    CDApi cdApi;
    CoinPairingDao coinPairingDao;
    private String exchange;


    public CoinPairingRepository(CDApi cdApi, CoinPairingDao coinPairingDao) {
        this.cdApi = cdApi;
        this.coinPairingDao = coinPairingDao;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange.toLowerCase();
    }

    @NonNull
    public LiveData<CDResource<List<CoinPairingEntity>>> coinPairings() {
        return new NetworkBoundResource<List<CoinPairingEntity>, List<CoinPairingEntity>>() {

            @Override
            protected void saveCallResult(@NonNull List<CoinPairingEntity> items) {
                coinPairingDao.insertAll(items);
            }

            @NonNull
            @Override
            protected LiveData<List<CoinPairingEntity>> loadFromDb() {
                return coinPairingDao.coinPairings();
            }

            @NonNull
            @Override
            protected Call<List<CoinPairingEntity>> createCall() {
                if(exchange == null) {
                    return cdApi.getCoinPairingsFromExchange(exchange);
                }
                else
                    return cdApi.getCoinPairingsFromExchange("binance");
            }
        }.getAsLiveData();
    }

}
