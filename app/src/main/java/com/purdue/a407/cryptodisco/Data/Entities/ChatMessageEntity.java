package com.purdue.a407.cryptodisco.Data.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by Kenny on 2/14/2018.
 */


/*
    A class for chat message
    Every message needs a identification (who sent the message) and the message
 */
@Entity
public class ChatMessageEntity {

    private String identification;

    @NonNull
    @PrimaryKey
    private String message;

    public ChatMessageEntity(String identification, String message) {
        this.identification = identification;
        this.message = message;
    }

    // getter
    public String getIdentification() {
        return identification;
    }

    public String getMessage() {
        return message;
    }

    // setter
    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
