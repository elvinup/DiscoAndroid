package com.purdue.a407.cryptodisco.Data.DAOs;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.purdue.a407.cryptodisco.Data.Entities.ExchangeEntity;
import com.purdue.a407.cryptodisco.Data.Entities.UserExchangeEntity;

import java.util.List;

@Dao
public interface UserExchangeDao {
    @Query("SELECT * FROM UserExchangeEntity")
    LiveData<List<UserExchangeEntity>> userExchanges();

    @Query("SELECT * FROM UserExchangeEntity")
    List<UserExchangeEntity> userExchangesNotLive();

    @Query("SELECT * FROM UserExchangeEntity WHERE name = :exchName")
    UserExchangeEntity getExchangeByName(String exchName);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserExchangeEntity entity);

    @Update
    void update(UserExchangeEntity entity);

    @Delete
    void delete(UserExchangeEntity entity);

    @Query("DELETE FROM UserExchangeEntity")
    void clear();
}
