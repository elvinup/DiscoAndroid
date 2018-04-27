package com.purdue.a407.cryptodisco.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.purdue.a407.cryptodisco.Data.Entities.ChatMessageEntity;
import com.purdue.a407.cryptodisco.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<ChatMessageEntity> messages;
    String deviceId;

    public ChatMessageAdapter(Context context, List<ChatMessageEntity> messages, String deviceId) {
        this.context = context;
        this.messages = messages;
        this.deviceId = deviceId;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        if(viewType == R.layout.holder_item_message_send) {
            return new SendViewHolder(view);
        }
        else
            return new ReceiveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof SendViewHolder) {
            ((SendViewHolder)holder).bind(messages.get(position));
        }
        else if(holder instanceof ReceiveViewHolder) {
            ((ReceiveViewHolder)holder).bind(messages.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addAll(List<ChatMessageEntity> newMessages) {
        messages.clear();
        messages.addAll(newMessages);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessageEntity entity = messages.get(position);
        if(entity.getUid().equals(deviceId)) {
            return R.layout.holder_item_message_send;
        }
        else
            return R.layout.holder_item_message_receive;
    }

    public class SendViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.message)
        TextView message;

        public SendViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(ChatMessageEntity entity) {
            this.message.setText(entity.getMessage());
        }
    }

    public class ReceiveViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.user)
        TextView user;

        @BindView(R.id.message)
        TextView message;

        public ReceiveViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(ChatMessageEntity entity) {
            this.user.setText(entity.getNickname());
            this.message.setText(entity.getMessage());
        }
    }

}
