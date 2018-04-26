package com.purdue.a407.cryptodisco.Adapters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.purdue.a407.cryptodisco.Data.Entities.ExchangeEntity;
import com.purdue.a407.cryptodisco.Fragments.ExchangeFragment;
import com.purdue.a407.cryptodisco.Fragments.MyExchangeFragment;
import com.purdue.a407.cryptodisco.Interfaces.RecyclerViewFilterInterface;
import com.purdue.a407.cryptodisco.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ExchangesAdapter extends RecyclerView.Adapter<ExchangesAdapter.ExchangeHolder>
{

    Context context;
    List<ExchangeEntity> exchanges;
    Map<String, Integer> mapImages;

    public ExchangesAdapter(Context context, List<ExchangeEntity> exchanges) {
        this.context = context;
        this.exchanges = exchanges;
        setUpMap();
    }

    public void setUpMap() {
        mapImages = new HashMap<>();
        mapImages.put("binance", 270);
        mapImages.put("okex", 294);
        mapImages.put("huobi", 102);
        mapImages.put("kraken",24);
        mapImages.put("hitbtc", 42);
        mapImages.put("coinone", 174);
        mapImages.put("kucoin", 311);
        mapImages.put("qryptos", 163);
        mapImages.put("gateio", 302);
    }


    @Override
    public ExchangeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.holder_view_exchange, parent, false);
        ExchangeHolder viewHolder = new ExchangeHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ExchangeHolder holder, int position) {
        ExchangeEntity exchange = exchanges.get(position);
        String base = "https://s2.coinmarketcap.com/static/img/exchanges/64x64/";
        Integer id = mapImages.get(exchange.getName());
        base += String.valueOf(id);
        base += ".png";
        Picasso.get()
                .load(base)
                .centerCrop()
                .resize(50,50)
                .error(R.drawable.emoji_1f30f)
                .into(holder.imageView);
        holder.exchangeName.setText(exchange.getName());
        holder.cardView.setOnClickListener(view -> {
            ExchangeFragment fragment = ExchangeFragment.newInstance(exchange.getName());
            AppCompatActivity activity = (AppCompatActivity)context;
            activity.getSupportFragmentManager().beginTransaction().
                    replace(R.id.replaceView,fragment).addToBackStack("" +
                    "exchange").commit();
        });
    }

    @Override
    public int getItemCount() {
        return exchanges.size();
    }

    public void addAll(List<ExchangeEntity> newExchanges) {
        exchanges.clear();
        exchanges.addAll(newExchanges);
        notifyDataSetChanged();
    }

    public void clear() {
        exchanges.clear();
        notifyDataSetChanged();
    }

    public class ExchangeHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.exchangeNameText)
        TextView exchangeName;

        @BindView(R.id.cv)
        CardView cardView;

        CircleImageView imageView;

        public ExchangeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            imageView = itemView.findViewById(R.id.imgView);
        }
    }

}
