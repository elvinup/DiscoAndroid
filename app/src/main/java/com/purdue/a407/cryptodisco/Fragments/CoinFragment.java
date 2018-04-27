package com.purdue.a407.cryptodisco.Fragments;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.google.gson.Gson;
import com.kyleohanian.databinding.modelbindingforms.Listeners.OnBindDialogCancelListener;
import com.kyleohanian.databinding.modelbindingforms.Listeners.OnBindDialogCreateListener;
import com.kyleohanian.databinding.modelbindingforms.UIObjects.ModelForm;
import com.purdue.a407.cryptodisco.Adapter.ChatRoomAdapter;
import com.purdue.a407.cryptodisco.Adapters.ExchangesAdapter;

import com.purdue.a407.cryptodisco.Api.CDApi;
import com.purdue.a407.cryptodisco.Api.SqlCount;
import com.purdue.a407.cryptodisco.App;
import com.purdue.a407.cryptodisco.Data.AppDatabase;
import com.purdue.a407.cryptodisco.Data.Entities.WatchListEntity;
import com.purdue.a407.cryptodisco.Helpers.DeviceID;
import com.purdue.a407.cryptodisco.R;
import com.purdue.a407.cryptodisco.ViewModels.ExchangeViewModel;
import com.webcerebrium.kucoin.KucoinApi;
import com.webcerebrium.kucoin.KucoinApiException;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.binance.dto.account.BinanceAccountInformation;
import org.knowm.xchange.binance.dto.account.DepositAddress;
import org.knowm.xchange.binance.service.BinanceAccountService;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.gateio.service.GateioAccountService;
import org.knowm.xchange.gateio.service.GateioAccountServiceRaw;
import org.knowm.xchange.kucoin.KucoinExchange;
import org.knowm.xchange.kucoin.service.KucoinAccountService;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CoinFragment extends Fragment {


    String titleString;

    @Inject
    DeviceID deviceID;

    @BindView(R.id.coin_like)
    Button coinLike;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.fromLabel)
    TextView fromLabel;

    @BindView(R.id.toLabel)
    TextView toLabel;

    @BindView(R.id.addressFrom)
    TextView addressFrom;

    @BindView(R.id.addressTo)
    TextView addressTo;

    @Inject
    AppDatabase appDatabase;

    @Inject
    CDApi cdApi;


    ExchangeViewModel viewModel;

    ArrayAdapter<Object> arrayAdapter;
    Context context;

    String depositAddress = "";

    boolean kucoinHas = true;
    boolean gateioHas = true;
    boolean binanceHas = true;

    public CoinFragment() {
        // Required empty public constructor
    }

    public static CoinFragment newInstance(String title) {
        CoinFragment coinFragment = new CoinFragment();
        coinFragment.setTitle(title);
        return coinFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_coin, container, false);
        ButterKnife.bind(this, view);
        ((App) getActivity().getApplication()).getNetComponent().inject(this);
        context = getActivity();

        int coinNum = appDatabase.coinDao().getID(titleString);
        Log.d("Coin name", titleString);


        WatchListEntity watchListEntity = new WatchListEntity(deviceID.getDeviceID(), coinNum);
        cdApi.numberOfLikedCoins(watchListEntity).enqueue(new Callback<List<SqlCount>>() {
            @Override
            public void onResponse(Call<List<SqlCount>> call, Response<List<SqlCount>> response) {
                if (response.code() != 200) {
                    Log.d("Not 200", String.valueOf(response.code()));
                    return;
                }
                int numliked =  response.body().get(0).count;
                if (numliked > 0) {
                    coinLike.setText("Unlike");
                } else if (numliked == 0){
                    coinLike.setText("Like");
                }
            }

            @Override
            public void onFailure(Call<List<SqlCount>> call, Throwable t) {

            }
        });

        Log.d("COINNUM", Integer.toString(coinNum));

        coinLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                WatchListEntity watchListEntity = new WatchListEntity(deviceID.getDeviceID(), coinNum);


                int coinNum = appDatabase.coinDao().getID(titleString);
                //numliked = appDatabase.watchlistDao().getTimeUserLikedCoin(deviceID.getDeviceID(), coinNum);
                //int numliked = 0;
                cdApi.numberOfLikedCoins(watchListEntity).enqueue(new Callback<List<SqlCount>>() {
                    @Override
                    public void onResponse(Call<List<SqlCount>> call, Response<List<SqlCount>> response) {
                        if (response.code() != 200) {
                            Log.d("Not 200", String.valueOf(response.code()));
                            return;
                        }

                        int numliked =  response.body().get(0).count;

                        Log.d("Response", Integer.toString(response.body().get(0).getCount()));
                        Log.d("Response", response.body().get(0).toString());

                        if (numliked == 0) {

                            cdApi.insertLikedCoin(watchListEntity).enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.code() != 200) {
                                        Log.d("insert error ", String.valueOf(response.code()));
                                    } else {
                                        Log.d("error ", "Sucesss");

                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Log.d("error ", "Fail");

                                }
                            });
                            //appDatabase.watchlistDao().insertLike(deviceID.getDeviceID(), coinNum);

                            coinLike.setText("Unlike");
                        } else if (numliked > 0) {
                            coinLike.setText("Like");
                            appDatabase.watchlistDao().removeLike(deviceID.getDeviceID(), coinNum);

                            cdApi.removeWatchList(watchListEntity).enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.code() != 200) {
                                        Log.d("remove error ", String.valueOf(response.code()));
                                    } else {
                                        Log.d("error ", "Sucess");

                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Log.d("error ", t.getLocalizedMessage());

                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<List<SqlCount>> call, Throwable t) {
                        Log.d("error", t.getLocalizedMessage());
                    }
                });
                Log.d("COINNUM", Integer.toString(coinNum));
               // Log.d("NUMLIKED", Integer.toString(numliked));


            }
        });

        title.setText(titleString);


        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance("eAvqgbHiIbrAHg0Yu8ACBrAPDBTBWePQuxae22znBGrU2hrPly98dQmmEMyzLNYd", "FgyCVcuKGc9Y9KTzZwQGQgR8dyurV7vdUIkf84yjgToZVxo4g9TVKbvIkwe5lV3G");
        BinanceApiRestClient client = factory.newRestClient();



        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {

                Exchange gateioApi = ApiHelpers.gateio(getContext(),"","");
                Exchange kucoinApi = ApiHelpers.kucoin(getContext(), "", "");
                Exchange binanceApi = ApiHelpers.binance(getContext(), "", "");

                KucoinAccountService service = new KucoinAccountService(kucoinApi);
                GateioAccountService service2 = new GateioAccountService(gateioApi);
                BinanceAccountService service3 = new BinanceAccountService(binanceApi);


                if (titleString.equals("BTC")) {
                    try {
                        //BinanceAccountService binanceAccountService = new BinanceAccountService(binance);

                        com.binance.api.client.domain.account.DepositAddress depositAddress = client.getDepositAddress("BTC");
                        //Fina Fuckin Lee
                        String walletid = depositAddress.getAddress();


                        String balance =  service2.getAccountInfo().getWallet().getBalance(new Currency("BTC")).toString();
                        String balance2 = service3.getAccountInfo().getWallet().getBalance(new Currency("BTC")).toString();

                       if (balance.contains("available=0,")) {
                           gateioHas = false;
                       }
                       if (balance2.contains("available=0,")) {
                           binanceHas = false;
                       }



                       System.out.println(balance2);
                       return walletid;
                    } catch (Exception e) {
                        return e.getLocalizedMessage();
                    }
                }
                else if (titleString.equals("ETH")) {
                    try {
                        com.binance.api.client.domain.account.DepositAddress depositAddress = client.getDepositAddress("ETH");
                        String walletid = depositAddress.getAddress();

                        String balance = service2.getAccountInfo().getWallet().getBalance(new Currency("ETH")).toString();
                        String balance2 = service3.getAccountInfo().getWallet().getBalance(new Currency("ETH")).toString();

                        if (balance.contains("available=0,")) {
                            gateioHas = false;
                        }
                        if (balance2.contains("available=0,")) {
                            binanceHas = false;
                        }
                        return walletid;
                    } catch (Exception e) {
                        return e.getLocalizedMessage();
                    }
                }
                else if (titleString.equals("XLM")) {
                    try {
                        com.binance.api.client.domain.account.DepositAddress depositAddress = client.getDepositAddress("XLM");
                        String walletid = depositAddress.getAddress();
                        String balance = service2.getAccountInfo().getWallet().getBalance(new Currency("XLM")).toString();
                        String balance2 = service3.getAccountInfo().getWallet().getBalance(new Currency("XLM")).toString();

                        System.out.println("XLMBalance" + balance);
                        if (balance.contains("available=0,")) {
                            gateioHas = false;
                        }
                        if (balance2.contains("available=0,")) {
                            binanceHas = false;
                        }
                        return walletid;
                    } catch (Exception e) {
                        return e.getLocalizedMessage();
                    }
                }
                return "Nothing";
            }
            @Override
            protected void onPostExecute(String walletid) {
                if(walletid == null) {
                    walletid = "null";
                }
                Log.d("walletId", walletid);
                depositAddress = walletid;
            }
        }.execute();

        return view;
    }

    public void setTitle(String titleString) {
        this.titleString = titleString;
    }

    @OnClick(R.id.exchange_from)
    public void onExchangeFrom(View view) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_coin_withdrawal, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.item_kucoin:
                        // do whatever
                        if (kucoinHas) {
                            fromLabel.setText("Kucoin");
                            addressFrom.setText("16vBGW5pPPYebwJhv4bCNrs2MWYG3SNznR");
                        }
                        break;
                    case R.id.item_gateio:
                        // do whatever
                        if (gateioHas) {
                            fromLabel.setText("Gateio");
                            addressFrom.setText("1QEbysfQQaQbyk8pFxz1D55uJ6z7nyZNzR");
                        }
                        else {
                            item.setVisible(false);
                        }
                        break;
                    case R.id.item_binance:
                        if (binanceHas) {
                            fromLabel.setText("Binance");
                            addressFrom.setText(depositAddress);
                        }
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    @OnClick(R.id.exchange_to)
    public void onExchangeTo(View view) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_coin_withdrawal, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.item_kucoin:
                        // do whatever
                        if (kucoinHas) {
                            toLabel.setText("Kucoin");
                            addressTo.setText("16vBGW5pPPYebwJhv4bCNrs2MWYG3SNznR");
                        }
                        break;
                    case R.id.item_gateio:
                        if (gateioHas) {
                            toLabel.setText("Gateio");
                            addressTo.setText("1QEbysfQQaQbyk8pFxz1D55uJ6z7nyZNzR");
                        }
                        else {
                            item.setVisible(false);
                        }
                        break;
                    case R.id.item_binance:
                        if (binanceHas) {
                            toLabel.setText("binance");
                            addressTo.setText(depositAddress);
                        }
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }





}
