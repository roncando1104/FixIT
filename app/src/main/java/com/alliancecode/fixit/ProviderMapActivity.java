package com.alliancecode.fixit;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.icu.text.IDNA;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.telephony.gsm.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alliancecode.fixit.Model.UserDetails;
import com.bumptech.glide.Glide;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.firebase.client.Firebase;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProviderMapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, RoutingListener {   //RoutingListener for Directions

    //////////////////////////////////
    //GoogleMap
    private GoogleMap nMap;

    //GoogleApiClient
    GoogleApiClient nGoogleApiClient;

    //Location
    Location nLastLocation;

    //LocationRequest
    LocationRequest nLocationRequest;
    // private SupportMapFragment mapFragment;
    // FusedLocationProviderClient mFusedLocationClient;

    //SupportMapFragment
  //  SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    SupportMapFragment mapFragment;

    ////////////////////////////////

    ///String
    private String consumerId = "";
    public static String requestId;
    private String rateAve;
    private String pendingProId;
    private String houseToWork, dateWork, timeWork,  paymentTobe,paymentTobeDig, paymDig, consumerRequest, consuNote , consuHouseType, item_one,item_two, item_three, item_four, item_five;
    private Float paymentTobeDigit, paymDigit;

    //   private String msgPending = "You have work schedule on ";
 private String conId, conName, conPhone, conSerReq, dateSch, houseLoc, paym,  conTimeReq,  profImage, conNote, conHouseType;
 //   private String conId, conSerReq, dateSch, houseLoc, paym,  conTimeReq,  conNote, conHouseType;

    private String schedControl = "";
    private String isSched = "";
  private String schedConName, schedConPhone, schedConProfileImage, schedProName, schedProAge, schedProPhone, schedProProfileImage, proUsername,proServiceType, conEmail;
  //  private String  schedProAge, schedProPhone, schedProProfileImage,schedProName, proUsername,proServiceType, conEmail;

    private String dateToday;
  //  private String userId;
  //  private String nName, nAddress, nAge, nPhone, nServiceType;
    private String nAddress, nAge, nServiceType, nName;

    //   private String nProfileImageUrl;
    private String houseLocation;
    private String nServiceDate;
    private String passChanged;
    private String pressControl = "";

    public static String userId, nProfileImageUrl, nUserName, nPhone;
    public static String consumerChatId, nConsumerChatProfileImageUrl, consumerChatName, consumerChatPhone;

 //   public static String schedConProfileImage, schedConName, schedConPhone ;   // connected
  //  public static String profImage, conName, conPhone;    //schedControl = "schedule"





    //Button
    private Button nLogout, nSettings;
    private Button nServiceStatusAccept, nServiceStatusCancel, nServiceStatusSchedComplete;
    private Button nNo, nYes;


    //Layout
    private LinearLayout nConsumerInfo;
    private LinearLayout nFragmentCallService;
    private LinearLayout nProviderInfo;
    private RelativeLayout nFragmentMap;

    //ImageView
    private ImageView nConsumerProfileImage;
    private ImageView nProviderProfileImage;
    private ImageView nProfileImage;

    //TextView
    private TextView nConsumerName, nConsumerRequest, nConsumerPhone, nConsumerHouse, nConsumerDate, nConsumerTime, nConsumerPayment,  nHouseType, nItemOne, nItemTwo,nItemThree, nItemFour, nItemFive, nConsumerNote;
    private TextView nProviderName, nProviderPhone, nProviderServiceType;
    private TextView tvNameField, tvPhoneField, tvAddressField, tvAgeField, tvServiceTypeField;
    private TextView txtProviderName, txtStars;
    private TextView itemLabel1, itemLabel2, itemLabel3, itemLabel4, itemLabel5;

    private Button nBack;

    /////////
    private DrawerLayout nRootLayout;


    //Boolean
    private Boolean isLoggingOut = false;
    private Boolean hasRequest = true;
    private Boolean isAccountHasIssue = false;
    private Boolean isAccountClear = false;
    private Boolean hasRating = false;
    private Boolean isPosted = false;


    private Boolean isSure = false;

    //Marker
    private Marker providerMarker;
    private Marker deviceConsumerMarker;


    //DatabaseReference
    private DatabaseReference nProviderDatabase;

    //FirebaseDatabase
    private FirebaseDatabase nDbase;

    //FirebaseAuth
    private FirebaseAuth nAuth;

    //Font
    private Typeface fontNunitoB;

    //////////////////////////

    //integer
    final int LOCATION_REQUEST_CODE = 1;
    private int status = 0;
    private  int state = 0;
    private int control = 0;

    ///////////////////

    /////////////////////////////////////////////////////pullup
    TextView noUsersText, tvProfile, tvNumProfile;
  //  EditText chatWithET;                           ///
    ImageButton buttonMessage, buttonCall;
    ImageView upDownIV;
    String name = "";
    private Firebase myFirebase;
    String url = "https://fixit-dbae9.firebaseio.com/";

    private static final String TAG = "ChatActivity";
    private SlidingUpPanelLayout mLayout;
    private final static int REQUEST_PHONE_CALL = 1;



    //latitude longitude
    private LatLng destinationLatLng;
    private LatLng houseLatLng;
    private LatLng latlng;



    //CardView
    private CardView nFragmentPlaceAutocomplete;

    //CircleImageView
    private CircleImageView imageAvatar;

    ////////====================================================
    SharedPreferences permissionStatus;
    private boolean sentToSetting = false;
    public static final int SMS_PERMISSION_CONSTANT = 100;
    public static final int REQUEST_PERMISSION_SETTING = 101;

    FirebaseStorage storage;
    StorageReference storageReference;

  //  private static final String TAG = "ChatActivity";
  //  private SlidingUpPanelLayout mLayout;
  //  private final static int REQUEST_PHONE_CALL = 1;

    private String callPhone;


////////////////============================
///////////////////



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_map);

        polylines = new ArrayList<>();//////////////////////////////////
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);





            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////pullup
        final UserDetails userdetails = new UserDetails();

        upDownIV = (ImageView) findViewById(R.id.IVupDown);
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        nFragmentMap = (RelativeLayout) findViewById(R.id.fragmentMap);

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
        //    TextView t = (TextView) findViewById(R.id.noUsersText);
        //   t.setText(Html.fromHtml("Information of task"));


        /*usersList = (ListView)findViewById(R.id.usersList);*/
        noUsersText = (TextView) findViewById(R.id.noUsersText);
        tvProfile = (TextView) findViewById(R.id.profileTV);
        tvNumProfile = (TextView) findViewById(R.id.profileNumber);
        buttonMessage = (ImageButton) findViewById(R.id.messageButton);
        buttonCall = (ImageButton) findViewById(R.id.callButton);
      //  chatWithET = (EditText) findViewById(R.id.etChatwith);

        nHouseType = (TextView) findViewById(R.id.houseType);
        nItemOne = (TextView) findViewById(R.id.item1);
        nItemTwo= (TextView) findViewById(R.id.item2);
        nItemThree = (TextView) findViewById(R.id.item3);
        nItemFour = (TextView) findViewById(R.id.item4);
        nItemFive = (TextView) findViewById(R.id.item5);
        itemLabel1 = (TextView) findViewById(R.id.item1Label);
        itemLabel2 = (TextView) findViewById(R.id.item2Labe2);
        itemLabel3 = (TextView) findViewById(R.id.item3Labe3);
        itemLabel4 = (TextView) findViewById(R.id.item4Labe4);
        itemLabel5 = (TextView) findViewById(R.id.item5Labe5);

        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE); ////////////////============================

        ////////////////============================
        buttonMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*name = chatWithET.getText().toString();*/
                Firebase.setAndroidContext(getApplicationContext());
                myFirebase = new Firebase(url);

                //           name = chatWithET.getText().toString();
     //           tvProfile.setText(name);
     //           tvProfile.setVisibility(View.VISIBLE);
      //          userdetails.chatWith = tvProfile.getText().toString();
              //  startActivity(new Intent(ProviderMapActivity.this, Chat.class));
                Intent intent = new Intent(ProviderMapActivity.this, Chat.class);
                intent.putExtra("ConOrPro", "Providers");                 //The value "Consumers" is the Consmers
                startActivity(intent);

            }
        });




 /*
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
                    myFirebase.child("users").child(name).child("contact").addValueEventListener(new com.firebase.client.ValueEventListener() {
                        @Override
                        public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String numValue = dataSnapshot.getValue(String.class);
                                tvNumProfile.setText(numValue);
                            }else{
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

  */






        //--------------------- checking the permission for send sms -------------------------------//===============================================

        if(ActivityCompat.checkSelfPermission(ProviderMapActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(ProviderMapActivity.this, Manifest.permission.SEND_SMS)){

                AlertDialog.Builder builder = new AlertDialog.Builder(ProviderMapActivity.this);
                builder.setTitle("Need SMS Permission");
                builder.setMessage("This app needs SMS permission to send SMS notification");
                builder.setPositiveButton("GRANT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(ProviderMapActivity.this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }else if(permissionStatus.getBoolean(Manifest.permission.SEND_SMS, false)){
                AlertDialog.Builder builder = new AlertDialog.Builder(ProviderMapActivity.this);
                builder.setTitle("Need SMS Permission");
                builder.setMessage("This app needs SMS permission to send SMS notification");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSetting = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to settings, then app to grant SMS permission", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }else {
                ActivityCompat.requestPermissions(ProviderMapActivity.this, new String[]{Manifest.permission.SEND_SMS}
                        , SMS_PERMISSION_CONSTANT);
            }
            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.SEND_SMS, true);
            editor.commit();
        }

        //--------------------- checking the permission for send sms -------------------------------//======================================






////////////////============================

        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(schedControl.equals("schedule")){
                    callPhone = conPhone;
                }else{
                    callPhone = schedConPhone;
                }

                if(ActivityCompat.checkSelfPermission(ProviderMapActivity.this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                  //  String phoneNumber = tvNumProfile.getText().toString();
                    String substrPhone = callPhone.substring(1);
                    Intent i = new Intent(Intent.ACTION_CALL);
                    i.setData(Uri.parse("tel:+63"+ substrPhone));
                    startActivity(i);
                }else{
                   // String phoneNumber = tvNumProfile.getText().toString();
                    String substrPhone = callPhone.substring(1);
                    Intent i = new Intent(Intent.ACTION_DIAL);
                    i.setData(Uri.parse("tel:+63"+ substrPhone));
                    startActivity(i);
                }
            }

        });
////////////////============================
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////pullup_END


















////////////////////////////////////////////////////////

////////////////////////////////////////////////////////

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ProviderMapActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            // return;
        } else {
            mapFragment.getMapAsync(this);
        }




        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();



        Calendar cal = Calendar.getInstance();
        //  Date currentLocalTime = calendar.getTime();
        dateToday = new SimpleDateFormat("MM-dd-yyyy").format(cal.getTime());

        nConsumerInfo = (LinearLayout) findViewById(R.id.consumerInfo);

        nConsumerProfileImage = (ImageView) findViewById(R.id.consumerProfileImage);

        nConsumerName = (TextView) findViewById(R.id.consumerName);
        nConsumerRequest = (TextView) findViewById(R.id.consumerRequest);
        nConsumerPhone = (TextView) findViewById(R.id.consumerPhone);
        nConsumerHouse = (TextView) findViewById(R.id.consumerHouse);
        nConsumerDate = (TextView) findViewById(R.id.consumerDate);
        nConsumerTime = (TextView) findViewById(R.id.consumerTime);
        nConsumerPayment = (TextView) findViewById(R.id.consumerPayment);
        nConsumerNote = (TextView) findViewById(R.id.consumerNote);

        nServiceStatusAccept = (Button) findViewById(R.id.serviceStatusAccept);
        nServiceStatusCancel = (Button) findViewById(R.id.serviceStatusCancel);
        nServiceStatusSchedComplete = (Button) findViewById(R.id.serviceStatusSchedComplete);

        nFragmentMap = (RelativeLayout) findViewById(R.id.fragmentMap);
        nFragmentCallService = (LinearLayout) findViewById(R.id.fragmentCallService);

////////////////////////////////////////////////////////////
        //  nProviderInfo = (LinearLayout) findViewById(R.id.providerInfo);

        // nProviderProfileImage = (ImageView) findViewById(R.id.providerProfileImage);

        //  nProviderName = (TextView) findViewById(R.id.providerName);
        // nProviderPhone = (TextView) findViewById(R.id.providerPhone);
        //  nProviderServiceType = (TextView) findViewById(R.id.providerServiceType);

        nRootLayout = (DrawerLayout) findViewById(R.id.drawerProvider_layout);

//////////////////////////////////////////////////////////////////////////////////////////
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerProvider_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
//////////////////////////////////////////////////////////////////////////////////////////


        //////////////////////////////////////
        nServiceStatusAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (status) {
                    case 1:
                        status = 2;
                        erasePolylines();
                        //          if (destinationLatLng.latitude != 0.0 && destinationLatLng.longitude != 0.0) {
                        //              getRouteToMarker(destinationLatLng);
                        //          }
                        //houseLocation
         //               getRouteToMarker(houseLatLng);

                        acceptRequest();

                        if (isSched.equals("on")){
                            nServiceStatusAccept.setText("SAVE Schedule");
                        }else{
                            nServiceStatusAccept.setText("Service COMPLETED");
                        }

                        break;

                    case 2:


                   //    try{
                           ////////////////////////////////
              //             if(isSure){
               //                if (isSched.equals("on")){
               //                    pendingRecord();
               //                }else{
                //                   historyRecord();
                 //              }

                 //              requestIsDone();
                 //              endService();
                 //              break;
                   //        }else{
                               pressControl = "normal";
                               serviceDoneConfirmation();
                               break;
                  //         }

                           //////////////////////////////

        //               }catch (Exception e){
         //                  e.printStackTrace();
              //         }


                }
            }


        });
        ///////////////////////////////////////////////////

        //////////////////////////////////////
        nServiceStatusSchedComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pressControl = "schedule";
                serviceDoneConfirmation();

          //      schedControl = "schedule";
           //     historyRecord();
           //     requestIsDone();
           //     endService();


            }


        });
        ///////////////////////////////////////////////////
        ///////////////////////////////////////////////////
        nServiceStatusCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   nServiceStatusCancel.setVisibility(View.GONE);

                pressControl = "cancel";
                serviceDoneConfirmation();

            //    cancelByProvider();
              //  endService();
            }
        });
        //////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////Navigation Header Settings
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_viewProvider);
        navigationView.setNavigationItemSelectedListener(this);
        View navigationHeaderView = navigationView.getHeaderView(0);
        txtProviderName = (TextView) navigationHeaderView.findViewById(R.id.txtProviderName);
        txtStars = (TextView) navigationHeaderView.findViewById(R.id.txtStars);

        imageAvatar = (CircleImageView) navigationHeaderView.findViewById(R.id.profileImage);


        nDbase = FirebaseDatabase.getInstance();
        nProviderDatabase = nDbase.getReference().child("Users").child("Providers").child(userId);
      //  fontNunitoB = Typeface.createFromAsset(getAssets(), "fonts/Nunito-Regular.ttf");
        //txtStars.setTypeface(fontNunitoB);

        nProviderDatabase.addValueEventListener(new ValueEventListener() {   /////////////////////nProviderDatabase
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //   String name = (String) dataSnapshot.child("Users").child("Consumers").child(userId).child("name").getValue();
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {

                    java.util.Map<String, Object> map = (java.util.Map<String, Object>) dataSnapshot.getValue();

                    if (map.get("name") != null) {
                        schedProName = map.get("name").toString();
                        nUserName = schedProName;
                        txtProviderName.setText(map.get("name").toString());
                    }

                    if (map.get("phone") != null) {
                        schedProPhone = map.get("phone").toString();
                    }

                    if (map.get("age") != null) {
                        schedProAge = map.get("age").toString();
                    }

                    if (map.get("username") != null) {
                        proUsername = map.get("username").toString();
                    }

                    if (map.get("serviceType") != null) {
                        proServiceType = map.get("serviceType").toString();
                    }


                    if (map.get("profileImageUrl") != null) {
                        schedProProfileImage = map.get("profileImageUrl").toString();

                        if (map.get("profileImageUrl").equals("")) {
                            imageAvatar.setImageDrawable(getResources().getDrawable(R.mipmap.ic_profileimage));
                        }else{
                            Glide.with(getApplication()).load(map.get("profileImageUrl").toString()).into(imageAvatar);
                        }
                    }else{
                        imageAvatar.setImageDrawable(getResources().getDrawable(R.mipmap.ic_profileimage));
                    }




                    /////////////////////////////////////////////////////////////////////// Calculating Star Rating
                    int ratingSum = 0;
                    float ratingTotalCount = 0;
                    float ratingAverage = 0;
                    for (DataSnapshot child: dataSnapshot.child("rating").getChildren()){                                //Specifically for field/table "rating"

                        ratingSum = ratingSum + Integer.valueOf(child.getValue().toString());
                        ratingTotalCount++;
                    }

                    if (ratingTotalCount !=0){
                        hasRating = true;
                        ratingAverage = ratingSum / ratingTotalCount;
                        rateAve = String.format("%.1f", ratingAverage);
                        txtStars.setText(String.valueOf(rateAve));
                    }

                    if (map.get("aveRating") != null) {
                        if (hasRating){
                            nProviderDatabase.child("aveRating").setValue(rateAve);
                        }

                    }else{
                     nProviderDatabase.child("aveRating").setValue(5); }


                    ///////////////////////////////////////////////////////////////////////Calculating Star Rating_END


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////Navigation Header Settings_END

        ////////////////////
        final DatabaseReference cancelRefere = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(userId).child("cancelCount");
        cancelRefere.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                }else{
                    cancelRefere.setValue(0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
///////////////////

        //    Calendar cal = Calendar.getInstance();
        //  Date currentLocalTime = calendar.getTime();
        //   dateToday = new SimpleDateFormat("MM-dd-yyyy").format(cal.getTime());

        workSchedule();
        getAssignedConsumer();

    }

    /////////////////////////////////////
    private View check_layout2;
    private int cancelCoun;

    private void serviceDoneConfirmation() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);

        if (pressControl.equals("onBackPressed")) {
            check_layout2 = inflater.inflate(R.layout.layout_message_onbackpressed, null);

        } else {
            check_layout2 = inflater.inflate(R.layout.layout_message_confirmserviceworkdone, null);

        }

        // View check_layout = inflater.inflate(R.layout.layout_message_blank, null);
        nNo = check_layout2.findViewById(R.id.doneNot);
        nYes = check_layout2.findViewById(R.id.doneYes);
        dialog.setView(check_layout2);
        // dialog.setView(R.layout.layout_message_confirmserviceworkdone);

        dialog.setCancelable(false);
        final AlertDialog dialogBuilder = dialog.show();


        ///////////////////////
        nYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();





                if (pressControl.equals("schedule")){
                    isSchedDoneConfirmed();
                }else if(pressControl.equals("normal")) {
                    isDoneConfirmed();
                }else if(pressControl.equals("cancel") || pressControl.equals("onBackPressed") ) {

                   isCancelled();
                }

            }

        });


        nNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
                status = 2;
             //   isSure = false;
                return;
            }
        });
    }

    ////////////////////////////////

    private void isCancelled() {

        cancelByProvider();
      //  endService();
    }
    //////////////////////


    //////////////////isSchedDoneConfirmed
    private void isSchedDoneConfirmed() {

        schedControl = "schedule";
        historyRecord();
        requestIsDone();
        endService();
    }

    //////////////////////////isDoneConfirmed
    private void isDoneConfirmed() {
//        if(isSure) {
            if (isSched.equals("on")) {
                pendingRecord();
            } else {
                historyRecord();
            }

            requestIsDone();
            endService();
 //       }
    }

    //////////////////////////////////

    /////////////////////////////////////acceptRequest
    private void acceptRequest() {

        //////removing value in the database
        if (userId != null){
            DatabaseReference acceptRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(userId).child("serviceRequest");
            acceptRef.removeValue();   //////removing value in the database

        }
    }
/////////////////////////////////////acceptRequest_END



    ///////////////////////////////////////////////////////////////////////////////////////////////workSchedule  scheduling
    private void workSchedule() {

        DatabaseReference sched = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(userId).child("WorkSchedule").child(dateToday);
     //   DatabaseReference sched = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(userId).child("WorkSchedule").child("08-15-2018");

        sched.addListenerForSingleValueEvent(new ValueEventListener() {

           @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    schedControl = "schedule";
                    //           Toast.makeText(ProviderMapActivity.this, "The Consumer has " + schedControl ,Toast.LENGTH_LONG).show(); //edit

                    // status = 2;


                   nServiceStatusCancel.setVisibility(View.GONE);
                   nServiceStatusAccept.setVisibility(View.GONE);
                    nServiceStatusSchedComplete.setVisibility(View.VISIBLE);
                    //////////////////////////////////////////////////////////////////////////////child.getKey().equals(" ")....string = child.getValue().toString();
                    for (DataSnapshot child: dataSnapshot.getChildren()) {

                        if(child.getKey().equals("consumerId")) {
                            conId = child.getValue().toString();
                            consumerChatId = conId;
                        }

                        if(child.getKey().equals("consumerName")) {
                            conName = child.getValue().toString();
                            consumerChatName = conName;
                        }

                        if(child.getKey().equals("consumerPhone")) {
                            conPhone = child.getValue().toString();
                            consumerChatPhone = conPhone;
                        }

                        if(child.getKey().equals("consumerServiceRequest")) {
                            conSerReq = child.getValue().toString();
                        }

                        if(child.getKey().equals("dateSched")) {
                            dateSch = child.getValue().toString();
                        }

                        if(child.getKey().equals("dateSched")) {
                            conHouseType = child.getValue().toString();
                        }


                        if(child.getKey().equals("houseLocation")) {
                            houseLoc = child.getValue().toString();
                        }

                        if(child.getKey().equals("payment")) {
                            paym = child.getValue().toString();
                        }

                        if(child.getKey().equals("paymentDigit")) {
                            paymDig = child.getValue().toString();
                            paymDigit = Float.parseFloat(paymDig);
                        }

                        if(child.getKey().equals("consumerNote")) {
                            conNote = child.getValue().toString();
                        }

                        if(child.getKey().equals("pendingId")) {
                            pendingProId = child.getValue().toString();
                        }

                        if(child.getKey().equals("timeRequest")) {
                            conTimeReq = child.getValue().toString();
                        }



                        if(child.getKey().equals("profileImageUrl")) {
                            profImage = child.getValue().toString();
                            nConsumerChatProfileImageUrl = profImage;
                        }

                    }

                    //    if(schedControl=="schedule"){
                    schedConsumerRequest();
                    autoSms();
                    //    }

                }else{
                    getAssignedConsumer();
                    // finish();
                }


                //    schedConsumerRequest();
                //  historyRecord();
                // endService();
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//////////////////////


    }




    ////////////////////////////
    private void autoSms() {
        try {
            String ContactNum = conPhone;
                  //*String subStrContact = ContactNum.substring(1);*//*
            String messageToSend = getString(R.string.autoSMS);

            String smsSent = "SMS SENT";
            String smsDelivered = "SMS DELIVERED";

            //---------------------- monitoring the status of sms notification ------------------//
            PendingIntent sentPI = PendingIntent.getBroadcast(ProviderMapActivity.this, 0, new Intent(smsSent), 0);
            PendingIntent deliveredPI = PendingIntent.getBroadcast(ProviderMapActivity.this, 0, new Intent(smsDelivered), 0);

            //Receiver for sent sms
            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    switch (getResultCode())
                    {
                        case Activity.RESULT_OK:
                            Toast toastSentNotif1 = Toast.makeText(ProviderMapActivity.this, "Customer SMS Notification has been sent", Toast.LENGTH_LONG);
                            toastSentNotif1.setGravity(Gravity.CENTER, 0, 0);
                            toastSentNotif1.show();
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            Toast toastSentNotif2 = Toast.makeText(ProviderMapActivity.this, "Unable to send SMS Notification"
                                    +"\ndue to Generic Failure", Toast.LENGTH_LONG);
                            toastSentNotif2.setGravity(Gravity.CENTER, 0, 0);
                            toastSentNotif2.show();
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            Toast toastSentNotif3 = Toast.makeText(ProviderMapActivity.this, "No Service available."
                                    +"\nUnable to send SMS Notification", Toast.LENGTH_LONG);
                            toastSentNotif3.setGravity(Gravity.CENTER, 0, 0);
                            toastSentNotif3.show();
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            Toast toastSentNotif4 = Toast.makeText(ProviderMapActivity.this, "Unable to send SMS Notification"
                                    +"\ndue to Null PDU", Toast.LENGTH_LONG);
                            toastSentNotif4.setGravity(Gravity.CENTER, 0, 0);
                            toastSentNotif4.show();
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            Toast toastSentNotif5 = Toast.makeText(ProviderMapActivity.this, "Radio Off Failure."
                                    + "\nUnable to send SMS Notification", Toast.LENGTH_LONG);
                            toastSentNotif5.setGravity(Gravity.CENTER, 0, 0);
                            toastSentNotif5.show();
                            break;
                    }
                }
            }, new IntentFilter(smsSent));

            //Receiver for delivered sms
            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    switch (getResultCode())
                    {
                        case Activity.RESULT_OK:
                            Toast.makeText(getBaseContext(), "Customer SMS Notification delivered",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case Activity.RESULT_CANCELED:
                            Toast.makeText(getBaseContext(), "Customer SMS Notification not delivered",
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }, new IntentFilter(smsDelivered));
            //---------------------- monitoring the status of sms notification ------------------//

            SmsManager.getDefault().sendTextMessage(ContactNum,
                    null, messageToSend, sentPI, deliveredPI);


        } catch (Exception e) {
            Toast toastNotification = Toast.makeText(ProviderMapActivity.this, e.getMessage().toString() + "\n"
                    + "Unable to send sms notification to customer", Toast.LENGTH_LONG);
            toastNotification.setGravity(Gravity.CENTER, 0, 0);
            toastNotification.show();
        }

    }
    ////////////////////////////////////////////



/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////workSchedule  scheduling_END

    /////////////////////////////////////////////////////////////
    DatabaseReference workRef;
    private void schedConsumerRequest() {
        /////////////////////////////////////////
        schedControl = "schedule";

        String providerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        workRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(providerId).child("consumerRequest");

        workRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                }else{
                    // Map<String, Object> workmap = new HashMap<>();
                    HashMap workmap = new HashMap();
                    workmap.put("consumerId", conId);
                    workmap.put("consumerName", conName);
                    workmap.put("consumerPhone", conPhone);
                    workmap.put("consumerServiceRequest", conSerReq);
                    workmap.put("houseLocation", houseLoc);
                    workmap.put("payment", paym);
                    workmap.put("dateSched", dateSch);
                    workmap.put("houseType", conHouseType);
                    workmap.put("timeRequest", conTimeReq );
                    workmap.put("consumerNote", conNote );
                    workmap.put("profileImageUrl", profImage );
                    //      map.put("houseLocationLat", destinationLatLng.latitude);
                    //      map.put("houseLocationLng", destinationLatLng.longitude);
                    workRef.updateChildren(workmap);



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });


        ///////////////////////////////////////////


        // getRouteToMarker(houseLatLng);

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////workSchedule  scheduling_END


    ////////////////////////////////////////////////////////////////working



    private void working() {

        LatLng latlng = new LatLng(nLastLocation.getLatitude(), nLastLocation.getLongitude());
        nMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        nMap.moveCamera(CameraUpdateFactory.zoomTo(16));

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference refWorking = FirebaseDatabase.getInstance().getReference("providerWorking");
        GeoFire geoFireWorking = new GeoFire(refWorking);

        if (providerMarker != null) {
            providerMarker.remove();
        }
        geoFireWorking.setLocation(userId, new GeoLocation(nLastLocation.getLatitude(), nLastLocation.getLongitude()));
        providerMarker = nMap.addMarker(new MarkerOptions().position(latlng).title("Service Provider").icon(BitmapDescriptorFactory.fromResource(R.mipmap.fixit_provider)));       // #13 Change Marker  ++++++++++++
    }

    /////////////////////////////////////////////////////////////



    //////////////////////////////////////////////////requestIsDone
    private void requestIsDone() {
        if (userId != null){  //////removing value in the database
            state = 2;
            DatabaseReference doneRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(userId).child("requestDone");
            doneRef.removeValue();   //////removing value in the database

        }
    }

    //////////////////////////////////////////////cancelByProvider
   // private DatabaseReference cancelRefer;
    private void cancelByProvider() {
        if (userId != null){  //////removing value in the database
            state = 1;
            DatabaseReference conRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(userId).child("cancelRequest");
            conRef.removeValue();   //////removing value in the database

            ///////////////
      //      final DatabaseReference cancelRefer = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(userId);
          final DatabaseReference  cancelRefer = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(userId);

            cancelRefer.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
          //          java.util.Map<String, Object> map = (java.util.Map<String, Object>) dataSnapshot.getValue();
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    if (dataSnapshot.exists()) {

                        if (map.get("cancelCount") != null) {
                            cancelCoun = Integer.valueOf(map.get("cancelCount").toString());
                            cancelCoun = cancelCoun + 1;

                            HashMap cancelMap = new HashMap();
                            cancelMap.put("cancelCount", cancelCoun);
                            cancelRefer.updateChildren(cancelMap);


                            if(cancelCoun >= 6){
                                accountIsBlocked();
                            }else if(cancelCoun == 5 ){
                                blockAccountReminder();
                            }else if(cancelCoun == 4 ){
                                oneMonthSuspension();
                            }else if(cancelCoun == 3 ){
                                suspensionReminder();
                            }else if(cancelCoun == 2) {
                                avoidCancellationReminder();
                            }else {
                                endService();
                            }

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
////////////////

        }


    }

    private void accountIsBlocked() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View check_layout = inflater.inflate(R.layout.layout_message_cancelcountsix, null);
        dialog.setView(check_layout);
        dialog.setCancelable(false);

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                isAccountHasIssue = true;
                endService();

            }
        });
        dialog.show();
    }
    private void blockAccountReminder() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View check_layout = inflater.inflate(R.layout.layout_message_cancelcountfive, null);
        dialog.setView(check_layout);
        dialog.setCancelable(false);

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                isAccountClear = true;
                endService();

            }
        });
        dialog.show();
    }

    ////////////////////////////////////
    private void oneMonthSuspension() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 30);
        final String dateSuspension  = new SimpleDateFormat("MM-dd-yyyy").format(cal.getTime());

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View check_layout = inflater.inflate(R.layout.layout_message_cancelcountfour, null);
        dialog.setView(check_layout);
        dialog.setCancelable(false);

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                DatabaseReference suspendRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(userId).child("accountSuspended");
                suspendRef.setValue(dateSuspension);

                isAccountHasIssue = true;
                endService();


            }
        });
        dialog.show();
    }


    /////////////////////////////////////
    private void suspensionReminder() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View check_layout = inflater.inflate(R.layout.layout_message_cancelcountthree, null);
        dialog.setView(check_layout);
        dialog.setCancelable(false);

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                isAccountClear = true;
                endService();


            }
        });
        dialog.show();
    }
    /////////////////////////////
    private void avoidCancellationReminder() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View check_layout = inflater.inflate(R.layout.layout_message_cancelcounttwo, null);
        dialog.setView(check_layout);
        dialog.setCancelable(false);

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                isAccountClear = true;
                endService();


            }
        });
        dialog.show();
    }
    ////////////////////////////////////



    /////////////////////////////////////////////////////////onCreate_END


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////endService
    private void endService() {
///////////////////////////////////////////////////////////////////////



        if (schedControl.equals("schedule")){
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            ////removing the today schedule
           DatabaseReference Consusched = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(userId).child("WorkSchedule").child(dateToday);
      //        DatabaseReference Consusched = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(userId).child("WorkSchedule").child("08-15-2018");
            Consusched.removeValue();

            ////removing the consumerRequest
            DatabaseReference ProPendingRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(userId).child("pending").child(pendingProId);
            ProPendingRef.removeValue();   //////removing value in the database

            DatabaseReference providerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(userId).child("consumerRequest");
            providerRef.removeValue();   //////removing value in the database


            if (isAccountHasIssue){
                Intent intent = new Intent(ProviderMapActivity.this, WelcomeMainProvider.class);
                startActivity(intent);
                finish();
            }

                if (state==1 && isAccountClear){
                state = 0;
                ///////Intent to itself
                Intent intent= getIntent();
                startActivity(intent);

            }else if (state==2){
                state = 0;
                Intent intent = new Intent(ProviderMapActivity.this, WelcomeMainProvider.class);
                startActivity(intent);
            }


            if (deviceConsumerMarker != null) {    //////////////////////
                deviceConsumerMarker.remove();
            }

            if (assignedConsumerDeviceLocationRefListener != null) {    // #12 Canceling-an--Request @@@@@@@    bugs fix...adding if statement to remove the EventListener
                assignedConsumerDeviceLocationRef.removeEventListener(assignedConsumerDeviceLocationRefListener);    // #12 Canceling-an--Request @@@@@@@  removing EventListener
            }

      //      nConsumerInfo.setVisibility(View.VISIBLE);

          nConsumerInfo.setVisibility(View.GONE);         //#17 Display Consumer Info in Providers Screen... // if the Provider is not already working or not working.
            nConsumerName.setText("");                    //#17 Display Consumer Info in Providers Screen..
            nConsumerPhone.setText("");
            nConsumerRequest.setText("");
            nConsumerDate.setText("");
            nConsumerPayment.setText("");
            nConsumerNote.setText("");
            nConsumerProfileImage.setImageResource(R.mipmap.ic_profileimage);      //#17 Display Consumer Info in Providers Screen..
            nConsumerHouse.setText("");     //#18 place-autocomplete API........
            schedControl = "";
            isSched = "";
            consumerId = "";
            pressControl = "";




        }else{

            nServiceStatusAccept.setText("ACCEPT");
            erasePolylines();
            isSched = "";
            schedControl = "";

            if (isAccountHasIssue){
                Intent intent = new Intent(ProviderMapActivity.this, WelcomeMainProvider.class);
                startActivity(intent);
                finish();
            }

            if (state==1 && isAccountClear){
                serviceHasEnded.removeEventListener(serviceHasEndedListener);
                Intent intent= getIntent();
                startActivity(intent);

            }else if (state==2){
                state = 0;
                Intent intent = new Intent(ProviderMapActivity.this, WelcomeMainProvider.class);
                startActivity(intent);
            }

            //delete consumer Request
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference providerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(userId).child("consumerRequest");
            providerRef.removeValue();   //////removing value in the database


            //delete consumer Location
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("consumerRequest");
            GeoFire geoFire = new GeoFire(ref);
            geoFire.removeLocation(consumerId);
            consumerId = "";

            if (deviceConsumerMarker != null) {    //////////////////////
                deviceConsumerMarker.remove();
            }

            if (assignedConsumerDeviceLocationRefListener != null) {    // #12 Canceling-an--Request @@@@@@@    bugs fix...adding if statement to remove the EventListener
                assignedConsumerDeviceLocationRef.removeEventListener(assignedConsumerDeviceLocationRefListener);    // #12 Canceling-an--Request @@@@@@@  removing EventListener
            }
        //    nConsumerInfo.setVisibility(View.VISIBLE);
            nConsumerInfo.setVisibility(View.GONE);         //#17 Display Consumer Info in Providers Screen... // if the Provider is not already working or not working.
            nConsumerName.setText("");                    //#17 Display Consumer Info in Providers Screen..
            nConsumerPhone.setText("");
            nConsumerRequest.setText("");
            nConsumerDate.setText("");
            nConsumerPayment.setText("");
            nConsumerNote.setText("");
            nConsumerProfileImage.setImageResource(R.mipmap.ic_profileimage);      //#17 Display Consumer Info in Providers Screen..
            nConsumerHouse.setText("");     //#18 place-autocomplete API........
            pressControl = "";



        }

/////////////////////////////////////////////////////////////////////////////
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////endService_END













    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////history/pendingRecord  ...saving into the main "History/Pending" database
    DatabaseReference consumerHistoryRef;

    private void historyRecord() {

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference providerHistoryRef;
        DatabaseReference historyRef;
        DatabaseReference ServicesPerformed;


        providerHistoryRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(userId).child("history");
        historyRef = FirebaseDatabase.getInstance().getReference().child("History");
        ServicesPerformed = FirebaseDatabase.getInstance().getReference().child("ServicesPerformed");


        /////////////////


            if (schedControl.equals("schedule")){
                //   Toast.makeText(ProviderMapActivity.this, "History Schedule",Toast.LENGTH_LONG).show(); //edit
                try{
                consumerHistoryRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(conId).child("history");
                }catch(Exception e){
                    Toast.makeText(this, "Failed: The consumer is OFFLINE", Toast.LENGTH_SHORT).show();
                }

            }else{
                consumerHistoryRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(consumerId).child("history");
            }








        requestId = historyRef.push().getKey();        ///////creating distinct Key

        providerHistoryRef.child(requestId).setValue(true);
        consumerHistoryRef.child(requestId).setValue(true);



        ///////////////////////////////////////////validation date
        Calendar cal = Calendar.getInstance();
        //  Date currentLocalTime = calendar.getTime();
        final String date = new SimpleDateFormat("MM-dd-yyyy").format(cal.getTime());

        HashMap map = new HashMap();

        if (isSched.equals("off") ||schedControl.equals("schedule") ){
            map.put("dateDone",date);
        }else{
            map.put("dateDone","pending");
        }


        if (isSched.equals("off") ){
            map.put("payment",paymentTobe);
            //           map.put("dateDone",date);
        }else if (schedControl.equals("schedule")){
            map.put("payment",paym);
            //           map.put("dateDone",date);
        }else{
            map.put("payment","pending");

        }


        map.put("provider", userId);
        map.put("providerName", schedProName);
        map.put("providerPhone", schedProPhone);
        map.put("consumer", consumerId);
        map.put("consumerName", schedConName);
        map.put("consumerPhone", schedConPhone);
        map.put("rating", 0);
        map.put("timestamp", getCurrentTimeStamp() );   //the Value is in the getCurrentTimeStamp function,
        map.put("location",houseToWork );
        //   map.put("dateSched",nServiceDate.toString() );
        map.put("dateSched",dateWork);
        map.put("timeRequest",timeWork);
        map.put("locationMap/providerLocationMap/latitude",latlng.latitude);
        map.put("locationMap/providerLocationMap/longitude",latlng.longitude);
        map.put("locationMap/consumerLocationMap/latitude",houseLatLng.latitude);
        map.put("locationMap/consumerLocationMap/longitude",houseLatLng.longitude);


        historyRef.child(requestId).updateChildren(map);


        HashMap Servicesmap = new HashMap();
        Servicesmap.put("AddressWorkDone",houseToWork);//
        Servicesmap.put("Code",requestId);
        Servicesmap.put("ConsumerID",conEmail);//
        Servicesmap.put("ConsumerName",schedConName);//
        Servicesmap.put("Date",dateWork);//
        Servicesmap.put("ConsumerProfileImage",schedConProfileImage);//


        if (isSched.equals("off") ){
            Servicesmap.put("Payment",paymentTobeDigit);//
        }else {
            Servicesmap.put("Payment",paymDigit);//
        }
        Servicesmap.put("ProviderProfileImage",schedProProfileImage);//
        Servicesmap.put("ProviderID",proUsername);//
        Servicesmap.put("ProviderName",schedProName);//
        Servicesmap.put("ServiceType",proServiceType);//
        Servicesmap.put("Time",timeWork);//

        ServicesPerformed.child(requestId).updateChildren(Servicesmap);



        ///////////////////////////////////////////validation date
    }



    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////historyRecord -END


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////pendingRecord  ...saving into the main "Pending" database

    private void pendingRecord() {

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference pendingRef = FirebaseDatabase.getInstance().getReference().child("Pending");

        DatabaseReference providerPendingRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(userId).child("pending");
        DatabaseReference consumerPendingRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(consumerId).child("pending");


        //     DatabaseReference schedConRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(consumerId).child("RequestServiceSchedule").child(dateWork);
        //      DatabaseReference schedProRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(userId).child("WorkSchedule").child(dateWork);


        requestId = pendingRef.push().getKey();        ///////creating distinct Key

        providerPendingRef.child(requestId).setValue(true);
        consumerPendingRef.child(requestId).setValue(true);


        HashMap mapPendingSched = new HashMap();

        // mapProSched.put("serviceID", serviceId);
        mapPendingSched.put("consumerId", consumerId);
        mapPendingSched.put("providerId", userId);
        mapPendingSched.put("consumerName",schedConName );
        mapPendingSched.put("consumerPhone", schedConPhone);
        mapPendingSched.put("consumerServiceRequest", consumerRequest);
        mapPendingSched.put("houseLocation", houseToWork);
        mapPendingSched.put("payment", paymentTobe);
        mapPendingSched.put("dateSched", dateWork);
        mapPendingSched.put("timeRequest", timeWork);
        mapPendingSched.put("consumerNote", consuNote);
        mapPendingSched.put("profileConImageUrl", schedConProfileImage);
        mapPendingSched.put("providerName",schedProName );
        mapPendingSched.put("providerAge",schedProAge );
        mapPendingSched.put("providerPhone", schedProPhone);
        mapPendingSched.put("profileProImageUrl", schedProProfileImage);

        pendingRef.child(requestId).updateChildren(mapPendingSched);

        //    HashMap mapConSched = new HashMap();

        //    mapConSched.put("serviceID", serviceId);
        //   mapConSched.put("providerId", providerFoundID);
        //   mapConSched.put("consumerId", userId);

        //     mapConSched.put("consumerServiceRequest", nRequestServiceType);
        //    mapConSched.put("houseLocation", nAddress);
        //   mapConSched.put("charge", nPayment);
        //    mapConSched.put("serviceDateToWork", nServiceDate);
        //    mapConSched.put("aveRating", ratingAverage);
        //  mapConSched.put("consumerNote", nConsumerNote);

        //   schedConRef.updateChildren(mapConSched);



        //    private String , , , , , , ;




/////////     consumerName, consumerPhone, , , , , , ,   , providerName, providerPhone;






////////////////

    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////pendingRecord -END


    ////////////////////////////////////////////////////////////////////////////////////////////////////////getCurrentTimeStamp
    private Long getCurrentTimeStamp() {        //note: Long dataTypes, coz it is a long number
        Long timestamp = System.currentTimeMillis() / 1000;          //note: dividing into 1000
        return timestamp;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////getCurrentTimeStamp_END

    DatabaseReference providerReferen;
    ///////////////////////////////////////////////////////////////////////////// 1st without buttonClick  getAssignedConsumer
    public void getAssignedConsumer() {

        createRequestDone();

        String providerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference assignedConsumerRef;

        assignedConsumerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(providerId).child("consumerRequest").child("consumerId");




        assignedConsumerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {               // Note: if dataSnapshot is exist it means that there is current job ongoing...
                if (dataSnapshot.exists()) {                      // #12 Canceling-an-Request @@@@@@@       if the child(consumerId) is removed this dataSnapshot.exists() will be false (else)
                    status = 1;

                    consumerId = dataSnapshot.getValue().toString();
                    consumerChatId = consumerId;


                    if (schedControl.equals("schedule")){
                        // Toast.makeText(ProviderMapActivity.this, "consumerRequest ON TIME EXIST",Toast.LENGTH_LONG).show(); //edit
                        //call a function
                        getAssignedConsumerDeviceLocation();
                        getAssignedConsumerHouseAddress();  //#18 place-autocomplete API.... method for consumerHouse Location ..
                        getAssignedConsumerInfo();
                    }else{
                        // Toast.makeText(ProviderMapActivity.this, "DATE TODAY consumerRequest ",Toast.LENGTH_LONG).show(); //edit

                        //call a function
                        getAssignedConsumerDeviceLocation();
                        getAssignedConsumerHouseAddress();  //#18 place-autocomplete API.... method for consumerHouse Location ..
                        getAssignedConsumerInfo();
                        getHasServiceEnded();   ///tobe added if Cancellation is needed in schedule
                    }







                } else if (schedControl.equals("schedule")){
                    //              Toast.makeText(ProviderMapActivity.this, "consumerRequest LATE EXIST",Toast.LENGTH_LONG).show(); //edit

                }else{                                         // #12 Canceling-an--Request @@@@@@@        checking if the child(consumerId) is removed.
                    endService();

                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void createRequestDone() {
        ////////////////////////////////////////////////////////////schedule control
       //will use as a Token
        final String providerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        providerReferen = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(providerId).child("requestDone");
        providerReferen.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                }else{
                    HashMap donemap = new HashMap();
                    donemap.put("consumerServiceRequest", conSerReq);
                    providerReferen.updateChildren(donemap);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        /////////////////////////////////////////////////////////schedule control
    }

/*//////////////////////////////////////////////////////////////////////////////////////////getHasServiceEnded
private DatabaseReference serviceHasEndedRef;
    private ValueEventListener serviceHasEndedRefListener;
    private void getHasServiceEnded() {
        serviceHasEndedRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(consumerId).child("consumerRequest");
        serviceHasEndedRefListener = serviceHasEndedRef.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {


                } else {
                    Toast.makeText(ProviderMapActivity.this, "The Consumer has CANCELLED the request ! ",Toast.LENGTH_LONG).show(); //edit
                    endService();
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
*/ ///////////////////////////////////////////////////////////////////////////////////////////



  /*  //////////////////////////////////////////////////////////////////////////////////////////5th withoutButtonClick getHasServiceEnded
    private DatabaseReference serviceHasEnded;
    private ValueEventListener serviceHasEndedListener;
    private void getHasServiceEnded() {

        if (schedControl.equals("schedule")){
            serviceHasEnded = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(conId).child("cancelRequest");
        }else{
            serviceHasEnded = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(consumerId).child("cancelRequest");
        }


        serviceHasEndedListener = serviceHasEnded.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {


         //       } else {
               //   Toast.makeText(ProviderMapActivity.this, "cancelRequest Exist ",Toast.LENGTH_LONG).show(); //edit
         //           state = 1;
         //           endService();
                } else  if (schedControl.equals("schedule")){
                    ///////////////
                    Toast.makeText(ProviderMapActivity.this, "The Consumer has CANCELLED the request ! ", Toast.LENGTH_SHORT).show(); //edit
                    serviceHasEnded.removeEventListener(serviceHasEndedListener);
                    state = 1;
                    endService();
                }else{
                    Toast.makeText(ProviderMapActivity.this, "The Consumer has CANCELLED the request ! ", Toast.LENGTH_SHORT).show(); //edit
                    serviceHasEnded.removeEventListener(serviceHasEndedListener);
                    state = 1;
                    endService();
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    ///////////////////////////////////////////////////////////////////////////////////////////   */


    //////////////////////////////////////////////////////////////////////////////////////////5th withoutButtonClick getHasServiceEnded
    private DatabaseReference serviceHasEnded;
    private ValueEventListener serviceHasEndedListener;
    private void getHasServiceEnded() {

        if (schedControl.equals("schedule")){
            serviceHasEnded = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(conId).child("cancelRequest");
        }else{
            serviceHasEnded = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(consumerId).child("cancelRequest");
        }

        serviceHasEndedListener=  serviceHasEnded.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {


                } else  if (schedControl.equals("schedule")){
                    ///////////////
                    if(!isPosted){
                        isPosted = true;
                        Toast.makeText(ProviderMapActivity.this, "The Consumer has CANCELLED the request ! ", Toast.LENGTH_SHORT).show(); //edit
                    }

                    state = 1;
                    serviceHasEnded.removeEventListener(serviceHasEndedListener);
                    endService();
                }else{
                    if(!isPosted){
                        isPosted = true;
                        Toast.makeText(ProviderMapActivity.this, "The Consumer has CANCELLED the request ! ", Toast.LENGTH_SHORT).show(); //edit
                    }                    state = 1;
                    serviceHasEnded.removeEventListener(serviceHasEndedListener);
                    endService();

                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    ///////////////////////////////////////////////////////////////////////////////////////////


//////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////2ndd without ButtonCLick getAssignedConsumerPickupLocation
    // Marker deviceConsumerMarker;
    private DatabaseReference assignedConsumerDeviceLocationRef;
    private ValueEventListener assignedConsumerDeviceLocationRefListener;

    public void getAssignedConsumerDeviceLocation() {



            /////////////////////map Reconnection
            if (schedControl.equals("schedule")){
                assignedConsumerDeviceLocationRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(conId).child("RequestServiceSchedule").child(dateToday).child("locationMap").child(conId).child("l");

            }else{

                assignedConsumerDeviceLocationRef = FirebaseDatabase.getInstance().getReference().child("consumerRequest").child(consumerId).child("l");

            }



        assignedConsumerDeviceLocationRefListener = assignedConsumerDeviceLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                if (dataSnapshot.exists() && !consumerId.equals("")) {
                    List<Object> map = (List<Object>) dataSnapshot.getValue();     //use List in array
                    double locationLat = 0;
                    double locationLng = 0;

                    if (map.get(0) != null) {
                        locationLat = Double.parseDouble(map.get(0).toString());
                    }

                    if (map.get(1) != null) {
                        locationLng = Double.parseDouble(map.get(1).toString());
                    }

                    houseLatLng = new LatLng(locationLat, locationLng);


                    if (deviceConsumerMarker != null) {
                        deviceConsumerMarker.remove();
                    }


                    deviceConsumerMarker = nMap.addMarker(new MarkerOptions().position(houseLatLng).title("Consumer House").icon(BitmapDescriptorFactory.fromResource(R.mipmap.fixit_house)));

                           getRouteToMarker(houseLatLng);///////////  skip for meantime for destination
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    /////////////////////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////////////////////////////getRouteToMarker    ...compile 'com.github.jd-alexander:library:1.1.0'

    private void getRouteToMarker(LatLng houseLatLng) {

        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(new LatLng(nLastLocation.getLatitude(), nLastLocation.getLongitude()), houseLatLng)
                .build();
        routing.execute();
    }
    ///////////////////////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////////////3rd without ButtonCLick getAssignedConsumerHouseAddress
    public void getAssignedConsumerHouseAddress() {
        String providerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //   final DatabaseReference assignedConsumerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(providerId).child("consumerRequest").child("houseLocation");
        final DatabaseReference assignedConsumerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(providerId).child("consumerRequest");

        //  assignedConsumerRef.addValueEventListener(new ValueEventListener() {
        assignedConsumerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();


                    if (map.get("houseLocation") != null) {
                        houseToWork = map.get("houseLocation").toString();
                        nConsumerHouse.setText(houseToWork);

                    } else {
                        nConsumerHouse.setText("House: Not provided..");
                    }
                    if (map.get("dateSched") != null) {
                        dateWork = map.get("dateSched").toString();
                        nConsumerDate.setText(dateWork);

                        /////////
                        if (dateWork.equals(dateToday)) {
                            isSched = "off";
                            //            Toast.makeText(ProviderMapActivity.this, "OFF" ,Toast.LENGTH_SHORT).show(); //edit
                        } else {
                            isSched = "on";
                            //              Toast.makeText(ProviderMapActivity.this, "ON",Toast.LENGTH_SHORT).show(); //edit
                        }
                        /////////

                    } else {
                        nConsumerDate.setText("Date: Not provided..");
                    }

                    if (map.get("timeRequest") != null) {
                        timeWork = map.get("timeRequest").toString();
                        nConsumerTime.setText(timeWork);

                    } else {
                        nConsumerTime.setText("Date: Not provided..");
                    }

                    if (map.get("payment") != null) {
                        paymentTobe = map.get("payment").toString();
                        nConsumerPayment.setText(paymentTobe);
                    } else {
                        nConsumerPayment.setText("Charged Not provided..");
                    }

                    if (map.get("paymentDigit") != null) {
                        paymentTobeDig = map.get("paymentDigit").toString();
                        paymentTobeDigit = Float.parseFloat(paymentTobeDig);

                    }

                    if (map.get("consumerServiceRequest") != null) {
                        consumerRequest = map.get("consumerServiceRequest").toString();
                        nConsumerRequest.setText(consumerRequest);

                    } else {
                        nConsumerRequest.setText("Service Type Not provided..");
                    }

                    if (map.get("consumerNote") != null) {
                        consuNote = map.get("consumerNote").toString();
                        nConsumerNote.setText(consuNote);

                    } else {
                        nConsumerNote.setText("Note: Not provided..");
                    }



                    if (map.get("houseType") != null) {
                        consuHouseType = map.get("houseType").toString();
                        nHouseType.setText(consuHouseType);

                    } else {
                        nHouseType.setText("Note: Not provided..");
                    }

                    //            if (map.get("serviceAdditionalDetails") != null) {
                    //                serviceDetails = map.get("serviceAdditionalDetails").toString();
                    //               nServiceAdditionalDetails.setText("Service Details: " + serviceDetails);
                    //            }

                    int x = 0;

                    if (map.get("itemOne") != null) {
                        nItemOne.setVisibility(View.VISIBLE);
                        itemLabel1.setVisibility(View.VISIBLE);
                        item_one = map.get("itemOne").toString();
                        x = x + 1;
                        nItemOne.setText("No."+x +" "+ item_one);
                    }

                    if (map.get("itemTwo") != null) {
                        nItemTwo.setVisibility(View.VISIBLE);
                        itemLabel2.setVisibility(View.VISIBLE);
                        item_two = map.get("itemTwo").toString();
                        x = x + 1;
                        nItemTwo.setText("No."+x +" "+ item_two);

                        //   nItemTwo.setText("Work Details: " + item_two);
                    }

                    if (map.get("itemThree") != null) {
                        nItemThree.setVisibility(View.VISIBLE);
                        itemLabel3.setVisibility(View.VISIBLE);
                        item_three = map.get("itemThree").toString();
                        x = x + 1;
                        nItemThree.setText("No."+x +" "+ item_three);
                    }

                    if (map.get("itemFour") != null) {
                        nItemFour.setVisibility(View.VISIBLE);
                        itemLabel4.setVisibility(View.VISIBLE);
                        item_four = map.get("itemFour").toString();
                        x = x + 1;
                        nItemFour.setText("No."+x +" "+ item_four);
                }

                    if (map.get("itemFive") != null) {
                        nItemFive.setVisibility(View.VISIBLE);
                        itemLabel5.setVisibility(View.VISIBLE);
                        item_five = map.get("itemFive").toString();
                        x = x + 1;
                        nItemFive.setText("No."+x +" "+ item_five);
                    }

                    Double houseLat = 0.0;
                    Double houseLng = 0.0;
                    if (map.get("houseLocationLat") != null) {
                        houseLat = Double.valueOf(map.get("houseLocationLat").toString());
                    }

                    if (map.get("houseLocationLng") != null) {
                        houseLng = Double.valueOf(map.get("houseLocationLng").toString());
                    }

                    destinationLatLng = new LatLng(houseLat, houseLng);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    ///////////////////////////////////////////////////////////////////////////////////

    //             map.put("houseLocation", houseLocation);
    //     map.put("houseLocationLat", destinationLatLng.latitude);
    //     map.put("houseLocationLng", destinationLatLng.longitude);

    ///////////////////////////////////////////////////////////////////////////////////////////////4th without ButtonCLick getAssignedConsumerInfo
    private void getAssignedConsumerInfo() {

        try{
            if (hasRequest){
                serviceRequestFound();
            }
        }catch(Exception e){
            e.printStackTrace();
        }


        isPosted = false;
        nConsumerInfo.setVisibility(View.VISIBLE);
        DatabaseReference nConsumerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(consumerId);
        nConsumerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    if (map.get("name") != null) {
                        schedConName = map.get("name").toString();
                        consumerChatName = schedConName;
                        //   nConsumerName.setText("Consumer Name: "+ map.get("name").toString());
                        nConsumerName.setText(map.get("name").toString());

                    }


                    if (map.get("phone") != null) {
                        schedConPhone = map.get("phone").toString();
                        consumerChatPhone = schedConPhone;
                        nConsumerPhone.setText(map.get("phone").toString());

                    }

                    if (map.get("email") != null) {
                        conEmail = map.get("email").toString();

                    }

                    if (map.get("profileImageUrl") != null) {
                        schedConProfileImage = map.get("profileImageUrl").toString();
                        nConsumerChatProfileImageUrl = schedConProfileImage;

                        if (map.get("profileImageUrl").equals("")) {
                            nConsumerProfileImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_profileimage));
                        }else{
                            //  schedProProfileImage = map.get("profileImageUrl").toString();
                            Glide.with(getApplication()).load(map.get("profileImageUrl").toString()).into(nConsumerProfileImage);
                        }
                    }else{
                        nConsumerProfileImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_profileimage));
                    }





                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    //////////////////////////
    private View check_layout;
    private void serviceRequestFound() {

        hasRequest = false;

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        if (schedControl.equals("schedule")){
             check_layout = inflater.inflate(R.layout.layout_message_scheduledatearriveprovider, null);

        }else {
             check_layout = inflater.inflate(R.layout.layout_message_servicerequestfound, null);

        }


        dialog.setView(check_layout);
        dialog.setCancelable(false);

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();



            }
        });
        dialog.show();
    }
///////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////onLocationChanged
    @Override
    public void onLocationChanged(Location location) {
        if (getApplicationContext() != null) {

            //provider location
            nLastLocation = location;
            latlng = new LatLng(location.getLatitude(), location.getLongitude());
            nMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
            nMap.moveCamera(CameraUpdateFactory.zoomTo(15));

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            DatabaseReference refAvailable = FirebaseDatabase.getInstance().getReference("providerAvailable");
            DatabaseReference refWorking = FirebaseDatabase.getInstance().getReference("providerWorking");


            GeoFire geoFireAvailable = new GeoFire(refAvailable);
            GeoFire geoFireWorking = new GeoFire(refWorking);

            if (providerMarker != null) {
                providerMarker.remove();
            }


            switch (consumerId) {

                case "ok":
                    geoFireAvailable.removeLocation(userId);
                    geoFireWorking.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()));
                    providerMarker = nMap.addMarker(new MarkerOptions().position(latlng).title("Service Provider").icon(BitmapDescriptorFactory.fromResource(R.mipmap.fixit_provider)));       // #13 Change Marker  ++++++++++++
                    break;

                case "":
                    geoFireWorking.removeLocation(userId);
                    geoFireAvailable.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()));
                    providerMarker = nMap.addMarker(new MarkerOptions().position(latlng).title("Service Provider").icon(BitmapDescriptorFactory.fromResource(R.mipmap.fixit_provider)));       // #13 Change Marker  ++++++++++++
                    break;

                default:
                    geoFireAvailable.removeLocation(userId);
                    geoFireWorking.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()));
                    providerMarker = nMap.addMarker(new MarkerOptions().position(latlng).title("Service Provider").icon(BitmapDescriptorFactory.fromResource(R.mipmap.fixit_provider)));       // #13 Change Marker  ++++++++++++

                    break;
            }
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////onConnected
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        nLocationRequest = new LocationRequest();
        nLocationRequest.setInterval(2000);
        nLocationRequest.setFastestInterval(2000);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ProviderMapActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            // return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(nGoogleApiClient, nLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    ///////////////////////////////////////////////////////////////////////////////////////////////


    /////////////////////////onRequestPermissionsResult
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    /////adjustment codes added_ re: pullup layout
                    try {
                        mapFragment.getMapAsync(this);

                    } catch (Exception e) {
                        e.printStackTrace();
                        finish();
                        startActivity(getIntent());

                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Please provide permission", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
    ////////////////////////

    ////////////////////////////////////////////////////////////////disconnectProvider
    private void disconnectProvider() {

        if (nGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(nGoogleApiClient, this);

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("providerAvailable");

            GeoFire geoFire = new GeoFire(ref);
            geoFire.removeLocation(userId);
        }
    }
    ////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////onMapReady
    @Override
    public void onMapReady(GoogleMap googleMap) {
        nMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ProviderMapActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            // return;
        }
        buildGoogleApiClient();
        nMap.setMyLocationEnabled(true);


    }

    /////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////buildGoogleApiClient
    protected synchronized void buildGoogleApiClient() {
        nGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        nGoogleApiClient.connect();

    }
    ////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onStop() {
        super.onStop();
        if (!isLoggingOut) {
            disconnectProvider();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        isLoggingOut = false;
    }
    ////////////////////////////////////////////////////////////////////////////////////

 /*   //////////////////////////////////////////////onStart
   @Override
 protected void onStart() {


       super.onStart();

  }
    //////////////////////////////////////////////
*/

    //////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////RoutingListeners methods
    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.primary_dark_material_light};

    @Override
    public void onRoutingFailure(RouteException e) {
        if (e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) { /////////////////////////Drawing a Route

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(latlng);
        builder.include(houseLatLng);
        LatLngBounds bounds = builder.build();
        ///padding
        int width = getResources().getDisplayMetrics().widthPixels;
        int padding = (int)(width * 0.1);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds,padding);

        //camera animate
        nMap.animateCamera(cameraUpdate);
        nMap.addMarker(new MarkerOptions().position(houseLatLng).title("Consumer House").icon(BitmapDescriptorFactory.fromResource(R.mipmap.fixit_house)));       // #13 Change Marker  ++++++++++++
        nMap.addMarker(new MarkerOptions().position(latlng).title("Service Provider").icon(BitmapDescriptorFactory.fromResource(R.mipmap.fixit_provider)));       // #13 Change Marker  ++++++++++++





        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i < route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = nMap.addPolyline(polyOptions);
            polylines.add(polyline);

            //       Toast.makeText(getApplicationContext(), "Route " + (i + 1) + ": distance - " + route.get(i).getDistanceValue() + ": duration - " + route.get(i).getDurationValue(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingCancelled() {
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////RoutingListeners methods _ENDS

    /////////////////////////////////////////////////////////////////////////////////////// Erasing Polylines
    public void erasePolylines() {
        for (Polyline line : polylines) {
            line.remove(); ////removing each lines

        }
        polylines.clear(); ////clearing the polylines array
    }
    ///////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////Part of Navigation Bar Menu
    @Override
    public void onBackPressed() {

   //     DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

  //      if (drawer.isDrawerOpen(GravityCompat.START)) {
  //          drawer.closeDrawer(GravityCompat.START);
 //       }


        try{

            DatabaseReference refWork= FirebaseDatabase.getInstance().getReference().child("providerWorking").child(userId);
            refWork.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        pressControl = "onBackPressed";
                        serviceDoneConfirmation();
                    }else{
                        Intent intent = new Intent(ProviderMapActivity.this, WelcomeMainProvider.class);
                        startActivity(intent);
                        finish();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }

    //    super.onBackPressed();

     //   super.onBackPressed();
        //       DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //       if (drawer.isDrawerOpen(GravityCompat.START)) {
        //          drawer.closeDrawer(GravityCompat.START);
        //      } else {
        //         super.onBackPressed();
        //     }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //   if (id == R.id.action_settings) {
        //         return true;
        //    }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_account) {
            accountProvider();
        } else if (id == R.id.nav_history) {
            historyDetails();
        }else if (id == R.id.nav_pending) {
            pendingDetails();
        }else if (id == R.id.nav_changePassword) {
            changePassword();
        } else if (id == R.id.nav_logout) {
            signOut();
        }
        //else if (id == R.id.nav_manage) {

        // } else if (id == R.id.nav_share) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerProvider_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////changePassword
private void changePassword() {

    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("CHANGE PASSWORD");
    //  dialog.setMessage(conName+", you have pending service request. Please check your 'Pending Service Request' for details.");

    LayoutInflater inflater = LayoutInflater.from(this);
    View change_layout = inflater.inflate(R.layout.layout_change_password, null);

    final TextInputLayout edtOldPassword = change_layout.findViewById(R.id.textInput_oldPassword);
    final TextInputLayout edtNew = change_layout.findViewById(R.id.textInput_newPassword);
    final TextInputLayout edtConfirm = change_layout.findViewById(R.id.textInput_confirmPassword);

    builder.setView(change_layout);

    builder.setPositiveButton("CHANGE",
            new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                }
            });
    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
        }
    });


    final AlertDialog dialog = builder.create();
    dialog.show();
    dialog.setCancelable(false);

    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (TextUtils.isEmpty(edtOldPassword.getEditText().getText().toString())) {
                edtOldPassword.setErrorEnabled(true);
                edtOldPassword.setError("Old password is empty.");
                return;
            }else
                edtOldPassword.setErrorEnabled(false); edtOldPassword.setError(null);

            if (TextUtils.isEmpty(edtNew.getEditText().getText().toString())) {

                edtNew.setErrorEnabled(true);
                edtNew.setError("New password is empty.");
                return;
            }else
                edtNew.setErrorEnabled(false); edtNew.setError(null);

            if (TextUtils.isEmpty(edtConfirm.getEditText().getText().toString())) {

                edtConfirm.setErrorEnabled(true);
                edtConfirm.setError("Confirm password is empty.");
                return;
            }else
                edtConfirm.setErrorEnabled(false); edtConfirm.setError(null);

            if (edtNew.getEditText().getText().toString().length() < 8) {
                Snackbar.make(nRootLayout, "Password too short: Must be atleast 8 characters !!!", Snackbar.LENGTH_SHORT).show();
                return;
            }

            final String pwdOld = edtOldPassword.getEditText().getText().toString();
            final String pwd1 = edtNew.getEditText().getText().toString();
            final String pwd2 = edtConfirm.getEditText().getText().toString();

            if (!pwd1.equals(pwd2)) {
                edtConfirm.setErrorEnabled(true);
                edtConfirm.setError("Password does not matched.");
                return;
            }else
                edtConfirm.setErrorEnabled(false); edtConfirm.setError(null);

            ////////////////////////////////////change password
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            final String email = user.getEmail();

            AuthCredential credential = EmailAuthProvider.getCredential(email,pwdOld);

            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.updatePassword(pwd1).addOnCompleteListener(new OnCompleteListener<Void>() {

                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            dialog.dismiss();
                                            passChanged = pwd1;
                                            passwordChange();
                                            Snackbar.make(nRootLayout, "Password has changed successfully!!!", Snackbar.LENGTH_SHORT).show();

                                        } else {
                                            dialog.dismiss();
                                            Snackbar.make(nRootLayout, "Password does not changed !!!", Snackbar.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                edtOldPassword.setErrorEnabled(false); edtOldPassword.setError(null);
                                edtOldPassword.setErrorEnabled(true); edtOldPassword.setError("Old Password is incorrect.");

                            }
                        }
                    });

        }

    });

    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            dialog.dismiss();
        }
    });

    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            dialog.dismiss();
        }
    });

}

    private void passwordChange() {
        nProviderDatabase = nDbase.getReference().child("Users").child("Providers").child(userId);

        Map provInfo = new HashMap();
        provInfo.put("password", passChanged);
        nProviderDatabase.updateChildren(provInfo);
    }
    //////////////////////////////////////////changePassword_END
    /////////////////////////////////////////////////////////////////////////historyDetais
    public void historyDetails(){
        Intent intent = new Intent(ProviderMapActivity.this, HistoryActivity.class);
        intent.putExtra("consumerOrProvider", "Providers");                 //The value "Consumers" is the Consmers
        startActivity(intent);
       // finish();
    }
    ////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////pendingDetails
    public void pendingDetails(){
        Intent intent = new Intent(ProviderMapActivity.this, PendingActivityProviders.class);
        intent.putExtra("consumerOrProvider", "Providers");                 //The value "Consumers" is the Consmers
        startActivity(intent);
      //  finish();
    }
    ////////////////////////////////////////////////////////

    ////////////////////////////////////////////////signOut
    public void signOut() {

        isLoggingOut = true;
        disconnectProvider();
        //automatic logout
        FirebaseAuth.getInstance().signOut();
        finishAffinity();

        //    Intent intent = new Intent(ProviderMapActivity.this, ProviderLoginActivity.class);
    //    startActivity(intent);
    //    finish();
    //    return;
    }
//////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////=============********************************accountProvider
    public void accountProvider() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
       // dialog.setTitle("PERSONAL ACCOUNT");

        LayoutInflater inflater = LayoutInflater.from(this);
        View account_layout = inflater.inflate(R.layout.layout_account_provider, null);

        tvNameField = account_layout.findViewById(R.id.name);
        tvAddressField = account_layout.findViewById(R.id.address);
        tvAgeField = account_layout.findViewById(R.id.age);
        tvPhoneField = account_layout.findViewById(R.id.phoneNumber);
        tvServiceTypeField = account_layout.findViewById(R.id.serviceType);
        nBack = account_layout.findViewById(R.id.backProv);

        nProfileImage = account_layout.findViewById(R.id.profileImage);

        dialog.setView(account_layout);
        dialog.setCancelable(false);

        final AlertDialog dialogshow = dialog.show();

        nAuth = FirebaseAuth.getInstance();
        userId = nAuth.getCurrentUser().getUid();
        nProviderDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(userId);


        /////////////////////////////////////////////////////////////////////// nProviderDatabase_ValueEventListener
        nProviderDatabase.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    if (map.get("name") != null) {
                        nName = map.get("name").toString();
                        tvNameField.setText(nName);
                    }

                    if (map.get("address") != null) {
                        nAddress = map.get("address").toString();
                        tvAddressField.setText(nAddress);
                    }

                    if (map.get("age") != null) {
                        nAge = map.get("age").toString();
                        tvAgeField.setText(nAge);
                    }

                    if (map.get("phone") != null) {
                        nPhone = map.get("phone").toString();
                        tvPhoneField.setText(nPhone);
                    }



                    if (map.get("profileImageUrl") != null) {

                        nProfileImageUrl = map.get("profileImageUrl").toString();

                        if (map.get("profileImageUrl").equals("")) {
                            nProfileImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_profileimage));
                        }else{
                            Glide.with(getApplication()).load(nProfileImageUrl).into(nProfileImage);
                        }
                    }else{
                        nProfileImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_profileimage));
                    }





                    if (map.get("serviceType") != null) {
                        nServiceType = map.get("serviceType").toString();
                        tvServiceTypeField.setText(nServiceType);

                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////

/*
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }


        });
        dialog.show();

*/
        nBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogshow.dismiss();
                return;
            }
        });

    }
    //////////////////////////////////////////////////////////////////////////////////////////********************************accountProvider_end


}