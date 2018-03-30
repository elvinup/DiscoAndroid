package com.purdue.a407.cryptodisco.Data.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class CoinEntity {
    @PrimaryKey(autoGenerate = true)
    int id;
    String short_name;
    String name;
    int price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }



    public CoinEntity(int id, String short_name, String name, int price) {
        this.id = id;
        this.short_name = short_name;
        this.name = name;
        this.price = price;

    }


}
