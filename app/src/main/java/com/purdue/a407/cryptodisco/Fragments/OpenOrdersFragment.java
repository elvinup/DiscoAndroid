package com.purdue.a407.cryptodisco.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.purdue.a407.cryptodisco.Adapters.OpenOrdersAdapter;
import com.purdue.a407.cryptodisco.Adapters.TradeHistoryAdapter;
import com.purdue.a407.cryptodisco.Helpers.AsyncTaskParams;
import com.purdue.a407.cryptodisco.R;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.binance.service.BinanceTradeService;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.dto.trade.UserTrade;
import org.knowm.xchange.dto.trade.UserTrades;
import org.knowm.xchange.service.trade.params.TradeHistoryParamsAll;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OpenOrdersFragment extends Fragment {

    @BindView(R.id.openOrdersRecycler)
    RecyclerView recyclerView;

    OpenOrdersAdapter openOrdersAdapter;


    MaterialDialog dialog;

    MyExchangeFragment myExchangeFragment;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_open_orders, null);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        openOrdersAdapter = new OpenOrdersAdapter(getActivity(), new ArrayList<>());
        recyclerView.setAdapter(openOrdersAdapter);
        dialog = new MaterialDialog.Builder(getActivity())
                .title("Loading Open Orders...")
                .content("Please Wait")
                .progress(true, 0).build();
        return view;
    }

    public void callback(MyExchangeFragment fragment) {
        this.myExchangeFragment = fragment;
    }

    public void setCoinPairing(String newPairing, Exchange exchange) {
        runAsyncTask(new AsyncTaskParams(newPairing, exchange));
    }


    public void runAsyncTask(AsyncTaskParams params) {
        dialog.show();
        new AsyncTask<AsyncTaskParams, Void, List<LimitOrder>>() {
            @Override
            protected List<LimitOrder> doInBackground(AsyncTaskParams... params) {
                List<LimitOrder> orders = fillList(params[0].getExchange(), params[0].getCoinPairing());
                return orders;
            }

            @Override
            protected void onPostExecute(List<LimitOrder> orders) {
                dialog.dismiss();
                if(getActivity() != null) {
                    openOrdersAdapter.addAll(orders);
                }
            }
        }.execute(params);
    }

    public List<LimitOrder> fillList(Exchange exchange, String pairing) {
        BinanceTradeService service = new BinanceTradeService(exchange);
        TradeHistoryParamsAll params = new TradeHistoryParamsAll();
        try {
            params.setCurrencyPair(new CurrencyPair(pairing));
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
            OpenOrders orders = service.getOpenOrders(new CurrencyPair(pairing));
            for(LimitOrder order: orders.getOpenOrders()) {
                System.out.println(order.getId());
            }
            System.out.println("Finished printing trades");
            return orders.getOpenOrders();
        }
        catch(Exception e) {
            Log.d("EXCEPTION", e.getLocalizedMessage());
            return new ArrayList<>();
        }
    }
}
