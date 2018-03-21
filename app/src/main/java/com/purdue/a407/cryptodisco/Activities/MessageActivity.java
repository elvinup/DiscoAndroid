package com.purdue.a407.cryptodisco.Activities;

import android.arch.lifecycle.ViewModelProvider;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.purdue.a407.cryptodisco.Adapter.MessageAdapter;
import com.purdue.a407.cryptodisco.Api.CDApi;
import com.purdue.a407.cryptodisco.App;
import com.purdue.a407.cryptodisco.Data.Entities.ChatMessageEntity;
import com.purdue.a407.cryptodisco.Helpers.LoadingDialog;
import com.purdue.a407.cryptodisco.R;
import com.purdue.a407.cryptodisco.ViewModels.ChatMsgViewModel;
import com.purdue.a407.cryptodisco.ViewModels.ChatRoomsViewModel;

import java.util.ArrayList;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MessageActivity extends AppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;


    @Inject
    ChatRoomsViewModel viewModel;

    @Inject
    ChatMsgViewModel chatMsgViewModel;

    @Inject
    CDApi cdApi;

    // declare variables
    private LoadingDialog progressDialog;
    private static StringBuilder stringBuilderMessage;
    private static StringBuilder stringBuilderNickname;
    public static StringBuilder stringBuilderRoomID;

    private static StringBuilder messageStringBuilder;
    private static StringBuilder nicknameStringBuilder;

    private static ArrayList<ChatMessageEntity> chatMessageList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_page);

        //You need this to connect to the database
        ((App) getApplication()).getNetComponent().inject(this);


        //Just testing to see if we can retrieve the chat messages
        chatMsgViewModel.getChatmessagesList().observe(this, listResponse -> {
            if (listResponse.isLoading()) {
                //progressDialog.show(getSupportFragmentManager());
                return;
            }

            stringBuilderMessage = new StringBuilder();
            stringBuilderNickname = new StringBuilder();
            stringBuilderRoomID = new StringBuilder();

            for (ChatMessageEntity msg : listResponse.getData()) {
                stringBuilderRoomID.append(msg.getChatroom_id() + "\n");
            }

            for (ChatMessageEntity msg : listResponse.getData()) {
                stringBuilderMessage.append(msg.getMessage() + "\n");
            }

            for (ChatMessageEntity msg : listResponse.getData()) {
                stringBuilderNickname.append(msg.getNickname() + "\n");
            }


            //Toast.makeText(MessageActivity.this, stringBuilderMessage.toString(), Toast.LENGTH_SHORT).show();
            //Toast.makeText(MessageActivity.this, stringBuilderNickname.toString(), Toast.LENGTH_SHORT).show();
            //Toast.makeText(MessageActivity.this, stringBuilderRoomID.toString(), Toast.LENGTH_SHORT).show();

            // display message and id
            ListView listView = (ListView) findViewById(R.id.chat_messages);
            String[] fullMessage = stringBuilderMessage.toString().split("\n");
            String[] fullNickname = stringBuilderNickname.toString().split("\n");
            String[] roomID = stringBuilderRoomID.toString().split("\n");

            // variables from ChatActivity.java
            String groupName = getIntent().getStringExtra("groupName");
            String groupID = getIntent().getStringExtra("groupID");

            chatMessageList = new ArrayList<>();

            messageStringBuilder = new StringBuilder();
            nicknameStringBuilder = new StringBuilder();

            for (int i = 0; i < fullMessage.length; i++) {
                if (roomID[i].equals(groupID)) {
                    messageStringBuilder.append(fullMessage[i] + "\n");
                    nicknameStringBuilder.append(fullNickname[i] + "\n");
                }
            }

            String[] message = messageStringBuilder.toString().split("\n");
            String[] nickname = nicknameStringBuilder.toString().split("\n");

            for (int i = 0; i < message.length; i++) {
                ChatMessageEntity messageEntity = new ChatMessageEntity(message[i], nickname[i]);
                chatMessageList.add(messageEntity);
            }

            MessageAdapter messageAdapter = new MessageAdapter(this, R.layout.display_message, chatMessageList);
            listView.setAdapter(messageAdapter);

            //Toast.makeText(MessageActivity.this, messageStringBuilder.toString(),Toast.LENGTH_SHORT).show();

            // set groupName in message page
            TextView groupNameTextView = (TextView) findViewById(R.id.groupNameChatPage);
            groupNameTextView.setTextColor(Color.WHITE);
            groupNameTextView.setText(groupName);

        });

        // send message

        // auto generate random 4 digits number as Uid
        Random rand = new Random();

        int randomUid = rand.nextInt(9000) + 1000;
        int randomNickname = rand.nextInt(90000) + 10000; // generate random 5 digits number for nickname for now

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
                    // sending message with message, randomUid, randomNickname to groupID
                    ChatMessageEntity msg = new ChatMessageEntity(message, "" + randomUid, "" +
                            randomNickname, Integer.parseInt(getIntent().getStringExtra("groupID")));


                    //This is just expanded to test for response codes
                    cdApi.sendMessage(msg).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code() != 200) {
                                // Error
                                Log.d("ChatResult", String.valueOf(response.code()));
                                //Intent myIntent = new Intent(MessageActivity.this, HomeActivity.class);
                                //startActivity(myIntent);
                            } else {
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

                    // update new message

                    //Toast.makeText(MessageActivity.this, stringBuilderMessage.toString(), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(MessageActivity.this, messageStringBuilder.toString(), Toast.LENGTH_SHORT).show();

                    chatMessageList.add(new ChatMessageEntity(message, "" + randomNickname));
                    MessageAdapter messageAdapter = new MessageAdapter(MessageActivity.this, R.layout.display_message, chatMessageList);
                    ListView listView = (ListView) findViewById(R.id.chat_messages);

                    listView.setAdapter(messageAdapter);

                }
            }
        });
    }
}
