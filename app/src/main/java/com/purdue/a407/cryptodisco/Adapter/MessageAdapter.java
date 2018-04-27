package com.purdue.a407.cryptodisco.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.purdue.a407.cryptodisco.Data.Entities.ChatMessageEntity;
import com.purdue.a407.cryptodisco.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kenny on 2/22/2018.
 */

public class MessageAdapter extends ArrayAdapter<ChatMessageEntity> {

    private static final String TAG = "MessageListAdapter";
    private Context mContext;
    int mResource;
    Map<String, Integer> mapImages;

    public MessageAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ChatMessageEntity> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String message = getItem(position).getMessage();
        String nickname = getItem(position).getNickname();

        ChatMessageEntity ChatMessage = new ChatMessageEntity(message,nickname);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView messageTextView = (TextView) convertView.findViewById(R.id.text_message);
        TextView nicknameTextView = (TextView) convertView.findViewById(R.id.nick_name);

        messageTextView.setText(message);
        nicknameTextView.setText(nickname);

        return convertView;
    }
}