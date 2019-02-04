package com.alliancecode.fixit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.provider.SelfDestructiveThread;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import android.support.v7.widget.Toolbar;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.security.Key;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.alliancecode.fixit.R.color.colorAccent;

public class Chat extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    String KEY_REPLY = "key_reply";
    public static final int NOTIFICATIO_ID = 1;
    LinearLayout layout;
    RelativeLayout layout2;
  //  ImageView sendButton, chatwithProfileImg, callChatwithImg;
    ImageView sendButton,  callChatwithImg;

    CircleImageView chatwithProfileImg;                //===============================
    TextView chatwithName, chatwithPnumber;
    EditText messageArea;
    ScrollView scrollView;
    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;
    Firebase reference1, reference2;
    String message, msgStatus, messageKey;
    int countChatWithMsg1, countChatWithMsg2, totalMsg, userReference, textSize, addTextSize, txtDeterminator;
    String AmPm;
    MediaPlayer ringSend, ringReceived;
    private MyRecyclerViewAdapter adapter, adapterCon;
    String url = "https://fixit-dbae9.firebaseio.com/";
    /* String url = "https://chatfixit.firebaseio.com/";*/
    /*String url2 = "https://androidchatapp-2feee.firebaseio.com/";*/
    Firebase myFirebase;

    private String chatWithName, chatWithPhone, chatWithProfileImage, chatWithId;
    private String userID, userName, userPhone, userProfileImage ;

    ArrayList<String> quickText, quickTextCon;

    AlertDialog alertDialog1;
    CharSequence[] values = {"Small Text", "Medium Text", "Large Text", "Original Size"};

    ArrayList<String> list = new ArrayList<String>();
    //textview of chat - addmessages
    TextView textView;
    private Toolbar chatwithProfileToolbar;

    private String ConOrPro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        layout = (LinearLayout)findViewById(R.id.layout1);
        layout2 = (RelativeLayout)findViewById(R.id.layout2);
        sendButton = (ImageView)findViewById(R.id.sendButton);
        messageArea = (EditText)findViewById(R.id.messageArea);
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        chatwithProfileImg = (CircleImageView) findViewById(R.id.chatwithImg);   //==================================
        chatwithName = (TextView)findViewById(R.id.tvChatwithName);
        chatwithPnumber = (TextView)findViewById(R.id.tvChatwithNumber);
        callChatwithImg = (ImageView)findViewById(R.id.callImage);

        chatwithProfileToolbar = (Toolbar)findViewById(R.id.profileToolbar);
        setSupportActionBar(chatwithProfileToolbar);

        ConOrPro = getIntent().getExtras().getString("ConOrPro");

        if(ConOrPro.equals("Providers")){
            chatWithName = ProviderMapActivity.consumerChatName;   //chatWith Consumers
            chatWithPhone = ProviderMapActivity.consumerChatPhone;   //chatWith Consumers
            chatWithProfileImage = ProviderMapActivity.nConsumerChatProfileImageUrl;   //chatWith Consumers
            chatWithId = ProviderMapActivity.consumerChatId;   //chatWith Consumers
            userID = ProviderMapActivity.userId;   //user
            userName = ProviderMapActivity.nUserName;   //user
            userPhone = ProviderMapActivity.nPhone;   //user
            userProfileImage = ProviderMapActivity.nProfileImageUrl;   //user
        } else {
            chatWithName = ConsumerMapActivity.providerChatName;   //chatWith Providers
            chatWithPhone = ConsumerMapActivity.providerChatPhone;   //chatWith Providers
            chatWithProfileImage = ConsumerMapActivity.nProviderChatProfileImageUrl;   //chatWith Providers
            chatWithId = ConsumerMapActivity.providerChatId;   //chatWith Providers
            userID = ConsumerMapActivity.userId;   //user
            userName = ConsumerMapActivity.nUserName;   //user
            userPhone = ConsumerMapActivity.nPhone;   //user
            userProfileImage = ConsumerMapActivity.nProfileImageUrl;   //user
        }
        //------------------------------------------- toolbar information ------------------------------------//
  //      public static String userId, nProfileImageUrl, nName, nPhone;
  //      public static String consumerChatId, nConsumerChatProfileImageUrl, consumerChatName, consumerChatPhone;

        chatwithName.setText(chatWithName);
        chatwithPnumber.setText(chatWithPhone);

        /*Toast.makeText(this, "username " + userName, Toast.LENGTH_SHORT).show();
*/
///////////////////////
  /*
        Firebase.setAndroidContext(getApplicationContext());
        myFirebase = new Firebase(url);
        myFirebase.child("users").child(name).child("contact").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String pNumber = dataSnapshot.getValue(String.class);
                    chatwithPnumber.setText(pNumber);
                }else {
                    chatwithPnumber.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    */
///////////////////////////////////////////////


        callChatwithImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ActivityCompat.checkSelfPermission(Chat.this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                    String phoneNumber = chatwithPnumber.getText().toString();
                    String subStrPNum = phoneNumber.substring(1);
                    Intent call = new Intent(Intent.ACTION_CALL);
                    call.setData(Uri.parse("tel:+63"+ subStrPNum));
                    startActivity(call);
                }else {
                    String phoneNumber = chatwithPnumber.getText().toString();
                    String subStrPNum = phoneNumber.substring(1);
                    Intent call = new Intent(Intent.ACTION_DIAL);
                    call.setData(Uri.parse("tel:+63"+ subStrPNum));
                    startActivity(call);
                }
            }
        });


 /*


        /////////////////getting the image of the chatwith user
        storage = FirebaseStorage.getInstance();
        final StorageReference getImageRef = storage.getReferenceFromUrl("gs://fixitchatmodule.appspot.com").child("profile_images").child(UserDetails.chatWith);

        ////////////// getting the image of a user from firebase

        try{
            final File localFile = File.createTempFile("image", "jpeg");
            getImageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {


                    Glide
                            .with(getApplicationContext())
                            //add here .using(new FirebaseImageLoader()) // add dependecy in gradle for offline viewing
                            .load(localFile)
                            .transform(new CircleTransform(Chat.this))
                            .into(chatwithProfileImg);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Glide
                            .with(getApplicationContext())
                            .load("image")
                            .placeholder(R.drawable.ic_account_circle_white)
                            .transform(new CircleTransform(Chat.this))
                            .into(chatwithProfileImg);

                }
            });

        }catch (IOException e){
            Toast.makeText(Chat.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        ///////////////////////////////////////////

 */
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        try{

             Glide.with(getApplication()).load(chatWithProfileImage).into(chatwithProfileImg);

        }catch(Exception e){
            e.printStackTrace();
        }





        //------------------------------------------- toolbar information ------------------------------------//

        /*textView.setTextSize(30);*/

        //quick text for the service provider
        /*ArrayList<String>*/ quickText = new ArrayList<>();
        quickText.add("Ok.");
        quickText.add("Ok, I'm on my way.");
        quickText.add("I'll text you once I arrive.");
        quickText.add("Thank You.");
        quickText.add("Hi.");
        quickText.add("Hello.");
        quickText.add("Welcome.");
        quickText.add("Yes.");
        quickText.add("No.");

        //quick text for the consumer
       /* ArrayList<String>*/ quickTextCon = new ArrayList<>();
        quickTextCon.add("Ok.");
        quickTextCon.add("Ok, I'll wait on you.");
        quickTextCon.add("Please text me upon your arrival.");
        quickTextCon.add("Thank You.");
        quickTextCon.add("Hi.");
        quickTextCon.add("Hello.");
        quickTextCon.add("Welcome.");
        quickTextCon.add("Yes.");
        quickTextCon.add("No.");

        final RecyclerView recyclerView = findViewById(R.id.textList);
        LinearLayoutManager horizontalLayout = new LinearLayoutManager(Chat.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayout);


        clearExistingNotifications();

        Firebase.setAndroidContext(this);
      //  reference1 = new Firebase(url + "messages/" + UserDetails.username + "_" + UserDetails.chatWith);
      //  reference2 = new Firebase(url + "messages/" + UserDetails.chatWith + "_" + UserDetails.username);

        reference1 = new Firebase(url + "ChatMessages/" + userID + "_" + chatWithId);
        reference2 = new Firebase(url + "ChatMessages/" + chatWithId + "_" + userID);

 //       name = UserDetails.username;
        /*Toast.makeText(Chat.this, name, Toast.LENGTH_LONG).show();*/


/*
        ////////////////////////////////////////
        Firebase.setAndroidContext(getApplicationContext());
        myFirebase = new Firebase(url);
        myFirebase.child("users").child(name).child("type").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    String userType = dataSnapshot.getValue(String.class);
                    if (userType.equals("serviceprovider")) {
                        adapter = new MyRecyclerViewAdapter(Chat.this, quickText);
                        adapter.setClickListener(Chat.this);
                        recyclerView.setAdapter(adapter);
                        userReference = 1;

                    }// continue to change the visibility for type of user
                    else if(userType.equals("consumer")){
                        adapterCon = new MyRecyclerViewAdapter(Chat.this, quickTextCon);
                        adapterCon.setClickListener(Chat.this);
                        recyclerView.setAdapter(adapterCon);
                        userReference = 2;

                    }else{
                        recyclerView.setVisibility(View.GONE);
                    }
                }else {
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
*/
        /////////////////////////////////////////

        if(ConOrPro.equals("Providers")){

            adapter = new MyRecyclerViewAdapter(Chat.this, quickText);
            adapter.setClickListener(Chat.this);
            recyclerView.setAdapter(adapter);
            userReference = 1;

        }// continue to change the visibility for type of user
        else if(ConOrPro.equals("Consumers")){
            adapterCon = new MyRecyclerViewAdapter(Chat.this, quickTextCon);
            adapterCon.setClickListener(Chat.this);
            recyclerView.setAdapter(adapterCon);
            userReference = 2;

        }else{
            recyclerView.setVisibility(View.GONE);
        }




        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //date
                Calendar calDate = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
                DateFormat date = new SimpleDateFormat("MMMM dd, yyyy");
                date.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                String currentDate = date.format(calDate.getTime());
                //time
                Calendar calTime = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
                DateFormat time = new SimpleDateFormat("hh:mm aa");
                time.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                String currentTime = time.format(calTime.getTime());
                int hour = Integer.parseInt(currentTime.substring(0, 1));
                int minute = Integer.parseInt(currentTime.substring(3,4));


                String messageText = messageArea.getText().toString();
                msgStatus = "unread";

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", userName);
                    map.put("status", msgStatus);


                   /* createInlineNotification();*/
                    //add here a status for sent msg: when the user send a message it will push a status of unread

                    if(calTime.get(Calendar.AM_PM) == Calendar.AM){
                        AmPm = "AM";
                    }else if(calTime.get(Calendar.AM_PM) == Calendar.PM){
                        AmPm = "PM";
                    }

                    String strHrsToShow = (calTime.get(Calendar.HOUR) == 0) ?"12":calTime.get(Calendar.HOUR)+"";
                    String strMinToShow = (calTime.get(Calendar.MINUTE)<10)?"0"+minute:calTime.get(Calendar.MINUTE)+"";

                    map.put("date", currentDate + " AT " + strHrsToShow +":"+ strMinToShow + " " + AmPm);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    messageArea.setText("");
                }

            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if(dataSnapshot.exists()){

                    msgStatus = "Read";

                    Map map = dataSnapshot.getValue(Map.class);
                    message = map.get("message").toString();
                    String username = map.get("user").toString();
                    String date = map.get("date").toString();
                    String msgLoadedStatus = map.get("status").toString();

                    if(username.equals(userName)){

                        addMessageBox(message, 2);
                        addDateTime(date, 2);


                    }else {

                        //chatwith person
                        addUser(chatWithName, 1);
                        addMessageBox(message, 1);
                        addDateTime(date,1);

                        //add here a status for received msg: when the user opens the app and messages for chatwith loads, it change the status to read.
                        //then, put an if-else statement where it will check if the status is not yet read for InlineNotification purpose

                        Firebase.setAndroidContext(getApplicationContext());
                        myFirebase = new Firebase(url);
                        myFirebase.child("ChatMessages/").child(chatWithId + "_" + userID)//reference2
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.exists()){
                                    /*List<String> list = new ArrayList<>();*/
                                            for(final DataSnapshot dataSnap : dataSnapshot.getChildren()){
                                                messageKey = dataSnap.getKey();

                                                //once the message loaded into the chat screen of the receiver, the status will change to read
                                                myFirebase.child("ChatMessages/").child(chatWithId + "_" + userID).child(messageKey)
                                                        .child("status").setValue("read");//reference2

                                            }
                                        }else {
                                            Toast.makeText(Chat.this, "Message failed", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });



                        //notification method
                        createInlineNotification();
                        //------------------------------ for single notification ---------------------------//


                    }////////// end of if-else for determining the user type to load

                }else {
                    Toast.makeText(Chat.this, "Error loading messages", Toast.LENGTH_SHORT).show();
                }
                //find a way to sync msg in offline state
               /* reference1.keepSynced(true);
                reference2.keepSynced(true);*/
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }
///////////////////////////////////////////


    private void clearExistingNotifications(){
        int notificationId = getIntent().getIntExtra("notification", 0);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);
    }

    private void createInlineNotification(){

        Firebase.setAndroidContext(getApplicationContext());
        myFirebase = new Firebase(url);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setSmallIcon(android.R.drawable.stat_notify_chat);
        Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.whistle_short);

        Firebase.setAndroidContext(getApplicationContext());
        myFirebase = new Firebase(url);
//        String name = UserDetails.chatWith;
 //       UserDetails.chatWith = name;

        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.fixit_logo));
        builder.setContentTitle("Message from: " + chatWithName);
        builder.setContentText(message);
        builder.setSound(sound);
        builder.setSubText("Tap to Login");

        Intent intent;
        //   Intent intent = new Intent(getApplicationContext(), Login.class);
        if(ConOrPro.equals("Providers")){
            intent = new Intent(getApplicationContext(), ProviderLoginActivity.class);
        }else{
            intent = new Intent(getApplicationContext(), ConsumerLoginActivity.class);
        }


        //----------- code for back press -----------------------//
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        if(ConOrPro.equals("Providers")){
            stackBuilder.addParentStack(ProviderMapActivity.class);
        }else{
            stackBuilder.addParentStack(ConsumerMapActivity.class);
        }
    //    stackBuilder.addParentStack(Users.class);
        stackBuilder.addNextIntent(intent);
        //----------- code for back press -----------------------//

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());

    }

 /*   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
*/
  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.textSize:
                createDialogBoxWithRbutton();
                return true;

            case R.id.textDelete:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

   /* public void createDialogBoxWithRbutton(){


        AlertDialog.Builder builder = new AlertDialog.Builder(Chat.this);
        builder.setTitle("Select From the Options");

        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {



                switch (item){
                    case 0:
                        txtDeterminator = 1;

                        break;
                    case 1:
                        txtDeterminator = 2;

                        break;
                    case 2:
                        txtDeterminator = 3;

                        break;
                }
                alertDialog1.dismiss();

            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();
    }*/

    public void addMessageBox(String message, int type){


        textView = new TextView(Chat.this);


        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.original));
        textView.setText(message);
        list.add(textView.getText().toString());



        final CircleImageView imgView = new CircleImageView(Chat.this);


        LinearLayout childLayout = new LinearLayout(Chat.this);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.setMargins(0, 0, 0, 0);

        LinearLayout.LayoutParams LPimgView = new LinearLayout.LayoutParams(100, 100/*ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT*/);


        //////////////////////////////////
        if(type == 1){
            //layout and bubble for the chatwith
            lp2.gravity = Gravity.LEFT;
            lp2.setMargins(10, 0, 0, 0);

            textView.setBackgroundResource(R.drawable.received_bubble);
            textView.setTextColor(Color.BLACK);
            textView.setPadding(20, 20, 20, 35);
            textView.setGravity(Gravity.CENTER);

            countChatWithMsg1++;

            ringSend = MediaPlayer.create(Chat.this, R.raw.send);
            ringSend.start();
            /*createInlineNotification();*/



            /////////////////getting the image of the chatwith user
  //====        storage = FirebaseStorage.getInstance();
  //=====          final StorageReference getImageRef = storage.getReferenceFromUrl("gs://fixitchatmodule.appspot.com").child("profile_images").child(UserDetails.chatWith);

            ////////////// getting the image of a user from firebase

            try{
                Glide.with(getApplication()).load(chatWithProfileImage).into(imgView);

                /*
                final File localFile = File.createTempFile("image", "jpeg");
                getImageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {


                        Glide
                                .with(getApplicationContext())
                                //add here .using(new FirebaseImageLoader()) // add dependecy in gradle for offline viewing
                                .load(localFile)
                                .transform(new CircleTransform(Chat.this))
                                .into(imgView);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Glide
                                .with(getApplicationContext())
                                .load("image")
                                .placeholder(R.drawable.ic_account_circle)
                                .transform(new CircleTransform(Chat.this))
                                .into(imgView);

                    }
                });

                */


            }catch (Exception e){
                e.printStackTrace();
             //   Toast.makeText(Chat.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            ///////////////////////////////////////////


            childLayout.addView(imgView, 0);
            childLayout.addView(textView, 1);
        }else {
            lp2.gravity = Gravity.RIGHT;
            lp2.setMargins(320, 15, 0, 0);
            lp2.weight = 0.8f;
            textView.setBackgroundResource(R.drawable.send_bubble);
            textView.setTextColor(Color.WHITE);
            textView.setPadding(20, 20, 20, 35);
            ringReceived = MediaPlayer.create(Chat.this, R.raw.received);
            ringReceived.start();
            childLayout.addView(textView);

            countChatWithMsg2++;

        }
//////////////////////////////////


        textView.setLayoutParams(lp2);
        imgView.setLayoutParams(LPimgView);
        layout.addView(childLayout);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
    public void addUser(String username, int type){
        TextView tvSender = new TextView(Chat.this);
        tvSender.setText(Html.fromHtml("<small>"+username+"</small>"));

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if(type == 1){
            //sets the username of the chatwith
            lp2.gravity = Gravity.LEFT;
            lp2.setMargins(120, 0, 0, 0);
            tvSender.setTextColor(Color.GRAY);
            tvSender.setGravity(View.FOCUS_LEFT);
        }else {
            lp2.gravity = Gravity.RIGHT;
        }
        tvSender.setLayoutParams(lp2);
        layout.addView(tvSender);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
    public void addDateTime(String date, int type){
        TextView tvDate = new TextView(Chat.this);
        tvDate.setText(Html.fromHtml("<small>"+date+"</small><br>"));

        LinearLayout childLayout = new LinearLayout(Chat.this);

        final ImageView checkImg = new ImageView(Chat.this);
        final LinearLayout.LayoutParams ImgCheck = new LinearLayout.LayoutParams(25, 25);
        ImgCheck.setMargins(0, 0, 0, 0);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        /*lp2.weight = 1.0f;*/
        if(type == 1){
            lp2.gravity = Gravity.LEFT;
            lp2.setMargins(100, 0, 0, 0);
            tvDate.setTextColor(Color.GRAY);
            tvDate.setGravity(View.FOCUS_LEFT);

            childLayout.addView(tvDate);
        }else {
            lp2.gravity = Gravity.RIGHT;
            lp2.setMargins(305, 0, 0, 0);
            tvDate.setTextColor(Color.GRAY);
            tvDate.setGravity(View.FOCUS_RIGHT);

            Firebase.setAndroidContext(getApplicationContext());
            myFirebase = new Firebase(url);
            myFirebase.child("ChatMessages/").child(chatWithId + "_" + userID)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){

                                for(final DataSnapshot dataSnapCheck : dataSnapshot.getChildren()){
                                    messageKey = dataSnapCheck.getKey();

                                    myFirebase.child("ChatMessages/").child( chatWithId+ "_" + userID).child(messageKey)
                                            .child("message").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {


                                            if(!dataSnapshot.exists()){

                                                checkImg.setBackgroundResource(R.drawable.ic_msg_notsent);


                                            }else{
                                                 //    Glide
                                                 //       .with(getApplicationContext())
                                                  //      .load("image2")
                                                   //     .placeholder(R.drawable.ic_msg_sent_check)
                                                    //    .transform(new CircleTransform(Chat.this))
                                                     //   .into(checkImg);

                                                checkImg.setBackgroundResource(R.drawable.ic_msg_sent_check);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(FirebaseError firebaseError) {

                                        }
                                    });
                                }

                            }else {
                                Toast.makeText(Chat.this, "Message failed", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });


            childLayout.addView(tvDate, 0);
            childLayout.addView(checkImg, 1);

        }
        tvDate.setLayoutParams(lp2);
        checkImg.setLayoutParams(ImgCheck);
        layout.addView(childLayout);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
    @Override
    public void onItemClick(View view, final int position){

        if(userReference == 1){// service provider
            String getText = adapter.getItem(position);
            messageArea.setText(getText);
        }else if(userReference == 2){// consumer
            String getText = adapterCon.getItem(position);
            messageArea.setText(getText);
        }

    }
}

