package com.purdue.a407.cryptodisco.Activities;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

//import com.purdue.a407.cryptodisco.Adapter.ChatRoomAdapter;
import com.purdue.a407.cryptodisco.Adapter.ChatRoomAdapter;
import com.purdue.a407.cryptodisco.App;
import com.purdue.a407.cryptodisco.Data.Entities.ChatMessageEntity;
import com.purdue.a407.cryptodisco.Data.Entities.ChatRoomEntity;
import com.purdue.a407.cryptodisco.Data.Entities.ExchangeEntity;
import com.purdue.a407.cryptodisco.Helpers.LoadingDialog;
import com.purdue.a407.cryptodisco.R;
import com.purdue.a407.cryptodisco.ViewModels.ChatMsgViewModel;
import com.purdue.a407.cryptodisco.ViewModels.ChatRoomsViewModel;
import com.purdue.a407.cryptodisco.ViewModels.ExchangesViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.Nullable;

public class ChatActivity extends AppCompatActivity {

    @Inject ViewModelProvider.Factory viewModelFactory;

    @Inject
    ChatRoomsViewModel viewModel;

    @Inject
    ChatMsgViewModel chatMsgViewModel;

    private LoadingDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room);
        ((App) getApplication()).getNetComponent().inject(this);
        ButterKnife.bind(this);
        progressDialog = LoadingDialog.create();

        viewModel.getChatroomsList().observe(this, listResponse -> {
            if(listResponse.isLoading()) {
                progressDialog.show(getSupportFragmentManager());
                return;
            }
            else
                progressDialog.cancel();

            StringBuilder stringBuilderName = new StringBuilder();
            StringBuilder stringBuilderDescription = new StringBuilder();
            StringBuilder stringBuilderID = new StringBuilder();

            for(ChatRoomEntity room: listResponse.getData()) {
                stringBuilderName.append(room.getName() + "\n");
            }

            for(ChatRoomEntity room: listResponse.getData()) {
                stringBuilderDescription.append(room.getDescription() + "\n");
            }

            for(ChatRoomEntity room: listResponse.getData()) {
                stringBuilderID.append(room.getId() + "\n");
            }

            //Toast.makeText(ChatActivity.this, stringBuilderName.toString(),Toast.LENGTH_SHORT).show();
            //Toast.makeText(ChatActivity.this, stringBuilderDescription.toString(),Toast.LENGTH_SHORT).show();
            //Toast.makeText(ChatActivity.this, stringBuilderID.toString(),Toast.LENGTH_SHORT).show();


            // display group name and description
            ListView listView = (ListView) findViewById(R.id.group_list);
            String[] groupName = stringBuilderName.toString().split("\n");
            String[] description = stringBuilderDescription.toString().split("\n");
            String[] id = stringBuilderID.toString().split("\n");

            ArrayList<ChatRoomEntity> chatRoomList = new ArrayList<>();

            for (int i = 0; i < groupName.length; i++) {
                ChatRoomEntity chatRoom = new ChatRoomEntity(description[i], groupName[i]);
                chatRoomList.add(chatRoom);
            }

            ChatRoomAdapter chatRoomAdapter = new ChatRoomAdapter(this, R.layout.room_list, chatRoomList);
            listView.setAdapter(chatRoomAdapter); // display items in group page

            // group onclick
            listView.setOnItemClickListener((adapterView, view, i, l) -> {

                //Toast.makeText(ChatActivity.this, groupName[i].toString(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChatActivity.this, MessageActivity.class);
                intent.putExtra("groupName", groupName[i]);
                intent.putExtra("groupID", id[i]);
                startActivity(intent);
            });

            //ArrayAdapter<String> groupNameAdapter = new ArrayAdapter<String>(this, R.layout.room_list, R.id.room_list_group_name, groupName);
            //ArrayAdapter<String> descriptionAdapter = new ArrayAdapter<String>(this, R.layout.room_list, R.id.room_list_description, description);

            //listView.setAdapter(groupNameAdapter);
            //listView.setAdapter(descriptionAdapter);

        });
    }

}
