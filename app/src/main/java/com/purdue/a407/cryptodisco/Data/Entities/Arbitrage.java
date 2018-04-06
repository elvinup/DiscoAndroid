package com.purdue.a407.cryptodisco.Data.Entities;

public class Arbitrage {
    CoinPairingEntity first;
    CoinPairingEntity second;

    public Arbitrage(){}

    public Arbitrage(CoinPairingEntity first, CoinPairingEntity second) {
        this.first = first;
        this.second = second;
    }

    public CoinPairingEntity getFirst() {
        return first;
    }

    public void setFirst(CoinPairingEntity first) {
        this.first = first;
    }

    public CoinPairingEntity getSecond() {
        return second;
    }

    public void setSecond(CoinPairingEntity second) {
        this.second = second;
    }
}
