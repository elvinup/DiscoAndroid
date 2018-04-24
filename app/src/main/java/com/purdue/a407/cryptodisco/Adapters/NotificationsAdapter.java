package com.purdue.a407.cryptodisco.Adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.purdue.a407.cryptodisco.App;
import com.purdue.a407.cryptodisco.Data.AppDatabase;
import com.purdue.a407.cryptodisco.Data.Entities.Arbitrage;
import com.purdue.a407.cryptodisco.Data.Entities.NotificationsEntity;
import com.purdue.a407.cryptodisco.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    Context context;
    List<NotificationsEntity> notifications;
    AppDatabase database;

    public NotificationsAdapter(Context context, List<NotificationsEntity> notifications, AppDatabase database) {
        this.context = context;
        this.notifications = notifications;
        this.database = database;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).
                inflate(R.layout.holder_view_notifications, parent, false);
        LinearLayout master = view.findViewById(R.id.masterLayout);
        master.setBackgroundColor(ContextCompat.getColor(context, viewType));
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NotificationsEntity entity = notifications.get(position);
        Arbitrage arb = new Gson().fromJson(entity.getMessage(), Arbitrage.class);
        String translate = String.format("There is a percent difference between exchanges" +
                        " %s and %s with the coin pairing: %s",
                arb.getFirst().getExchange(), arb.getSecond().getExchange(), arb.getFirst().getCoin_short());
        holder.alert.setText("Arbitrage Alert!!!!");
        holder.description.setText(translate);
        holder.exchange1.setText(arb.getFirst().getExchange());
        holder.exchange2.setText(arb.getSecond().getExchange());
        holder.market1.setText(arb.getFirst().getMarket_short());
        holder.market2.setText(arb.getSecond().getMarket_short());
        holder.price1.setText(String.valueOf(arb.getFirst().getPrice()));
        holder.price2.setText(String.valueOf(arb.getSecond().getPrice()));
        holder.coin1.setText(arb.getFirst().getCoin_short());
        holder.coin2.setText(arb.getSecond().getCoin_short());
        long date = Long.parseLong(entity.getTimeStamp());
        Date date1 = new Date();
        date1.setTime(date);
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy, hh:mm");
        holder.timestamp.setText(formatter.format(date1));
        holder.masterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entity.setChecked(true);
                database.notificationsDao().update(entity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public void addAll(List<NotificationsEntity> newNotifs) {
        notifications.clear();
        notifications.addAll(newNotifs);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        NotificationsEntity notificationsEntity = notifications.get(position);
        if(notificationsEntity.isChecked()) {
            return android.R.color.white;
        }
        else {
            return android.R.color.holo_blue_light;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.alert)
        TextView alert;

        @BindView(R.id.description)
        TextView description;

        @BindView(R.id.exchange1)
        TextView exchange1;

        @BindView(R.id.exchange2)
        TextView exchange2;

        @BindView(R.id.market1)
        TextView market1;

        @BindView(R.id.market2)
        TextView market2;

        @BindView(R.id.price1)
        TextView price1;

        @BindView(R.id.price2)
        TextView price2;

        @BindView(R.id.coin1)
        TextView coin1;

        @BindView(R.id.coin2)
        TextView coin2;

        @BindView(R.id.timestamp)
        TextView timestamp;

        @BindView(R.id.masterLayout)
        LinearLayout masterLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
