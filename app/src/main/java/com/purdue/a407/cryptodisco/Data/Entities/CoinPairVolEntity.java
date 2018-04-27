package com.purdue.a407.cryptodisco.Data.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

public class CoinPairVolEntity {

    @NonNull
    String first;

    @NonNull
    String second;

    @NonNull
    String volume;

    CoinPairVolEntity(String first, String second, String volume) {
        this.first = first;
        this.second = second;
        this.volume = volume;
    }

    @NonNull
    public String getFirst() {
        return first;
    }

    @NonNull
    public String getSecond() {
        return second;
    }

    @NonNull
    public String getVolume() {
        return volume;
    }
}
