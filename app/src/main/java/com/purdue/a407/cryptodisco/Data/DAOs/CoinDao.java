package com.purdue.a407.cryptodisco.Data.DAOs;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.purdue.a407.cryptodisco.Data.Entities.ChatRoomEntity;
import com.purdue.a407.cryptodisco.Data.Entities.CoinEntity;
import com.purdue.a407.cryptodisco.Data.Entities.CoinEntity;
import com.purdue.a407.cryptodisco.Data.Entities.ExchangeEntity;

import java.util.List;

import javax.inject.Inject;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface CoinDao {

    @Query("SELECT * FROM CoinEntity")
    LiveData<List<CoinEntity>> getCoins();

    @Query("SELECT * FROM CoinEntity")
    List<CoinEntity> coinsNotLive();

    @Query("SELECT * FROM CoinEntity WHERE id = :id")
    LiveData<List<CoinEntity>> coinById(int id);

    @Insert(onConflict = REPLACE)
    void saveAll(List<CoinEntity> coinEntities);

    @Query("DELETE FROM CoinEntity")
    void clear();

    @Query("SELECT id FROM CoinEntity WHERE short_name = :shortName")
    int getID(String shortName);

    @Query("SELECT COUNT (*) FROM WatchListEntity WHERE user = :user AND coin = :coin")
    int getTimeUserLikedCoin(String user, int coin);

}