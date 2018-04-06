package com.purdue.a407.cryptodisco.Data.DAOs;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.purdue.a407.cryptodisco.Data.Entities.NotificationsEntity;

import java.util.List;

@Dao
public interface NotificationsDao {

    @Query("SELECT * FROM NotificationsEntity")
    public LiveData<List<NotificationsEntity>> getNotifications();


    @Insert
    void insert(NotificationsEntity entity);

    @Update
    void update(NotificationsEntity entity);


    @Query("DELETE FROM NotificationsEntity")
    void clear();
}
