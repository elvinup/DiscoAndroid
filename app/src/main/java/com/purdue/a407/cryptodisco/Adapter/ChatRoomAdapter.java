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
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.gson.Gson;
import com.purdue.a407.cryptodisco.Activities.MessageActivity;
import com.purdue.a407.cryptodisco.Api.CDApi;
import com.purdue.a407.cryptodisco.Data.Entities.ChatJoin;
import com.purdue.a407.cryptodisco.Data.Entities.ChatRoomEntity;
import com.purdue.a407.cryptodisco.DependencyInjection.Modules.GlideApp;
import com.purdue.a407.cryptodisco.Helpers.DeviceID;
import com.purdue.a407.cryptodisco.R;
import com.purdue.a407.cryptodisco.Svg.SvgSoftwareLayerSetter;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
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

    RequestBuilder<PictureDrawable> requestBuilder;

    public ChatRoomAdapter(@NonNull Context context, List<ChatRoomEntity> objects,
                           CDApi api, DeviceID deviceID) {
        this.mContext = context;
        this.objects = objects;
        this.cdApi = api;
        this.deviceID = deviceID;
        setSvgBuilder();
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
        String base = "https://cdn.worldvectorlogo.com/logos/";
        String desc = entity.getDescription();
        desc.replaceAll(" ", "-");
        base += desc.toLowerCase() + ".svg";
        Uri uri = Uri.parse(base);
        Log.d("Base String", base);
        requestBuilder.load(uri).into(holder.circleImageView);
        holder.chatRoom.setText(entity.getName());
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

    public void setSvgBuilder() {
        requestBuilder = GlideApp.with(mContext)
                .as(PictureDrawable.class)
                .placeholder(R.drawable.emoji_00a9)
                .error(R.drawable.emoji_00a9)
                .transition(DrawableTransitionOptions.withCrossFade())
                .listener(new SvgSoftwareLayerSetter());

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
