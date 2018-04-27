package com.purdue.a407.cryptodisco.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.purdue.a407.cryptodisco.Fragments.TradeHistoryFragment;
import com.purdue.a407.cryptodisco.R;

import org.knowm.xchange.binance.dto.trade.OrderType;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.UserTrade;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TradeHistoryAdapter extends RecyclerView.Adapter<TradeHistoryAdapter.ViewHolder> {

    Context context;
    List<UserTrade> userTrades;
    private int rows = 3;

    public TradeHistoryAdapter(Context context, List<UserTrade> userTrades) {
        this.context = context;
        this.userTrades = userTrades;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.holder_item_trade_history,
                parent, false);
        LinearLayout layout = view.findViewById(R.id.holderMasterLayout);
        layout.setBackgroundColor(ContextCompat.getColor(context, viewType));
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String value = getValue(position);
        holder.value.setText(value);
    }

    @Override
    public int getItemViewType(int position) {
        UserTrade userTrade = userTrades.get(position / rows);
        Order.OrderType orderType = userTrade.getType();
        if(orderType == Order.OrderType.BID) {
            return R.color.lineColorOpaque1;
        }
        else if(orderType == Order.OrderType.ASK) {
            return R.color.lineColorOpaque2;
        }
        else {
            return R.color.unknownOpaque;
        }
    }

    public String getValue(int position) {
        UserTrade userTrade = userTrades.get(position / rows);
        int mod = position % rows;
        if(mod == 0) {
            return String.valueOf(userTrade.getOriginalAmount());
        }
        else if(mod == 1) {
            return String.valueOf(userTrade.getPrice());
        }
        else if(mod == 2) {
            SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy, hh:mm");
            return format.format(userTrade.getTimestamp());
        }
        return "";
    }

    @Override
    public int getItemCount() {
        return userTrades.size() * rows;
    }

    public void addAll(List<UserTrade> newTrades) {
        userTrades.clear();
        userTrades.addAll(newTrades);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.value)
        TextView value;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
