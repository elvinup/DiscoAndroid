package com.purdue.a407.cryptodisco.Fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.purdue.a407.cryptodisco.R;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.binance.BinanceExchange;
import org.knowm.xchange.binance.dto.trade.OrderSide;
import org.knowm.xchange.binance.dto.trade.OrderType;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.meta.CurrencyPairMetaData;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.dto.trade.StopOrder;
import org.knowm.xchange.gateio.GateioExchange;
import org.knowm.xchange.utils.jackson.CurrencyPairDeserializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderDialog extends Fragment {

    Schema schema;
    Order.OrderType orderType;
    CurrencyPair currencyPair;
    ExchType exchType;
    String error;
    Exchange exchange;

    @BindView(R.id.stopLimitPrice)
    EditText stopLimit;

    @BindView(R.id.originalAmount)
    EditText amountToTrade;

    @BindView(R.id.remainingPrice)
    EditText remaining;

    @OnClick(R.id.create)
    public void onCreate() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
//                if(exchType == ExchType.BINANCE)
//                    exchange = ApiHelpers.binance("","");
//                else if(exchType == ExchType.GATEIO)
//                    exchange = ApiHelpers.gateio("","");
//                else
//                    exchange = ApiHelpers.binance("","");
                exchange = ApiHelpers.binance(getContext(),"","");
                String s = getResult();
                return s;
            }
            @Override
            protected void onPostExecute(String s) {
                if(s == null) {
                    Toast.makeText(getActivity(), "There was an error", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
            }
        }.execute();

    }

    public OrderDialog() {}

    public void setCurrencyPair(String currencyString, ExchType exchType) {
        currencyPair = new CurrencyPair(currencyString);
        this.exchType = exchType;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater,
                                 ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.dialog_orders, null);
        ButterKnife.bind(this, view);
        return view;

    }

    public String getResult() {
        if(schema == null) {
            return null;
        }
        if(orderType == null) {
            return null;
        }
        if(amountToTrade.getText().toString().isEmpty()) {
            return null;
        }
        if(remaining.getText().toString().isEmpty()) {
            return null;
        }
        if(schema == Schema.MARKET) {
            MarketOrder.Builder marketBuilder = new MarketOrder.Builder(orderType, currencyPair);
            marketBuilder.originalAmount(new BigDecimal(Double.
                    parseDouble(amountToTrade.getText().toString())));
            marketBuilder.remainingAmount(new BigDecimal(Double.
                    parseDouble(remaining.getText().toString())));
            marketBuilder.timestamp(new Date());
            MarketOrder marketOrder = marketBuilder.build();
            try {
                return exchange.getTradeService().placeMarketOrder(marketOrder);
            } catch (Exception e) {
                return e.getLocalizedMessage();
            }
        }
        else if(schema == Schema.LIMIT) {
            if(stopLimit.getText().toString().isEmpty()) {
                return null;
            }
            LimitOrder.Builder limitBuilder = new LimitOrder.Builder(orderType, currencyPair);
            limitBuilder.limitPrice(new BigDecimal(Double.
                    parseDouble(stopLimit.getText().toString())));
            limitBuilder.originalAmount(new BigDecimal(Double.
                    parseDouble(amountToTrade.getText().toString())));
            limitBuilder.remainingAmount(new BigDecimal(Double.
                    parseDouble(remaining.getText().toString())));
            limitBuilder.timestamp(new Date());
            limitBuilder.id("Test");
            LimitOrder limitOrder = limitBuilder.build();
            try {
                return exchange.getTradeService().placeLimitOrder(limitOrder);
            } catch (Exception e) {
                return e.getLocalizedMessage();
            }
        }
        else if(schema == Schema.STOP) {
            if(stopLimit.getText().toString().isEmpty()) {
                return null;
            }
            StopOrder.Builder stopBuilder = new StopOrder.Builder(orderType, currencyPair);
            stopBuilder.stopPrice(new BigDecimal(Double.
                    parseDouble(stopLimit.getText().toString())));
            stopBuilder.originalAmount(new BigDecimal(Double.
                    parseDouble(amountToTrade.getText().toString())));
            stopBuilder.remainingAmount(new BigDecimal(Double.
                    parseDouble(remaining.getText().toString())));
            StopOrder stopOrder = stopBuilder.build();
            try {
                return exchange.getTradeService().placeStopOrder(stopOrder);
            } catch (Exception e) {
                return e.getLocalizedMessage();
            }
        }
        return null;

    }

     @OnClick({R.id.marketOrderBtn, R.id.limitOrderBtn, R.id.stopOrderBtn})
    public void onSchemaTypeClicked(RadioButton radioButton) {
        // Is the button now checked?
        boolean checked = radioButton.isChecked();
        // Check which radio button was clicked
        switch (radioButton.getId()) {
            case R.id.marketOrderBtn:
                if (checked) {
                    schema = Schema.MARKET;
                }
                break;
            case R.id.limitOrderBtn:
                if (checked) {
                    schema = Schema.LIMIT;
                }
                break;
            case R.id.stopOrderBtn:
                if (checked) {
                    schema = Schema.STOP;
                }
                break;
        }
    }

    @OnClick({R.id.buyBtn, R.id.sellBtn})
    public void onOrderTypeClicked(RadioButton radioButton) {
        // Is the button now checked?
        boolean checked = radioButton.isChecked();
        // Check which radio button was clicked
        switch (radioButton.getId()) {
            case R.id.buyBtn:
                if (checked) {
                    orderType = Order.OrderType.BID;
                }
                break;
            case R.id.sellBtn:
                if (checked) {
                    orderType = Order.OrderType.ASK;
                }
                break;
        }
    }
}
