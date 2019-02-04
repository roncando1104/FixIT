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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

//import static com.alliancecode.fixit.Model.HistoryViewHolders.servicedID;

public class ConsumerMapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, RoutingListener {

    final int LOCATION_REQUEST_CODE = 1;

    private GoogleMap nMap;
    GoogleApiClient nGoogleApiClient;
    Location nLastLocation;
    LocationRequest nLocationRequest;

    // private PlaceAutocompleteFragment autocompleteFragment;

    private Button nRequest;

    private Handler handler;
    private long timeRemaining = 30000;

    private long setMinute = 30000;
    private Runnable runnable;

    private LatLng deviceLocation;
    private LatLng destinationLatLng;
    private LatLng providerLatLng;

    // SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    SupportMapFragment mapFragment;
    private Boolean requestBol = false;
    private Boolean isLoggingOut = false;
    private Boolean isNextControl;
    private Boolean hasNoProvider = false;
    private Boolean hasTimerEnd = false;
    private Boolean isTimerStart = false;


    private Marker houseMarker;

    private LinearLayout nConfirmRequest, nScheduleAlert;
    private TextView nMessageBooking;

    /////////////////////////////////////////////////////pullup
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

/////////////////////////////////

    private String houseLocation;
    private String proIsHere = "no";
    private String controlStr = "";
    private String providerKey = "";
    private String pressControl = "";
    private String items = null;

    private String item1, item2,item3,item4,item5;

    private Boolean schedRequestController = false;
    private Boolean hasRequest = true;
    private Boolean isRecall = false;
    private Boolean isProviderHere = true;
    private Boolean isTimerEnd = false;

    // private Boolean isProvidersCancel = false ;



    private LinearLayout nProviderInfo, nChatLayout;
    private ImageView nProviderProfileImage;
    private TextView nProviderName, nProviderPhone, nProviderServiceType, nProviderAge;

    //  private CardView nFragmentPlaceAutocomplete;
    private RelativeLayout nFragmentMap;
    // private LinearLayout nFragmentMap;

    private LinearLayout nFragmentCallService;
    private Typeface fontNunitoB;

    // ////////////////////////
    private String nRequestServiceType, nServiceDate, nPayment, nPaymentDigit, nAddress, nServiceTime, nHouseType, nItemOne, nItemTwo, nItemThree, nItemFour, nItemFive, nConsumerNote, serviceAdditionalDetails;

    private String providerschedId;
    private String newImage;

    private DrawerLayout nRootLayout;

    ///////////////////////Settings
    private DatabaseReference nConsumerDatabase;
    private FirebaseDatabase nDbase;
    private FirebaseAuth nAuth;
  //  private String userId;
  //  private String nName, pName, schName;
    private String pName,nName, schName;

  //  private String nPhone, pPhone, pAge, schPhone;
    private String pPhone, pAge, schPhone;

    private String pService;
 //   private String nProfileImageUrl, schProfileImageUrl, schProviderProfileImageUrl, conImage;
    private String schProfileImageUrl, schProviderProfileImageUrl, conImage;

    //  private ImageView nProfileImage;
    private CircleImageView nProfileImage;
    private static final int REQUEST_CODE = 1;
    private Uri resultUri;
    private TextView tvNameField;
    private TextView tvPhoneField, txtConsumerName;
    private CircleImageView imageAvatar;

    private Button nUpdate, nBack;
    private Button nNo, nYes, nOk;

    private RatingBar nRatingBar;
    private RatingBar nRatingStar;
    private TextView nRateScale;
    private EditText nFeedback;
    private Button nSubmit;
    private int rating;
    private String nFeedbackStr = "";
    private String nRateScaleStr = "";

    private float ratingAverage;
    private String rateAve;

    private Calendar cal;
    private String date;
    private String schedControl = "";
    private String isSched = "";
    private String isSchedCancel = "";
    private String isSchedDone = "";
    private String isSchedEndedNatural = "";


    private Boolean scheduleFound;

    //  private String serviceId;
    private DatabaseReference historyServiceInformationRef;
    private FirebaseDatabase historyService;


    private int remarks = 0;
    private int counter = 0;

    private String serviceId;
    private String pendingConId;
    private String passChanged;

    private Float nPaymentDigFloat;

    public static String userId, nProfileImageUrl, nUserName, nPhone;
    public static String providerChatId, nProviderChatProfileImageUrl, providerChatName, providerChatPhone;
    ////////////////////////////////////////

    //  NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
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

    String url = "https://fixit-dbae9.firebaseio.com/";

  //  private static final String TAG = "ChatActivity";
  //  private SlidingUpPanelLayout mLayout;
 //   private final static int REQUEST_PHONE_CALL = 1;

////////////////============================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

///////////////////////////////////////////////////////////////////////////////////////////

     //   Toast.makeText(getApplicationContext(), String.valueOf(timeRemaining).toString(), Toast.LENGTH_SHORT).show();


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





        /*usersList = (ListView)findViewById(R.id.usersList);*/
        noUsersText = (TextView) findViewById(R.id.noUsersText);
        tvProfile = (TextView) findViewById(R.id.profileTV);
        tvNumProfile = (TextView) findViewById(R.id.profileNumber);
        buttonMessage = (ImageButton) findViewById(R.id.messageButton);
        buttonCall = (ImageButton) findViewById(R.id.callButton);
        chatWithET = (EditText) findViewById(R.id.etChatwith);

        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE); ////////////////============================

        ////////////////============================
        buttonMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase.setAndroidContext(getApplicationContext());
                myFirebase = new Firebase(url);


                Intent intent = new Intent(ConsumerMapActivity.this, Chat.class);
                intent.putExtra("ConOrPro", "Consumers");                 //The value "Consumers" is the Consmers
                startActivity(intent);

            }
        });


       /* //--------------------- checking the permission for send sms -------------------------------//===============================================

        if(ActivityCompat.checkSelfPermission(ConsumerMapActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(ConsumerMapActivity.this, Manifest.permission.SEND_SMS)){

                AlertDialog.Builder builder = new AlertDialog.Builder(ConsumerMapActivity.this);
                builder.setTitle("Need SMS Permission");
                builder.setMessage("This app needs SMS permission to send SMS notification");
                builder.setPositiveButton("GRANT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(ConsumerMapActivity.this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CONSTANT);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(ConsumerMapActivity.this);
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
                ActivityCompat.requestPermissions(ConsumerMapActivity.this, new String[]{Manifest.permission.SEND_SMS}
                        , SMS_PERMISSION_CONSTANT);
            }
            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.SEND_SMS, true);
            editor.commit();
        }

        //--------------------- checking the permission for send sms -------------------------------//======================================
*/





////////////////============================

        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(ActivityCompat.checkSelfPermission(ConsumerMapActivity.this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                    //  String phoneNumber = tvNumProfile.getText().toString();
                    String substrPhone = pPhone.substring(1);
                    Intent i = new Intent(Intent.ACTION_CALL);
                    i.setData(Uri.parse("tel:+63"+ substrPhone));
                    startActivity(i);
                }else{
                    // String phoneNumber = tvNumProfile.getText().toString();
                    String substrPhone = pPhone.substring(1);
                    Intent i = new Intent(Intent.ACTION_DIAL);
                    i.setData(Uri.parse("tel:+63"+ substrPhone));
                    startActivity(i);
                }
            }

        });
////////////////============================


        ///////////////////////////////////////////////////


        //  SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);


        /////////////////

        //////////////////////////////////////////////////////////////////
        nRootLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //    nFragmentPlaceAutocomplete = (CardView) findViewById(R.id.fragmentPlaceAutocomplete);

        //      nFragmentMap = (LinearLayout) findViewById(R.id.fragmentMap);
//
        // nFragmentMap = (RelativeLayout) findViewById(R.id.fragmentMap);

        nFragmentCallService = (LinearLayout) findViewById(R.id.fragmentCallService);


        nProviderInfo = (LinearLayout) findViewById(R.id.providerInfo);
        nChatLayout = (LinearLayout) findViewById(R.id.chatLayout);


        nProviderProfileImage = (ImageView) findViewById(R.id.providerProfileImage);

        nProviderName = (TextView) findViewById(R.id.providerName);
        nProviderPhone = (TextView) findViewById(R.id.providerPhone);
        nProviderServiceType = (TextView) findViewById(R.id.providerServiceType);
        nProviderAge = (TextView) findViewById(R.id.providerAge);

        nMessageBooking = (TextView) findViewById(R.id.messageBooking);

        nRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        //     nRatingStar = (RatingBar) findViewById(R.id.ratingStarBar);
        //   historyServiceInformationDb = FirebaseDatabase.getInstance().getReference().child("History").child(serviceId);
        //  serviceId = getIntent().getExtras().getString("serviceId");               //getting Intent Extras Bundle from "HistoryViewHolders"


        nRequest = (Button) findViewById(R.id.request);


        //    nRadioGroup = (RadioGroup)findViewById(R.id.radioGroupServiceType);//////////////////////////////
        // nRadioGroup.check(R.id.rdoPlumber);

        destinationLatLng = new LatLng(0.0, 0.0); ///////////////////////////default location


////////////////////////////////////////////////fromTaskReceiver
        cal = Calendar.getInstance();
        date = new SimpleDateFormat("MM-dd-yyyy").format(cal.getTime());

   //     item1 = "";
   //     item2 = "";
   //     item3 = "";
    //    item4 = "";
    //    item5 = "";
    //    nItemOne = "";
    //    nItemThree = "";
    //    nItemTwo = "";
    //    nItemFour = "";
    //    nItemFive = "";

        TaskReceiver provider = new TaskReceiver();
        nRequestServiceType = provider.providerType;
        nAddress = provider.address;
        nServiceDate = provider.serviceDate;
        nServiceTime = provider.serviceTime;
        nConsumerNote = provider.consumerNote;
        nPayment = provider.payment;
        nPaymentDigit = provider.paymentDigit;

        if (nPaymentDigit != null) {
            nPaymentDigFloat = Float.parseFloat(nPaymentDigit);
        } else {
        }



         item1 = provider.itemOne;
         item2 = provider.itemTwo;
         item3 = provider.itemThree;
         item4 = provider.itemFour;
         item5 = provider.itemFive;


        nHouseType = provider.houseType;
        //    serviceAdditionalDetails = provider.serviceAdditionalDetails;

        try{
            if (item1.equals("")|| item1.equals(null)) {
                item1 = "";
                nItemOne = "";
            }else{
                nItemOne = item1;
             //   Toast.makeText(ConsumerMapActivity.this, nItemOne, Toast.LENGTH_SHORT).show();

            }

            if (item2.equals("")|| item2.equals(null)) {
                item2 = "";
                nItemTwo = "";

            }else{
                nItemTwo = item2;
            //    Toast.makeText(ConsumerMapActivity.this, nItemTwo, Toast.LENGTH_SHORT).show();

            }


            if (item3.equals("")|| item3.equals(null)) {
                item3 = "";
                nItemThree = "";

            }else{
                nItemThree = item3;
              //  Toast.makeText(ConsumerMapActivity.this, nItemThree, Toast.LENGTH_SHORT).show();

            }


            if (item4.equals("")|| item4.equals(null)) {
                item4 = "";
                nItemFour = "";

            }else{
                nItemFour = item4;
              //  Toast.makeText(ConsumerMapActivity.this, nItemFour, Toast.LENGTH_SHORT).show();

            }

            if (item5.equals("")|| item5.equals(null)) {
                item5 = "";
                nItemFive = "";

            }else{
                nItemFive = item5;
              //  Toast.makeText(ConsumerMapActivity.this, nItemFive, Toast.LENGTH_SHORT).show();

            }
        }catch(Exception e){
            e.printStackTrace();
        }





        //      Toast.makeText(getApplicationContext(), nRequestServiceType, Toast.LENGTH_SHORT).show();
        //     Toast.makeText(getApplicationContext(), nAddress, Toast.LENGTH_SHORT).show();
        //      Toast.makeText(getApplicationContext(), nServiceDate, Toast.LENGTH_SHORT).show();
        //     Toast.makeText(getApplicationContext(), nConsumerNote, Toast.LENGTH_SHORT).show();
        //    Toast.makeText(getApplicationContext(), nPayment, Toast.LENGTH_SHORT).show();


//////////////////////////////////////////////
  /*      /////////////////////////////////////////////////////////////////PlaceAutocompleteFragment
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setHint("Type your home address...");
     //   autocompleteFragment.setText(nAddress);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
           //  houseLocation = place.getName().toString();
             houseLocation = place.getAddress().toString();

                destinationLatLng = place.getLatLng();///////////////////getting the location
                //  Log.i(TAG, "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                // Log.i(TAG, "An error occurred: " + status);
            }

         //   public void setText (CharSequence text){


         //   }
        });



*/////////////////////////////////////////////////////////////////////////


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////================================================================
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //   DrawerLayout drawer = (DrawerLayout) findViewById(R.id.sliding_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
/////////////////////////////////////////////////////////////////////////////////////////Navigation Header Settings
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View navigationHeaderView = navigationView.getHeaderView(0);
        txtConsumerName = (TextView) navigationHeaderView.findViewById(R.id.txtConsumerName);
        imageAvatar = (CircleImageView) navigationHeaderView.findViewById(R.id.profileImage);
        //  txtStars = (TextView) navigationHeaderView.findViewById(R.id.txtStars);

//        fontNunitoB = Typeface.createFromAsset(getAssets(),"fonts/Nunito-Regular.ttf");
        //    txtStars.setTypeface(fontNunitoB);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        nDbase = FirebaseDatabase.getInstance();
        nConsumerDatabase = nDbase.getReference().child("Users").child("Consumers").child(userId);

        //       historyServiceInformationRef = FirebaseDatabase.getInstance().getReference().child("History").child(serviceId);


        nConsumerDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //   String name = (String) dataSnapshot.child("Users").child("Consumers").child(userId).child("name").getValue();
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    java.util.Map<String, Object> map = (java.util.Map<String, Object>) dataSnapshot.getValue();

                    if (map.get("name") != null) {

                        schName = map.get("name").toString();
                        nUserName = schName;
                        txtConsumerName.setText(map.get("name").toString());
                        txtConsumerName.setTypeface(fontNunitoB);
                    }

                    if (map.get("phone") != null) {

                        schPhone = map.get("phone").toString();

                    }


                    if (map.get("profileImageUrl") != null) {
                        schProfileImageUrl = map.get("profileImageUrl").toString();

                        if (map.get("profileImageUrl").equals("")) {
                            imageAvatar.setImageDrawable(getResources().getDrawable(R.mipmap.ic_profileimage));
                        }else{
                            Glide.with(getApplication()).load(map.get("profileImageUrl").toString()).into(imageAvatar);
                        }
                    }else{
                        imageAvatar.setImageDrawable(getResources().getDrawable(R.mipmap.ic_profileimage));
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


/////////////////////////////////////////////////////////////////////////////////////////Navigation Header Settings_END=========================================================================







/////////////////////
         final DatabaseReference cancelRefe = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(userId).child("cancelCount");
        cancelRefe.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                }else{
                    cancelRefe.setValue(0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
///////////////////

/////////////////////////////////////////timer
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                if(providerFound){
                    handler.removeCallbacksAndMessages(null);
                    handler.removeCallbacks(runnable);
                }else{

                    timeRemaining = timeRemaining - 1000;
                    if(timeRemaining > 0){
                        handler.postDelayed(this, 1000);

                    }else{
                     //   nMessageBooking.setText("Sorry there is NO AVAILABLE/ONLINE Service Provider now n Please call again...");
                        handler.removeCallbacksAndMessages(null);
                        handler.removeCallbacks(runnable);
                        nMessageBooking.setVisibility(View.GONE);
                 //       Toast.makeText(ConsumerMapActivity.this, "minuto"+Long.valueOf(timeRemaining).toString(), Toast.LENGTH_SHORT).show();
                 //       Toast.makeText(ConsumerMapActivity.this,"radius      : "+ radius, Toast.LENGTH_SHORT).show();
                 //       Toast.makeText(ConsumerMapActivity.this,"counter      : "+ counter, Toast.LENGTH_SHORT).show();

                        if(!hasNoProvider){
                            hasNoProvider = true;
                         //   String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                         //   DatabaseReference ref = FirebaseDatabase.getInstance().getReference("consumerRequest");
                         //   GeoFire geoFire = new GeoFire(ref);
                          //  geoFire.removeLocation(userId);

                          noAvailableProvider();

                        }
                    }
                }

            }


        };
//////////////////////////


////////////////////////////////////////////////////////////////////////////////Calling Service provider
        nRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //     Toast.makeText(getApplicationContext(), nRequestServiceType, Toast.LENGTH_SHORT).show();


                //      isRecall = true;


                if (requestBol) {             ////////everything back to initial value again,

                    isCancelled();
                    //      cancelByConsumer();
                    //     endService();

                } else {

                    //                int selectedId = nRadioGroup.getCheckedRadioButtonId();   ///////////////////////////////
                    //               final RadioButton nRadioButton = (RadioButton) findViewById(selectedId);//////////////////////////

                    //                   if(nRadioButton.getText() == null){////////////////////////
                    //                      return;
                    //                  }

                    //             nRequestServiceType = nRadioButton.getText().toString();///////////////////////////

                    requestBol = true;
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    try{
                        //////setting the Location
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("consumerRequest");
                        GeoFire geoFire = new GeoFire(ref);
                      //  geoFire.setLocation(userId, new GeoLocation(nLastLocation.getLatitude(), nLastLocation.getLongitude()));///==================
                        geoFire.setLocation(userId, new GeoLocation(nLastLocation.getLatitude(), nLastLocation.getLongitude()), new GeoFire.CompletionListener() {
                                    @Override
                                    public void onComplete(String key, DatabaseError error) {

                                    }
                                });

                                deviceLocation = new LatLng(nLastLocation.getLatitude(), nLastLocation.getLongitude());
                        //   deviceLocationSched = new LatLng(nLastLocationSched.getLatitude(), nLastLocationSched.getLongitude());         to be added

                        if (houseMarker != null) {
                            houseMarker.remove();
                        }
                        houseMarker = nMap.addMarker(new MarkerOptions().position(deviceLocation).title("Consumer House").icon(BitmapDescriptorFactory.fromResource(R.mipmap.fixit_house)));

                    }catch(Exception e){
                        e.printStackTrace();
                    }


                    nMessageBooking.setText("Looking for Service Provider...");
                    nMessageBooking.setVisibility(View.VISIBLE);
                    nRequest.setVisibility(View.INVISIBLE);
                    getClosestProvider();

                }


            }
        });
//////////////////////////////////////////////////////////////////////////////////


        ///adjustment codes added_ re: pullup layout
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ConsumerMapActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            //  startActivity(new Intent(ConsumerMapActivity.this, ConsumerMapActivity.class));

            // return;
        } else {
            mapFragment.getMapAsync(this);
        }
/////////////////////////////////////////////////

        RequestServiceSchedule();
    }

    /////////////////////////////////////////////noAvailableProvider
    private void noAvailableProvider() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        //        View check_layout = inflater.inflate(R.layout.layout_message_noproviderfound, null);
        View check_layout = inflater.inflate(R.layout.layout_message_noproviderfound, null);

        nOk = check_layout.findViewById(R.id.noAvailableOk);
        dialog.setView(check_layout);
        dialog.setCancelable(false);
        final AlertDialog dialogBuilder = dialog.show();

        nOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
                //  dialogInterface.dismiss();
                isTimerEnd = true;
                hasTimerEnd = true;
                 requestBol = false;
                 remarks = 1;
                 isSchedCancel = "";
                 isSched = "";

                endService();
         //    Intent intent = new Intent(ConsumerMapActivity.this, ConsumerMapActivity.class);
        //     startActivity(intent);
        //        Toast.makeText(ConsumerMapActivity.this, "Refresh", Toast.LENGTH_SHORT).show();
            //    geoQuery.removeAllListeners();
               // geoFire.removeLocation(userId);
           //     providerLocation.removeValue();
           //     counter = 0;
          //      timeRemaining =45000;
          //      radius =1;
           //     isTimerStart = false;
            //    nRequest.setVisibility(View.VISIBLE);
           //     requestBol = false;


             //  return;
       //   Toast.makeText(getApplicationContext(), String.valueOf(timeRemaining).toString(), Toast.LENGTH_SHORT).show();
            }
        });
        // dialog.show();
    }

    /////////////////////////////////////isCancelled
    private int cancelCoun;
    private View check_layout2;
   // private DatabaseReference cancelRef;

    private void isCancelled() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        if(pressControl.equals("onBackPressed")){
            check_layout2 = inflater.inflate(R.layout.layout_message_onbackpressed, null);

        }else{
            check_layout2 = inflater.inflate(R.layout.layout_message_confirmserviceworkdone, null);

        }

        nNo = check_layout2.findViewById(R.id.doneNot);
        nYes = check_layout2.findViewById(R.id.doneYes);
        dialog.setView(check_layout2);

        dialog.setCancelable(false);
        final AlertDialog dialogBuilder = dialog.show();


        ///////////////////////
        nYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
                ///////////////
              //  final DatabaseReference cancelRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(userId);
              final DatabaseReference cancelRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(userId);
                cancelRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                            if (map.get("cancelCount") != null) {
                                cancelCoun = Integer.valueOf(map.get("cancelCount").toString());
                                cancelCoun = cancelCoun + 1;

                                HashMap cancelMap = new HashMap();
                                cancelMap.put("cancelCount", cancelCoun);
                                cancelRef.updateChildren(cancelMap);

                 //               Toast.makeText(getApplicationContext(),"Count put "+cancelCoun , Toast.LENGTH_SHORT).show();


                                if(cancelCoun >= 6){
                                    accountIsBlocked();
                                }else if(cancelCoun == 5 ){
                                    blockAccountReminder();
                                }else if(cancelCoun == 4 ){
                                    oneMonthSuspension();
                                }else if(cancelCoun == 3 ){
                                    suspensionReminder();
                                }else if(cancelCoun == 2 ) {
                                    avoidCancellationReminder();
                                }else {
                                    cancelByConsumer();
                                    endService();
                                }

                            }
                        }
                 //       else {
                 //           cancelCoun = 1;
              //              Toast.makeText(getApplicationContext(),"Count No cancel yet "+cancelCoun , Toast.LENGTH_SHORT).show();
                //        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                /////////////


            //    Toast.makeText(getApplicationContext(),"Count put "+cancelCoun , Toast.LENGTH_SHORT).show();


            //    cancelByConsumer();
             //   endService();



            }

        });


        nNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
                requestBol = true;
                return;
            }
        });
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
                cancelByConsumer();
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
                cancelByConsumer();
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

                DatabaseReference suspendRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(userId).child("accountSuspended");
                suspendRef.setValue(dateSuspension);

                cancelByConsumer();
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
                cancelByConsumer();
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

                cancelByConsumer();
                endService();

            }
        });
        dialog.show();
    }
    ////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////RequestServiceSchedule
    private void RequestServiceSchedule() {

        //to reactivate ratingField
        isSched = "";

        //to test the RequestServiceSchedule
        cal = Calendar.getInstance();
        date = new SimpleDateFormat("MM-dd-yyyy").format(cal.getTime());
        DatabaseReference Consumersched = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(userId).child("RequestServiceSchedule").child(date);
        //   DatabaseReference Consumersched = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(userId).child("RequestServiceSchedule").child("08-15-2018");


        Consumersched.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    java.util.Map<String, Object> map = (java.util.Map<String, Object>) dataSnapshot.getValue();

                    schedControl = "schedule";
                    nRequest.setVisibility(View.GONE);

                    if (map.get("providerId") != null) {
                        providerschedId = map.get("providerId").toString();
                        providerChatId = providerschedId;
                    }


                    getProviderLocation();
                    scheduleDateArrive();
                    nProviderInfo.setVisibility(View.VISIBLE);
                    nChatLayout.setVisibility(View.VISIBLE);
                    //    Toast.makeText(ConsumerMapActivity.this, "PASOK ",Toast.LENGTH_LONG).show(); //edit

                   /* ////////////////////////////////
                    if (dataSnapshot.child("name") != null) {        //other way of getting value

                        nProviderName.setText(dataSnapshot.child("name").toString());   //other way of getting value
                    }
                    */////////////////////////////////

                    if (map.get("providerName") != null) {
                        pName = map.get("providerName").toString();
                        providerChatName = pName;
                        nProviderName.setText("Name: " + pName);
                        //   Toast.makeText(ConsumerMapActivity.this, "Name: "+  pName ,Toast.LENGTH_LONG).show(); //edit
                    }
                    if (map.get("providerPhone") != null) {
                        pPhone = map.get("providerPhone").toString();
                        providerChatPhone = pPhone;
                        nProviderPhone.setText("Phone: " + pPhone);
                    }

                    if (map.get("providerAge") != null) {
                        pAge = map.get("providerAge").toString();
                        nProviderAge.setText("Age: " + pAge);

                    }

                    if (map.get("consumerServiceRequest") != null) {
                        pService = map.get("consumerServiceRequest").toString();
                        nProviderServiceType.setText("Service: " + pService);

                    }
                    /////tobe added later
                    //               if (map.get("charge") != null) {
                    //                   pCharge = map.get("charge").toString();
                    //                   nProviderServiceCharge.setText("Service: "+pCharge);

                    //               }

                    //               if (map.get("serviceDateToWork") != null) {
                    //                   pDateToWork = map.get("serviceDateToWork").toString();
                    //                   nProviderServiceDateToWork.setText("Service: "+pDateToWork);

                    //               }
                    if (map.get("pendingId") != null) {
                        pendingConId = map.get("pendingId").toString();
                    }


                    if (map.get("profileImageUrl") != null) {
                        conImage  = map.get("profileImageUrl").toString();
                        nProviderChatProfileImageUrl = conImage;
                        Glide.with(getApplication()).load(map.get("profileImageUrl").toString()).into(nProviderProfileImage);
                    }

                    /////////////////////////////////////////////////////////////////////// Calculating Star Rating
                    // float ratingAverage;
                    if (map.get("aveRating") != null) {

                        ratingAverage = Float.valueOf(map.get("aveRating").toString());
                        nRatingBar.setRating(ratingAverage);
                        nRatingBar.setIsIndicator(true);
                    }

                    ///////////////////////////////////////////////////////////////////////Calculating Star Rating_END


                    ////schedule token ValueEventListener
                    if (!schedRequestController) {
                        ////////////////////////////////////////////////////////////schedule control
                        schedRequestController = true;

                        final DatabaseReference providerReferenc = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(providerschedId).child("requestDone");
                        providerReferenc.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                } else {

                                    HashMap donemap = new HashMap();
                                    donemap.put("consumerServiceRequest", pService);
                                    providerReferenc.updateChildren(donemap);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        //         DatabaseReference providerReferen = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(providerschedId).child("requestDone");
                        //         HashMap donemap = new HashMap();
                        //        donemap.put("consumerServiceRequest", pService);
                        //        providerReferen.updateChildren(donemap);
                        /////////////////////////////////////////////////////////schedule control
                    }
                    //      getProviderLocation();
                    serviceDone();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    ////////////////////////
    // private DatabaseReference mapConsuRef;
    private void scheduleDateArrive() {
//////////////////


        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        View check_layout = inflater.inflate(R.layout.layout_message_scheduledatearrive, null);

        dialog.setView(check_layout);
        dialog.setCancelable(false);

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                requestBol = true;
                isNextControl = true;
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                ///////////map Reconnection
                DatabaseReference mapConsuRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(userId).child("RequestServiceSchedule").child(date).child("locationMap");
                DatabaseReference refWorking = FirebaseDatabase.getInstance().getReference("providerWorking");

                GeoFire geoFire = new GeoFire(mapConsuRef);
                geoFire.setLocation(userId, new GeoLocation(nLastLocation.getLatitude(), nLastLocation.getLongitude()), new GeoFire.CompletionListener() {
                            @Override
                            public void onComplete(String key, DatabaseError error) {
//
                            }
                        });
                  deviceLocation = new LatLng(nLastLocation.getLatitude(), nLastLocation.getLongitude());

          //      geoFire.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {
           //         @override
           //         public void onComplete(String key, DatabaseError error) {

          //          }
           //     });

                if (houseMarker != null) {
                    houseMarker.remove();
                }

                houseMarker = nMap.addMarker(new MarkerOptions().position(deviceLocation).title("Consumer House").icon(BitmapDescriptorFactory.fromResource(R.mipmap.fixit_house)));

                //            GeoFire geoFirepro = new GeoFire(refWorking);
                //            providerLatLng = new LatLng(locationLat, locationLng);
                //             geoFirepro.setLocation(providerschedId, new GeoLocation(locationLat,locationLng));
                //            nProviderMarker = nMap.addMarker(new MarkerOptions().position(providerLatLng).title("Service Provider").icon(BitmapDescriptorFactory.fromResource(R.mipmap.fixit_provider)));


                getProviderLocation();

                /////////////////////
            }
        });
        dialog.show();
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////RequestServiceSchedule_END


    ////////////////////////////////////////////////////////cancelByConsumer
    private void cancelByConsumer() {
        requestBol = false;
        remarks = 1;
        isSchedCancel = "yes";
        if (userId != null) {  //////removing value in the database
            DatabaseReference conRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(userId).child("cancelRequest");
            conRef.removeValue();   //////removing value in the database

        }

        /////////////////
        DatabaseReference consumerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(userId).child("cancelRequest");
        HashMap consumermap = new HashMap();
        consumermap.put("consumerServiceRequest", nRequestServiceType);
        consumerRef.updateChildren(consumermap);


    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////getClosestProvider.... creation of "consumerRequest"
    private int radius = 5;
    private Boolean providerFound = false;
    private String providerFoundID;
    private DatabaseReference providerLocation;
    private CountDownTimer myTimer;
    private GeoQuery geoQuery;
    private GeoFire geoFire;
    private void getClosestProvider() {    //creating a function

        //BUGS using Timer:  Reason: Input dispatching timed out (Waiting to send non-key event because the touched window has not finished processing certain input events that were delivered to it over 500.0ms ago.  Wait queue length: 2.  Wait queue head age: 6072.1ms.)

/*

        if(!isTimerStart){
            timeRemaining = 30000;
            isTimerStart = true;
            //timer kick start
            handler.postDelayed(runnable, 1000);

 Toast.makeText(ConsumerMapActivity.this,  nServiceDate ,Toast.LENGTH_LONG).show(); //edit
            Toast.makeText(ConsumerMapActivity.this,  nRequestServiceType ,Toast.LENGTH_LONG).show(); //edit
            Toast.makeText(ConsumerMapActivity.this,  item1 ,Toast.LENGTH_LONG).show(); //edit
            Toast.makeText(ConsumerMapActivity.this,  item2 ,Toast.LENGTH_LONG).show(); //edit
            Toast.makeText(ConsumerMapActivity.this,  item3 ,Toast.LENGTH_LONG).show(); //edit
            Toast.makeText(ConsumerMapActivity.this,  item4 ,Toast.LENGTH_LONG).show(); //edit
            Toast.makeText(ConsumerMapActivity.this,  item5 ,Toast.LENGTH_LONG).show(); //edit
        }
*/


/////////////////////////////////////Timer

            try{
                if(!isTimerStart){
                    isTimerStart = true;
                    myTimer =   new CountDownTimer(setMinute, 1000) {

                        public void onTick(long millisUntilFinished) {
                            //  mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                        }

                        public void onFinish() {
                            myTimer.cancel();
                            //  mTextField.setText("done!");
                            if(!isTimerEnd){
                                //      Toast.makeText(ConsumerMapActivity.this, "DONE TImer", Toast.LENGTH_SHORT).show();
                                isTimerEnd = true;
                                nMessageBooking.setVisibility(View.GONE);

                                if(providerFound){
                                }else
                                  noAvailableProvider();
                            }
                        }
                    }.start();
                }

            }catch(Exception e){
                e.printStackTrace();
            }


////////////////////////////////////////////////////


/*
        ///////////////////////////////////////////////////Timer
        try {
            if (isTimerEnd) {
                isTimerEnd = false;
                nMessageBooking.setVisibility(View.GONE);
                if(!hasTimerEnd){
                    hasTimerEnd = true;
                    noAvailableProvider();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

////////////////////////////////////////////////////
*/


        providerLocation = FirebaseDatabase.getInstance().getReference().child("providerAvailable");
        geoFire = new GeoFire(providerLocation);


        geoQuery = geoFire.queryAtLocation(new GeoLocation(deviceLocation.latitude, deviceLocation.longitude), radius);
        geoQuery.removeAllListeners();



        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {

            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                ////////////////////
                if (!providerFound && requestBol) {
                    final DatabaseReference nProviderDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(key);////////////////////Checking First the Database...
                    nProviderDatabase.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //                    Map<String, Object> providerMap = (Map<String, Object>) dataSnapshot.getValue();////////////////////getting the Database value...

                            /////////////////////////////////////////////
                            Map<String, Object> providerMap = (Map<String, Object>) dataSnapshot.getValue();////////////////////getting the Database value...

                            providerKey = dataSnapshot.getKey().toString();

                            if (providerKey!=null){
                                hasSchedule(providerKey);
                            }


                            if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {



                                if (nServiceDate.equals(date)) {
                                    isSched = "off";
                                    //                Toast.makeText(ConsumerMapActivity.this, nServiceDate +" off "+ date ,Toast.LENGTH_SHORT).show(); //edit

                                } else {
                                    isSched = "on";
                                    isSchedCancel = "no";
                                    //              Toast.makeText(ConsumerMapActivity.this, nServiceDate +" on "+ date ,Toast.LENGTH_SHORT).show(); //edit

                                }
                                ///////////////////////////////////////////

                                if (providerFound) {  //////////////To not continue looking after serviceType found
                                    //             Toast.makeText(ConsumerMapActivity.this, "Provider is Found!",Toast.LENGTH_SHORT).show(); //edit
                                    return;
                                }
//
                                if (providerMap.get("serviceType").equals(nRequestServiceType) && (controlStr.equals("no"))) { //////////////////////////////////Finding ServiceType      ***if Conditions...
                                    //           Toast.makeText(ConsumerMapActivity.this, controlStr + controls   + nServiceDate ,Toast.LENGTH_LONG).show(); //edit

                                    providerFound = true;
                                    providerFoundID = dataSnapshot.getKey();
                                    providerChatId = providerFoundID;



                                    // DatabaseReference providerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(providerFoundID);
                                    DatabaseReference providerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(providerFoundID).child("consumerRequest");
                                    String consumerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    HashMap map = new HashMap();                                                  ///////Saving data into Firebase
                                    map.put("consumerId", consumerId);
                                    map.put("consumerServiceRequest", nRequestServiceType);
                                    map.put("houseLocation", nAddress);
                                    map.put("payment", nPayment);
                                    map.put("paymentDigit", nPaymentDigit);
                                    map.put("dateSched", nServiceDate);
                                    map.put("timeRequest", nServiceTime);
                                    map.put("consumerNote", nConsumerNote);
                                    map.put("houseType", nHouseType);
                                    //        map.put("serviceAdditionalDetails", serviceAdditionalDetails);

                                    try{
                                        if (item1.equals("")|| item1.equals(null)) {
                                            item1 ="";
                                        }else{
                                            map.put("itemOne", item1);
                                        }

                                        if (item2.equals("")|| item2.equals(null)) {
                                            item2 ="";

                                        }else{
                                            map.put("itemTwo", item2);

                                        }

                                        if (item3.equals("")|| item3.equals(null)) {
                                            item3 ="";

                                        }else{
                                            map.put("itemThree", item3);
                                        }

                                        if (item4.equals("")|| item4.equals(null)) {
                                            item4 ="";

                                        }else{
                                            map.put("itemFour", item4);

                                        }

                                        if (item5.equals("")|| item5.equals(null)) {
                                            item5 ="";

                                        }else{
                                            map.put("itemFive", item5);

                                        }
                                    }catch(Exception e){
                                        e.printStackTrace();
                                    }



                                    //      map.put("houseLocationLat", destinationLatLng.latitude);
                                    //      map.put("houseLocationLng", destinationLatLng.longitude);
                                    providerRef.updateChildren(map);                                              ///////Saving data into Firebase


                                    /////////////refreshing data from TaskReceiver  bugsFixed


                                    //Tokens
                                    DatabaseReference consumerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(userId).child("cancelRequest");
                                    HashMap consumermap = new HashMap();
                                    consumermap.put("consumerServiceRequest", nRequestServiceType);
                                    consumerRef.updateChildren(consumermap);

                                    DatabaseReference providerRefer = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(providerFoundID).child("cancelRequest");
                                    HashMap providermap = new HashMap();
                                    providermap.put("consumerServiceRequest", nRequestServiceType);
                                    providerRefer.updateChildren(providermap);

                                    DatabaseReference conServRequest = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(providerFoundID).child("serviceRequest");
                                    HashMap conServRequestMap = new HashMap();
                                    conServRequestMap.put("consumerServiceRequest", nRequestServiceType);
                                    conServRequest.updateChildren(conServRequestMap);

                                    DatabaseReference providerReferen = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(providerFoundID).child("requestDone");
                                    HashMap donemap = new HashMap();
                                    donemap.put("consumerServiceRequest", nRequestServiceType);
                                    providerReferen.updateChildren(donemap);


                                    //call function
                                    getProviderLocation();
                                    getProviderInformation();
                                    requestAccepted();
                                    serviceCancelled();
                                    serviceDone();
                                    //  ratingStar();
                                    getHasServiceEnded();


                                    //    nMessageBooking.setText("Looking for Provider Location");
                                    nMessageBooking.setText("");
                                    nMessageBooking.setVisibility(View.GONE);
                                    nRequest.setVisibility(View.VISIBLE);
                                    nRequest.setText("CANCEL");


                                    controlStr = "";
                                }
                            }
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                ///////////////////////////////
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

                if (!providerFound) {
                    getClosestProvider();


                /*
                    try {
                        if (counter>=4) {
                            isTimerEnd = true;
                            radius = 5;
                            counter = 0;
                            //geoQuery.removeAllListeners();
                            //providerLocation.removeValue();
                        }




                            radius++;
                            if (radius >= 4000) {
                                radius = 5;
                                counter++;
                            }
                            getClosestProvider();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                   */
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });

    }
    /////////////////////////////////


    /////////////////////////////////////////////////the provider has a schedule
    private void hasSchedule(String key) {
        DatabaseReference nProDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(key).child("WorkSchedule");
        nProDatabase.orderByKey().equalTo(nServiceDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    controlStr = "yes";

                } else
                    controlStr = "no";


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    /////////////////////////////////////////////////the provider has a schedule


    private DatabaseReference requestAcceptRef;
    private ValueEventListener requestAcceptListener;

    //////////////////////////////////////////////////////////////////////////requestAccepted
    private void requestAccepted() {


        //serviceHasEndedRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(providerFoundID).child("consumerRequest").child("consumerId");
        // serviceHasEndedRefListener = serviceHasEndedRef.addValueEventListener(new ValueEventListener() {
        requestAcceptRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(providerFoundID).child("serviceRequest");
        requestAcceptListener = requestAcceptRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                } else {
                    Toast.makeText(ConsumerMapActivity.this, "The Service Provider has ACCEPTED your request ! ", Toast.LENGTH_SHORT).show(); //edit
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    //////////////////////////////////////////////////////////////////////////////////////requestAccepted_END

    /////////////////////////////////////////////////////////////////////////////////////////////////////getClosestProvider_END


    /////////////////////////////////////////////////// getProviderLocation
    private Marker nProviderMarker;
    // private Double locationLatPro, locationLngPro;
    // private Double locationLat;
    //  private Double locationLng;

    private DatabaseReference providerLocationRef;
    private ValueEventListener providerLocationRefListener;
    private Boolean isProviderOffline = false;

    public void getProviderLocation() {

        if (schedControl.equals("schedule")) {

            providerLocationRef = FirebaseDatabase.getInstance().getReference().child("providerWorking").child(providerschedId).child("l");


        } else {
            providerLocationRef = FirebaseDatabase.getInstance().getReference().child("providerWorking").child(providerFoundID).child("l");
        }


        providerLocationRefListener = providerLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && requestBol) {
                    List<Object> map = (List<Object>) dataSnapshot.getValue();
                    double locationLat = 0;
                    double locationLng = 0;
                    //  nMessageBooking.setText("Provider Found");
                    nRequest.setText("CANCEL");

                    if (map.get(0) != null) {
                        //              locationLatPro = Double.parseDouble(map.get(0).toString());              ///
                        locationLat = Double.parseDouble(map.get(0).toString());              ///

                    }

                    if (map.get(1) != null) {
                        //             locationLngPro = Double.parseDouble(map.get(1).toString());               ///
                        locationLng = Double.parseDouble(map.get(1).toString());
                    }

                    providerLatLng = new LatLng(locationLat, locationLng);
                    //         providerLatLng = new LatLng(locationLatPro, locationLngPro);

                    //      nProviderMarker = nMap.addMarker(new MarkerOptions().position(providerLatLng).title("Service Provider").icon(BitmapDescriptorFactory.fromResource(R.mipmap.fixit_provider)));

                    if (nProviderMarker != null) {
                        nProviderMarker.remove();
                    }

                    //////////////////////////////
                    //   nProviderMarker = nMap.addMarker(new MarkerOptions().position(providerLatLng).title("Your Service Provider").icon(BitmapDescriptorFactory.fromResource(R.mipmap.fixit_provider)));       // #13 Change Marker  ++++++++++++


                    if (schedControl.equals("schedule")) {

                        try {

                            if (locationLat <= 0 && !isProviderOffline && isNextControl) {
                                //           Toast.makeText(ConsumerMapActivity.this, "The Service Provider is OFFLINE !", Toast.LENGTH_SHORT).show(); //edit
                            } else {
                                providerIsOnline();
                                //             Toast.makeText(ConsumerMapActivity.this, "The Service Provider is NOW ONLINE !", Toast.LENGTH_SHORT).show(); //edit
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    getRouteToMarker(providerLatLng);///////////  skip for meantime

                    ///////////////////////////////////////////////////////////////Distance between two
                    Location loc1 = new Location("");
                    loc1.setLatitude(deviceLocation.latitude);
                    loc1.setLongitude(deviceLocation.longitude);

                    Location loc2 = new Location("");
                    loc2.setLatitude(providerLatLng.latitude);
                    loc2.setLongitude(providerLatLng.longitude);

                    float distance = loc1.distanceTo(loc2);

                    //  String.valueOf(distance);

                    if (distance < 20) {
                        //  nMessageBooking.setText("Provider is Here : " + String.valueOf(distance) + " meter/s.");
                        //   nRequest.setText("CANCEL");
                        if (isSched.equals("on")) {

                        } else if (proIsHere.equals("no")) {

                            if(isProviderHere){
                                isProviderHere = false;
                                providerIsHere();
                                //  autoSms();
                            }

                        }


                    } else {
                        //     nMessageBooking.setText("Provider Found: " + String.valueOf(distance) + " meter/s.");

                        nRequest.setText("CANCEL");
                    }


                    nProviderMarker = nMap.addMarker(new MarkerOptions().position(providerLatLng).title("Your Service Provider").icon(BitmapDescriptorFactory.fromResource(R.mipmap.fixit_provider)));       // #13 Change Marker  ++++++++++++


                } else {
                    isProviderOffline = true;
                    /////////
                    try {
                        if (schedControl.equals("schedule") && isProviderOffline && isNextControl) {
                            isProviderOffline = false;
                            providerIsOffline();
                            //          Toast.makeText(ConsumerMapActivity.this, "The Service Provider is OFFLINE !", Toast.LENGTH_SHORT).show(); //edit
                        } else {
                        }

                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                    //////////
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

   /* ////////////////////////////
    private void autoSms() {
        try {
            String ContactNum = tvNumProfile.getText().toString();
                  *//*String subStrContact = ContactNum.substring(1);*//*
            String messageToSend = getString(R.string.autoSMS);

            String smsSent = "SMS SENT";
            String smsDelivered = "SMS DELIVERED";

            //---------------------- monitoring the status of sms notification ------------------//
            PendingIntent sentPI = PendingIntent.getBroadcast(ConsumerMapActivity.this, 0, new Intent(smsSent), 0);
            PendingIntent deliveredPI = PendingIntent.getBroadcast(ConsumerMapActivity.this, 0, new Intent(smsDelivered), 0);

            //Receiver for sent sms
            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    switch (getResultCode())
                    {
                        case Activity.RESULT_OK:
                            Toast toastSentNotif1 = Toast.makeText(ConsumerMapActivity.this, "Customer SMS Notification has been sent", Toast.LENGTH_LONG);
                            toastSentNotif1.setGravity(Gravity.CENTER, 0, 0);
                            toastSentNotif1.show();
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            Toast toastSentNotif2 = Toast.makeText(ConsumerMapActivity.this, "Unable to send SMS Notification"
                                    +"\ndue to Generic Failure", Toast.LENGTH_LONG);
                            toastSentNotif2.setGravity(Gravity.CENTER, 0, 0);
                            toastSentNotif2.show();
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            Toast toastSentNotif3 = Toast.makeText(ConsumerMapActivity.this, "No Service available."
                                    +"\nUnable to send SMS Notification", Toast.LENGTH_LONG);
                            toastSentNotif3.setGravity(Gravity.CENTER, 0, 0);
                            toastSentNotif3.show();
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            Toast toastSentNotif4 = Toast.makeText(ConsumerMapActivity.this, "Unable to send SMS Notification"
                                    +"\ndue to Null PDU", Toast.LENGTH_LONG);
                            toastSentNotif4.setGravity(Gravity.CENTER, 0, 0);
                            toastSentNotif4.show();
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            Toast toastSentNotif5 = Toast.makeText(ConsumerMapActivity.this, "Radio Off Failure."
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
            Toast toastNotification = Toast.makeText(ConsumerMapActivity.this, e.getMessage().toString() + "\n"
                    + "Unable to send sms notification to customer", Toast.LENGTH_LONG);
            toastNotification.setGravity(Gravity.CENTER, 0, 0);
            toastNotification.show();
        }

    }
        ////////////////////////////////////////////*/


    ///////////////////////////providerIsOnline
    private void providerIsOnline() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        //  dialog.setTitle("PROVIDER LOCATION ALERT");
        //   dialog.setMessage("Provider is here !");

        LayoutInflater inflater = LayoutInflater.from(this);
        View check_layout = inflater.inflate(R.layout.layout_message_onlineprovider, null);

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
////////////////////////////////////

    ///////////////////////////providerIsOffline
    private void providerIsOffline() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        //  dialog.setTitle("PROVIDER LOCATION ALERT");
        //   dialog.setMessage("Provider is here !");

        LayoutInflater inflater = LayoutInflater.from(this);
        View check_layout = inflater.inflate(R.layout.layout_message_offlineprovider, null);

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
/////////////////////////////

    ///////////////////////////////////////
    private void getRouteToMarker(LatLng providerLatLng) {

        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(new LatLng(nLastLocation.getLatitude(), nLastLocation.getLongitude()), providerLatLng)
                .build();
        routing.execute();
    }

    /////////////////
    private void providerIsHere() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        //  dialog.setTitle("PROVIDER LOCATION ALERT");
        //   dialog.setMessage("Provider is here !");

        LayoutInflater inflater = LayoutInflater.from(this);
        View check_layout = inflater.inflate(R.layout.layout_message_providerhere, null);

        dialog.setView(check_layout);
        dialog.setCancelable(false);

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                proIsHere = "yes";
            }
        });
        dialog.show();
    }

    //////////////////////////////////////////////////


    //////////////////////////////////////////////////////////////////////////getProviderInformation......children

    private void getProviderInformation() {

        //   Toast.makeText(ConsumerMapActivity.this, "Service Provider FOUND ",Toast.LENGTH_SHORT).show(); //edit
        if (hasRequest) {
            hasRequest = false;
            providerIsFound();
        }

        myTimer.cancel();
        isTimerEnd = true;
        nProviderInfo.setVisibility(View.VISIBLE);
        nChatLayout.setVisibility(View.VISIBLE);
        DatabaseReference nProviderDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(providerFoundID);
        nProviderDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    java.util.Map<String, Object> map = (java.util.Map<String, Object>) dataSnapshot.getValue();

                   /* ////////////////////////////////
                    if (dataSnapshot.child("name") != null) {        //other way of getting value

                        nProviderName.setText(dataSnapshot.child("name").toString());   //other way of getting value
                    }
                    */////////////////////////////////

                    if (map.get("name") != null) {
                        pName = map.get("name").toString();
                        providerChatName = pName;
                        nProviderName.setText("Name: " + pName);
                    }

                    if (map.get("phone") != null) {
                        pPhone = map.get("phone").toString();
                        providerChatPhone = pPhone;
                        nProviderPhone.setText("Phone: " + pPhone);
                    }

                    if (map.get("age") != null) {
                        pAge = map.get("age").toString();
                        nProviderAge.setText("Age: " + pAge);

                    }

                    if (map.get("serviceType") != null) {
                        pService = map.get("serviceType").toString();
                        nProviderServiceType.setText("Service: " + pService);

                    }

                    if (map.get("profileImageUrl") != null) {
                        schProviderProfileImageUrl = map.get("profileImageUrl").toString();
                        nProviderChatProfileImageUrl = schProviderProfileImageUrl;
                        Glide.with(getApplication()).load(map.get("profileImageUrl").toString()).into(nProviderProfileImage);
                    }


                    /////////////////////////////////////////////////////////////////////// Calculating Star Rating
                    float ratingAverage;
                    if (map.get("aveRating") != null) {

                        ratingAverage = Float.valueOf(map.get("aveRating").toString());
                        rateAve = String.format("%.1f", ratingAverage);

                        nRatingBar.setRating(ratingAverage);
                        nRatingBar.setIsIndicator(true);
                    }

                    ///////////////////////////////////////////////////////////////////////Calculating Star Rating_END


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    ////////////////////
    private void providerIsFound() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        View check_layout = inflater.inflate(R.layout.layout_message_providerfound, null);

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


    //////////////////////////////////////////////////////////////////////////getProviderInformation_END


    /////////////////////////////////////////////////////////////////////////////////////////////////////////serviceDone

    private DatabaseReference referen;
    private DatabaseReference serviceDoneRef;

    private ValueEventListener serviceDoneListener;
    //  private ValueEventListener getPendingIdListener;
    //   Query lastQ;

    public void serviceDone() {

        ////////////////
        if (schedControl.equals("schedule")) {
            serviceDoneRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(providerschedId).child("requestDone");
        } else {
            serviceDoneRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(providerFoundID).child("requestDone");
        }

        serviceDoneListener = serviceDoneRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {


                } else {
                    //  Toast.makeText(ConsumerMapActivity.this, "The Service is Done! ",Toast.LENGTH_LONG).show(); //edit

                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////getting the last key added...
                    if (isSched.equals("on")) {

                        referen = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(userId).child("pending");

                    } else {
                        referen = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(userId).child("history");

                    }

                    Query lastQuery = referen.orderByKey().limitToLast(1);
                    lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot child : dataSnapshot.getChildren()) {

                                serviceId = child.getKey();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //Handle possible errors.
                        }
                    });
                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


                    /////////////////////////////////////////////////////////
                    if (isSched.equals("on")) {
                        scheduling();
                    }
                    ///////////////////////////////////////////////////

                    if (isSched.equals("on")) {

                    } else if (schedControl.equals("schedule")) {
                        ratingField();   ///scheduling   tobe filtered
                    } else {
                        ratingField();   ///scheduling   tobe filtered

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////serviceDone_END

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////scheduling
    private void scheduling() {


        //  Toast.makeText(ConsumerMapActivity.this, "PENDING ID "+ serviceId , Toast.LENGTH_LONG).show();


        DatabaseReference schedConRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(userId).child("RequestServiceSchedule").child(nServiceDate);
        DatabaseReference schedProRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(providerFoundID).child("WorkSchedule").child(nServiceDate);
        //  DatabaseReference schedPendingRef = FirebaseDatabase.getInstance().getReference().child("Pending").child(pendingId);
        //  String consumerId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        HashMap mapProSched = new HashMap();
        //   mapProSched.put("serviceID", serviceId);
        //   mapProSched.put("pendingId", serviceId);
        mapProSched.put("consumerId", userId);
        mapProSched.put("providerId", providerFoundID);
        mapProSched.put("consumerName", schName);
        mapProSched.put("consumerPhone", schPhone);
        mapProSched.put("consumerServiceRequest", nRequestServiceType);
        mapProSched.put("houseLocation", nAddress);
        mapProSched.put("payment", nPayment);
        mapProSched.put("paymentDigit", nPaymentDigFloat);
        mapProSched.put("houseType", nHouseType);

        mapProSched.put("dateSched", nServiceDate);
        mapProSched.put("timeRequest", nServiceTime);
        mapProSched.put("consumerNote", nConsumerNote);
        mapProSched.put("profileImageUrl", schProfileImageUrl);
        //   mapProSched.put("locationMap/providerLocationMap/latitude",providerLatLng.latitude);
        //  mapProSched.put("locationMap/providerLocationMap/longitude",providerLatLng.longitude);
        //   mapProSched.put("locationMap/consumerLocationMap/latitude",deviceLocation.latitude);
        //   mapProSched.put("locationMap/consumerLocationMap/longitude",deviceLocation.longitude);
        schedProRef.updateChildren(mapProSched);

        /////////////////////////////
        HashMap mapConSched = new HashMap();
        //  mapConSched.put("serviceID", serviceId);
        //    mapConSched.put("pendingId", serviceId);
        mapConSched.put("providerId", providerFoundID);
        mapConSched.put("consumerId", userId);
        mapConSched.put("providerName", pName);
        mapConSched.put("providerAge", pAge);
        mapConSched.put("providerPhone", pPhone);
        mapConSched.put("consumerServiceRequest", nRequestServiceType);
        mapConSched.put("houseType", nHouseType);

        //   mapConSched.put("houseLocation", nAddress);
        mapProSched.put("consumerName", schName);
        mapConSched.put("charge", nPayment);
        mapConSched.put("chargeDigit", nPaymentDigFloat);
        mapConSched.put("serviceDateToWork", nServiceDate);
        mapConSched.put("aveRating", rateAve);
        mapConSched.put("consumerNote", nConsumerNote);
        mapConSched.put("profileImageUrl", schProviderProfileImageUrl);
        //    mapConSched.put("locationMap/providerLocationMap/latitude",providerLatLng.latitude);
        //   mapConSched.put("locationMap/providerLocationMap/longitude",providerLatLng.longitude);
        //   mapConSched.put("locationMap/consumerLocationMap/latitude",deviceLocation.latitude);
        //   mapConSched.put("locationMap/consumerLocationMap/longitude",deviceLocation.longitude);
        schedConRef.updateChildren(mapConSched);

        CreatependingId();

        //    HashMap mapPendingSched = new HashMap();
        //    mapPendingSched.put("pendingId", pendingId);
        //    schedPendingRef.updateChildren(mapPendingSched);

        //   isSched = "done";
        //  lastQ.removeEventListener(getPendingIdListener);

    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////scheduling_END

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////CreatependingId
    private void CreatependingId() {


        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        // dialog.setTitle("THE SERVICE PROVIDER CONFIRM YOUR REQUEST!");
        // dialog.setMessage("");

        LayoutInflater inflater = LayoutInflater.from(this);
        View check_layout = inflater.inflate(R.layout.layout_message_confirmrequest, null);
        //    nConfirmRequest = (LinearLayout)findViewById(R.id.linearconfirmRequest);
        //   nConfirmRequest.setVisibility(View.VISIBLE);
        dialog.setView(check_layout);
        dialog.setCancelable(false);

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                DatabaseReference schedConRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(userId).child("RequestServiceSchedule").child(nServiceDate);
                DatabaseReference schedProRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(providerFoundID).child("WorkSchedule").child(nServiceDate);
                DatabaseReference schedPendingRef = FirebaseDatabase.getInstance().getReference().child("Pending").child(serviceId);

                //  String consumerId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                HashMap mapProSched = new HashMap();
                //   mapProSched.put("serviceID", serviceId);
                mapProSched.put("pendingId", serviceId);
                schedProRef.updateChildren(mapProSched);

                /////////////////////////////

                HashMap mapConSched = new HashMap();
                mapConSched.put("pendingId", serviceId);
                schedConRef.updateChildren(mapConSched);

                HashMap mapPenSched = new HashMap();
                mapPenSched.put("pendingId", serviceId);
                schedPendingRef.updateChildren(mapPenSched);

                isSchedDone = "yes";
                isSchedCancel = "";
                isSched = "";
                endService();


                //             Toast.makeText(ConsumerMapActivity.this, "PENDING ID "+ serviceId , Toast.LENGTH_LONG).show();

            }
        });
        dialog.show();

        ////////////////////////////////////////////////////////////////////////////////////////////CreatependingId_END

        // ValueEventListener getPendingIdListener;
        // Query lastQ;

        //  Toast.makeText(ConsumerMapActivity.this, "PENDING ID "+ pendingId +"servicedate = "+ nServiceDate, Toast.LENGTH_LONG).show();


    }


    ////////////////////////////////////////ratingField
    private  DatabaseReference accountProRef;
    public void ratingField() {


        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("SERVICE RATING");
        // builder.setMessage("Please rate the service");

        LayoutInflater inflater = LayoutInflater.from(this);
        View rating_layout = inflater.inflate(R.layout.layout_rating, null);

        nRatingStar = rating_layout.findViewById(R.id.ratingStarBar);
        nFeedback = rating_layout.findViewById(R.id.etFeedback);
        nSubmit = rating_layout.findViewById(R.id.btnSubmit);
        nRateScale = rating_layout.findViewById(R.id.tvRatingScale);


        builder.setView(rating_layout);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.show();      ////// builder to dialog... to make dialog.dismiss() working


        nRatingStar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                DatabaseReference refe;
                DatabaseReference historyServiceInformationDb = FirebaseDatabase.getInstance().getReference().child("History").child(serviceId);
                historyServiceInformationDb.child("rating").setValue(v);

                if (schedControl.equals("schedule") ) {
                    refe = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(providerschedId).child("rating");  //creating new field/table "rating"
                    accountProRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(providerschedId).child("cancelCount");

                } else {
                    refe = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(providerFoundID).child("rating");  //creating new field/table "rating"
                    accountProRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(providerFoundID).child("cancelCount");

                }

                refe.child(serviceId).setValue(v);     //creating new field "serviceId" into "rating" field/table, then putting a value into it.

                nRateScale.setText(String.valueOf(v));

                rating = (int) ratingBar.getRating();

                switch ((int) ratingBar.getRating()) {
                    case 1:
                        nRateScale.setText("Very bad");
                        nRateScaleStr = "1";
                        break;
                    case 2:
                        nRateScale.setText("Need some improvement");
                        nRateScaleStr = "2";
                        break;
                    case 3:
                        nRateScale.setText("Good");
                        nRateScaleStr = "3";
                        break;
                    case 4:
                        nRateScale.setText("Great");
                        nRateScaleStr = "4";
                        break;
                    case 5:
                        nRateScale.setText("Awesome. I love it");
                        nRateScaleStr = "5";
                        break;
                    default:
                        nRateScale.setText("");
                }
/////////////////////

                //////////////////////////////////

            }
        });

        nSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                DatabaseReference accountRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(userId).child("cancelCount");

                accountRef.setValue(0);
                accountProRef.setValue(0);




                if (nFeedback.getText().toString().isEmpty()) {

                    nFeedback.setText("No suggestion/feedback given.");

                } else {

                    nFeedbackStr = nFeedback.getText().toString();
                    nFeedback.setText("");
                    remarks = 1;
                    endService();

                    //        Toast.makeText(ConsumerMapActivity.this, " PENDING ID " + serviceId , Toast.LENGTH_LONG).show();
                    if (schedControl.equals("schedule")) {
                        schedControl = "";
                        Toast.makeText(ConsumerMapActivity.this, "Thank you for sharing your feedback", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ConsumerMapActivity.this, WelcomeMain.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ConsumerMapActivity.this, "Thank you for sharing your feedback", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ConsumerMapActivity.this, WelcomeMain.class);
                        startActivity(intent);
                    }
                }
///////////////////////////////Feedback reports
                DatabaseReference feedbackRef = FirebaseDatabase.getInstance().getReference().child("Feedback").child(serviceId);

                 HashMap feedbackMap = new HashMap();
                feedbackMap.put("dateSched", nServiceDate);
                feedbackMap.put("serviceId", serviceId);
                feedbackMap.put("providerName", pName);
                feedbackMap.put("consumerName", schName);
                feedbackMap.put("consumerServiceRequest", nRequestServiceType);
                feedbackMap.put("houseLocation", nAddress);
                feedbackMap.put("providerImage", schProviderProfileImageUrl);
                feedbackMap.put("consumerImage", schProfileImageUrl);
                feedbackMap.put("ratingStar", nRateScaleStr);
                feedbackMap.put("Feedback", nFeedbackStr);

                feedbackRef.updateChildren(feedbackMap);
                accountRef.setValue(0);

                //////////////////////////////////////
            }
        });
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////ratingField_END

    ///////////////////////////////////////////////
    private void setRating() {

        //    DatabaseReference nProviderRatingDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(providerFoundID).child("age");  //creating new field/table "rating"
        //    nProviderRatingDb.setValue(true);                                 //creating new field "serviceId" into "rating" field/table, then putting a value intoage it.


        DatabaseReference nProviderRatingDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(providerFoundID);

        //   float rating = ratingBar.getRating();
        nProviderRatingDb.child("ratingnew").setValue(String.valueOf(rating));

        //   HashMap mamap = new HashMap();
        //   mamap.put("ratingnew", rating);
        //   nProviderRatingDb.updateChildren(mamap);

    }


////////////////////////////////


    /////////////////////////////////////////////////////////////////////////////////////////////////////////serviceCancelled
    private DatabaseReference serviceHasCancelledRef;
    ValueEventListener serviceHasCancelledListener;

    public void serviceCancelled() {


        //serviceHasEndedRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(providerFoundID).child("consumerRequest").child("consumerId");
        // serviceHasEndedRefListener = serviceHasEndedRef.addValueEventListener(new ValueEventListener() {
        serviceHasCancelledRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(providerFoundID).child("cancelRequest");
        serviceHasCancelledListener = serviceHasCancelledRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {


                    //              } else {
                    //                  Toast.makeText(ConsumerMapActivity.this, "The Service Provider has CANCELLED your request ! ", Toast.LENGTH_SHORT).show(); //edit
                    //                 remarks = 1;
                    //                 isSchedCancel = "";
                    //                 isSched = "";
                    //                 endService();


                    //            }


                    /////////
                } else if (schedControl.equals("schedule")) {
                    ///////////////
                    Toast.makeText(ConsumerMapActivity.this, "The Service Provider has CANCELLED your request ! ", Toast.LENGTH_SHORT).show(); //edit
                    remarks = 1;
                    isSchedCancel = "";
                    isSched = "";
                  //  isProvidersCancel = true;

                    serviceHasCancelledRef.removeEventListener(serviceHasCancelledListener);

                    endService();
                } else {
                    Toast.makeText(ConsumerMapActivity.this, "The Service Provider has CANCELLED your request ! ", Toast.LENGTH_SHORT).show(); //edit
                    remarks = 1;
                    isSchedCancel = "";
                    isSched = "";
                 //   isProvidersCancel = true;
                    serviceHasCancelledRef.removeEventListener(serviceHasCancelledListener);
                    endService();

                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////serviceCancelled_END

    ////////////////////////////////////////


    /////////////////////////////////////////////////////////////////////////////////////////////////////////getHasServiceEnded
    private DatabaseReference serviceHasEndedRef;
    private ValueEventListener serviceHasEndedRefListener;

    public void getHasServiceEnded() {
        serviceHasEndedRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(providerFoundID).child("consumerRequest").child("consumerId");
        serviceHasEndedRefListener = serviceHasEndedRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {


                } else {

                    //         if (isSched.equals("on")  && isSchedCancel.equals("no")){
                    //           isSchedEndedNatural = "yes";
                    //      }


                    remarks = 0;
                    endService();
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////getHasServiceEnded_END


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////endService
    private void endService() {

        if (isSchedCancel.equals("yes")) {

            Intent intent = new Intent(ConsumerMapActivity.this, WelcomeMain.class);
            startActivity(intent);
        }

        if (schedControl.equals("schedule")) {

            ////removing the Pending Request
            DatabaseReference PendingRef = FirebaseDatabase.getInstance().getReference().child("Pending").child(pendingConId);
            PendingRef.removeValue();   //////removing value in the database

            DatabaseReference Consusched = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(userId).child("RequestServiceSchedule").child(date);
            //   DatabaseReference Consusched = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(userId).child("RequestServiceSchedule").child("07-30-2018");
            Consusched.removeValue();

            providerschedId = null;

            //////////////////////removing the pending reference code
            DatabaseReference ConPendingRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(userId).child("pending").child(pendingConId);
            ConPendingRef.removeValue();

            requestBol = false;
            //   geoQuery.removeAllListeners();                                             //////removing ALL geoQuery Listener
            //    providerLocationRef.removeEventListener(providerLocationRefListener);      //////removing ALL LOCATION Map Listener
//     serviceHasEndedRef.removeEventListener(serviceHasEndedRefListener);       //////removing serviceHasEndedRef Listener

            //   serviceHasCancelledRef.removeEventListener(serviceHasCancelledRefListener);       //////removing serviceHasEndedRef Listener
            serviceDoneRef.removeEventListener(serviceDoneListener);
            requestAcceptRef.removeEventListener(requestAcceptListener);       //////removing serviceHasEndedRef Listener
            serviceDoneRef.removeEventListener(serviceDoneListener);
            serviceHasCancelledRef.removeEventListener(serviceHasCancelledListener);

            isSched = "";
            isSchedCancel = "";
            providerFound = false;
            radius = 5;
            remarks = 0;

            if (nProviderMarker != null) {
                nProviderMarker.remove();
            }

            nMessageBooking.setText("");
            nRequest.setText("Call a service provider");

            nProviderInfo.setVisibility(View.GONE);
            nChatLayout.setVisibility(View.GONE);


            nProviderName.setText("");
            nProviderPhone.setText("");
            nProviderAge.setText("");
            nProviderProfileImage.setImageResource(R.mipmap.ic_profileimage);
            nProviderServiceType.setText("");

            //   Toast.makeText(ConsumerMapActivity.this, "Thank you for sharing your feedback" , Toast.LENGTH_SHORT).show();
            //  Intent intent =  new Intent(ConsumerMapActivity.this, WelcomeMain.class);
            //  startActivity(intent);

///not schedule
        } else {

            isTimerStart = false;
            isTimerEnd = false;
            isSched = "";
            isSchedCancel = "";
            requestBol = false;
            counter = 0;
            geoQuery.removeAllListeners();                                             //////removing ALL geoQuery Listener

            //////////////////////////
     //       if(hasTimerEnd){

      //      }else{
                if(providerLocationRef !=null){
                    providerLocationRef.removeEventListener(providerLocationRefListener);      //////removing ALL LOCATION Map Listener
                }

                if(serviceHasEndedRef !=null){
                    serviceHasEndedRef.removeEventListener(serviceHasEndedRefListener);       //////removing serviceHasEndedRef Listener
                }

                if(serviceDoneRef !=null){
                    serviceDoneRef.removeEventListener(serviceDoneListener);       //////removing serviceHasEndedRef Listener
                }

                if(requestAcceptRef !=null){
                    requestAcceptRef.removeEventListener(requestAcceptListener);       //////removing serviceHasEndedRef Listener
                }

                if(serviceDoneRef !=null){
                    serviceDoneRef.removeEventListener(serviceDoneListener);
                }

                if(serviceHasCancelledRef !=null){
                    serviceHasCancelledRef.removeEventListener(serviceHasCancelledListener);
                }

      //      }
            ////////////////////////////

            //////////////////////////////
            if (remarks == 1) {
                hasRequest = true;
                 if(hasTimerEnd){

                 }else{
                     DatabaseReference providerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(providerFoundID).child("consumerRequest");
                     providerRef.removeValue();   //////removing value in the database
                     providerFoundID = null;

                 }

            } else if (remarks == 0) {
            }
            /////////////////////////////////


            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("consumerRequest");
            GeoFire geoFire = new GeoFire(ref);
          //  geoFire.removeLocation(userId);
            geoFire.removeLocation(userId);
            remarks = 0;
            providerFound = false;
            radius = 5;
       //     String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
       //     DatabaseReference ref = FirebaseDatabase.getInstance().getReference("consumerRequest");
       //     GeoFire geoFire = new GeoFire(ref);
       //     geoFire.removeLocation(userId);

            if (nProviderMarker != null) {
                nProviderMarker.remove();
            }
            nMessageBooking.setText("");
            nRequest.setText("Call a service provider");

            nProviderInfo.setVisibility(View.GONE);
            nChatLayout.setVisibility(View.GONE);

            nProviderName.setText("");
            nProviderPhone.setText("");
            nProviderAge.setText("");
            nProviderProfileImage.setImageResource(R.mipmap.ic_profileimage);
            nProviderServiceType.setText("");

            if (isSchedDone.equals("yes")) {
                isSchedDone = "";
                Intent intent = new Intent(ConsumerMapActivity.this, WelcomeMain.class);
                startActivity(intent);
                // finish();
            }
            if(hasTimerEnd){
               Intent intent = new Intent(ConsumerMapActivity.this, ConsumerMapActivity.class);
               startActivity(intent);
             //  Intent intent = getIntent();
              //  finish();
             //  startActivity(intent);
            }
      //      else{
       //         Intent intent= getIntent();
        //        startActivity(intent);
         //   }


        }


    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////endService_END


    //////////////////////////////////////////////onMapReady
    @Override
    public void onMapReady(GoogleMap googleMap) {
        nMap = googleMap;


        try{
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ConsumerMapActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                // return;
            }
            buildGoogleApiClient();
            nMap.setMyLocationEnabled(true);

        }catch(Exception e){
            e.printStackTrace();
        }



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


    //////////////////////////////////////////////////////////////////////////onLocationChanged
    @Override
    public void onLocationChanged(Location location) {

        if (getApplicationContext() != null) {

            nLastLocation = location;
            LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
            nMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
            nMap.moveCamera(CameraUpdateFactory.zoomTo(15));

           /*
            /////////////////////////////////////////
            //  String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            try{

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("consumerRequest");
                GeoFire geoFire = new GeoFire(ref);
                geoFire.setLocation(userId, new GeoLocation(nLastLocation.getLatitude(), nLastLocation.getLongitude()));///===============================
                deviceLocation = new LatLng(nLastLocation.getLatitude(), nLastLocation.getLongitude());
                //   deviceLocationSched = new LatLng(nLastLocationSched.getLatitude(), nLastLocationSched.getLongitude());         to be added

                if (houseMarker != null) {
                    houseMarker.remove();
                }
                houseMarker = nMap.addMarker(new MarkerOptions().position(deviceLocation).title("Consumer House").icon(BitmapDescriptorFactory.fromResource(R.mipmap.fixit_house)));

            }catch(Exception e){
                e.printStackTrace();
            }
            /////////////////////////////////////////////
          */


            //   String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            //   DatabaseReference ref = FirebaseDatabase.getInstance().getReference("providerAvailable");

            //   GeoFire geoFire = new GeoFire(ref);
            //   geoFire.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()));

        }

    }
    //////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////onConnected
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        nLocationRequest = new LocationRequest();
        nLocationRequest.setInterval(2000);
        nLocationRequest.setFastestInterval(2000);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ConsumerMapActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            // return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(nGoogleApiClient, nLocationRequest, this);

    }

    //////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    //////////////////////////////////////////////////////////////////

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

    ///////////////////////////////////////disconnectProvider
    private void disconnectProvider() {

        if (nGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(nGoogleApiClient, this);

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("providerAvailable");

            GeoFire geoFire = new GeoFire(ref);
            geoFire.removeLocation(userId);
        }
    }
    //////////////////////////////////////

    private void disconnectConsumerRequest() {
        isLoggingOut = false;
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference consumerRequestRef = FirebaseDatabase.getInstance().getReference().child("consumerRequest").child(userId);

        consumerRequestRef.removeValue();

    }

    //=======================================================================================================================================================================
    //////////////////////////////////////////////onStop
    @Override
    protected void onStop() {

        super.onStop();
        if (!isLoggingOut) {
            disconnectProvider();
            disconnectConsumerRequest();

        }

    }

    @Override
    protected void onPause() {

        super.onPause();

    }

    //////////////////////////////////////////////onStart
   @Override
     protected void onStart() {
       hasNoProvider = false;
       hasTimerEnd = false;
       isTimerStart = false;
       counter = 0;
       super.onStart();
 }
    //////////////////////////////////////////////

    //////////////////////////////////////////////onResume
    @Override
    protected void onResume() {

        super.onResume();
        // this.onCreate(null);     //to reload activity
    }///////////////////////////////////

    @Override
    public void onBackPressed() {
         DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }


        //  } else {

        try {
            if (requestBol) {
                pressControl = "onBackPressed";
                isCancelled();
            } else {
               item1 = "";
                item2 = "";
                item3 = "";
                item4 = "";
                item5 = "";
                nItemOne = "";
                nItemThree = "";
                nItemTwo = "";
                nItemFour = "";
                nItemFive = "";


                Intent intent = new Intent(ConsumerMapActivity.this, WelcomeMain.class);
                startActivity(intent);
                finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    //    super.onBackPressed();

        //
        // }
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
            accountConsumer();
        } else if (id == R.id.nav_history) {
            historyDetails();

        } else if (id == R.id.nav_pending) {
            pendingDetails();

        } else if (id == R.id.nav_changePassword) {
            changePassword();

        } else if (id == R.id.nav_logout) {
            signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

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

    }

    private void passwordChange() {
        Map consumerInfo = new HashMap();
        consumerInfo.put("password", passChanged);
        nConsumerDatabase.updateChildren(consumerInfo);


    }
    //////////////////////////////////////////changePassword_END
    ////////////////////////////////////////////////signOut
    public void signOut() {

        isLoggingOut = true;
        disconnectProvider();
        //automatic logout
        FirebaseAuth.getInstance().signOut();
        finishAffinity();
    //    Intent intent = new Intent(ConsumerMapActivity.this, ConsumerLoginActivity.class);
     //   startActivity(intent);
     //   finish();
     //   return;
    }
//////////////////////////////////////////////signOut_END



    /////////////////////////////////////////////////////////////////////////historyDetais
    public void historyDetails() {
        Intent intent = new Intent(ConsumerMapActivity.this, HistoryActivity.class);
        intent.putExtra("consumerOrProvider", "Consumers");                 //The value "Consumers" is the Consmers
        startActivity(intent);
        //  finish();
    }
    ////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////////////pendingDetails
    public void pendingDetails() {
        Intent intent = new Intent(ConsumerMapActivity.this, PendingActivity.class);
        intent.putExtra("fromActivity", "welcome");
        startActivity(intent);
        //  finish();
    }
    ////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////********************************accountConsumer
    public void accountConsumer() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        //  dialog.setTitle("PERSONAL ACCOUNT");
        //  dialog.setMessage("Please use your email to sign in");

        LayoutInflater inflater = LayoutInflater.from(this);
        View account_layout = inflater.inflate(R.layout.layout_account_consumer, null);

        tvNameField = account_layout.findViewById(R.id.name);
        tvPhoneField = account_layout.findViewById(R.id.phoneNumber);
        nProfileImage = account_layout.findViewById(R.id.profileImage);
        nUpdate = account_layout.findViewById(R.id.update);
        nBack = account_layout.findViewById(R.id.back);

        dialog.setView(account_layout);

        final AlertDialog dialogshow = dialog.show();

        nAuth = FirebaseAuth.getInstance();
        userId = nAuth.getCurrentUser().getUid();
        nConsumerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(userId);


///////////////nProfileImage_selecting image
        nProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE);


            }
        });
        //////////////


        /////////////////////////////////////////////////////////////////////// nConsumerDatabase_ValueEventListener
        nConsumerDatabase.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    if (map.get("name") != null) {
                        nName = map.get("name").toString();
                        nUserName = nName;
                        tvNameField.setText(nName);
                    }

                    if (map.get("phone") != null) {
                        nPhone = map.get("phone").toString();
                        tvPhoneField.setText(nPhone);

                    }

                    if (map.get("profileImageUrl") != null) {
                        nProfileImageUrl = map.get("profileImageUrl").toString();
                        Glide.with(getApplication()).load(nProfileImageUrl).into(nProfileImage);
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////


///////////////
/*
        dialog.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                ////////////////////////getConsumerInformation
                if (tvPhoneField.getText().toString().length() != 11) {
                    Snackbar.make(nRootLayout, "Please enter valid phone number", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                saveConsumerInformation();
             startActivity(new Intent(ConsumerMapActivity.this, ConsumerMapActivity.class));
            }


        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();

    }
*///////////////

        //////////////////////






        nUpdate.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {
                dialogshow.dismiss();
                if (tvPhoneField.getText().toString().length() != 11) {
                    Snackbar.make(nRootLayout, "Please enter valid phone number", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                saveConsumerInformation();
                startActivity(new Intent(ConsumerMapActivity.this, ConsumerMapActivity.class));
            }

        });


        nBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogshow.dismiss();
                return;
            }
        });
    }
    //////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////********************************accountConsumer_end


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////saveConsumerInformation
    private void saveConsumerInformation() {



        nName = tvNameField.getText().toString();
        nPhone = tvPhoneField.getText().toString();

        Map consumerInfo = new HashMap();
        consumerInfo.put("name", nName);
        consumerInfo.put("phone", nPhone);
        nConsumerDatabase.updateChildren(consumerInfo);

        if (resultUri != null) {
            StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profile_images").child(userId);

            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filePath.putBytes(data);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //   Toast.makeText(ConsumerMapActivity.this, "Failed upload image", Toast.LENGTH_SHORT ).show();
                    finish();
                    return;
                }
            });

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {       //#16 Saving profile image... creating listener
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    Map newImage = new HashMap();
                    newImage.put("profileImageUrl", downloadUrl.toString());
                    nConsumerDatabase.updateChildren(newImage);


                    finish();
                    return;


                }
            });
        } else {
            finish();
            return;
        }


    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////nProfileImage_onActivityResult_
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {            //#16 Saving profile image   Creating the preDefined method onActivityResult
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                final Uri imageUri = data.getData();                                               //#16 Saving profile image   // getting picture from gallery   ...final
                resultUri = imageUri;
                final InputStream imageStream = getContentResolver().openInputStream(resultUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                nProfileImage.setImageBitmap(selectedImage);
                //   String imageName = UUID.randomUUID().toString();   //Random name image upload

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(deviceLocation);
        builder.include(providerLatLng);
        LatLngBounds bounds = builder.build();
        ///padding
        int width = getResources().getDisplayMetrics().widthPixels;
        int padding = (int) (width * 0.1);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        //camera animate
        nMap.animateCamera(cameraUpdate);

        if (nProviderMarker != null) {
            nProviderMarker.remove();
        }
        if (houseMarker != null) {
            houseMarker.remove();
        }

        nProviderMarker = nMap.addMarker(new MarkerOptions().position(providerLatLng).title("Service Provider").icon(BitmapDescriptorFactory.fromResource(R.mipmap.fixit_provider)));       // #13 Change Marker  ++++++++++++
        houseMarker = nMap.addMarker(new MarkerOptions().position(deviceLocation).title("Consumer House").icon(BitmapDescriptorFactory.fromResource(R.mipmap.fixit_house)));


    }

    @Override
    public void onRoutingCancelled() {

    }
    ////////////////////////////////////////////////////////////////


}