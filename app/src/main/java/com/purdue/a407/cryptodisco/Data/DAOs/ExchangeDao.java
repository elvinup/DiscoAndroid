package com.purdue.a407.cryptodisco.Data.DAOs;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.purdue.a407.cryptodisco.Data.Entities.ExchangeEntity;

import java.util.List;

import io.reactivex.Flowable;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface ExchangeDao {

    @Query("SELECT * FROM ExchangeEntity")
    LiveData<List<ExchangeEntity>> exchanges();

    @Query("SELECT * FROM ExchangeEntity")
    List<ExchangeEntity> exchangesNotLive();

    @Query("SELECT * FROM ExchangeEntity WHERE name LIKE :search")
    List<ExchangeEntity> getExchangesBySearch(String search);

    @Insert(onConflict = REPLACE)
    void saveAll(List<ExchangeEntity> exchangeEntities);

    @Query("DELETE FROM ExchangeEntity")
    void clear();
}
