package com.purdue.a407.cryptodisco.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.purdue.a407.cryptodisco.Activities.ChatActivity;
import com.purdue.a407.cryptodisco.Activities.MessageActivity;
import com.purdue.a407.cryptodisco.Api.CDApi;
import com.purdue.a407.cryptodisco.Data.Entities.ChatJoin;
import com.purdue.a407.cryptodisco.Data.Entities.ChatRoomEntity;
import com.purdue.a407.cryptodisco.Helpers.DeviceID;
import com.purdue.a407.cryptodisco.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Kenny on 2/20/2018.
 */

public class ChatRoomAdapter extends ArrayAdapter<ChatRoomEntity>{

    private static final String TAG = "ChatRoomListAdapter";
    private Context mContext;
    int mResource;
    CDApi cdApi;
    DeviceID deviceID;

    public ChatRoomAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ChatRoomEntity> objects,
                           CDApi api, DeviceID deviceID) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
        this.cdApi = api;
        this.deviceID = deviceID;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String groupName = getItem(position).getName();
        String description = getItem(position).getDescription();
        int id = getItem(position).getId();

        ChatRoomEntity ChatRoom = new ChatRoomEntity(groupName, description);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView groupText = (TextView) convertView.findViewById(R.id.room_list_group_name);
        TextView descriptionText = (TextView) convertView.findViewById(R.id.room_list_description);

        groupText.setText(groupName);
        descriptionText.setText(description);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("groupName", groupName);
                intent.putExtra("groupID", "0");
                mContext.startActivity(intent);
            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                cdApi.joinChat(new ChatJoin(deviceID.getDeviceID(),
                        String.valueOf(ChatRoom.getId()))).enqueue(new Callback<Void>() {
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

        return convertView;
    }
}
