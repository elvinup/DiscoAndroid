package com.purdue.a407.cryptodisco.Fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.purdue.a407.cryptodisco.App;
import com.purdue.a407.cryptodisco.Data.Entities.TrailStop;
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
import com.purdue.a407.cryptodisco.Helpers.DeviceID;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.purdue.a407.cryptodisco.Api.CDApi;


import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDialog extends Fragment {

    Schema schema;
    Order.OrderType orderType;
    CurrencyPair currencyPair;
    ExchType exchType;
    String error;
    Exchange exchange;

    @Inject
    CDApi api;

    @Inject
    DeviceID deviceID;

    @BindView(R.id.stopLimitPrice)
    EditText stopLimit;

    @BindView(R.id.originalAmount)
    EditText amountToTrade;

    @OnClick(R.id.create)
    public void onCreateOrder() {
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
        View view = layoutInflater.inflate(R.layout.dialog_orders, viewGroup, false);
        ButterKnife.bind(this, view);
        ((App) getActivity().getApplication()).getNetComponent().inject(this);
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
        if(schema == Schema.MARKET) {

            MarketOrder.Builder marketBuilder = new MarketOrder.Builder(orderType, currencyPair);
            marketBuilder.originalAmount(new BigDecimal(Double.
                    parseDouble(amountToTrade.getText().toString())));
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
            Toast.makeText(getActivity(), "In schema", Toast.LENGTH_SHORT).show();
            if(stopLimit.getText().toString().isEmpty()) {
                return null;
            }
            StopOrder.Builder stopBuilder = new StopOrder.Builder(orderType, currencyPair);
            stopBuilder.stopPrice(new BigDecimal(Double.
                    parseDouble(stopLimit.getText().toString())));
            stopBuilder.originalAmount(new BigDecimal(Double.
                    parseDouble(amountToTrade.getText().toString())));
            StopOrder stopOrder = stopBuilder.build();
            try {
                return exchange.getTradeService().placeStopOrder(stopOrder);
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
            LimitOrder limitOrder = limitBuilder.build();

            try{
                return exchange.getTradeService().placeLimitOrder(limitOrder);
            } catch (Exception e) {
                return e.getLocalizedMessage();
            }
        }
        else if (schema == Schema.TRAIL) {
            Looper.prepare();
            if(stopLimit.getText().toString().isEmpty()) {
                return null;
            }

            TrailStop trailStop = new TrailStop(deviceID.getDeviceID(),
                                                amountToTrade.getText().toString(),
                                                String.valueOf(((orderType == Order.OrderType.BID) ? 1 : 0)),
                                                 currencyPair.base.getCurrencyCode(),
                                                 currencyPair.counter.getCurrencyCode(),
                                                 getExchangeName(exchange.getTradeService().toString()),
                                                 stopLimit.getText().toString());
            api.trailstop(trailStop).enqueue(new Callback<Void>() {

                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() != 200) {
                        // Error
                        Toast.makeText(getActivity(), "Bad response code", Toast.LENGTH_SHORT).show();
                        Log.d("TrailStop", String.valueOf(response.errorBody().toString()));
                    } else {
                        // Success
                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                        Log.d("TrailStop", "Success");

                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getActivity(), "More annoying failure", Toast.LENGTH_SHORT).show();
                    Log.d("TrailStop", "Failure");
                }
                });
                return "Success";
        }
        return null;

    }

    @OnClick({R.id.marketOrderBtn, R.id.limitOrderBtn, R.id.stopOrderBtn, R.id.trailstopOrderBtn})
    public void onSchemaTypeClicked(RadioButton radioButton) {
        // Is the button now checked?
        boolean checked = radioButton.isChecked();
        // Check which radio button was clicked
        switch (radioButton.getId()) {
            case R.id.marketOrderBtn:
                if (checked) {
                    stopLimit.setVisibility(View.GONE);
                    schema = Schema.MARKET;
                }
                break;
            case R.id.limitOrderBtn:
                if (checked) {
                    stopLimit.setVisibility(View.VISIBLE);
                    stopLimit.setHint("Limit Price");
                    schema = Schema.LIMIT;
                }
                break;
            case R.id.stopOrderBtn:
                if (checked) {
                    stopLimit.setVisibility(View.VISIBLE);
                    stopLimit.setHint("Stop Price");
                    schema = Schema.STOP;
                }
                break;
            case R.id.trailstopOrderBtn:
                if (checked) {
                    stopLimit.setVisibility(View.VISIBLE);
                    stopLimit.setHint("Trail Amount");
                    schema = Schema.TRAIL;
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

    public String getExchangeName(String exchangePackageName) {
        if(exchangePackageName.contains("binance")) {
            return "Binance";
        } else if(exchangePackageName.contains("gateio")) {
            return "GateIO";
        } else if(exchangePackageName.contains("kraken")) {
            return "Kraken";
        } else {
            return "";
        }
    }

}
