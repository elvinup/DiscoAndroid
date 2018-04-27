package com.purdue.a407.cryptodisco.Activities;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import com.purdue.a407.cryptodisco.Adapter.ChatRoomAdapter;
import com.google.gson.Gson;
import com.purdue.a407.cryptodisco.Adapter.ChatRoomAdapter;
import com.purdue.a407.cryptodisco.Api.CDApi;
import com.purdue.a407.cryptodisco.App;
import com.purdue.a407.cryptodisco.Data.Entities.ChatMessageEntity;
import com.purdue.a407.cryptodisco.Data.Entities.ChatRoomEntity;
import com.purdue.a407.cryptodisco.Data.Entities.ExchangeEntity;
import com.purdue.a407.cryptodisco.Helpers.DeviceID;
import com.purdue.a407.cryptodisco.Helpers.LoadingDialog;
import com.purdue.a407.cryptodisco.R;
import com.purdue.a407.cryptodisco.ViewModels.ChatMsgViewModel;
import com.purdue.a407.cryptodisco.ViewModels.ChatRoomsViewModel;
import com.purdue.a407.cryptodisco.ViewModels.ExchangesViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.annotations.Nullable;

public class ChatActivity extends AppCompatActivity {

    @Inject ViewModelProvider.Factory viewModelFactory;

    @Inject
    ChatRoomsViewModel viewModel;

    @Inject
    ChatMsgViewModel chatMsgViewModel;

    @Inject
    CDApi cdApi;

    @Inject
    DeviceID deviceID;

    private LoadingDialog progressDialog;

    @BindView(R.id.group_list)
    RecyclerView chatRooms;

    @BindView(R.id.title)
    TextView title;


    ChatRoomAdapter adapter;

    @OnClick(R.id.topBackButton)
    public void onBack() {
        onBackPressed();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room);
        ButterKnife.bind(this);
        ((App) getApplication()).getNetComponent().inject(this);
        title.setText("Chatrooms");
        progressDialog = LoadingDialog.create();
        adapter = new ChatRoomAdapter(getApplicationContext(), new ArrayList<>(), cdApi, deviceID);
        chatRooms.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        chatRooms.setAdapter(adapter);
        viewModel.getChatroomsList().observe(this, listResponse -> {
            if(listResponse.isLoading()) {
                progressDialog.show(getSupportFragmentManager());
                return;
            }
            else
                progressDialog.cancel();

            adapter.addAll(listResponse.getData());

        });
    }

}
