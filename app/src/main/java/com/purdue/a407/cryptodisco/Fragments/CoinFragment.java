package com.purdue.a407.cryptodisco.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.purdue.a407.cryptodisco.Api.CDApi;
import com.purdue.a407.cryptodisco.Api.SqlCount;
import com.purdue.a407.cryptodisco.App;
import com.purdue.a407.cryptodisco.Data.AppDatabase;
import com.purdue.a407.cryptodisco.Data.Entities.WatchListEntity;
import com.purdue.a407.cryptodisco.Helpers.DeviceID;
import com.purdue.a407.cryptodisco.R;
import com.purdue.a407.cryptodisco.ViewModels.ExchangeViewModel;

import java.util.List;

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


    @Inject
    AppDatabase appDatabase;

    @Inject
    CDApi cdApi;


    ExchangeViewModel viewModel;

    ArrayAdapter<Object> arrayAdapter;
    Context context;

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
                        fromLabel.setText("Kucoin");
                        break;
                    case R.id.item_gateio:
                        // do whatever
                        fromLabel.setText("Gateio");
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
                        toLabel.setText("Kucoin");
                        break;
                    case R.id.item_gateio:
                        toLabel.setText("Gateio");
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }



}
