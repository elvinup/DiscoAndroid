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

public class ChatActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    @Inject ViewModelProvider.Factory viewModelFactory;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.chatrooms_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

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
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
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

            for(ChatRoomEntity room: listResponse.getData()) {
                stringBuilderName.append(room.getName() + "\n");
            }

            for(ChatRoomEntity room: listResponse.getData()) {
                stringBuilderDescription.append(room.getDescription() + "\n");
            }

            //Toast.makeText(ChatActivity.this, stringBuilderName.toString(),Toast.LENGTH_SHORT).show();
            //Toast.makeText(ChatActivity.this, stringBuilderDescription.toString(),Toast.LENGTH_SHORT).show();


            // display group name and description
            ListView listView = (ListView) findViewById(R.id.group_list);
            String[] groupName = stringBuilderName.toString().split("\n");
            String[] description = stringBuilderDescription.toString().split("\n");
            ArrayList<ChatRoomEntity> chatRoomList = new ArrayList<>();

            for (int i = 0; i < groupName.length; i++) {
                ChatRoomEntity chatRoom = new ChatRoomEntity(description[i], groupName[i]);
                chatRoomList.add(chatRoom);
            }

            ChatRoomAdapter chatRoomAdapter = new ChatRoomAdapter(this, R.layout.room_list, chatRoomList);
            listView.setAdapter(chatRoomAdapter); // display items in group page

            // group onclick
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(ChatActivity.this, MessageActivity.class);
                    startActivity(intent);
                }
            });

            //ArrayAdapter<String> groupNameAdapter = new ArrayAdapter<String>(this, R.layout.room_list, R.id.room_list_group_name, groupName);
            //ArrayAdapter<String> descriptionAdapter = new ArrayAdapter<String>(this, R.layout.room_list, R.id.room_list_description, description);

            //listView.setAdapter(groupNameAdapter);
            //listView.setAdapter(descriptionAdapter);

        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_chat) {

        } else if (id == R.id.nav_settings) {

        }
        return true;
    }
}
