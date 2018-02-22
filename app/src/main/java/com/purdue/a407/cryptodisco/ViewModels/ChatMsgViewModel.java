package com.purdue.a407.cryptodisco.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.purdue.a407.cryptodisco.CacheData.CDResource;
import com.purdue.a407.cryptodisco.Data.Entities.ChatMessageEntity;
import com.purdue.a407.cryptodisco.Data.Entities.ChatMessageEntity;
import com.purdue.a407.cryptodisco.Repos.ChatMsgRepository;
import com.purdue.a407.cryptodisco.Repos.ChatRoomRepository;

import java.util.List;

import javax.inject.Inject;

public class ChatMsgViewModel extends ViewModel{
    @NonNull
    private final ChatMsgRepository repository;


    private final LiveData<CDResource<List<ChatMessageEntity>>> chatMessages;

    @Inject
    public ChatMsgViewModel(@NonNull ChatMsgRepository repository) {
        this.repository = repository;
        chatMessages = repository.chatMessages();
    }

    @MainThread
    @NonNull
    public LiveData<CDResource<List<ChatMessageEntity>>> getChatmessagesList() {
        return chatMessages;
    }
}
