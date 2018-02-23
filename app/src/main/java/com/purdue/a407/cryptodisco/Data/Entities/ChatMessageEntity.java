package com.purdue.a407.cryptodisco.Data.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
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
    int id;

    String message;
    String nickname;
    String uid;
    int chatroom_id;

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getUid() {
        return uid;
    }

    public String getNickname() {
        return nickname;
    }

    public int getChatroom_id() { return chatroom_id; }

    public void setId(int id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setChatroom_id(int chatroom_id) {
        this.chatroom_id = chatroom_id;
    }


    public ChatMessageEntity(String message, String nickname) {
        this.message = message;
        this.nickname = nickname;
    }

    @Ignore // might need to delete that
    public ChatMessageEntity(String message, String uid, String nickname, int chatroom_id) {
        this.message = message;
        this.uid = uid;
        this.nickname = nickname;
        this.chatroom_id = chatroom_id;
    }

}
