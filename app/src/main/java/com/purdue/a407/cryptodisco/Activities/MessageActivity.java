package com.purdue.a407.cryptodisco.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.bluetooth.BluetoothClass;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.purdue.a407.cryptodisco.Adapter.ChatRoomAdapter;
import com.purdue.a407.cryptodisco.Adapter.MessageAdapter;
import com.purdue.a407.cryptodisco.Adapters.ChatMessageAdapter;
import com.purdue.a407.cryptodisco.Api.CDApi;
import com.purdue.a407.cryptodisco.App;
import com.purdue.a407.cryptodisco.Data.AppDatabase;
import com.purdue.a407.cryptodisco.Data.Entities.ChatMessageEntity;
import com.purdue.a407.cryptodisco.Data.Entities.ChatRoomEntity;
import com.purdue.a407.cryptodisco.Helpers.DeviceID;
import com.purdue.a407.cryptodisco.Helpers.LoadingDialog;
import com.purdue.a407.cryptodisco.R;
import com.purdue.a407.cryptodisco.ViewModels.ChatMsgViewModel;
import com.purdue.a407.cryptodisco.ViewModels.ChatRoomsViewModel;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MessageActivity extends AppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    AppDatabase appDatabase;
    @Inject
    DeviceID deviceID;

    @Inject
    ChatRoomsViewModel viewModel;

    @Inject
    ChatMsgViewModel chatMsgViewModel;

    @Inject
    CDApi cdApi;

    // declare variables

    @BindView(R.id.chat_messages)
    RecyclerView messages;

    @BindView(R.id.title)
    TextView chatName;



    ChatMessageAdapter chatMessageAdapter;

    ChatRoomEntity entity;



    @OnClick(R.id.enteredMessage)
    public void onEntered() {
        messages.scrollToPosition(chatMessageAdapter.getItemCount() - 1);
    }

    @OnClick(R.id.topBackButton)
    public void onBack() {
        onBackPressed();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_page);
        ButterKnife.bind(this);
        //You need this to connect to the database
        ((App) getApplication()).getNetComponent().inject(this);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        messages.setLayoutManager(manager);
        chatMessageAdapter = new ChatMessageAdapter(getApplicationContext(), new ArrayList<>(), deviceID.getDeviceID());
        messages.setAdapter(chatMessageAdapter);
        entity = new Gson().fromJson(getIntent().getStringExtra("group"), ChatRoomEntity.class);
        chatName.setText(entity.getDescription());

        appDatabase.chatmsgDao().chatMessages(entity.getId()).observe(this, new Observer<List<ChatMessageEntity>>() {
            @Override
            public void onChanged(@Nullable List<ChatMessageEntity> chatMessageEntities) {
                Log.d("OBSERVED CHANGE!!!", "INSIDE CHAT MESSAGES");
                chatMessageAdapter.addAll(chatMessageEntities);
                if(chatMessageAdapter.getItemCount() > 0)
                    messages.smoothScrollToPosition(chatMessageAdapter.getItemCount() - 1);
            }
        });


        cdApi.getChatMessages(String.valueOf(entity.getId())).enqueue(new Callback<List<ChatMessageEntity>>() {
            @Override
            public void onResponse(Call<List<ChatMessageEntity>> call, Response<List<ChatMessageEntity>> response) {
                if(response.code() != 200) {
                    Log.d("RESULT: ERROR", String.valueOf(response.code()));
                    return;
                }
                Log.d("RESULT: SUCCESS", response.body().toString());
                appDatabase.chatmsgDao().saveAll(response.body());
                if(chatMessageAdapter.getItemCount() > 0)
                    messages.scrollToPosition(chatMessageAdapter.getItemCount() - 1);
            }

            @Override
            public void onFailure(Call<List<ChatMessageEntity>> call, Throwable t) {
                Log.d("RESULT: FAILURE", t.getLocalizedMessage());
            }
        });

        KeyboardVisibilityEvent.setEventListener(this, isOpen -> {
            Log.d("Keyboard", "Keyboard has changed visibility");
            if(chatMessageAdapter.getItemCount() > 0)
                messages.smoothScrollToPosition(chatMessageAdapter.getItemCount() - 1);
        });

        // get input text upon clicking the send button
        ImageButton sendMessage = (ImageButton) findViewById(R.id.send_message);
        EditText enteredMessage = (EditText) findViewById(R.id.enteredMessage);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = enteredMessage.getText().toString();
                enteredMessage.setText("");

                if (message.length() == 0) {
                    Toast.makeText(MessageActivity.this, "message cannot be empty", Toast.LENGTH_LONG).show();
                } else {
                    ChatMessageEntity msg = new ChatMessageEntity(message, deviceID.getDeviceID(), deviceID.getDeviceID(),
                            entity.getId());
                    cdApi.sendMessage(msg).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(response.code() != 200) {
                                Log.d("SENDING MESSAGE", "ERROR");
                            }
                            else {
                                Log.d("SENDING MESSAGE", "SUCCESS");
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.d("SENDING MESSAGE", "FAILURE");
                        }
                    });
//                    appDatabase.chatmsgDao().insert(msg);
                }
                //This is just expanded to test for response codes
//                    cdApi.sendMessage(msg).enqueue(new Callback<Void>() {
//                        @Override
//                        public void onResponse(Call<Void> call, Response<Void> response) {
//                            if (response.code() != 200) {
//                                // Error
//                                Log.d("ChatResult", String.valueOf(response.code()));
//                            } else {
//                                // Success
//                                Log.d("ChatResult", "Success");
//                                appDatabase.chatmsgDao().insert(msg);
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<Void> call, Throwable t) {
//                            // Failure
//                            Log.d("ChatResult", "Failure");
//                            //Intent myIntent = new Intent(MessageActivity.this, HomeActivity.class);
//                            //startActivity(myIntent);
//                        }
//                    });

            }
        });
    }
}
