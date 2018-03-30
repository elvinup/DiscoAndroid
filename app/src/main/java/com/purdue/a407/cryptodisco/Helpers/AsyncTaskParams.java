package com.purdue.a407.cryptodisco.Helpers;

import org.knowm.xchange.Exchange;

public class AsyncTaskParams {
    private String coinPairing;
    private Exchange exchange;

    public AsyncTaskParams(String coinPairing, Exchange exchange) {
        this.coinPairing = coinPairing;
        this.exchange = exchange;
    }

    public String getCoinPairing() {
        return coinPairing;
    }

    public void setCoinPairing(String coinPairing) {
        this.coinPairing = coinPairing;
    }

    public Exchange getExchange() {
        return exchange;
    }

    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }
}
