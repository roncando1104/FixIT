package com.alliancecode.fixit;

        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.res.ColorStateList;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Color;
        import android.graphics.Typeface;
        import android.location.Location;
        import android.net.Uri;
        import android.os.Build;
        import android.os.Bundle;
        import android.provider.MediaStore;
        import android.support.annotation.NonNull;
        import android.support.design.widget.Snackbar;
        import android.support.design.widget.TextInputLayout;
        import android.support.v7.app.AlertDialog;
        import android.text.TextUtils;
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
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.ProgressBar;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.bumptech.glide.Glide;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.android.gms.location.LocationRequest;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.Marker;
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

        import java.io.ByteArrayOutputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.text.DateFormat;
        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.Calendar;
        import java.util.Date;
        import java.util.HashMap;
        import java.util.Locale;
        import java.util.Map;

        import de.hdodenhof.circleimageview.CircleImageView;

//import static com.alliancecode.fixit.Model.HistoryViewHolders.servicedID;

public class WelcomeMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final int LOCATION_REQUEST_CODE = 1;

    private GoogleMap nMap;
    GoogleApiClient nGoogleApiClient;
    Location nLastLocation;
    LocationRequest nLocationRequest;

    // private PlaceAutocompleteFragment autocompleteFragment;

    private Button nRequest;

    private LatLng deviceLocation;
    private LatLng destinationLatLng;

    private LinearLayout nScheduleAlert;

    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

    private Boolean requestBol = false;
    private Boolean isLoggingOut = false;
    private Boolean isDialogDismiss = false;

    private Marker houseMarker;

    private TextView nMessageBooking;

    private Boolean hasRequest ;
    private Boolean hasPassedRequest ;
    private Boolean isSuspensionExpire = false ;

    private Boolean hasNotified = false;
    private Boolean isAccountHasIssue = false;
    private Boolean isAccountClear = false;
    private Boolean isFeedbackUpdateDone = false;
    private Boolean isEmailVerified = true;

    private Boolean hasRequestConfirmed = false;


    private String pendingId;
    private String serviceTypeRequest, proName, charge, serviceDate, consuName;

    private String schProfileImageUrl;
    private String consumerFeedbackImageValue;
    private String feedBackImage;
    private  String feedbackServiceId;
    private String feedbackServiceIdFound;
    private String feedbackOldImageValue;
    private String imageValue;

    private String houseLocation;

    private ProgressBar progressBar;

    private LinearLayout nProviderInfo;
    private ImageView nProviderProfileImage;
    private TextView nProviderName, nProviderPhone, nProviderServiceType;

    //  private CardView nFragmentPlaceAutocomplete;
    private RelativeLayout nFragmentMap;
    private LinearLayout nFragmentCallService;
    private Typeface fontNunitoB;

    //  private RadioGroup nRadioGroup;////////////////////////
    private String nRequestServiceType, nServiceDate, nPayment, nAddress, nServiceTime, nConsumerNote;

    private DrawerLayout nRootLayout, drawer;

    private String pathKey;
    private DatabaseReference feedbackRef;
    ///////////////////////Settings
    private DatabaseReference nConsumerDatabase;
    private FirebaseDatabase nDbase;
    private FirebaseAuth nAuth;
    private String userId, requestId;
    private String nName, pName;

    private String passChanged;
    private String nPhone, pPhone;
    private String pService;
    private String nProfileImageUrl;
    private String conName, conPass;
    //  private ImageView nProfileImage;
    private CircleImageView nProfileImage;
    private static final int REQUEST_CODE = 1;
    private Uri resultUri;
    private TextView tvNameField;
    private TextView tvPhoneField, txtConsumerName ;
    private CircleImageView imageAvatar;
    private Button nUpdate, nBack;


    private Button nGetService;
    private int rating;
    private int count;
    private int countFeedback;
    private int countFeedbackRoute;



    ////////////////////////////////////////

    //  NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btnBook = (Button)findViewById(R.id.btnBook);

        //////////////////////////////////////////////////////////////////
        nRootLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        progressBar = (ProgressBar)findViewById(R.id.progressbar);

        ////////////////////////////////////////////////////////////////////
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    checkEmailVerified();

                }catch (Exception e){
                    e.printStackTrace();

                }


                try {
                    accountStatus();

                    if(hasRequest){
                        scheduleNotif();
                        //   finish();
                        return;
                    }

                    if(!hasRequest && isAccountClear){
                        Intent intent = new Intent(WelcomeMain.this, ConsumerHomeActivity.class);
                        startActivity(intent);
                        // finish();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////================================================================
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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


//////////////////////////////////////////////////////////////////



        ///////////

        nConsumerDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //   String name = (String) dataSnapshot.child("Users").child("Consumers").child(userId).child("name").getValue();
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    java.util.Map<String, Object> map = (java.util.Map<String, Object>) dataSnapshot.getValue();

                    if (map.get("name") != null) {
                        conName = map.get("name").toString();
                        txtConsumerName.setText(map.get("name").toString());
                    //    txtConsumerName.setTypeface(fontNunitoB);
                    }

                    if (map.get("password") != null) {
                        conPass = map.get("password").toString();
                        //    txtConsumerName.setTypeface(fontNunitoB);
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




        suspensionExpire();
        accountStatus();

        if(!isAccountHasIssue){
            schedule();
            passedSched();
            checkRequestSchedule();
        }


//////////////////////////////////////////////////////////////////////////////////

    }

    ///////////////////////////////////////////////////////suspensionExpire
    private Date suspendDateToday, suspendDateCheck;
    private String suspendDate;
    private Boolean hasAccountSuspension = false;
    private DatabaseReference suspendRef;
    private void suspensionExpire() {
        final DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        final Calendar cal = Calendar.getInstance();
        final String dateNow  = new SimpleDateFormat("MM-dd-yyyy").format(cal.getTime());

        //   monthPas = Integer.parseInt(dateNow.substring(0,2));
        //   dayPas = Integer.parseInt(dateNow.substring(0,2));


        suspendRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(userId);
        suspendRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    if (map.get("accountSuspended") != null) {
                        suspendDate = map.get("accountSuspended").toString();
                        hasAccountSuspension = true;
                    }else{
                        hasAccountSuspension = false;
                    }

                    if (hasAccountSuspension) {
                        try {
                            suspendDateCheck = df.parse(suspendDate);
                            suspendDateToday = df.parse(dateNow);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        if (suspendDateCheck.before(suspendDateToday)) {
                            isSuspensionExpire = true;
                //            Toast.makeText(WelcomeMain.this, "isSuspensionExpire Yes: " + suspendDate, Toast.LENGTH_LONG).show();

                        } else {
                //            Toast.makeText(WelcomeMain.this, "isSuspensionExpire No : " + suspendDate, Toast.LENGTH_LONG).show();
                        }
                    }



                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    //////////////////////////////////////////
    private void accountStatus() {
        //  DatabaseReference suspendRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(userId).child("cancelCount");
        DatabaseReference suspendRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(userId);

        suspendRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    if (map.get("cancelCount")!=null) {
                        count = Integer.valueOf(map.get("cancelCount").toString());
                        if (count >=6 ){
                            isAccountHasIssue = true;
                            blockedAccountNotif();
                        }else if (count == 4 && !isSuspensionExpire){
                            isAccountHasIssue = true;
                            suspendedAccountNotif();
                        }else{
                            isAccountClear = true;
                        }

                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    //////////////////////////////////
    private void suspendedAccountNotif() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        View check_layout = inflater.inflate(R.layout.layout_message_cancelcountfour, null);
        dialog.setView(check_layout);
        dialog.setCancelable(false);

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }

    private void blockedAccountNotif() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View check_layout = inflater.inflate(R.layout.layout_message_cancelcountsix, null);
        dialog.setView(check_layout);
        dialog.setCancelable(false);

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }


    /////////////////////////////////////////////////////////////////////////passedSched



    ////////////////////////////////////////////////////////passedSched
private Date datetoday, datepass;
private String datePass;
private DatabaseReference passSche;


    private void passedSched() {
        final DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        final Calendar cal = Calendar.getInstance();
        final String dateNow  = new SimpleDateFormat("MM-dd-yyyy").format(cal.getTime());

     //   monthPas = Integer.parseInt(dateNow.substring(0,2));
     //   dayPas = Integer.parseInt(dateNow.substring(0,2));


        passSche = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(userId).child("RequestServiceSchedule");
        passSche.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
      //          Map<String, Object> map = (Map<String, Object>) dataSnapshot.getChildren();

                if (dataSnapshot.exists()){
                for(DataSnapshot child: dataSnapshot.getChildren()){
                    datePass = child.getKey();

                  }

          //      pendingId = map.get(datePass)

                try {
                    datepass = df.parse(datePass);
                    datetoday = df.parse(dateNow);

                } catch (ParseException e) {          //ParseException issue
                    e.printStackTrace();
                }


                    if(datepass.before(datetoday)){
                        hasPassedRequest = true;
                        deletePassPending();
                        //            Toast.makeText(WelcomeMain.this, "May pending: "+datepass, Toast.LENGTH_LONG).show();
                    }else{
                        hasPassedRequest = false;
                        //           Toast.makeText(WelcomeMain.this, "NO PENDING: "+datetoday, Toast.LENGTH_LONG).show();
                    }



                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////deletePassPending
    private DatabaseReference passPending;
    private void deletePassPending() {

        final DatabaseReference pendId = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(userId).child("RequestServiceSchedule").child(datePass);
        pendId.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child: dataSnapshot.getChildren()){

                        if(child.getKey().equals("pendingId")){
                            pendingId = child.getValue().toString();
                            passPending = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(userId).child("pending").child(pendingId);

                        }

                        if(child.getKey().equals("providerName")) {
                            proName = child.getValue().toString();
                        }

                        if(child.getKey().equals("consumerServiceRequest")) {
                            serviceTypeRequest = child.getValue().toString();
                        }

                        if(child.getKey().equals("charge")) {
                            charge = child.getValue().toString();
                        }

                        if(child.getKey().equals("serviceDateToWork")) {
                            serviceDate = child.getValue().toString();
                        }

                        if(child.getKey().equals("consumerName")) {
                            consuName = child.getValue().toString();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        View check_layout = inflater.inflate(R.layout.layout_message_overdueschedule, null);

        dialog.setView(check_layout);
        dialog.setCancelable(false);

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                ServicesUnperformed();
                passSche.removeValue();
                passPending.removeValue();

            }
        });
        dialog.show();
    }


    ///////////////////////////////////////
    private void ServicesUnperformed() {

       DatabaseReference overDue = FirebaseDatabase.getInstance().getReference().child("PendingServicesUnperformed");
      //  requestId = overDue.push().getKey();
        HashMap map = new HashMap();                                                  ///////Saving data into Firebase
        map.put("providerName", proName);
        map.put("consumerName", consuName);
        map.put("consumerServiceRequest", serviceTypeRequest);
        map.put("payment", charge);
        map.put("dateSched", serviceDate);

        overDue.child(pendingId).updateChildren(map);                                              ///////Saving data into Firebase

    }
////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////schedule
    private void schedule() {
        Calendar cal = Calendar.getInstance();
        final String date = new SimpleDateFormat("MM-dd-yyyy").format(cal.getTime());

     DatabaseReference Consumersched = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(userId).child("RequestServiceSchedule").child(date);
     //     DatabaseReference Consumersched = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(userId).child("RequestServiceSchedule").child("08-15-2018");

        Consumersched.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    Intent intent = new Intent(WelcomeMain.this, ConsumerMapActivity.class);
                    startActivity(intent);
                  //  finish();
                }else{}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////schedule

    ///////////////////////////////////////
    private void checkRequestSchedule() {




        DatabaseReference Consumersched = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(userId).child("RequestServiceSchedule");
        Consumersched.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                if (dataSnapshot.exists() && !hasPassedRequest){
                        hasRequest = true;
                        if(!hasNotified){
                            scheduleNotif();
                            hasNotified = true;
                        }
                }else{
                    hasRequest = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //////////////////////////////////////
    private void scheduleNotif() {
        hasRequest = true;
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        View check_layout = inflater.inflate(R.layout.layout_message, null);
     //   nScheduleAlert = (LinearLayout)findViewById(R.id.linearscheduleAlert);
     //   nScheduleAlert.setVisibility(View.VISIBLE);
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
    ////////////////////////////////////////////////////////////////////////////////////////////schedule_END


    ///////////////////////////////////////////////////////checkEmailVerified
    private void checkEmailVerified() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        boolean emailVerified = user.isEmailVerified();
        if (!emailVerified) {

            hasRequest = null;
            isEmailVerified = false;
            verifyEmail();
        }
    //   else{
//  //  }
    }
////////////////////////////////////////////////
/////////////
private void verifyEmail() {

    final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

    LayoutInflater inflater = LayoutInflater.from(this);
    View sent_layout = inflater.inflate(R.layout.layout_message_emailverification, null);
    dialog.setView(sent_layout);
    dialog.setCancelable(false);

    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        //    signOut();
         //   Intent intent = new Intent(WelcomeMain.this, ConsumerLoginActivity.class);
         //   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          //  startActivity(intent);
        }
    });
    dialog.show();

}


    ///////////////////////////////
    //////////////////////////////////////////////onStop
    @Override
    protected void onStop() {
        super.onStop();
        //      if (!isLoggingOut) {

        //     }

    }



    @Override
    public void onBackPressed() {
        finishAffinity();
            super.onBackPressed();
        }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
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

            try{
                checkEmailVerified();

            }catch (Exception e){
                e.printStackTrace();
            }
            if(isEmailVerified){
                accountConsumer();
            }

        } else if (id == R.id.nav_history) {
            historyDetails();

        }  else if (id == R.id.nav_pending) {
            pendingDetails();
        }
        else if (id == R.id.nav_changePassword) {

            try{
                checkEmailVerified();

            }catch (Exception e){
                e.printStackTrace();

            }
            if(isEmailVerified){
                changePassword();
            }

        }else if (id == R.id.nav_logout) {
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

    /////////////////////////////////////////////////////////////////////////pendingDetails
    public void pendingDetails(){
        Intent intent = new Intent(WelcomeMain.this, PendingActivity.class);
        intent.putExtra("fromActivity", "welcome");                 //The value "Consumers" is the Consmers
        startActivity(intent);
      //  finish();
    }
    ////////////////////////////////////////////////////////

    ////////////////////////////////////////////////signOut
    public void signOut() {

        isLoggingOut = true;
        //     disconnectProvider();
        //automatic logout
        FirebaseAuth.getInstance().signOut();
        finishAffinity();

        //   Intent intent = new Intent(WelcomeMain.this, ConsumerLoginActivity.class);
     //   startActivity(intent);
      //  finish();
       // return;
    }
//////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////historyDetais
    public void historyDetails(){
        Intent intent = new Intent(WelcomeMain.this, HistoryActivity.class);
        intent.putExtra("consumerOrProvider", "Consumers");                 //The value "Consumers" is the Consmers
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
        dialog.setCancelable(false);


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
                startActivityForResult(intent, REQUEST_CODE );



            }
        });
        //////////////





        /////////////////////////////////////////////////////////////////////// nConsumerDatabase_ValueEventListener
        nConsumerDatabase.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    if (map.get("name")!=null){
                        nName = map.get("name").toString();
                        tvNameField.setText(nName);
                    }

                    if (map.get("phone")!=null){
                        nPhone = map.get("phone").toString();
                        tvPhoneField.setText(nPhone);

                    }

                    if (map.get("profileImageUrl")!=null){
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


////////////////////////////
        nUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogshow.dismiss();
                if (tvPhoneField.getText().toString().length() != 11) {
                    Snackbar.make(nRootLayout, "Please enter valid phone number", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                saveConsumerInformation();
                if(isFeedbackUpdateDone){
                   // startActivity(new Intent(WelcomeMain.this, WelcomeMain.class));
                    Intent intent = getIntent();
                    startActivity(intent);
                }
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

        if (resultUri != null){
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
                   final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                   feedBackImage =  downloadUrl.toString();


                 feedBackProfileImageUpdate();



                        Map newImage = new HashMap();
                        newImage.put("profileImageUrl", downloadUrl.toString());
                        nConsumerDatabase.updateChildren(newImage);
                        return;

             /*
                    Map newImage = new HashMap();
                    newImage.put("profileImageUrl", downloadUrl.toString());
                    nConsumerDatabase.updateChildren(newImage);
                   finish();
                    return;
              */


                }
            });
        } else {
            finish();
            return;
        }



    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////Update Consumer Image_ Feedback_
    private void feedBackProfileImageUpdate() {
        final DatabaseReference feedbackRef  = FirebaseDatabase.getInstance().getReference().child("Feedback");
        feedbackRef.orderByChild("consumerImage").equalTo(schProfileImageUrl)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                String feedbackId = dataSnapshot.getKey();
                                child.getRef().child("consumerImage").setValue(feedBackImage);
                                isFeedbackUpdateDone = true;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    ///////////////////////////////////////////////////////////////////nProfileImage_onActivityResult_
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {            //#16 Saving profile image   Creating the preDefined method onActivityResult
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try{
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
    ////////////////////////////////////////////////////////////////



}
