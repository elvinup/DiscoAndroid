package com.purdue.a407.cryptodisco.Fragments;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.purdue.a407.cryptodisco.Helpers.AsyncTaskParams;
import com.purdue.a407.cryptodisco.Helpers.DateAxisValueFormatter;
import com.purdue.a407.cryptodisco.R;

import org.knowm.xchange.Exchange;
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

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

public class GraphFragment extends Fragment {

    @BindColor(R.color.lineColor1)
    int lineColor1;

    @BindColor(R.color.lineColorOpaque1)
    int lineColorOpaque1;

    @BindColor(R.color.lineColor2)
    int lineColor2;

    @BindColor(R.color.lineColorOpaque2)
    int lineColorOpaque2;

    @BindView(R.id.radioGroup)
    RadioGroup group;

    LineChart lineChart;
    CandleStickChart mChart;

    MaterialDialog dialog;

    int currentCandleStickLevel = 5;

    MyExchangeFragment myExchangeFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, null);
        ButterKnife.bind(this, view);
        lineChart = view.findViewById(R.id.askBidChart);
        mChart = view.findViewById(R.id.candlestick);
        setUpCandleSticks();
        dialog = new MaterialDialog.Builder(getActivity())
                .title("Loading Graphs...")
                .content("Please Wait")
                .progress(true, 0).build();
        group.setOnCheckedChangeListener((radioGroup, i) -> {
            if(radioGroup.getCheckedRadioButtonId() == R.id.radio5) {
                currentCandleStickLevel = 5;
                myExchangeFragment.setCoinPairing();
            }
            else if(radioGroup.getCheckedRadioButtonId() == R.id.radio10) {
                currentCandleStickLevel = 10;
                myExchangeFragment.setCoinPairing();

            }
            else if(radioGroup.getCheckedRadioButtonId() == R.id.radio15) {
                currentCandleStickLevel = 15;
                myExchangeFragment.setCoinPairing();
            }
        });
        return view;
    }

    public void callback(MyExchangeFragment fragment) {
        this.myExchangeFragment = fragment;
    }

    public void setCoinPairing(String coinPairing, Exchange exchange) {
        runner(new AsyncTaskParams(coinPairing, exchange));
    }

    public void setUpCandleSticks() {
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setLabelCount(7, false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    public void runner(AsyncTaskParams params) {
        dialog.show();
        new AsyncTask<AsyncTaskParams, Void, Void>() {
            @Override
            protected Void doInBackground(AsyncTaskParams... params) {
                getStuff(params[0].getExchange(),params[0].getCoinPairing());
                return null;
            }
            @Override
            protected void onPostExecute(Void s) {
                dialog.dismiss();
                lineChart.invalidate();
                mChart.invalidate();
                dialog.dismiss();
            }
        }.execute(params);

    }

    public String getStuff(Exchange exchange, String coinPairing) {
        // Get Trades and Open orders of user
        getOrderBookData(exchange, coinPairing);
        getCandleStickData(exchange, coinPairing);

        return "";
    }

    public void getOrderBookData(Exchange exchange, String coinPairing) {
        try {
            MarketDataService marketDataService = exchange.getMarketDataService();
            OrderBook orderBook = marketDataService.getOrderBook(new CurrencyPair(coinPairing));
            BigDecimal accumulatedBidUnits = new BigDecimal("0");
            System.out.println("Inside of the thread");
            Log.d("BIDS","This is a message");
            ArrayList<Entry> bidVals = new ArrayList<>();
            ArrayList<Entry> askVals = new ArrayList<>();
            for(LimitOrder limitOrder: orderBook.getBids()) {
                accumulatedBidUnits = accumulatedBidUnits.add(limitOrder.getOriginalAmount());
//                Log.d("X Coordinate", String.valueOf(limitOrder.getLimitPrice()));
//                Log.d("Y Coordinate", String.valueOf(accumulatedBidUnits));

                bidVals.add(new Entry(limitOrder.getLimitPrice().floatValue(),
                        accumulatedBidUnits.floatValue()));
            }

            BigDecimal accumulatedBidUnits2 = new BigDecimal("0");
            Log.d("ASKS","This is a message");
            for(LimitOrder limitOrder: orderBook.getAsks()) {
                accumulatedBidUnits2 = accumulatedBidUnits2.add(limitOrder.getOriginalAmount());
//                Log.d("X Coordinate", String.valueOf(limitOrder.getLimitPrice()));
//                Log.d("Y Coordinate", String.valueOf(accumulatedBidUnits2));
                askVals.add(new Entry(limitOrder.getLimitPrice().floatValue(),
                        accumulatedBidUnits2.floatValue()));
            }

            LineDataSet bidSet = createSet(bidVals, "Bids", lineColor1, lineColorOpaque1);
            LineDataSet askSet = createSet(askVals, "Asks", lineColor2, lineColorOpaque2);
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            LineData data = new LineData();
            data.addDataSet(bidSet);
            data.addDataSet(askSet);
            lineChart.setData(data);
            Description description = new Description();
            description.setText("");
            lineChart.setDescription(description);

        } catch (IOException e) {
            Log.d("Error in getting orderbook", "Handled Exception");
        }
    }

    public void getCandleStickData(Exchange exchange, String coinPairing) {
        try {
            Date date = new Date();
            Long endDate = new Long(date.getTime());
            date.setTime(date.getTime() - 3600000L);
            Long startDate = new Long(date.getTime());
            Trades marketTrades =
                    exchange.getMarketDataService().
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
            int levels = currentCandleStickLevel;
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
                if(i % stickSize == stickSize - 1) {
                    closing = currentAmount;
                    candleEntries.add(new CandleEntry((float) index,
                            largest, smallest, opening, closing));
                    index++;
                    largest = Float.MIN_VALUE;
                    smallest = Float.MAX_VALUE;
                    opening = 0.0f;
                    closing = 0.0f;
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
            }
            mChart.getXAxis().setValueFormatter(new DateAxisValueFormatter(candleLabels));

            CandleDataSet dataSet = new CandleDataSet(candleEntries, "candlestick");
            dataSet.setDrawIcons(false);
            dataSet.setLabel("");
            dataSet.setValueTextSize(0f);
            dataSet.setDrawValues(false);
            dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            dataSet.setShadowColor(Color.DKGRAY);
            dataSet.setShadowWidth(0.7f);
            dataSet.setDecreasingColor(Color.RED);
            dataSet.setDecreasingPaintStyle(Paint.Style.FILL);
            dataSet.setIncreasingColor(Color.rgb(122, 242, 84));
            dataSet.setIncreasingPaintStyle(Paint.Style.FILL);
            dataSet.setNeutralColor(Color.BLUE);
            mChart.setData(new CandleData(dataSet));
            Description description = new Description();
            description.setText("");
            mChart.setDescription(description);
            Log.d("MARKET TRADES", "Ending to get market trades");
        }
        catch (Exception e) {
            Log.d("EXCEPTION", e.getLocalizedMessage());
        }
    }

    public LineDataSet createSet(ArrayList<Entry> entries, String label, int color, int colorOpaque) {
        Collections.sort(entries, new EntryXComparator());
        LineDataSet set;
        set = new LineDataSet(entries, label);
        set.setColor(color);
        set.setFillColor(colorOpaque);
        set.setCircleColor(color);
        set.setLineWidth(1f);
        set.setCircleRadius(3f);
        set.setDrawCircleHole(false);
        set.setValueTextSize(9f);
        set.setDrawFilled(true);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        return set;
    }




}
