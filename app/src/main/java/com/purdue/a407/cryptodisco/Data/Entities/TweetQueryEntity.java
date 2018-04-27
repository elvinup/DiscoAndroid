package com.purdue.a407.cryptodisco.Data.Entities;

public class TweetQueryEntity {
    private String query;
    private int count;

    public TweetQueryEntity(String query, int count) {
        this.query = query;
        this.count = count;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
