package com.purdue.a407.cryptodisco.Repos;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.purdue.a407.cryptodisco.Api.CDApi;
import com.purdue.a407.cryptodisco.CacheData.CDResource;
import com.purdue.a407.cryptodisco.CacheData.NetworkBoundResource;
import com.purdue.a407.cryptodisco.Data.DAOs.ChatmsgDao;
import com.purdue.a407.cryptodisco.Data.Entities.ChatMessageEntity;

import java.util.List;

import retrofit2.Call;

public class ChatMsgRepository {

    CDApi cdApi;
    ChatmsgDao chatmsgDao;


    public ChatMsgRepository(CDApi cdApi, ChatmsgDao chatmsgDao) {
        this.cdApi = cdApi;
        this.chatmsgDao = chatmsgDao;
    }

    @NonNull
    public LiveData<CDResource<List<ChatMessageEntity>>> chatMessages() {
        return new NetworkBoundResource<List<ChatMessageEntity>, List<ChatMessageEntity>>() {

            @Override
            protected void saveCallResult(@NonNull List<ChatMessageEntity> items) {
                chatmsgDao.saveAll(items);
            }

            @NonNull
            @Override
            protected LiveData<List<ChatMessageEntity>> loadFromDb() {
                return chatmsgDao.chatMessages();
            }

            @NonNull
            @Override
            protected Call<List<ChatMessageEntity>> createCall() {
                return cdApi.getChatMessages();
            }
        }.getAsLiveData();
    }

}
