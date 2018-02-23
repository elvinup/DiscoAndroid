package com.purdue.a407.cryptodisco.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.purdue.a407.cryptodisco.Data.Entities.ChatRoomEntity;
import com.purdue.a407.cryptodisco.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kenny on 2/20/2018.
 */

public class ChatRoomAdapter extends ArrayAdapter<ChatRoomEntity>{

    private static final String TAG = "ChatRoomListAdapter";
    private Context mContext;
    int mResource;

    public ChatRoomAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ChatRoomEntity> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String groupName = getItem(position).getName();
        String description = getItem(position).getDescription();

        ChatRoomEntity ChatRoom = new ChatRoomEntity(groupName, description);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView groupText = (TextView) convertView.findViewById(R.id.room_list_group_name);
        TextView descriptionText = (TextView) convertView.findViewById(R.id.room_list_description);

        groupText.setText(groupName);
        descriptionText.setText(description);

        return convertView;
    }
}
