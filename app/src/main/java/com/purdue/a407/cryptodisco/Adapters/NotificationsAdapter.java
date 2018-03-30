package com.purdue.a407.cryptodisco.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.messaging.RemoteMessage;
import com.purdue.a407.cryptodisco.Data.Entities.NotificationsEntity;
import com.purdue.a407.cryptodisco.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    Context context;
    List<NotificationsEntity> notifications;

    public NotificationsAdapter(Context context, List<NotificationsEntity> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).
                inflate(R.layout.holder_view_notifications, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NotificationsEntity entity = notifications.get(position);
        holder.message.setText(entity.getMessage());
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.message)
        TextView message;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
