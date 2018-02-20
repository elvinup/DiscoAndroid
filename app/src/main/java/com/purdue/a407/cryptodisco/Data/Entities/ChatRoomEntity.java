package com.purdue.a407.cryptodisco.Data.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class ChatRoomEntity {


    @NonNull
    @PrimaryKey
    private String name;

    private String description;


    public ChatRoomEntity(String identification, String name) {
        this.description = identification;
        this.name = name;
    }

    public String getdescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setdescription(String description) {
        this.description = description;
    }

    public void setname(String name) {
        this.name = name;
    }
}
