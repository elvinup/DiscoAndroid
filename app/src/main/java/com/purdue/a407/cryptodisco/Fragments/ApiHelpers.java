package com.purdue.a407.cryptodisco.Fragments;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.binance.BinanceExchange;

public class ApiHelpers {
    public static Exchange binance(String key, String secret) {
        String binance_key = "udk7YkWWkP9WU3mcbocs3kREido5JL4QwiTDxEzokwer5KpSYSUiMVWgD8PGmpAD";
        String binance_secret = "jiIcivGi7syQMlQviSHboVMl1kutOgmLJ4GhXvJBXwKdQlKe6RjMX5b7SJ3vt3dk";
        Exchange exchange = ExchangeFactory.INSTANCE.createExchange(BinanceExchange.class.getName());
        ExchangeSpecification bfxSpec = exchange.getDefaultExchangeSpecification();
        bfxSpec.setApiKey(binance_key);
        bfxSpec.setSecretKey(binance_secret);
        exchange.applySpecification(bfxSpec);
        return exchange;
    }

    public static Exchange gateio(String key, String secret) {
        ExchangeSpecification exchangeSpecification =
                new BinanceExchange().getDefaultExchangeSpecification();
        String gate_key = "BE35D93F-C891-43CD-BB2E-44DBB60351E7";
        String gate_secret = "245fb23f0558f84f8d282a90cef8d762cc2fb9aa59761b87509538492307dad9";
        exchangeSpecification.setApiKey(gate_key);
        exchangeSpecification.setSecretKey(gate_secret);
        Exchange exchange = ExchangeFactory.INSTANCE.createExchange(exchangeSpecification);
        return exchange;
    }
}
