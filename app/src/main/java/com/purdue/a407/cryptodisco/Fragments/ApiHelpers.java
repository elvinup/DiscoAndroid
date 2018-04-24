package com.purdue.a407.cryptodisco.Fragments;

import android.content.Context;

import com.purdue.a407.cryptodisco.R;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.binance.BinanceExchange;
import org.knowm.xchange.gateio.GateioExchange;
import org.knowm.xchange.kucoin.KucoinExchange;

public class ApiHelpers {
    public static Exchange binance(Context context, String key, String secret) {
        String binance_key = context.getResources().getString(R.string.binance_test_key);
        String binance_secret = context.getResources().getString(R.string.binance_test_secret);
        Exchange exchange = ExchangeFactory.INSTANCE.createExchange(BinanceExchange.class.getName());
        ExchangeSpecification bfxSpec = exchange.getDefaultExchangeSpecification();
        bfxSpec.setApiKey(binance_key);
        bfxSpec.setSecretKey(binance_secret);
        exchange.applySpecification(bfxSpec);
        return exchange;
    }

    public static Exchange gateio(String key, String secret) {
        ExchangeSpecification exchangeSpecification =
                new GateioExchange().getDefaultExchangeSpecification();
        String gate_key = "7AB68A07-0B28-4F80-A731-CB4845BB769C";
        String gate_secret = "404b70d164729a1b8108675137c244fea67f5a145c068eb01183ca815268658e";
        exchangeSpecification.setApiKey(gate_key);
        exchangeSpecification.setSecretKey(gate_secret);
        Exchange exchange = ExchangeFactory.INSTANCE.createExchange(exchangeSpecification);
        return exchange;
    }

    public static Exchange kucoin(String key, String secret) {
        ExchangeSpecification exchangeSpecification =
                new KucoinExchange().getDefaultExchangeSpecification();
        String kucoin_key = "5adf878603d644558b709565";
        String kucoin_secret = "5f4e56ec-5ea8-40cf-af9d-42f43fbfcafe";
        exchangeSpecification.setApiKey(kucoin_key);
        exchangeSpecification.setSecretKey(kucoin_secret);
        Exchange exchange = ExchangeFactory.INSTANCE.createExchange(exchangeSpecification);
        return exchange;
    }





}
