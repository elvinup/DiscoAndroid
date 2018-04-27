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
import com.purdue.a407.cryptodisco.Helpers.DeviceID;
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

    Context context;
    List<WatchListEntity> watchlists;
    List<CoinEntity> coinEntityList;
    String uuid;

    public CoinAdapter(Context context, List<WatchListEntity> watchlists, List<CoinEntity> coinEntityList, String uuid) {
        this.context = context;
        this.watchlists = watchlists;
        this.coinEntityList = coinEntityList;
        this.uuid = uuid;
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

        //Default watch list has at least 1 coin which is set to Bitcoin
        String watchlistCoin = "Bitcoin";
        String watchlistShortName = "BTC";
        for (CoinEntity ce: coinEntityList){
            // Match coin id
            if (watchlist.getCoin() == ce.getId()) {
                //Check if id is same
                if (watchlist.getUser().equals(uuid)) {
                    watchlistCoin = ce.getName();
                    watchlistShortName = ce.getShort_name();
                    break;
                }
            }
        }

        holder.coinName.setText(watchlistCoin);
        String finalWatchlistCoin = watchlistShortName;
        holder.cardView.setOnClickListener(view -> {
            CoinFragment fragment = CoinFragment.newInstance(finalWatchlistCoin);
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

    public List<WatchListEntity> filterWatchList(List<WatchListEntity> watchlists) {
        List<WatchListEntity> filteredList = new ArrayList<>();

        for (WatchListEntity we: watchlists) {
            for (CoinEntity ce : coinEntityList) {
                // Match coin id or userid
                if (we.getCoin() == ce.getId() && we.getUser().equals(uuid)) {
                    //watchlists.remove(counter);
                    //notifyDataSetChanged();
                    filteredList.add(we);
                    break;
                }
            }
        }

        return filteredList;
    }

    public List<CoinEntity> filterCoinList(List<WatchListEntity> watchlists) {
        List<CoinEntity> filteredList = new ArrayList<>();

        for (WatchListEntity we: watchlists) {
            for (CoinEntity ce : coinEntityList) {
                // Match coin id or userid
                if (we.getCoin() == ce.getId() && we.getUser().equals(uuid)) {
                    //watchlists.remove(counter);
                    //notifyDataSetChanged();
                    filteredList.add(ce);
                    break;
                }
            }
        }

        return filteredList;
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
