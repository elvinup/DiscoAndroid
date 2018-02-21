package com.purdue.a407.cryptodisco.Data.DAOs;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.purdue.a407.cryptodisco.Data.Entities.ChatRoomEntity;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface ChatroomDao {

    @Query("SELECT * FROM ChatRoomEntity")
    LiveData<List<ChatRoomEntity>> chatRooms();

    @Insert(onConflict = REPLACE)
    void saveAll(List<ChatRoomEntity> chatRoomEntities);

    @Query("DELETE FROM ChatRoomEntity")
    void clear();
}
