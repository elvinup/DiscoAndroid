package com.purdue.a407.cryptodisco.Data.DAOs;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.purdue.a407.cryptodisco.Data.Entities.CoinPairingEntity;

import java.util.List;

import javax.inject.Inject;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface CoinPairingDao {

    @Query("SELECT * FROM CoinPairingEntity")
    LiveData<List<CoinPairingEntity>> coinPairings();

    @Query("SELECT * FROM CoinPairingEntity WHERE exchange = :exch")
    LiveData<List<CoinPairingEntity>> coinPairingsByExchange(String exch);

    @Query("SELECT price FROM CoinPairingEntity WHERE coin_short = :coinShort " +
            "AND market_short = :marketShort")
    float getPrice(String coinShort, String marketShort);


    @Insert(onConflict = REPLACE)
    void insertAll(List<CoinPairingEntity> entities);

    @Query("DELETE FROM CoinPairingEntity")
    void deleteAll();
}
