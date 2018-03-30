package com.purdue.a407.cryptodisco.Data.DAOs;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;


import com.purdue.a407.cryptodisco.Data.Entities.CoinEntity;
import com.purdue.a407.cryptodisco.Data.Entities.WatchListEntity;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface WatchlistDao {

    @Query("SELECT * FROM WatchListEntity")
    LiveData<List<WatchListEntity>> getWatchLists();

    @Query("SELECT * FROM WatchListEntity")
    List<WatchListEntity> watchListsNotLive();
    
    @Query("SELECT COUNT (*) FROM WatchListEntity WHERE user = :user AND coin = :coin")
    int getTimeUserLikedCoin(String user, int coin);

    @Insert(onConflict = REPLACE)
    void saveAll(List<WatchListEntity> watchListEntities);
    
}
