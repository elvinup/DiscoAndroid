package com.purdue.a407.cryptodisco.Data.Entities;

public class TrailStop {
    String uuid;
    String size;
    String side;
    String coinShort;
    String marketShort;
    String exchange;
    String trail;

    public TrailStop(String uuid,
                     String size,
                     String side,
                     String coinShort,
                     String marketShort,
                     String exchange,
                     String trail) {
        this.uuid = uuid;
        this.size = size;
        this.side = side;
        this.coinShort = coinShort;
        this.marketShort = marketShort;
        this.exchange = exchange;
        this.trail = trail;
    }
}
