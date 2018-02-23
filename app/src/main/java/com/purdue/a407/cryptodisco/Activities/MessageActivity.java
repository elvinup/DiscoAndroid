package com.purdue.a407.cryptodisco.Activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

//import com.purdue.a407.cryptodisco.Adapter.ChatRoomAdapter;
import com.purdue.a407.cryptodisco.Adapter.ChatRoomAdapter;
import com.purdue.a407.cryptodisco.Adapter.MessageAdapter;
import com.purdue.a407.cryptodisco.Api.CDApi;
import com.purdue.a407.cryptodisco.App;
import com.purdue.a407.cryptodisco.CacheData.CDResource;
import com.purdue.a407.cryptodisco.Data.Entities.ChatMessageEntity;
import com.purdue.a407.cryptodisco.Data.Entities.ChatRoomEntity;
import com.purdue.a407.cryptodisco.Data.Entities.ExchangeEntity;
import com.purdue.a407.cryptodisco.Helpers.LoadingDialog;
import com.purdue.a407.cryptodisco.R;
import com.purdue.a407.cryptodisco.Repos.ChatMsgRepository;
import com.purdue.a407.cryptodisco.ViewModels.ChatMsgViewModel;
import com.purdue.a407.cryptodisco.ViewModels.ChatRoomsViewModel;
import com.purdue.a407.cryptodisco.ViewModels.ExchangesViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.Nullable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {

//    @Inject ViewModelProvider.Factory viewModelFactory;


    @Inject
    ChatRoomsViewModel viewModel;

    @Inject
    ChatMsgViewModel chatMsgViewModel;

    @Inject
    CDApi cdApi;

    private LoadingDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_page);

        //You need this to connect to the database
        ((App) getApplication()).getNetComponent().inject(this);

        //Just testing to see if we can retrieve the chat messages
        chatMsgViewModel.getChatmessagesList().observe(this, listResponse -> {
            if(listResponse.isLoading()) {
                //progressDialog.show(getSupportFragmentManager());
                return;
            }

            StringBuilder stringBuilderMessage = new StringBuilder();
            StringBuilder stringBuilderNickname = new StringBuilder();

            for(ChatMessageEntity msg: listResponse.getData()) {
                stringBuilderMessage.append(msg.getMessage() + "\n");
            }

            for(ChatMessageEntity msg: listResponse.getData()) {
                stringBuilderNickname.append(msg.getNickname() + "\n");
            }

            //Toast.makeText(MessageActivity.this, stringBuilderMessage.toString(), Toast.LENGTH_SHORT).show();
            //Toast.makeText(MessageActivity.this, stringBuilderNickname.toString(), Toast.LENGTH_SHORT).show();

            // display message and id
            ListView listView = (ListView) findViewById(R.id.chat_messages);
            String[] message = stringBuilderMessage.toString().split("\n");
            String[] nickname = stringBuilderNickname.toString().split("\n");
            ArrayList<ChatMessageEntity> chatMessageList = new ArrayList<>();

            for (int i = 0; i < message.length; i++) {
                ChatMessageEntity messageEntity = new ChatMessageEntity(message[i], nickname[i]);
                chatMessageList.add(messageEntity);
            }

            MessageAdapter messageAdapter = new MessageAdapter(this, R.layout.display_message, chatMessageList);
            listView.setAdapter(messageAdapter); // display items in group page
        });


        //@Kenny, To send a message, just do cdApi.sendMessage(ChatMessageEntity msg);
        ChatMessageEntity msg = new ChatMessageEntity("yo", "SOMEID12345", "testMonkey", 4);

        //This is just expanded to test for response codes
        cdApi.sendMessage(msg).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() != 200) {
                    // Error
                    Log.d("ChatResult", String.valueOf(response.code()));
                    //Intent myIntent = new Intent(MessageActivity.this, HomeActivity.class);
                    //startActivity(myIntent);
                }
                else {
                    // Success
                    Log.d("ChatResult", "Success");
                    //Intent myIntent = new Intent(MessageActivity.this, HomeActivity.class);
                    //startActivity(myIntent);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Failure
                Log.d("ChatResult", "Failure");
                //Intent myIntent = new Intent(MessageActivity.this, HomeActivity.class);
                //startActivity(myIntent);
            }
        });


    }
}
