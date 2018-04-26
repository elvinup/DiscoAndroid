package com.purdue.a407.cryptodisco.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.purdue.a407.cryptodisco.Activities.MessageActivity;
import com.purdue.a407.cryptodisco.Api.CDApi;
import com.purdue.a407.cryptodisco.Data.Entities.ChatJoin;
import com.purdue.a407.cryptodisco.Data.Entities.ChatRoomEntity;
import com.purdue.a407.cryptodisco.Helpers.DeviceID;
import com.purdue.a407.cryptodisco.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Interceptor;
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
    Map<String, Integer> mapImages;


    public ChatRoomAdapter(@NonNull Context context, List<ChatRoomEntity> objects,
                           CDApi api, DeviceID deviceID) {
        this.mContext = context;
        this.objects = objects;
        this.cdApi = api;
        this.deviceID = deviceID;
        setUpMap();
    }

    public void setUpMap() {
        mapImages = new HashMap<>();
        mapImages.put("BTC", 1);
        mapImages.put("ETH", 1027);
        mapImages.put("XRP", 52);
        mapImages.put("BCH",1831);
        mapImages.put("LTC", 2);
        mapImages.put("ADA", 2010);
        mapImages.put("NEO", 1376);
        mapImages.put("XLM", 512);
        mapImages.put("EOS", 1765);
        mapImages.put("DASH", 131);
        mapImages.put("MIOTA", 1720);
        mapImages.put("XMR", 328);
        mapImages.put("XEM", 873);
        mapImages.put("ETC", 1321);
        mapImages.put("VEN", 1904);
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
        String base = "https://s2.coinmarketcap.com/static/img/coins/64x64/";
        Integer id = mapImages.get(entity.getName());
        base += String.valueOf(id);
        base += ".png";
        Picasso.get()
                .load(base)
                .centerCrop()
                .resize(50,50)
                .error(R.drawable.emoji_1f30f)
                .into(holder.circleImageView);
        holder.chatRoom.setText(entity.getName());
        holder.chatRoomDesc.setText(entity.getDescription());
        holder.layout.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, MessageActivity.class);
            intent.putExtra("group", new Gson().toJson(entity));
            mContext.startActivity(intent);
        });
        holder.layout.setOnLongClickListener(view -> {
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

        @BindView(R.id.chatRoomDesc)
        TextView chatRoomDesc;

        CircleImageView circleImageView;

        @BindView(R.id.cv)
        LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            circleImageView = itemView.findViewById(R.id.imageView);
        }
    }
}
