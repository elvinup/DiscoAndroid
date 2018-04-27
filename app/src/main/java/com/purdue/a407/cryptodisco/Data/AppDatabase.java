package com.purdue.a407.cryptodisco.Data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.purdue.a407.cryptodisco.Data.DAOs.CoinDao;
import com.purdue.a407.cryptodisco.Data.DAOs.CoinPairingDao;
import com.purdue.a407.cryptodisco.Data.DAOs.ExchangeDao;
import com.purdue.a407.cryptodisco.Data.DAOs.NotificationsDao;
import com.purdue.a407.cryptodisco.Data.DAOs.UserExchangeDao;
import com.purdue.a407.cryptodisco.Data.DAOs.WatchlistDao;
import com.purdue.a407.cryptodisco.Data.Entities.CoinEntity;
import com.purdue.a407.cryptodisco.Data.Entities.CoinPairingEntity;
import com.purdue.a407.cryptodisco.Data.DAOs.ChatmsgDao;
import com.purdue.a407.cryptodisco.Data.DAOs.ChatroomDao;
import com.purdue.a407.cryptodisco.Data.DAOs.ExchangeDao;
import com.purdue.a407.cryptodisco.Data.Entities.ChatMessageEntity;
import com.purdue.a407.cryptodisco.Data.Entities.ChatRoomEntity;
import com.purdue.a407.cryptodisco.Data.Entities.ExchangeEntity;
import com.purdue.a407.cryptodisco.Data.Entities.NotificationsEntity;
import com.purdue.a407.cryptodisco.Data.Entities.UserExchangeEntity;
import com.purdue.a407.cryptodisco.Data.Entities.WatchListEntity;

@Database(entities = {ExchangeEntity.class, UserExchangeEntity.class,
        CoinPairingEntity.class, ChatRoomEntity.class, ChatMessageEntity.class,
        CoinEntity.class, WatchListEntity.class, NotificationsEntity.class}, version = 9, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ExchangeDao exchangeDao();

    public abstract UserExchangeDao userExchangeDao();

    public abstract CoinPairingDao coinPairingDao();

    public abstract ChatroomDao chatroomDao();

    public abstract ChatmsgDao chatmsgDao();

    public abstract NotificationsDao notificationsDao();
    public abstract CoinDao coinDao();

    public abstract WatchlistDao watchlistDao();
}
