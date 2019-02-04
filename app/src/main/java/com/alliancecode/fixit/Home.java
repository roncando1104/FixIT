package com.alliancecode.fixit;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alliancecode.fixit.Model.UserConsumer;
import com.bumptech.glide.Glide;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.service.Common;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {


    ////////////////////////
    private GoogleMap nMap;
    GoogleApiClient nGoogleApiClient;
    Location nLastLocation;
    LocationRequest nLocationRequest;
    // private SupportMapFragment mapFragment;
    // FusedLocationProviderClient mFusedLocationClient;
    private Button nLogout, nSettings;
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

    private String consumerId = "";
    private Boolean isLoggingOut = false;

    private LinearLayout nConsumerInfo;
    private ImageView nConsumerProfileImage;
    private TextView nConsumerName, nConsumerPhone, nConsumerHouse;
    private DatabaseReference nProviderDatabase;

//////////////////////////


    final int LOCATION_REQUEST_CODE = 1;


    private Button nRequest;

    private LatLng pickupLocation;


    private Boolean requestBol = false;

    private Marker pickupMarker;

    private TextView nMessageBooking;

    private String houseLocation;

    private LinearLayout nProviderInfo;
    private ImageView nProviderProfileImage;
    private TextView nProviderName, nProviderPhone, nProviderServiceType;

    private CardView nFragmentPlaceAutocomplete;
    private RelativeLayout nFragmentMap;
    private LinearLayout nFragmentCallService;

    //  drawerImage.setImageDrawable(nProfileImage);

    //  TextView drawerUsername = (TextView) headerView.findViewById(R.id.drawer_username);

    ///////////////////////Settings
  //  private DatabaseReference nProviderDatabase;
    private FirebaseDatabase nDbase;
    private FirebaseAuth nAuth;
    private String userId;
    private String nName,nPhone,nServiceType;
    private String nProfileImageUrl;
    private ImageView nProfileImage;
    private static final int REQUEST_CODE = 1;
    private Uri resultUri;
    private TextView tvNameField,tvPhoneField, tvServiceTypeField ;
    private TextView txtProviderName;
    private CircleImageView imageAvatar;


    ////////////////////////////////////////

    //  NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeprovider);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


////////////////////////////////////////////////////////
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Home.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            // return;
        } else {
            mapFragment.getMapAsync(this);
        }


        nConsumerInfo = (LinearLayout) findViewById(R.id.consumerInfo);

        nConsumerProfileImage = (ImageView) findViewById(R.id.consumerProfileImage);

        nConsumerName = (TextView) findViewById(R.id.consumerName);
        nConsumerPhone = (TextView) findViewById(R.id.consumerPhone);
        nConsumerHouse = (TextView) findViewById(R.id.consumerHouse);

////////////////////////////////////////////////////////////


        nFragmentMap = (RelativeLayout) findViewById(R.id.fragmentMap);
        nFragmentCallService = (LinearLayout) findViewById(R.id.fragmentCallService);


      //  nProviderInfo = (LinearLayout) findViewById(R.id.providerInfo);

       // nProviderProfileImage = (ImageView) findViewById(R.id.providerProfileImage);

      //  nProviderName = (TextView) findViewById(R.id.providerName);
       // nProviderPhone = (TextView) findViewById(R.id.providerPhone);
      //  nProviderServiceType = (TextView) findViewById(R.id.providerServiceType);


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerProvider_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


/////////////////////////////////////////////////////////////////////////////////////////Navigation Header Settings
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_viewProvider);
        navigationView.setNavigationItemSelectedListener(this);
        View navigationHeaderView = navigationView.getHeaderView(0);
        txtProviderName = (TextView) navigationHeaderView.findViewById(R.id.txtProviderName);
        imageAvatar = (CircleImageView) navigationHeaderView.findViewById(R.id.profileImage);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        nDbase = FirebaseDatabase.getInstance();
        nProviderDatabase = nDbase.getReference().child("Users").child("Providers").child(userId);


        nProviderDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //   String name = (String) dataSnapshot.child("Users").child("Consumers").child(userId).child("name").getValue();
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    java.util.Map<String, Object> map = (java.util.Map<String, Object>) dataSnapshot.getValue();

                    if (map.get("name") != null) {

                        txtProviderName.setText(map.get("name").toString());

                    }

                    if (map.get("profileImageUrl") != null) {

                        Glide.with(getApplication()).load(map.get("profileImageUrl").toString()).into(imageAvatar);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
/////////////////////////////////////////////////////////////////////////////////////////

        getAssignedConsumer();


    }
///////////-----------------------------------------------------------------------------------------------------------------------------
    /////////////////////////////////////////////////////////////////////////////getAssignedConsumer
    public void getAssignedConsumer() {
        String providerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference assignedConsumerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(providerId).child("consumerRequest").child("consumerRideId");

        assignedConsumerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {               // Note: if dataSnapshot is exist it means that there is current job ongoing...
                if (dataSnapshot.exists()) {                      // #12 Canceling-an-Uber-Request @@@@@@@       if the child(consumerRideId) is removed this dataSnapshot.exists() will be false (else)
                    consumerId = dataSnapshot.getValue().toString();

                    //call a function
                    getAssignedConsumerPickupLocation();
                    getAssignedConsumerHouse();  //#18 place-autocomplete API.... method for consumerHouse Location ..
                    getAssignedConsumerInfo();

                } else {                                         // #12 Canceling-an-Uber-Request @@@@@@@        checking if the child(consumerRideId) is removed.
                    consumerId = "";                               // #12 Canceling-an-Uber-Request @@@@@@@     initiating variable back again
                    if (pickupMarker != null) {                 // #12 Canceling-an-Uber-Request @@@@@@@      removing Marker
                        pickupMarker.remove();
                    }
                    if (assignedConsumerPickupLocationRefListener != null) {    // #12 Canceling-an-Uber-Request @@@@@@@    bugs fix...adding if statement to remove the EventListener
                        assignedConsumerPickupLocationRef.removeEventListener(assignedConsumerPickupLocationRefListener);    // #12 Canceling-an-Uber-Request @@@@@@@  removing EventListener
                    }
                    nConsumerInfo.setVisibility(View.GONE);         //#17 Display Consumer Info in Providers Screen... // if the Provider is not already working or not working.
                    nConsumerName.setText("");                    //#17 Display Consumer Info in Providers Screen..
                    nConsumerPhone.setText("");                     //#17 Display Consumer Info in Providers Screen..
                    nConsumerProfileImage.setImageResource(R.mipmap.ic_profileimage);      //#17 Display Consumer Info in Providers Screen..
                    nConsumerHouse.setText("House Location: --- ");     //#18 place-autocomplete API........


                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
//////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////getAssignedConsumerPickupLocation
   // Marker pickupMarker;
    private DatabaseReference assignedConsumerPickupLocationRef;
    private ValueEventListener assignedConsumerPickupLocationRefListener;

    public void getAssignedConsumerPickupLocation() {
        assignedConsumerPickupLocationRef = FirebaseDatabase.getInstance().getReference().child("consumerRequest").child(consumerId).child("l");

        assignedConsumerPickupLocationRefListener = assignedConsumerPickupLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && !consumerId.equals("")) {
                    List<Object> map = (List<Object>) dataSnapshot.getValue();
                    double locationLat = 0;
                    double locationLng = 0;

                    if (map.get(0) != null) {
                        locationLat = Double.parseDouble(map.get(0).toString());
                    }

                    if (map.get(1) != null) {
                        locationLng = Double.parseDouble(map.get(1).toString());
                    }

                    LatLng providerLatLng = new LatLng(locationLat, locationLng);
                    //     pickupMarker = nMap.addMarker(new MarkerOptions().position(providerLatLng).title("Pickup Location").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_house)));      //  #13 Change Marker
                    pickupMarker = nMap.addMarker(new MarkerOptions().position(providerLatLng).title("Pickup Location"));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
///////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////getAssignedConsumerHouse
    public void getAssignedConsumerHouse() {
        String providerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference assignedConsumerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(providerId).child("consumerRequest").child("houseLocation");

        //  assignedConsumerRef.addValueEventListener(new ValueEventListener() {
        assignedConsumerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String houseLocation = dataSnapshot.getValue().toString();
                    nConsumerHouse.setText("House Location: " + houseLocation);


                } else {
                    nConsumerHouse.setText("House Location: --- ");
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    ///////////////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////////////////////////getAssignedConsumerInfo
    private void getAssignedConsumerInfo() {

        nConsumerInfo.setVisibility(View.VISIBLE);
        DatabaseReference nConsumerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Consumers").child(consumerId);
        nConsumerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    if (map.get("name") != null) {

                        nConsumerName.setText(map.get("name").toString());
                    }

                    if (map.get("phone") != null) {

                        nConsumerPhone.setText(map.get("phone").toString());

                    }

                    if (map.get("profileImageUrl") != null) {

                        Glide.with(getApplication()).load(map.get("profileImageUrl").toString()).into(nConsumerProfileImage);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
///////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////onLocationChanged
    @Override
    public void onLocationChanged(Location location) {
        if (getApplicationContext() != null) {

            nLastLocation = location;
            LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
            nMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
            nMap.moveCamera(CameraUpdateFactory.zoomTo(16));

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            DatabaseReference refAvailable = FirebaseDatabase.getInstance().getReference("providerAvailable");
            DatabaseReference refWorking = FirebaseDatabase.getInstance().getReference("providerWorking");

            GeoFire geoFireAvailable = new GeoFire(refAvailable);
            GeoFire geoFireWorking = new GeoFire(refWorking);


            switch (consumerId) {
                case "":
                    geoFireWorking.removeLocation(userId);
                    geoFireAvailable.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()));
                    break;
                default:
                    geoFireAvailable.removeLocation(userId);
                    geoFireWorking.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()));
                    break;
            }
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////////////////onConnected
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        nLocationRequest = new LocationRequest();
        nLocationRequest.setInterval(1000);
        nLocationRequest.setFastestInterval(1000);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Home.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
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
                    mapFragment.getMapAsync(this);
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
        LocationServices.FusedLocationApi.removeLocationUpdates(nGoogleApiClient, this);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("providerAvailable");

        GeoFire geoFire = new GeoFire(ref);
        geoFire.removeLocation(userId);
    }
    ////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////onMapReady
    @Override
    public void onMapReady(GoogleMap googleMap) {
        nMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Home.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
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


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////Part of Navigation Bar Menu
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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


    ////////////////////////////////////////////////signOut
    public void signOut() {

        isLoggingOut = true;
        disconnectProvider();
        //automatic logout
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(Home.this, ProviderLoginActivity.class);
        startActivity(intent);
        finish();
        return;
    }
//////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////=============********************************accountProvider
    public void accountProvider() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("PERSONAL ACCOUNT");
        //  dialog.setMessage("Please use your email to sign in");

        LayoutInflater inflater = LayoutInflater.from(this);
        View account_layout = inflater.inflate(R.layout.activity_provider_settings, null);

        tvNameField = account_layout.findViewById(R.id.name);
        tvPhoneField = account_layout.findViewById(R.id.phoneNumber);
        tvServiceTypeField = account_layout.findViewById(R.id.serviceType);

        nProfileImage = account_layout.findViewById(R.id.profileImage);

        dialog.setView(account_layout);


        nAuth = FirebaseAuth.getInstance();
        userId = nAuth.getCurrentUser().getUid();
        nProviderDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(userId);


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


        /////////////////////////////////////////////////////////////////////// nProviderDatabase_ValueEventListener
        nProviderDatabase.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    if (map.get("name") != null) {
                        nName = map.get("name").toString();
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

                    if (map.get("serviceType")!=null){
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


        dialog.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();


                ////////////////////////getConsumerInformation

                saveProviderInformation();
                startActivity(new Intent(Home.this, Home.class));


            }


        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();

    }
    //////////////////////////////////////////////////////////////////////////////////////////********************************accountConsumer_end


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////saveConsumerInformation
    private void saveProviderInformation() {
        nName = tvNameField.getText().toString();
        nPhone = tvPhoneField.getText().toString();
        nServiceType = tvServiceTypeField.getText().toString();

        Map providerInfo = new HashMap();
        providerInfo.put("name", nName);
        providerInfo.put("phone", nPhone);
        providerInfo.put("serviceType", nServiceType);
        nProviderDatabase.updateChildren(providerInfo);

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
                    nProviderDatabase.updateChildren(newImage);

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
    ////////////////////////////////////////////////////////////////


}