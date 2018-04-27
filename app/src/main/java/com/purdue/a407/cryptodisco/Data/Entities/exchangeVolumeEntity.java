package com.purdue.a407.cryptodisco.Data.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import javax.annotation.Nonnull;

@Entity
public class exchangeVolumeEntity {

    @Nonnull
    @PrimaryKey
    public int index;
    public String exchange;
    public String price;

    public exchangeVolumeEntity(@Nonnull int index, String exchange, String price) {
        this.index = index;
        this.exchange = exchange;
        this.price = price;
    }

    @Nonnull
    public int getIndex() {
        return index;
    }

    public void setIndex(@Nonnull int index) {
        this.index = index;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
