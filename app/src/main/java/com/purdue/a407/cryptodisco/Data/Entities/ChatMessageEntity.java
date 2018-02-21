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
    int id;

    String message;
    String nickname;
    String UID;
    int chatroom_id;

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getUID() {
        return UID;
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

    public void setUID(String UID) {
        this.UID = UID;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setChatroom_id(int chatroom_id) {
        this.chatroom_id = chatroom_id;
    }



    public ChatMessageEntity(String message, String UID, String nickname, int chatroom_id) {
        this.message = message;
        this.UID = UID;
        this.nickname = nickname;
        this.chatroom_id = chatroom_id;
    }
}
