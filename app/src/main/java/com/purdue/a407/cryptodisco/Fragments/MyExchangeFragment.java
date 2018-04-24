package com.purdue.a407.cryptodisco.Fragments;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.data.LineDataSet;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.kyleohanian.databinding.modelbindingforms.UIObjects.ModelForm;
import com.purdue.a407.cryptodisco.App;
import com.purdue.a407.cryptodisco.Data.AppDatabase;
import com.purdue.a407.cryptodisco.Data.Entities.CoinPairingEntity;
import com.purdue.a407.cryptodisco.Data.Entities.UserExchangeEntity;
import com.purdue.a407.cryptodisco.R;
import com.purdue.a407.cryptodisco.Repos.CoinPairingRepository;
import com.purdue.a407.cryptodisco.ViewModels.ExchangeViewModel;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.ExchangeSpecification.*;
import org.knowm.xchange.binance.BinanceExchange;
import org.knowm.xchange.binance.service.BinanceTradeHistoryParams;
import org.knowm.xchange.binance.service.BinanceTradeService;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.dto.trade.StopOrder;
import org.knowm.xchange.dto.trade.UserTrade;
import org.knowm.xchange.dto.trade.UserTrades;
import org.knowm.xchange.gateio.GateioExchange;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.service.trade.TradeService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyExchangeFragment extends Fragment {

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.sellOrder)
    Button btnSell;

    private String exchange = "N/A";

    @BindView(R.id.searchText)
    AppCompatAutoCompleteTextView searchText;

    @BindView(R.id.coinPairing)
    TextView coinPairing;

    @BindColor(R.color.lineColor1)
    int lineColor1;

    @BindColor(R.color.lineColorOpaque1)
    int lineColorOpaque1;

    @BindColor(R.color.lineColor2)
    int lineColor2;

    @BindColor(R.color.lineColorOpaque2)
    int lineColorOpaque2;

    @Inject
    AppDatabase appDatabase;

    @Inject
    CoinPairingRepository coinPairingRepository;

    ExchangeViewModel viewModel;

    ArrayAdapter<Object> arrayAdapter;
    Context context;

    String currentPairing = "";
    Exchange exchangeApiHelper;

    GraphView graphView;

    LineGraphSeries<DataPoint> askDataSet;
    LineGraphSeries<DataPoint> bidDataSet;


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
        View view = inflater.inflate(R.layout.fragment_my_exchange,
                container, false);
        ButterKnife.bind(this, view);
        graphView = view.findViewById(R.id.askBidChart);
        ((App) getActivity().getApplication()).getNetComponent().inject(this);
        setUpViewModel();
//        runner();
        return view;
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
        coinPairing.setText(finalText);
        String localCoinPairing = coin + "/" + mark;
        runner(localCoinPairing);


    }

    public void setUpViewModel() {
        title.setText(exchange);
        btnSell.setVisibility(View.INVISIBLE);
        coinPairingRepository.setExchange(exchange);
        viewModel = new ExchangeViewModel(coinPairingRepository);
        context = getActivity();
        searchText.setOnItemClickListener((adapterView, view12, i, l) -> {
            View view1 = getActivity().getCurrentFocus();
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            searchText.clearFocus();
            btnSell.setVisibility(View.VISIBLE);
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

    @OnClick(R.id.sellOrder)
    public void onSell() {
        OrderDialog dialog = new OrderDialog();
        String[] coins = currentPairing.split("->");
        String coin = coins[0].trim();
        String mark = coins[1].trim();
        ExchType type;
        if(exchange.equals("binance"))
            type = ExchType.BINANCE;
        else if(exchange.equals("gateio"))
            type = ExchType.GATEIO;
        else
            type = ExchType.BINANCE;
        dialog.setCurrencyPair(coin + "/" + mark, type);
        getActivity().getSupportFragmentManager().beginTransaction().
                replace(R.id.replaceView, dialog).addToBackStack("order_dialog").commit();
    }

    public void runner(String localCoinPairing) {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... strs) {
//                if(exchType == ExchType.BINANCE)
//                    exchange = ApiHelpers.binance("","");
//                else if(exchType == ExchType.GATEIO)
//                    exchange = ApiHelpers.gateio("","");
//                else
//                    exchange = ApiHelpers.binance("","");

                exchangeApiHelper = ApiHelpers.binance("","");
                getStuff(strs[0]);
                return null;
            }
            @Override
            protected void onPostExecute(Void s) {
                graphView.addSeries(bidDataSet);
                graphView.addSeries(askDataSet);
                bidDataSet.setColor(lineColor1);
                bidDataSet.setDrawDataPoints(false);
                bidDataSet.setThickness(8);
                bidDataSet.setAnimated(true);
                bidDataSet.setDrawBackground(true);
                bidDataSet.setBackgroundColor(lineColorOpaque1);

                askDataSet.setColor(lineColor2);
                askDataSet.setDrawDataPoints(false);
                askDataSet.setThickness(8);
                askDataSet.setAnimated(true);
                askDataSet.setDrawBackground(true);
                askDataSet.setBackgroundColor(lineColorOpaque2);
                double lowestX = bidDataSet.getLowestValueX() < askDataSet.getLowestValueX() ?
                        bidDataSet.getLowestValueX(): askDataSet.getLowestValueX();
                double highestX = bidDataSet.getHighestValueX() > askDataSet.getHighestValueX() ?
                        bidDataSet.getHighestValueX(): askDataSet.getHighestValueX();
                double lowestY = bidDataSet.getLowestValueY() < askDataSet.getLowestValueY() ?
                        bidDataSet.getLowestValueY(): askDataSet.getLowestValueY();
                double highestY = bidDataSet.getHighestValueY() > askDataSet.getHighestValueY() ?
                        bidDataSet.getHighestValueY(): askDataSet.getHighestValueY();
                graphView.getGridLabelRenderer().setNumVerticalLabels(5);
                graphView.getGridLabelRenderer().setNumHorizontalLabels(5);
                graphView.getViewport().setMinX(lowestX - .001);
                graphView.getViewport().setMaxX(highestX + .001);
                graphView.getViewport().setMinY(lowestY);
                graphView.getViewport().setMaxY(highestY);
            }
        }.execute(localCoinPairing);
    }

    public String getStuff(String coinPairing) {
        BinanceTradeService service = new BinanceTradeService(exchangeApiHelper);
        try {
            BinanceTradeHistoryParams params = new BinanceTradeHistoryParams();
            params.setCurrencyPair(new CurrencyPair(coinPairing));
            UserTrades trades = service.getTradeHistory(params);
            System.out.println("Printing trades.......");
            for(UserTrade trade: trades.getUserTrades()) {
                System.out.println(trade.toString());
            }
            System.out.println("Finished printing trades");
        }
        catch(Exception e) {
            Log.d("EXCEPTION", e.getLocalizedMessage());
        }
        MarketDataService marketDataService = exchangeApiHelper.getMarketDataService();

        System.out.println("fetching data...");

        // Get the current orderbook
        try {
            graphView.removeAllSeries();
            OrderBook orderBook = marketDataService.getOrderBook(new CurrencyPair(coinPairing));
            List<DataPoint> dataBids = new ArrayList<>();
            List<DataPoint> dataAsks = new ArrayList<>();
            BigDecimal accumulatedBidUnits = new BigDecimal("0");
            System.out.println("Inside of the thread");
            Log.d("BIDS","This is a message");
            for(LimitOrder limitOrder: orderBook.getBids()) {
                accumulatedBidUnits = accumulatedBidUnits.add(limitOrder.getOriginalAmount());
                Log.d("X Coordinate", String.valueOf(limitOrder.getLimitPrice()));
                Log.d("Y Coordinate", String.valueOf(accumulatedBidUnits));
                dataBids.add(new DataPoint(limitOrder.getLimitPrice().doubleValue()
                        , accumulatedBidUnits.doubleValue()));
            }
            BigDecimal accumulatedBidUnits2 = new BigDecimal("0");
            Log.d("ASKS","This is a message");
            for(LimitOrder limitOrder: orderBook.getAsks()) {
                accumulatedBidUnits2 = accumulatedBidUnits2.add(limitOrder.getOriginalAmount());
                Log.d("X Coordinate", String.valueOf(limitOrder.getLimitPrice()));
                Log.d("Y Coordinate", String.valueOf(accumulatedBidUnits2));
                dataAsks.add(new DataPoint(limitOrder.getLimitPrice().doubleValue(),
                        accumulatedBidUnits2.doubleValue()));
            }
            DataPoint[] bidArr = new DataPoint[dataBids.size()];
            for(int i = 0; i < dataBids.size(); i++) {
                bidArr[i] = dataBids.get(i);
            }
            bidDataSet = new LineGraphSeries<>(bidArr);
            DataPoint[] askArr = new DataPoint[dataAsks.size()];
            for(int i = 0; i < dataAsks.size(); i++) {
                askArr[i] = dataAsks.get(i);
            }
            askDataSet = new LineGraphSeries<>(askArr);
        } catch (IOException e) {
            Log.d("Error in getting orderbook", "Handled Exception");
        }
        return "";
    }

}
