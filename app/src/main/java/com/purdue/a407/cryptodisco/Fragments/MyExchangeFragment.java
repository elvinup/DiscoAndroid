package com.purdue.a407.cryptodisco.Fragments;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.purdue.a407.cryptodisco.App;
import com.purdue.a407.cryptodisco.Data.AppDatabase;
import com.purdue.a407.cryptodisco.Data.Entities.CoinPairingEntity;
import com.purdue.a407.cryptodisco.Helpers.DateAxisValueFormatter;
import com.purdue.a407.cryptodisco.R;
import com.purdue.a407.cryptodisco.Repos.CoinPairingRepository;
import com.purdue.a407.cryptodisco.ViewModels.ExchangeViewModel;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.binance.service.BinanceMarketDataService;
import org.knowm.xchange.binance.service.BinanceTradeService;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.dto.marketdata.Trades;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.dto.trade.UserTrade;
import org.knowm.xchange.dto.trade.UserTrades;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.service.trade.params.TradeHistoryParamsAll;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyExchangeFragment extends TabbedFragment {

    @BindView(R.id.title)
    TextView title;

    private String exchange = "N/A";

    public static final int RES_DETAILS = 10000;

    @BindView(R.id.searchText)
    AppCompatAutoCompleteTextView searchText;



    @Inject
    AppDatabase appDatabase;

    @Inject
    CoinPairingRepository coinPairingRepository;

    ExchangeViewModel viewModel;

    ArrayAdapter<Object> arrayAdapter;
    Context context;

    String currentPairing = "";

    GraphFragment graphFragment;
    TradeHistoryFragment tradeHistoryFragment;
    OpenOrdersFragment openOrdersFragment;


    int currentlySelectedFragment = 0;


    public MyExchangeFragment() {
        // Required empty public constructor
    }

    public static MyExchangeFragment newInstance(String exchange) {
        MyExchangeFragment myExchangeFragment = new MyExchangeFragment();
        myExchangeFragment.setExchange(exchange);
        return myExchangeFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container,
                        Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_exchange,
                container, false);
        ButterKnife.bind(this, view);
        ((App) getActivity().getApplication()).getNetComponent().inject(this);
        setUpViewModel();
        graphFragment = new GraphFragment();
        graphFragment.callback(this);
        tradeHistoryFragment = new TradeHistoryFragment();
        tradeHistoryFragment.callback(this);
        openOrdersFragment = new OpenOrdersFragment();
        openOrdersFragment.callback(this);
        return view;
    }

    @Override
    public Fragment[] getFragments() {
        return new Fragment[]{graphFragment,
                tradeHistoryFragment, openOrdersFragment};
    }

    @Override
    public String[] getTitles() {
        return new String[]{"Graphs", "Trade History", "Open Orders"};
    }

    @Override
    public void onSelected(int position) {
        if(currentPairing.equals("")) {
            return;
        }
        String[] coins = currentPairing.split("->");
        String coin = coins[0].trim();
        String mark = coins[1].trim();
        Exchange exchange;
        if(this.exchange.equals("binance")) {
            exchange = ApiHelpers.binance(getActivity(),"","");
        } else if (this.exchange.equals("gateio")) {
            exchange = ApiHelpers.gateio(getActivity(),"","");
        } else if (this.exchange.equals("kraken")) {
            exchange = ApiHelpers.kraken(getActivity(),"","");
        }  else {
            exchange = ApiHelpers.hitbtc(getActivity(),"","");
        }

        if(position == 0) {
            currentlySelectedFragment = 0;
            graphFragment.setCoinPairing(coin + "/" + mark, exchange);
        }
        else if(position == 1) {
            currentlySelectedFragment = 1;
            tradeHistoryFragment.setCoinPairing(coin + "/" + mark, exchange);
        }
        else if(position == 2) {
            currentlySelectedFragment = 2;
            openOrdersFragment.setCoinPairing(coin + "/" + mark, exchange);
        }
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public void setCoinPairing() {
        currentPairing = searchText.getText().toString();
        String[] coins = currentPairing.split("->");
        String coin = coins[0].trim();
        String mark = coins[1].trim();
        float price = appDatabase.coinPairingDao().getPrice(coin, mark);
        String finalText = String.format("%s: %f", currentPairing, price);
        TextView coinPairingText;
        if(getView().findViewById(R.id.coinPairingTextValue) != null) {
            coinPairingText = getView().findViewById(R.id.coinPairingTextValue);
        }
        else {
            coinPairingText = new TextView(getActivity());
            coinPairingText.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            coinPairingText.setTextSize(14f);
            coinPairingText.setId(R.id.coinPairingTextValue);
            coinPairingText.setText(finalText);

            LinearLayout layout = getView().findViewById(R.id.masterLayout);
            layout.addView(coinPairingText, 2);
        }
        coinPairingText.setText(finalText);

        Exchange exchangeApi = ApiHelpers.binance(getActivity(), "", "");
        if (exchange.equals("binance")) {

            exchangeApi = ApiHelpers.binance(getActivity(), "", "");
        }
        else if (exchange.equals("gateio")) {
            exchangeApi = ApiHelpers.gateio(getContext(),"", "");
        }
        else if (exchange.equals("kucoin")) {
            exchangeApi = ApiHelpers.kucoin(getContext(),"", "");
        }

        if(currentlySelectedFragment == 0) {
            graphFragment.setCoinPairing(coin + "/" + mark, exchangeApi);
        }
        else if(currentlySelectedFragment == 1) {
            tradeHistoryFragment.setCoinPairing(coin + "/" + mark, exchangeApi);
        }
        else if(currentlySelectedFragment == 2) {
            openOrdersFragment.setCoinPairing(coin + "/" + mark, exchangeApi);
        }



    }

    public void setUpViewModel() {
        title.setText(exchange);
        coinPairingRepository.setExchange(exchange);
        viewModel = new ExchangeViewModel(coinPairingRepository);
        context = getActivity();
        searchText.setOnItemClickListener((adapterView, view12, i, l) -> {
            View view1 = getActivity().getCurrentFocus();
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            searchText.clearFocus();
            setCoinPairing();
        });

        viewModel.getCoinPairings().observe(getActivity(), listCDResource -> {
            if(listCDResource.isLoading()) {
                return;
            }
            List<CoinPairingEntity> coinPairingEntities = listCDResource.getData();
            ArrayList<String> strings = new ArrayList<>();
            for(CoinPairingEntity entity: coinPairingEntities) {
                strings.add(entity.getCoin_short() + " -> " + entity.getMarket_short());
            }
            arrayAdapter = new ArrayAdapter<>(context,
                    android.R.layout.simple_dropdown_item_1line,strings.toArray());
            searchText.setAdapter(arrayAdapter);
        });

    }


    @OnClick(R.id.createOrder)
    public void onOrder() {
        OrderDialog dialog = new OrderDialog();
        String[] coins = currentPairing.split("->");
        if(coins.length != 2) {
            Toast.makeText(getActivity(), "Please choose a coin pairing", Toast.LENGTH_SHORT).show();
            return;
        }
        String coin = coins[0].trim();
        String mark = coins[1].trim();
        ExchType type;
        if(exchange.equals("binance"))
            type = ExchType.BINANCE;
        else if(exchange.equals("gateio"))
            type = ExchType.GATEIO;
        else if(exchange.equals("kucoin"))
            type = ExchType.KUCOIN;
        else
            type = ExchType.BINANCE;
        dialog.setCurrencyPair(coin + "/" + mark, type);
        getActivity().getSupportFragmentManager().beginTransaction().
                replace(R.id.replaceView, dialog).addToBackStack("order_dialog").commit();
    }



}
