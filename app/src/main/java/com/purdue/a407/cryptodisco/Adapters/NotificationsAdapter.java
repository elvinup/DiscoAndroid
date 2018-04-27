package com.purdue.a407.cryptodisco.Adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.purdue.a407.cryptodisco.Data.AppDatabase;
import com.purdue.a407.cryptodisco.Data.Entities.Arbitrage;
import com.purdue.a407.cryptodisco.Data.Entities.NotificationsEntity;
import com.purdue.a407.cryptodisco.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ARB_CHECKED = 0;
    private static final int ARB_NOT_CHECKED = 1;
    private static final int TRAIL_CHECKED = 2;
    private static final int TRAIL_NOT_CHECKED = 3;

    Context context;
    List<NotificationsEntity> notifications;
    AppDatabase database;

    public NotificationsAdapter(Context context, List<NotificationsEntity> notifications, AppDatabase database) {
        this.context = context;
        this.notifications = notifications;
        this.database = database;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == ARB_CHECKED || viewType == ARB_NOT_CHECKED) {
            View view = LayoutInflater.from(context).
                    inflate(R.layout.holder_view_notifications_arb, parent, false);
            LinearLayout master = view.findViewById(R.id.masterLayout);
            if(viewType == ARB_CHECKED)
                master.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
            else if(viewType == ARB_NOT_CHECKED)
                master.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_blue_light));
            ArbViewHolder viewHolder = new ArbViewHolder(view);
            return viewHolder;
        }
        else if(viewType == TRAIL_CHECKED || viewType == TRAIL_NOT_CHECKED) {
            View view = LayoutInflater.from(context).
                    inflate(R.layout.holder_notification_trail_stop, parent, false);
            LinearLayout master = view.findViewById(R.id.masterLayout);
            if(viewType == TRAIL_CHECKED)
                master.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
            else if(viewType == TRAIL_NOT_CHECKED)
                master.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_blue_light));
            TSViewHolder viewHolder = new TSViewHolder(view);
            return viewHolder;
        }
        else {
            View view = LayoutInflater.from(context).
                inflate(R.layout.holder_notification_trail_stop, parent, false);
            TSViewHolder viewHolder = new TSViewHolder(view);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NotificationsEntity entity = notifications.get(position);
        if(holder instanceof ArbViewHolder)
            ((ArbViewHolder)holder).bind(entity);
        else if(holder instanceof TSViewHolder)
            ((TSViewHolder)holder).bind(entity);

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
        if(isJson(notificationsEntity.getMessage())) {
            if(notificationsEntity.isChecked()) {
                return ARB_CHECKED;
            }
            else {
                return ARB_NOT_CHECKED;
            }
        }
        else {
            if(notificationsEntity.isChecked()) {
                return TRAIL_CHECKED;
            }
            else {
                return TRAIL_NOT_CHECKED;
            }
        }
    }

    private boolean isJson(String json) {
        try {
            new JSONObject(json);
        } catch (JSONException e) {
            try {
                new JSONArray(json);
            } catch (JSONException e1) {
                return false;
            }
        }
        return true;
    }

    public class TSViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.timestamp)
        TextView timestamp;

        @BindView(R.id.description)
        TextView description;

        @BindView(R.id.masterLayout)
        LinearLayout masterLayout;

        public TSViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(NotificationsEntity entity) {
            long date = Long.parseLong(entity.getTimeStamp());
            Date date1 = new Date();
            date1.setTime(date);
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy, hh:mm");
            description.setText(entity.getMessage());
            timestamp.setText(formatter.format(date1));
            masterLayout.setOnClickListener(view -> {
                entity.setChecked(true);
                database.notificationsDao().update(entity);
            });
        }
    }
    public class ArbViewHolder extends RecyclerView.ViewHolder {

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


        public ArbViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(NotificationsEntity entity) {
            Arbitrage arb = new Gson().fromJson(entity.getMessage(), Arbitrage.class);
            String translate = String.format("There is a percent difference between exchanges" +
                            " %s and %s with the coin pairing: %s",
                    arb.getFirst().getExchange(), arb.getSecond().getExchange(), arb.getFirst().getCoin_short());
            alert.setText("Arbitrage Alert!!!!");
            description.setText(translate);
            exchange1.setText(arb.getFirst().getExchange());
            exchange2.setText(arb.getSecond().getExchange());
            market1.setText(arb.getFirst().getMarket_short());
            market2.setText(arb.getSecond().getMarket_short());
            price1.setText(String.valueOf(arb.getFirst().getPrice()));
            price2.setText(String.valueOf(arb.getSecond().getPrice()));
            coin1.setText(arb.getFirst().getCoin_short());
            coin2.setText(arb.getSecond().getCoin_short());
            long date = Long.parseLong(entity.getTimeStamp());
            Date date1 = new Date();
            date1.setTime(date);
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy, hh:mm");
            timestamp.setText(formatter.format(date1));
            masterLayout.setOnClickListener(view -> {
                entity.setChecked(true);
                database.notificationsDao().update(entity);
            });
        }
    }

}
