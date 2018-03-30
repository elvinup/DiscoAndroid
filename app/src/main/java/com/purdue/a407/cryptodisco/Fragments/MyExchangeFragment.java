package com.purdue.a407.cryptodisco.Fragments;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
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
    CandleStickChart mChart;

    LineGraphSeries<DataPoint> askDataOrderGraphGlobal;
    LineGraphSeries<DataPoint> bidDataOrderGraphGlobal;


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
        mChart = view.findViewById(R.id.candlestick);
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = mChart.getAxisLeft();
//        leftAxis.setEnabled(false);
        leftAxis.setLabelCount(7, false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

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
                exchangeApiHelper = ApiHelpers.binance(getContext(),"","");
                getStuff(strs[0]);
                return null;
            }
            @Override
            protected void onPostExecute(Void s) {
                graphView.addSeries(bidDataOrderGraphGlobal);
                graphView.addSeries(askDataOrderGraphGlobal);
                bidDataOrderGraphGlobal.setColor(lineColor1);
                bidDataOrderGraphGlobal.setDrawDataPoints(false);
                bidDataOrderGraphGlobal.setThickness(8);
                bidDataOrderGraphGlobal.setAnimated(true);
                bidDataOrderGraphGlobal.setDrawBackground(true);
                bidDataOrderGraphGlobal.setBackgroundColor(lineColorOpaque1);

                askDataOrderGraphGlobal.setColor(lineColor2);
                askDataOrderGraphGlobal.setDrawDataPoints(false);
                askDataOrderGraphGlobal.setThickness(8);
                askDataOrderGraphGlobal.setAnimated(true);
                askDataOrderGraphGlobal.setDrawBackground(true);
                askDataOrderGraphGlobal.setBackgroundColor(lineColorOpaque2);
                double lowestX = bidDataOrderGraphGlobal.getLowestValueX() < askDataOrderGraphGlobal.getLowestValueX() ?
                        bidDataOrderGraphGlobal.getLowestValueX(): askDataOrderGraphGlobal.getLowestValueX();
                double highestX = bidDataOrderGraphGlobal.getHighestValueX() > askDataOrderGraphGlobal.getHighestValueX() ?
                        bidDataOrderGraphGlobal.getHighestValueX(): askDataOrderGraphGlobal.getHighestValueX();
                double lowestY = bidDataOrderGraphGlobal.getLowestValueY() < askDataOrderGraphGlobal.getLowestValueY() ?
                        bidDataOrderGraphGlobal.getLowestValueY(): askDataOrderGraphGlobal.getLowestValueY();
                double highestY = bidDataOrderGraphGlobal.getHighestValueY() > askDataOrderGraphGlobal.getHighestValueY() ?
                        bidDataOrderGraphGlobal.getHighestValueY(): askDataOrderGraphGlobal.getHighestValueY();
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
        graphView.removeAllSeries();


        // Get Trades and Open orders of user
        BinanceTradeService service = new BinanceTradeService(exchangeApiHelper);
        TradeHistoryParamsAll params = new TradeHistoryParamsAll();
        try {
            params.setCurrencyPair(new CurrencyPair(coinPairing));
            String string_date = "01-01-2017";
            Date d = new Date();
            SimpleDateFormat f = new SimpleDateFormat("MM-dd-yyyy");
            try {
                d = f.parse(string_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            params.setStartTime(d);
            params.setEndTime(new Date());
            OpenOrders orders = service.getOpenOrders(new CurrencyPair(coinPairing));
            UserTrades trades = service.getTradeHistory(params);

            System.out.println("Printing trades.......");
            Log.d("Array Size", String.valueOf(trades.getTrades().size()));
            for(UserTrade trade: trades.getUserTrades()) {
                System.out.println(trade.toString());
            }
            for(LimitOrder order: orders.getOpenOrders()) {
                System.out.println(order.getId());
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
            bidDataOrderGraphGlobal = new LineGraphSeries<>(bidArr);
            DataPoint[] askArr = new DataPoint[dataAsks.size()];
            for(int i = 0; i < dataAsks.size(); i++) {
                askArr[i] = dataAsks.get(i);
            }
            askDataOrderGraphGlobal = new LineGraphSeries<>(askArr);
        } catch (IOException e) {
            Log.d("Error in getting orderbook", "Handled Exception");
        }

        // Get data for candlesticks
        try {
            Date date = new Date();
            Long endDate = new Long(date.getTime());
            date.setTime(date.getTime() - 3600000L);
            Long startDate = new Long(date.getTime());
            Trades marketTrades =
                    exchangeApiHelper.getMarketDataService().
                            getTrades(new CurrencyPair(coinPairing),null, startDate,
                                    endDate, null);
            Log.d("MARKET TRADES", "Starting to get market trades");


            ArrayList<CandleEntry> candleEntries = new ArrayList<>();
            ArrayList<String> candleLabels = new ArrayList<>();
            float largest = Float.MIN_VALUE;
            float smallest = Float.MAX_VALUE;

            float finalLargest = Float.MIN_VALUE;
            float finalSmallest = Float.MAX_VALUE;


            float opening = 0.0f;
            float closing = 0.0f;
            int index = 0;
            int levels = 10;
            int stickSize = marketTrades.getTrades().size() / levels;
            for(int i = 0; i < marketTrades.getTrades().size(); i++) {
                Trade trade = marketTrades.getTrades().get(i);
                float currentAmount = trade.getPrice().floatValue();
                if(currentAmount > finalLargest) {
                    finalLargest = currentAmount;
                }
                if(currentAmount < finalSmallest) {
                    finalSmallest = currentAmount;
                }
                if(i % stickSize == 0) {
                    opening = currentAmount;
                }
                if(currentAmount > largest) {
                    largest = currentAmount;
                }
                if(currentAmount < smallest) {
                    smallest = currentAmount;
                }
                if(i % stickSize == stickSize - 1 || i == marketTrades.getTrades().size() - 1) {
                    closing = currentAmount;
                    candleEntries.add(new CandleEntry((float) index,
                            largest,smallest, opening,closing));
                    index++;
                    largest = Float.MIN_VALUE;
                    smallest = Float.MAX_VALUE;
                    opening = 0.0f;
                    closing = 0.0f;
                }
                try {
                    Date finDate = trade.getTimestamp();
                    SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
                    String dateStr = format.format(finDate);
                    candleLabels.add(dateStr);
                }
                catch (Exception e) {
                    candleLabels.add(String.valueOf(i));
                }
            }

            CandleDataSet dataSet = new CandleDataSet(candleEntries, "candlestick");
            dataSet.setDrawIcons(false);
            dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            dataSet.setShadowColor(Color.DKGRAY);
            dataSet.setShadowWidth(0.7f);
            dataSet.setDecreasingColor(Color.RED);
            dataSet.setDecreasingPaintStyle(Paint.Style.FILL);
            dataSet.setIncreasingColor(Color.rgb(122, 242, 84));
            dataSet.setIncreasingPaintStyle(Paint.Style.FILL);
            dataSet.setNeutralColor(Color.BLUE);
            mChart.setData(new CandleData(dataSet));
            mChart.getXAxis().setValueFormatter(new DateAxisValueFormatter(candleLabels));
            mChart.invalidate();
            Log.d("MARKET TRADES", "Ending to get market trades");
        }
        catch (Exception e) {
            Log.d("EXCEPTION", e.getLocalizedMessage());
        }

        return "";
    }

}
