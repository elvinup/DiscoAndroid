package com.purdue.a407.cryptodisco.DependencyInjection.Modules;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.purdue.a407.cryptodisco.Api.CDApi;
import com.purdue.a407.cryptodisco.Data.AppDatabase;
import com.purdue.a407.cryptodisco.Data.DAOs.ChatroomDao;
import com.purdue.a407.cryptodisco.Data.DAOs.ExchangeDao;
import com.purdue.a407.cryptodisco.Data.Entities.ChatRoomEntity;
import com.purdue.a407.cryptodisco.Repos.ChatRoomRepository;
import com.purdue.a407.cryptodisco.Repos.ExchangeRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ViewModelModule.class})
public class DBModule {

    @Provides
    @Singleton
    public AppDatabase provideDb(Application app) {
        return Room.databaseBuilder(app, AppDatabase.class,"app-database").fallbackToDestructiveMigration().build();
    }

    @Provides
    @Singleton
    public ExchangeDao provideExchangeDao(AppDatabase db) {
        return db.exchangeDao();
    }

    @Provides
    @Singleton
    public ChatroomDao provideChatroomDao(AppDatabase db) { return db.chatroomDao(); }

    @Provides
    @Singleton
    public ExchangeRepository provideExchangeRepository(CDApi cdApi, ExchangeDao exchangeDao) {
        return new ExchangeRepository(cdApi, exchangeDao);
    }

    @Provides
    @Singleton
    public ChatRoomRepository provideChatroomRepository(CDApi cdApi, ChatroomDao chatroomDao) {
        return new ChatRoomRepository(cdApi, chatroomDao);
    }

}
