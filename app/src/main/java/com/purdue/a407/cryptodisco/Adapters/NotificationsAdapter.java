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
        Arbitrage arbitrage = new Gson().fromJson(entity.getMessage(), Arbitrage.class);
        holder.message.setText(entity.getMessage());
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

        @BindView(R.id.message)
        TextView message;

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
