package com.purdue.a407.cryptodisco.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.purdue.a407.cryptodisco.Activities.ChatActivity;
import com.purdue.a407.cryptodisco.Activities.MessageActivity;
import com.purdue.a407.cryptodisco.Adapters.TradeHistoryAdapter;
import com.purdue.a407.cryptodisco.Api.CDApi;
import com.purdue.a407.cryptodisco.Data.Entities.ChatJoin;
import com.purdue.a407.cryptodisco.Data.Entities.ChatRoomEntity;
import com.purdue.a407.cryptodisco.Helpers.DeviceID;
import com.purdue.a407.cryptodisco.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Kenny on 2/20/2018.
 */

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ViewHolder> {

    private static final String TAG = "ChatRoomListAdapter";
    private Context mContext;
    CDApi cdApi;
    DeviceID deviceID;
    List<ChatRoomEntity> objects;

    public ChatRoomAdapter(@NonNull Context context, List<ChatRoomEntity> objects,
                           CDApi api, DeviceID deviceID) {
        this.mContext = context;
        this.objects = objects;
        this.cdApi = api;
        this.deviceID = deviceID;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).
                inflate(R.layout.holder_chat_rooms, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChatRoomEntity entity = objects.get(position);
        holder.chatRoom.setText(entity.getName());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("group", new Gson().toJson(entity));
                mContext.startActivity(intent);
            }
        });

        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                cdApi.joinChat(new ChatJoin(deviceID.getDeviceID(),
                        String.valueOf(entity.getId()))).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code() != 200) {
                            Toast.makeText(mContext, "There was an error in subscribing to this chat: " +
                                            String.valueOf(response.code()),
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(mContext, "You have successfully subscribed to this chat",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(mContext, "Unable to connect to the servers",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public void addAll(List<ChatRoomEntity> newEntities) {
        objects.clear();
        objects.addAll(newEntities);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.chatRoom)
        TextView chatRoom;

        @BindView(R.id.cv)
        LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
