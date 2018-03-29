package com.purdue.a407.cryptodisco.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.purdue.a407.cryptodisco.Adapters.UserExchangesAdapter;
import com.purdue.a407.cryptodisco.App;
import com.purdue.a407.cryptodisco.Data.AppDatabase;
import com.purdue.a407.cryptodisco.Data.Entities.ExchangeEntity;
import com.purdue.a407.cryptodisco.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WatchlistFragment extends Fragment {

    @Inject
    AppDatabase appDatabase;

    @BindView(R.id.myWatchlistRecycler)
    RecyclerView watchlistRecycler;

    @BindView(R.id.title)
    TextView title;

    //UserExchangesAdapter exchangesAdapter;

   // List<ExchangeEntity> searchEntities = new ArrayList<>();
    //List<String> searchesByExchange = new ArrayList<>();

    ArrayAdapter<Object> arrayAdapter;
    Context context;

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
        //watchlistAdapter = new UserExchangesAdapter(getActivity(), new ArrayList<>());

        watchlistRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        //exchangesRecycler.setAdapter(watchlistAdapter);

        //appDatabase.userExchangeDao().userExchanges().observe(getActivity(), exchangeEntities ->
        //        exchangesAdapter.addAll(exchangeEntities));
        return view;
    }
}
