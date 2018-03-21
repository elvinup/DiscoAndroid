package com.purdue.a407.cryptodisco.Data.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class SettingsEntity {

    @PrimaryKey(autoGenerate = true)
    int id;
    String uuid;
    Boolean arbitrage;

    public SettingsEntity(String uuid, Boolean arbitrage) {
        this.uuid = uuid;
        this.arbitrage = arbitrage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Boolean getArbitrage() {
        return arbitrage;
    }

    public void setArbitrage(Boolean arbitrage) {
        this.arbitrage = arbitrage;
    }
}
