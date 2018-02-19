package com.purdue.a407.cryptodisco.Activities;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class group_chat extends AppCompatActivity implements View.OnClickListener {

    private FirebaseListAdapter<String> adapter;

    private ImageButton sendMessage;
    private ImageButton sendImage;
    //private ImageView showImage;
    private EditText enterTheMessage;
    private TextView showGroupName;
    private FirebaseDatabase mDatabase;
    private DatabaseReference groupReference;
    private DatabaseReference chartMessagesReference;
    private ListView listViewOfMessages;
    private Button joinbutton;

    private Button reportBtn;


    private Button deleteButton;
    //private String groupName;
    private String groupId;
    private String message;
    private String image;

    //Add by Frank
    private boolean MeInThisGroup = false;
    private boolean OtherInThisGroup = false;

    //private int message_count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_group_chat);
            listViewOfMessages = (ListView) findViewById(R.id.chat_messages);
            showGroupName = (TextView) findViewById(R.id.group_name);
            sendMessage = (ImageButton) findViewById(R.id.send_message);

            sendImage = (ImageButton) findViewById(R.id.add_picture);
            enterTheMessage = (EditText) findViewById(R.id.enterMessage);
            joinbutton = (Button) findViewById(R.id.joined_button);

            deleteButton = (Button) findViewById(R.id.leave_button);
            reportBtn = (Button) findViewById(R.id.groupReport);
            mDatabase = FirebaseDatabase.getInstance();

            groupReference = mDatabase.getReference().child("Group");
            chartMessagesReference = mDatabase.getReference().child("ChartMessages");
            Bundle b = getIntent().getExtras();
            groupId = b.getString("groupid");

            groupReference.child(groupId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        //GroupClass group = dataSnapshot.getValue(GroupClass.class);
                        if (dataSnapshot.child("groupName") != null) {
                            String groupName = dataSnapshot.child("groupName").getValue().toString();
                            showGroupName.setText(groupName);
                        } else {
                            Toast.makeText(group_chat.this, "cannot find groupName", Toast.LENGTH_LONG).show();
                        }
                    }catch (NullPointerException e){
                        Toast.makeText(group_chat.this, "This group is not existing anymore. Please go back.", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            //showGroupName.setText(groupName);

            final Global_variable global_variable = (Global_variable) getApplicationContext();
            final String uid = global_variable.getUser_id();
            final DatabaseReference ref = mDatabase.getReference();
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        ArrayList<String> groupIDs = (ArrayList<String>) dataSnapshot.child("Users").child(uid).child("groupIDs").getValue();
                        if (groupIDs == null)
                            return;
                        if ((Boolean) dataSnapshot.child("Group").child(groupId).child("is_permanent").getValue() && groupIDs.contains(groupId)) {
                            joinbutton.setText("permanent");
                            joinbutton.setEnabled(false);
                        } else if (groupIDs.contains(groupId)) {//Add by Group
                            MeInThisGroup = true;

                            joinbutton.setText("joined");
                            joinbutton.setEnabled(false);
                            Long start_date = (long) dataSnapshot.child("Group").child(groupId).child("date").getValue();
                            long current_time = System.currentTimeMillis();
                            long time_period = current_time - start_date;
                            double second = (double) time_period / 1000.0;
                            double hour = second / 3600;
                            if (hour >= 24)
                                Toast.makeText(group_chat.this, "This group is about to expired", Toast.LENGTH_LONG).show();
                        } else if (!groupIDs.contains(groupIDs)) {
                            deleteButton.setEnabled(false);
                        }
                    }catch (NullPointerException e){
                        Toast.makeText(group_chat.this, "This group is not existing anymore. Please go back.", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            adapter = new FirebaseListAdapter<String>(group_chat.this, String.class,
                    R.layout.activity_display_messages, groupReference.child(groupId).child("messageId")) {
                @Override
                protected void populateView(View v, final String model, final int position) {

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    //v = getLayoutInflater().inflate(R.layout.activity_display_messages,null);
                    final View v1 = v;
                    final String model_1 = model;
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            TextView showMessage = (TextView) v1.findViewById(R.id.text_message);
                            ImageView showImage = (ImageView) v1.findViewById(R.id.send_image);
                            showMessage.setVisibility(View.GONE);
                            showImage.setVisibility(View.GONE);

                            /*SHOW USER AVATAR(FRANK)*/
                            //TODO

                            if (dataSnapshot.child("ChartMessages").getValue() != null) {
                                String messageId = dataSnapshot.child("ChartMessages").child(model_1).child("messageKey").getValue().toString();
                                String userId = dataSnapshot.child("ChartMessages").child(model_1).child("uid").getValue().toString();
                                String message = null;
                                String imageString = null;
                                String nickName;
                                String landing_imgStr = null;

                                if (dataSnapshot.child("Users").child(userId).child("nickName").getValue() == null) {
                                    nickName = "anonymous";
                                } else {
                                    nickName = dataSnapshot.child("Users").child(userId).child("nickName").getValue().toString()+ ":";
                                    //landing_imgStr = dataSnapshot.child(userId).child("imgStr").getValue().toString();

                                    if (landing_imgStr == null){
                                        System.out.println(nickName + "str == null");
                                    }
                                }

                                if (dataSnapshot.child("ChartMessages").child(model_1).child("message").getValue() != null) {
                                    message = dataSnapshot.child("ChartMessages").child(model_1).child("message").getValue().toString();
                                    //TextView showMessage = (TextView) v1.findViewById(R.id.text_message);
                                    showMessage.setVisibility(View.VISIBLE);
                                    showMessage.setText(message + "\n");
                                    //showMessage.setVisibility(View.VISIBLE);
                                    //ImageView showImage = (ImageView) v1.findViewById(R.id.send_image);
                                    //((ViewGroup) v1).removeView(showImage);
                                    //showImage.setVisibility(View.GONE);
                                } else {
                                    //TextView showMessage = (TextView) v1.findViewById(R.id.text_message);
                                    //showMessage.setVisibility(View.GONE);
                                    //((ViewGroup) v1).removeView(showMessage);
                                }

                                if (dataSnapshot.child("ChartMessages").child(model_1).child("image").getValue() != null) {
                                    imageString = dataSnapshot.child("ChartMessages").child(model_1).child("image").getValue().toString();
                                    byte[] imageByte = Base64.decode(imageString, Base64.DEFAULT);
                                    BitmapFactory.Options opt = new BitmapFactory.Options();
                                    opt.inSampleSize = 1;
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length, opt);
                                    //ImageView showImage = (ImageView) v1.findViewById(R.id.send_image);
                                    showImage.setVisibility(View.VISIBLE);
                                    showImage.setImageBitmap(bitmap);
                                    //showImage.setVisibility(View.VISIBLE);
                                    //TextView showMessage = (TextView) v1.findViewById(R.id.text_message);
                                    //showMessage.setVisibility(View.GONE);
                                    //((ViewGroup) v1).removeView(showMessage);
                                } else {
                                    //ImageView showImage = (ImageView) v1.findViewById(R.id.send_image);
                                    //((ViewGroup) v1).removeView(showImage);
                                    //((ViewGroup)v1).addView();

                                }
                            /*String nickName;
                            if (dataSnapshot.child("Users").child(userId).child("nickName").getValue() == null) {
                                nickName = "anonymous";
                            } else {
                                nickName = dataSnapshot.child("Users").child(userId).child("nickName").getValue().toString();
                            }*/

                                v1.setTag(userId);
                                //COMMENT THIS OUT IF FINISH AVATAR(FRANK)
                                TextView showNickName = (TextView) v1.findViewById(R.id.nick_name);
                                //TextView showMessage = (TextView) v1.findViewById(R.id.text_message);
                                //showMessage.setText(message);
                                showNickName.setText(nickName);

                                /*SHOW USER AVATAR(FRANK)*/
//                                if (landing_imgStr != null) {
//                                    System.out.print("Pass");
//
//                                    byte[] imageByte = Base64.decode(landing_imgStr, Base64.DEFAULT);
//                                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
//
//                                    ImageButton Avatar = (ImageButton) findViewById(R.id.avatar);
//                                    Avatar.setImageBitmap(bitmap);
//                                }

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    listViewOfMessages.setSelection(position);
                }

            };
            groupReference.child(groupId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        long start_date = dataSnapshot.child("last_message").getValue(long.class);
                        long current_time = System.currentTimeMillis();
                        long time_period = current_time - start_date;
                        double second = (double) time_period / 1000.0;
                        double hour = second / 3600;
                        if (hour <= 24)
                            listViewOfMessages.setAdapter(adapter);
                    }catch (NullPointerException e){
                        Toast.makeText(group_chat.this, "This group is not existing anymore. Please go back.", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            //listViewOfMessages.setAdapter(adapter);

            sendImage.setOnClickListener(this);

            sendMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    message = enterTheMessage.getText().toString();
                    if (message.length() == 0) {
                        Toast.makeText(group_chat.this, "message cannot be empty", Toast.LENGTH_LONG).show();
                    } else {
//                    Global_variable global_variable = (Global_variable)getApplicationContext();
//                    ChartMessage chartMessage = new ChartMessage(message, global_variable.getUser_id());
                        groupReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            /*Global_variable global_variable = (Global_variable)getApplicationContext();
                            String userId = global_variable.getUser_id();
                            ChartMessage chartMessage = new ChartMessage(message, userId);*/
//                        ListAdapter adapter = new ListAdapter(group_chat.this, chartMessage);
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Global_variable global_variable = (Global_variable) getApplicationContext();
                                String userId = global_variable.getUser_id();
                                //ChartMessage chartMessage = new ChartMessage(message, userId);

                                if (dataSnapshot.child(groupId).getValue() == null){
                                    Toast.makeText(group_chat.this, "This group is not existing anymore. Please go back.", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                else if (dataSnapshot.child(groupId).child("messageId").getValue() == null) {
                                    long message_date = System.nanoTime();
                                    groupReference.child(groupId).child("last_message").setValue(message_date);
                                    String messageKey = chartMessagesReference.push().getKey();
                                    ChartMessage chartMessage = new ChartMessage(message, userId);
                                    chartMessage.setMessageKey(messageKey);
                                    chartMessagesReference.child(messageKey).setValue(chartMessage);
                                    ArrayList<String> messageId = new ArrayList<String>();
                                    messageId.add(messageKey);

                                    groupReference.child(groupId).child("messageId").setValue(messageId);

                                    //MessageAdapter adapter = new MessageAdapter(group_chat.this, messageId);
                                    //listViewOfMessages.setAdapter(adapter);
                                    //group.setChartMessages(chartMessages);
                                    //System.out.println("groupId_1"+groupId);
                                    //groupReference.child(groupId).child("chartMessages").setValue(chartMessages);
                                } else {
                                    long message_date = System.nanoTime();
                                    groupReference.child(groupId).child("last_message").setValue(message_date);
                                    String messageKey = chartMessagesReference.push().getKey();
                                    ChartMessage chartMessage = new ChartMessage(message, userId);
                                    chartMessage.setMessageKey(messageKey);
                                    chartMessagesReference.child(messageKey).setValue(chartMessage);

                                    ArrayList<String> messageId = (ArrayList<String>) dataSnapshot.child(groupId).child("messageId").getValue();
                                    messageId.add(messageKey);
                                    groupReference.child(groupId).child("messageId").setValue(messageId);

                                    //MessageAdapter adapter = new MessageAdapter(group_chat.this, messageId);
                                    //listViewOfMessages.setAdapter(adapter);
                                    //group.setChartMessages(chartMessages);
                                    //System.out.println("groupId_2"+groupId);
                                    //groupReference.child(groupId).child("chartMessages").setValue(chartMessages);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        //showGroupName.setText("");
                        //display();
                        enterTheMessage.setText("");
                    }
                    //display();
                }
            });


            listViewOfMessages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    View v = view;
                    final String other_uid = v.getTag().toString();
                    System.out.println("userId get from tag" + other_uid);
                    //System.out.println(uid);
                    //global_variable.setother_userid(u);

                    //Add by Frank (decide which go to which profile page based on privacy setting)
                    final DatabaseReference others_ref = FirebaseDatabase.getInstance().getReference().child("Users");
                    others_ref.child(other_uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //is other user in this group
                            ArrayList<String> groupIDs = (ArrayList<String>) dataSnapshot.child("groupIDs").getValue();
                            if (groupIDs != null) {
                                OtherInThisGroup = groupIDs.contains(groupId);
                            }
                            boolean current_mode = (boolean) dataSnapshot.child("privacy_mode").getValue();
                            System.out.print(other_uid + "\' Current mode is " + current_mode + ". ");

                            if (current_mode == true && (MeInThisGroup == false || OtherInThisGroup == false) ) {
                                System.out.println("Go to Others_profile_privacy.class.");
                                Intent i = new Intent(group_chat.this, Others_profile_privacy.class);
                                i.putExtra("other_uid", other_uid);
                                group_chat.this.startActivity(i);

                            } else {
                                System.out.println("Go to Others_profile.class.");
                                Intent i = new Intent(group_chat.this, Others_profile.class);
                                i.putExtra("other_uid", other_uid);
                                group_chat.this.startActivity(i);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });


            joinbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //final DatabaseReference ref = mDatabase.getReference();
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            long votes = (long) dataSnapshot.child("Group").child(groupId).child("vote").getValue();

                            votes++;
                            if (votes >= 10) {
                                ref.child("Group").child(groupId).child("is_permanent").setValue(true);

                                joinbutton.setText("permanent");
                            }
                            ref.child("Group").child(groupId).child("vote").setValue(votes);
                            ArrayList<String> groupIds = (ArrayList<String>) dataSnapshot.child("Users").child(uid).child("groupIDs").getValue();
                            if (groupIds != null) {
                                groupIds.add(groupId);
                            } else {
                                groupIds = new ArrayList<String>();
                                groupIds.add(groupId);
                            }
                            ref.child("Users").child(uid).child("groupIDs").setValue(groupIds);
                            joinbutton.setText("joined");
                            joinbutton.setEnabled(false);
                            deleteButton.setEnabled(true);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            });


            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //final DatabaseReference ref = mDatabase.getReference();
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child("Group").child(groupId).getValue() == null) {
                                Toast.makeText(group_chat.this, "This group is not existing anymore. Please go back.", Toast.LENGTH_LONG).show();
                                return;
                            } else {
                                ArrayList<String> groupIds = (ArrayList<String>) dataSnapshot.child("Users").child(uid).child("groupIDs").getValue();
                                groupIds.remove(groupId);
                                long votes = (long) dataSnapshot.child("Group").child(groupId).child("vote").getValue();
                                votes--;
                                ref.child("Group").child(groupId).child("vote").setValue(votes);
                                ref.child("Users").child(uid).child("groupIDs").removeValue();
                                ref.child("Users").child(uid).child("groupIDs").setValue(groupIds);
                        /*
                        ArrayList<String> memberIDs = (ArrayList<String>)dataSnapshot.child("Group").child(groupId).child("member_ids").getValue();
                        memberIDs.remove(uid);
                        ref.child("Group").child(groupId).child("member_ids").setValue(memberIDs);
                        if (memberIDs.size() == 0) {
                            ref.child("Group").child(groupId).setValue(null);
                        }
                        */
                                Intent i = new Intent(group_chat.this, homepage.class);
                                group_chat.this.startActivity(i);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });


            reportBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Group");
                    ref.child(groupId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ArrayList<String> reportIDs = (ArrayList<String>) dataSnapshot.child("reportIDs").getValue();

                            if (reportIDs == null) {
                                reportIDs = new ArrayList<String>();
                                reportIDs.add(uid);
                                ref.child(dataSnapshot.child("key").getValue().toString()).child("reportIDs").setValue(reportIDs);
                                Toast.makeText(group_chat.this, "Thank you for your report", Toast.LENGTH_LONG).show();
                                reportBtn.setEnabled(false);
                                //reportBtn.setText("Reported");
                            } else {
                                if (!reportIDs.contains(uid)) {
                                    reportIDs.add(uid);
                                    ref.child(dataSnapshot.child("key").getValue().toString()).child("reportIDs").setValue(reportIDs);
                                    Toast.makeText(group_chat.this, "Thank you for your report", Toast.LENGTH_LONG).show();
                                    reportBtn.setEnabled(false);
                                    //reportBtn.setText("Reported");
                                }
                                else {
                                    Toast.makeText(group_chat.this, "You already reported this group", Toast.LENGTH_LONG).show();
                                    reportBtn.setEnabled(false);
                                    //reportBtn.setText("Reported");
                                }
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            });

        } catch (NullPointerException e) {
            Toast.makeText(group_chat.this, "This group is not existing anymore. Please go back.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) { //Add by Frank fix null image select bug
            Uri imgUri = data.getData();

            Bitmap myBitmap = null;
            try {
                myBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imgUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            myBitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            myBitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            byte[] imgByte = bos.toByteArray();
            this.image = Base64.encodeToString(imgByte, Base64.DEFAULT);

            //Uri way
            //this.landing_imgStr = imgUri.toString();
        }

        if (this.image == null) {
            Toast.makeText(group_chat.this, "message cannot be empty", Toast.LENGTH_LONG).show();
        } else {
            Bundle b = getIntent().getExtras();
            groupId = b.getString("groupid");
            groupReference = FirebaseDatabase.getInstance().getReference().child("Group");
            chartMessagesReference = FirebaseDatabase.getInstance().getReference().child("ChartMessages");
            groupReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Global_variable global_variable = (Global_variable) getApplicationContext();
                    String userId = global_variable.getUser_id();
                    if (dataSnapshot.child(groupId).getValue() == null){
                        Toast.makeText(group_chat.this, "This group is not existing anymore. Please go back.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    else if (dataSnapshot.child(groupId).child("messageId").getValue() == null) {
                        long message_date = System.currentTimeMillis();
                        groupReference.child(groupId).child("last_message").setValue(message_date);
                        String messageKey = chartMessagesReference.push().getKey();
                        ChartMessage chartMessage = new ChartMessage(null, userId);
                        chartMessage.setImage(image);
                        chartMessage.setMessageKey(messageKey);
                        chartMessagesReference.child(messageKey).setValue(chartMessage);
                        ArrayList<String> messageId = new ArrayList<String>();
                        messageId.add(messageKey);

                        groupReference.child(groupId).child("messageId").setValue(messageId);
                    } else {
                        long message_date = System.currentTimeMillis();
                        groupReference.child(groupId).child("last_message").setValue(message_date);
                        String messageKey = chartMessagesReference.push().getKey();
                        ChartMessage chartMessage = new ChartMessage(null, userId);
                        chartMessage.setImage(image);
                        chartMessage.setMessageKey(messageKey);
                        chartMessagesReference.child(messageKey).setValue(chartMessage);
                        ArrayList<String> messageId = (ArrayList<String>) dataSnapshot.child(groupId).child("messageId").getValue();
                        messageId.add(messageKey);
                        groupReference.child(groupId).child("messageId").setValue(messageId);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }
}





© 2018 GitHub, Inc.
        Terms
        Privacy
        Security
        Status
        Help
        Contact GitHub
        API
        Training
        Shop
        Blog
        About