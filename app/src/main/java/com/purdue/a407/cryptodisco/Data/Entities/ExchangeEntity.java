package com.purdue.a407.cryptodisco.Data.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class ExchangeEntity {

    @NonNull
    @PrimaryKey
    String name;

    String url;

    public ExchangeEntity(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
