package com.purdue.a407.cryptodisco.Testing;

import java.util.ArrayList;

public class exchangeVolume {

    String exchange;
    double volume;

    ArrayList<exchangeVolume> exchanges = new ArrayList<exchangeVolume>();

    @Override
    public String toString() {
        return "Exchange: " + this.exchange + '\n'
                + "Volume: " + this.volume + '\n';
    }
}
