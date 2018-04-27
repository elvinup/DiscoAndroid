package com.webcerebrium.kucoin;


import com.google.gson.JsonArray;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class KucoinOrderBookLine {
    KucoinOrderSide side;
    BigDecimal price;
    BigDecimal quantity;
    BigDecimal volume;

    public KucoinOrderBookLine(KucoinOrderSide side, JsonArray arr) {
        this.side = side;
        this.price = arr.get(0).getAsBigDecimal();
        this.quantity = arr.get(1).getAsBigDecimal();
        this.volume = arr.get(2).getAsBigDecimal();
    }

    public KucoinOrderSide getSide() {
        return side;
    }

    public void setSide(KucoinOrderSide side) {
        this.side = side;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }
}
