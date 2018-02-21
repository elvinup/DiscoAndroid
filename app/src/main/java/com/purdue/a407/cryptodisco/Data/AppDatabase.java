package com.purdue.a407.cryptodisco.Data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.purdue.a407.cryptodisco.Data.DAOs.ChatmsgDao;
import com.purdue.a407.cryptodisco.Data.DAOs.ChatroomDao;
import com.purdue.a407.cryptodisco.Data.DAOs.ExchangeDao;
import com.purdue.a407.cryptodisco.Data.Entities.ChatMessageEntity;
import com.purdue.a407.cryptodisco.Data.Entities.ChatRoomEntity;
import com.purdue.a407.cryptodisco.Data.Entities.ExchangeEntity;

@Database(entities = {ExchangeEntity.class, ChatRoomEntity.class, ChatMessageEntity.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ExchangeDao exchangeDao();
    public abstract ChatroomDao chatroomDao();
    public abstract ChatmsgDao chatmsgDao();
}
