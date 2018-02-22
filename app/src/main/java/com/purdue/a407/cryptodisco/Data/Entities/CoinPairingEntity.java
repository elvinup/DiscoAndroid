package com.purdue.a407.cryptodisco.Data.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(primaryKeys = {"coin_short", "market_short", "exchange"})
public class CoinPairingEntity {

    @NonNull
    String coin_short;

    @NonNull
    String market_short;

    @NonNull
    String exchange;

    float price;

    public CoinPairingEntity(String coin_short, String market_short, String exchange, float price) {
        this.coin_short = coin_short;
        this.market_short = market_short;
        this.exchange = exchange;
        this.price = price;
    }

    public String getCoin_short() {
        return coin_short;
    }

    public void setCoin_short(String coin_short) {
        this.coin_short = coin_short;
    }

    public String getMarket_short() {
        return market_short;
    }

    public void setMarket_short(String market_short) {
        this.market_short = market_short;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
