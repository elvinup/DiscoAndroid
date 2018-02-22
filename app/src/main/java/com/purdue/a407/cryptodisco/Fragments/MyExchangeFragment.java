package com.purdue.a407.cryptodisco.Fragments;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.gateio.GateioExchange;
import org.knowm.xchange.service.trade.TradeService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.inject.Inject;

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

    @Inject
    AppDatabase appDatabase;

    @Inject
    CoinPairingRepository coinPairingRepository;

    ExchangeViewModel viewModel;

    ArrayAdapter<Object> arrayAdapter;
    Context context;

    String currentPairing = "";


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
        ((App) getActivity().getApplication()).getNetComponent().inject(this);
        setUpViewModel();
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
        ExchangeSpecification exchangeSpecification =
                new GateioExchange().getDefaultExchangeSpecification();
        String gate_key = "E397098B-F9AD-4131-A72A-88AB2C8DD844";
        String gate_secret = "a86b716063adacacc4b8785d9896a4031a33cf2c5c69ec03bff22a535af5843a";
        exchangeSpecification.setApiKey(gate_key);
        exchangeSpecification.setSecretKey(gate_secret);
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                Exchange gateio = ExchangeFactory.INSTANCE.createExchange(exchangeSpecification);
                LimitOrder.Builder limitBuilder = new LimitOrder.Builder(
                        Order.OrderType.ASK,CurrencyPair.ETH_BTC);
                limitBuilder.id("new_thing");
                Date date = new Date();
                limitBuilder.timestamp(date);
                limitBuilder.limitPrice(new BigDecimal(0.01));
                limitBuilder.originalAmount(new BigDecimal(1));
                limitBuilder.remainingAmount(new BigDecimal(.01));
                try {
                    String str = gateio.
                            getTradeService().placeLimitOrder(limitBuilder.build());
                    return str;
                } catch (Exception e) {
                    return e.getMessage();
                }
            }
            protected void onPostExecute(String feed) {
                Toast.makeText(context, feed, Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

}
