package com.purdue.a407.cryptodisco.Repos;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.purdue.a407.cryptodisco.Api.CDApi;
import com.purdue.a407.cryptodisco.CacheData.CDResource;
import com.purdue.a407.cryptodisco.CacheData.NetworkBoundResource;
import com.purdue.a407.cryptodisco.Data.DAOs.ChatroomDao;
import com.purdue.a407.cryptodisco.Data.DAOs.ExchangeDao;
import com.purdue.a407.cryptodisco.Data.Entities.ChatRoomEntity;

import java.util.List;

import retrofit2.Call;

public class ChatRoomRepository {

    CDApi cdApi;
    ChatroomDao chatroomDao;


    public ChatRoomRepository(CDApi cdApi, ChatroomDao chatroomDao) {
        this.cdApi = cdApi;
        this.chatroomDao = chatroomDao;
    }

    @NonNull
    public LiveData<CDResource<List<ChatRoomEntity>>> chatRooms() {
        return new NetworkBoundResource<List<ChatRoomEntity>, List<ChatRoomEntity>>() {

            @Override
            protected void saveCallResult(@NonNull List<ChatRoomEntity> items) {
                chatroomDao.clear();
                chatroomDao.saveAll(items);
            }

            @NonNull
            @Override
            protected LiveData<List<ChatRoomEntity>> loadFromDb() {
                return chatroomDao.chatRooms();
            }

            @NonNull
            @Override
            protected Call<List<ChatRoomEntity>> createCall() {
                return cdApi.getChatRooms();
            }
        }.getAsLiveData();
    }

}
