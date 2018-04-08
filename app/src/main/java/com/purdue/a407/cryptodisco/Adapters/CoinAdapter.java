package com.purdue.a407.cryptodisco.Adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.purdue.a407.cryptodisco.Api.CDApi;
import com.purdue.a407.cryptodisco.Data.AppDatabase;
import com.purdue.a407.cryptodisco.Data.DAOs.CoinDao;
import com.purdue.a407.cryptodisco.Data.Entities.CoinEntity;
import com.purdue.a407.cryptodisco.Data.Entities.WatchListEntity;
import com.purdue.a407.cryptodisco.Data.Entities.UserExchangeEntity;
import com.purdue.a407.cryptodisco.Fragments.CoinFragment;
import com.purdue.a407.cryptodisco.Fragments.MyExchangeFragment;
import com.purdue.a407.cryptodisco.R;
import com.purdue.a407.cryptodisco.ViewModels.CoinViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by KennyZheng on 3/29/18.
 */

public class CoinAdapter extends RecyclerView.Adapter<CoinAdapter.UserWatchlistHolder> {
    @Inject
    AppDatabase appDatabase;

    @Inject
    CDApi cdApi;


    Context context;
    List<WatchListEntity> watchlists;

    public CoinAdapter(Context context, List<WatchListEntity> watchlists) {
        this.context = context;
        this.watchlists = watchlists;
    }


    @Override
    public CoinAdapter.UserWatchlistHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.holder_view_exchange, parent, false);
        CoinAdapter.UserWatchlistHolder viewHolder = new CoinAdapter.UserWatchlistHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CoinAdapter.UserWatchlistHolder holder, int position) {
        WatchListEntity watchlist = watchlists.get(position);
        Log.d("Coin ID", Integer.toString(watchlist.getCoin()));

        List<CoinEntity> coinList = appDatabase.coinDao().coinsNotLive(); //coinByIdNotLive(watchlist.getCoin());

        Log.d("WatchList Size", Integer.toString(coinList.size()));
        String watchlistCoin = coinList.get(0).getName();


        /*
        cdApi.getCoins().enqueue(new Callback<List<CoinEntity>>() {
            @Override
            public void onResponse(Call<List<CoinEntity>> call, Response<List<CoinEntity>> response) {
                if (response.code() != 200) {
                    Log.d("Error", Integer.toString(response.code()));
                    return;
                }
                List<CoinEntity> coinList = response.body(); //appDatabase.coinDao().coinsNotLive(); //coinByIdNotLive(watchlist.getCoin());

                Log.d("WatchList Size", Integer.toString(coinList.size()));
                String watchlistCoin = coinList.get(0).getName();

            }

            @Override
            public void onFailure(Call<List<CoinEntity>> call, Throwable t) {

            }
        });
        */

        holder.coinName.setText(watchlistCoin);
        holder.cardView.setOnClickListener(view -> {
            CoinFragment fragment = CoinFragment.newInstance(watchlistCoin);
            AppCompatActivity activity = (AppCompatActivity)context;
            activity.getSupportFragmentManager().beginTransaction().
                    replace(R.id.replaceView,fragment).addToBackStack("coin").commit();
        });
    }

    @Override
    public int getItemCount() {
        return watchlists.size();
    }

    public void addAll(List<WatchListEntity> newwatchlists) {
        watchlists.clear();
        watchlists.addAll(newwatchlists);
        notifyDataSetChanged();
    }

    public class UserWatchlistHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cv)
        CardView cardView;

        @BindView(R.id.exchangeNameText)
        TextView coinName;

        public UserWatchlistHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
