package com.purdue.a407.cryptodisco.Fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.purdue.a407.cryptodisco.Adapters.CoinAdapter;
import com.purdue.a407.cryptodisco.Adapters.UserExchangesAdapter;
import com.purdue.a407.cryptodisco.Api.CDApi;
import com.purdue.a407.cryptodisco.App;
import com.purdue.a407.cryptodisco.Data.AppDatabase;
import com.purdue.a407.cryptodisco.Data.Entities.CoinEntity;
import com.purdue.a407.cryptodisco.Data.Entities.ExchangeEntity;
import com.purdue.a407.cryptodisco.Data.Entities.WatchListEntity;
import com.purdue.a407.cryptodisco.Helpers.DeviceID;
import com.purdue.a407.cryptodisco.R;
import com.purdue.a407.cryptodisco.Testing.exchangeVolumeTesting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WatchlistFragment extends Fragment {

    @Inject
    AppDatabase appDatabase;

    @BindView(R.id.myWatchlistRecycler)
    RecyclerView watchlistRecycler;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.searchCoins)
    AppCompatAutoCompleteTextView searchCoins;


    CoinAdapter watchlistAdapter;

   // List<ExchangeEntity> searchEntities = new ArrayList<>();
    //List<String> searchesByExchange = new ArrayList<>();

    ArrayAdapter<Object> arrayAdapter;
    Context context;

    @Inject
    CDApi cdApi;

    @Inject
    DeviceID deviceID;

    public WatchlistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_watchlist, container, false);
        ButterKnife.bind(this, view);
        ((App) getActivity().getApplication()).getNetComponent().inject(this);
        context = getActivity();
        title.setText("Watched Coins");

        List<CoinEntity> coinEntityList = appDatabase.coinDao().coinsNotLive();
        //Log.d("coin ent size", Integer.toString(coinEntityList.size()));

        String uuid = deviceID.getDeviceID();

        watchlistAdapter = new CoinAdapter(getActivity(), new ArrayList<>(), coinEntityList, uuid);

        watchlistRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        watchlistRecycler.setAdapter(watchlistAdapter);

        //Gets the entries from the watchlist in the database
        cdApi.getWatchListEntities().enqueue(new Callback<List<WatchListEntity>>() {
            @Override
            public void onResponse(Call<List<WatchListEntity>> call, Response<List<WatchListEntity>> response) {
                if (response.code() != 200) {

                }
                else {
                    watchlistAdapter.addAll(watchlistAdapter.filterWatchList(response.body()));
                }


            }

            @Override
            public void onFailure(Call<List<WatchListEntity>> call, Throwable t) {

            }
        });

        searchCoins.setOnItemClickListener((adapterView, view12, i, l) -> {
            View view1 = getActivity().getCurrentFocus();
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            searchCoins.clearComposingText();
            String coin = searchCoins.getText().toString();
            searchCoins.setText("");
            CoinFragment fragment = CoinFragment.newInstance(coin);
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.replaceView,fragment).addToBackStack("coin").commit();
        });



        new AsyncTask<Void, Void, List<CoinEntity>>() {
            @Override
            protected List<CoinEntity> doInBackground(Void... params) {
                //System.out.println(test);

                return appDatabase.coinDao().coinsNotLive();
            }
            @Override
            protected void onPostExecute(List<CoinEntity> coinsBySearch) {
                ArrayList<String> strings = new ArrayList<>();
                for(CoinEntity entity: coinsBySearch) {
                    strings.add(entity.getShort_name());
                }

                Log.d("Entity list size: ", Integer.toString(strings.size()));
                arrayAdapter = new ArrayAdapter<>(context,
                        android.R.layout.simple_dropdown_item_1line,strings.toArray());
                searchCoins.setAdapter(arrayAdapter);

            }
        }.execute();



        return view;
    }
}
