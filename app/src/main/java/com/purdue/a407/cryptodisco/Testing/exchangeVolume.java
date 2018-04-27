package com.purdue.a407.cryptodisco.Testing;

import com.purdue.a407.cryptodisco.Data.Entities.ExchangeEntity;

import java.util.ArrayList;

public class exchangeVolume {

    //String exchange;
    //double volume;

    public ArrayList<ExchangeEntity> retExchangeList = new ArrayList<>();
    public ArrayList<String> prices = new ArrayList<>();

    ArrayList<exchangeVolume> exchanges = new ArrayList<exchangeVolume>();

    @Override
    public String toString() {
        return "Exchange: " + this.retExchangeList + '\n'
                + "Volume: " + this.prices + '\n';
    }
}
