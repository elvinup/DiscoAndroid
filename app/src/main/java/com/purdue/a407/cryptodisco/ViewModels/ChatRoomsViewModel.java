package com.purdue.a407.cryptodisco.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.purdue.a407.cryptodisco.CacheData.CDResource;
import com.purdue.a407.cryptodisco.Data.Entities.ChatRoomEntity;
import com.purdue.a407.cryptodisco.Repos.ChatRoomRepository;

import java.util.List;

import javax.inject.Inject;

public class ChatRoomsViewModel extends ViewModel{
    @NonNull
    private final ChatRoomRepository repository;


    private final LiveData<CDResource<List<ChatRoomEntity>>> chatRooms;

    @Inject
    public ChatRoomsViewModel(@NonNull ChatRoomRepository repository) {
        this.repository = repository;
        chatRooms = repository.chatRooms();
    }

    @MainThread
    @NonNull
    public LiveData<CDResource<List<ChatRoomEntity>>> getChatroomsList() {
        return chatRooms;
    }
}
