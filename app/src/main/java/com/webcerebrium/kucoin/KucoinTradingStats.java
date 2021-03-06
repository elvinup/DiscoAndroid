package com.webcerebrium.kucoin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class KucoinTradingStats {

    List<KucoinTradingSymbol> listTradingSymbols = new LinkedList<>();

    public KucoinTradingStats(JsonObject response) throws KucoinApiException {
        if (!response.has("data")) {
            throw new KucoinApiException("Missing data in response object while trying to read trading stats");
        }
        JsonArray data = response.get("data").getAsJsonArray();
        listTradingSymbols.clear();
        for (JsonElement element: data) {
            KucoinTradingSymbol symbol = new KucoinTradingSymbol(element.getAsJsonObject());
            listTradingSymbols.add(symbol);
        }
    }

    public List<KucoinTradingSymbol> getMarketsOf(String coin) {
        List<KucoinTradingSymbol> result = new LinkedList<>();
        for (int i = 0; i < listTradingSymbols.size(); i++ ) {
            KucoinTradingSymbol tradingSymbol = listTradingSymbols.get(i);
            if (!tradingSymbol.isTrading()) continue;
            if (tradingSymbol.getCoinType().equals(coin) || tradingSymbol.getCoinTypePair().equals(coin)) {
                result.add(tradingSymbol);
            }
        }
        return result;
    }

    public Set<KucoinSymbol> getSymbolsOf(String coin) throws KucoinApiException {
        List<KucoinTradingSymbol> coins = getMarketsOf(coin);
        Set<KucoinSymbol> result = new TreeSet<>();
        for (KucoinTradingSymbol sym: coins) {
            result.add(sym.getSymbol());
        }
        return result;
    }

    public Set<String> getCoinsOf(String coin) throws KucoinApiException {
        List<KucoinTradingSymbol> coins = getMarketsOf(coin);
        Set<String> result = new TreeSet<>();
        for (KucoinTradingSymbol sym: coins) {
            result.add(sym.getSymbol().getOpposite(coin));
        }
        return result;
    }
}
