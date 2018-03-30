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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExchangesAdapter extends RecyclerView.Adapter<ExchangesAdapter.ExchangeHolder>
{

    Context context;
    List<ExchangeEntity> exchanges;

    public ExchangesAdapter(Context context, List<ExchangeEntity> exchanges) {
        this.context = context;
        this.exchanges = exchanges;
    }


    @Override
    public ExchangeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.holder_view_exchange, null, false);
        ExchangeHolder viewHolder = new ExchangeHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ExchangeHolder holder, int position) {
        ExchangeEntity exchange = exchanges.get(position);
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

        public ExchangeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
