package com.purdue.a407.cryptodisco.Data.DAOs;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.purdue.a407.cryptodisco.Data.Entities.ChatMessageEntity;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface ChatmsgDao {

    @Query("SELECT * FROM ChatMessageEntity")
    LiveData<List<ChatMessageEntity>> chatMessages();

    @Insert(onConflict = REPLACE)
    void saveAll(List<ChatMessageEntity> chatMessageEntities);

    @Query("DELETE FROM ChatMessageEntity")
    void clear();
}
