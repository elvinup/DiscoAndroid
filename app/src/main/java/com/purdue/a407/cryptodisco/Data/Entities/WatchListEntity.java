package com.purdue.a407.cryptodisco.Data.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by KennyZheng on 3/30/18.
 */

@Entity
public class WatchListEntity {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public WatchListEntity(String user, int coin) {
        this.user = user;
        this.coin = coin;
    }

    @PrimaryKey(autoGenerate = true)
    int id;
    String user;
    int coin;
    
}
