package com.purdue.a407.cryptodisco.Data.Entities;

public class ChatJoin {
    String uuid;
    String chatroom_id;

    public ChatJoin(String uuid, String chatroom_id) {
        this.uuid = uuid;
        this.chatroom_id = chatroom_id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getChatroom_id() {
        return chatroom_id;
    }

    public void setChatroom_id(String chatroom_id) {
        this.chatroom_id = chatroom_id;
    }
}
