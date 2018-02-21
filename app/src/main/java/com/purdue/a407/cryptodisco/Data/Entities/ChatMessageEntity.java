package com.purdue.a407.cryptodisco.Data.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Kenny on 2/14/2018.
 */


/*
    A class for chat message
    Every message needs a id (who sent the message) and the message
 */
@Entity
public class ChatMessageEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;


    private String message;
    private String UID;
    private String nickname;
    private int chatroomId;

    public ChatMessageEntity(String message, String uid, String nickname, int chatroomId) {
        this.message = message;
        UID = uid;
        this.nickname = nickname;
        this.chatroomId = chatroomId;
    }
}
