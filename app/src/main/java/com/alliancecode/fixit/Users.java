package com.alliancecode.fixit;


import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alliancecode.fixit.Model.UserDetails;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class Users extends AppCompatActivity {
    TextView noUsersText, tvProfile, tvNumProfile;
    EditText chatWithET;
    ImageButton buttonMessage, buttonCall;
    ImageView upDownIV;
    String name = "";
    Firebase myFirebase;
    String url2 = "https://androidchatapp-2feee.firebaseio.com/";
    private static final String TAG = "ChatActivity";
    private SlidingUpPanelLayout mLayout;
    private final static int REQUEST_PHONE_CALL = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pullup);


        final UserDetails userdetails = new UserDetails();

        upDownIV = (ImageView) findViewById(R.id.IVupDown);

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        //pullup slide
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset" + slideOffset);

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);

            }
        });

        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

            }
        });

        ///////////////
        TextView t = (TextView) findViewById(R.id.noUsersText);
        t.setText(Html.fromHtml("Information of task"));


        /*usersList = (ListView)findViewById(R.id.usersList);*/
        noUsersText = (TextView) findViewById(R.id.noUsersText);
        tvProfile = (TextView) findViewById(R.id.profileTV);
        tvNumProfile = (TextView) findViewById(R.id.profileNumber);
        buttonMessage = (ImageButton) findViewById(R.id.messageButton);
        buttonCall = (ImageButton) findViewById(R.id.callButton);
        chatWithET = (EditText) findViewById(R.id.etChatwith);


        buttonMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*name = chatWithET.getText().toString();*/
                Firebase.setAndroidContext(getApplicationContext());
                myFirebase = new Firebase(url2);

                name = chatWithET.getText().toString();
                tvProfile.setText(name);
                tvProfile.setVisibility(View.VISIBLE);
                userdetails.chatWith = tvProfile.getText().toString();
                startActivity(new Intent(Users.this, Chat.class));


            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Editable chatwith = chatWithET.getText();
                if (chatwith != null && chatwith.length() >= 1) {
                    name = chatWithET.getText().toString();
                    tvProfile.setText(name);
                    if (chatWithET.getText().toString() == "") {
                        tvProfile.setText("Name");
                        tvNumProfile.setText("Contact");
                    }

                    Firebase.setAndroidContext(getApplicationContext());
                    myFirebase = new Firebase(url2);
                    myFirebase.child("users").child(name).child("contact").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String numValue = dataSnapshot.getValue(String.class);
                                tvNumProfile.setText(numValue);
                            }else{
                                /*Toast.makeText(Users.this, "user not found", Toast.LENGTH_SHORT).show();*/
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
            }
        };
        chatWithET.addTextChangedListener(textWatcher);

        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneNumber = tvNumProfile.getText().toString();
                String substrPhone = phoneNumber.substring(1);
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:+63"+ substrPhone));

                //     if(ActivityCompat.checkSelfPermission(Users.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.checkSelfPermission(Users.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){

                    startActivity(i);
                }
            }

        });

    }


}
