package com.alliancecode.fixit;

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
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeConsumer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    final int LOCATION_REQUEST_CODE = 1;

    private GoogleMap nMap;
    GoogleApiClient nGoogleApiClient;
    Location nLastLocation;
    LocationRequest nLocationRequest;

    private Button nRequest;

    private LatLng pickupLocation;

    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

    private Boolean requestBol = false;
    private Boolean isLoggingOut = false;

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
    private DatabaseReference nConsumerDatabase;
    private FirebaseDatabase nDbase;
    private FirebaseAuth nAuth;
    private String userId;
    private String nName;
    private String nPhone;
    private String nProfileImageUrl;
    private ImageView nProfileImage;
    private static final int REQUEST_CODE = 1;
    private Uri resultUri;
    private TextView tvNameField;
    private TextView tvPhoneField, txtConsumerName;
    private CircleImageView imageAvatar;


    ////////////////////////////////////////

    //  NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //  SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeConsumer.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            // return;
        } else {
            mapFragment.getMapAsync(this);
        }


 /*       /////////////////////////////////////////////////////////////////PlaceAutocompleteFragment
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                houseLocation = place.getName().toString();
                //  Log.i(TAG, "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                // Log.i(TAG, "An error occurred: " + status);
            }
        });
*/////////////////////////////////////////////////////////////////////////

  //      nFragmentPlaceAutocomplete = (CardView) findViewById(R.id.fragmentPlaceAutocomplete);
        nFragmentMap = (RelativeLayout) findViewById(R.id.fragmentMap);
        nFragmentCallService = (LinearLayout) findViewById(R.id.fragmentCallService) ;


        nProviderInfo = (LinearLayout) findViewById(R.id.providerInfo);

        nProviderProfileImage = (ImageView) findViewById(R.id.providerProfileImage);

        nProviderName = (TextView) findViewById(R.id.providerName);
        nProviderPhone = (TextView) findViewById(R.id.providerPhone);
        nProviderServiceType = (TextView) findViewById(R.id.providerServiceType);

        nMessageBooking = (TextView) findViewById(R.id.messageBooking);

        nRequest = (Button)findViewById(R.id.request);





///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        nDbase = FirebaseDatabase.getInstance();
        nConsumerDatabase = nDbase.getReference().child("Users").child("Consumers").child(userId);


        nConsumerDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //   String name = (String) dataSnapshot.child("Users").child("Consumers").child(userId).child("name").getValue();
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    if (map.get("name") != null) {

                        txtConsumerName.setText(map.get("name").toString());

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

////////////////////////////////////////////////////////////////////////////////Calling Service provider
        nRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (requestBol){

                    requestBol = false;
                    geoQuery.removeAllListeners();
                    providerLocationRef.removeEventListener(providerLocationRefListener);


                    if (providerFoundID != null){
                        DatabaseReference providerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(providerFoundID).child("consumerRequest");
                        providerRef.removeValue();
                        providerFoundID = null;

                    }

                    providerFound = false;
                    radius = 1;

                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("consumerRequest");

                    GeoFire geoFire = new GeoFire(ref);
                    geoFire.removeLocation(userId);

                    if (nProviderMarker !=null){
                        nProviderMarker.remove();
                    }
                    nMessageBooking.setText("");
                    nRequest.setText("Call a service provider");

                    nProviderInfo.setVisibility(View.GONE);
                    nProviderName.setText("");
                    nProviderPhone.setText("");
                    nProviderProfileImage.setImageResource(R.mipmap.ic_profileimage);
                    nProviderServiceType.setText("Service Type: --- " );

                }else {
                    requestBol = true;
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("consumerRequest");

                    GeoFire geoFire = new GeoFire(ref);
                    geoFire.setLocation(userId, new GeoLocation(nLastLocation.getLatitude(), nLastLocation.getLongitude()));

                    pickupLocation = new LatLng(nLastLocation.getLatitude(), nLastLocation.getLongitude());
                    pickupMarker = nMap.addMarker(new MarkerOptions().position(pickupLocation).title("Consumer House").icon(BitmapDescriptorFactory.fromResource(R.mipmap.fixit_house)));
                    nMessageBooking.setText("Getting your Service Provider...");

                    nRequest.setVisibility(View.INVISIBLE);
                    getClosestProvider();

                }


            }
        });
//////////////////////////////////////////////////////////////////////////////////

    }



    ////////////////////////////////////////////////////////////////////////getClosestProvider
    private int radius = 1;
    private Boolean providerFound = false;
    private String providerFoundID;

    private GeoQuery geoQuery;  // #12 Canceling-an-Uber-Request @@@@@@@

    private void getClosestProvider() {    //creating a function
        final DatabaseReference providerLocation = FirebaseDatabase.getInstance().getReference().child("providerAvailable");

        GeoFire geoFire = new GeoFire(providerLocation);

        geoQuery = geoFire.queryAtLocation(new GeoLocation(pickupLocation.latitude, pickupLocation.longitude), radius);
        geoQuery.removeAllListeners();

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (!providerFound && requestBol) {
                    providerFound = true;
                    providerFoundID = key;

                    //
                    // DatabaseReference providerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(providerFoundID);
                    DatabaseReference providerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(providerFoundID).child("consumerRequest");     //#18 place-autocomplete API... alert provider for consumer request
                    String consumerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    HashMap map = new HashMap();
                    map.put("consumerRideId", consumerId);
                    map.put("houseLocation", houseLocation);       //#18 place-autocomplete API...
                    providerRef.updateChildren(map);

                    //call function
                    getProviderLocation();
                    getProviderInformation();            //#20 Save-Display Provider Info in Consumer Screen

                    nMessageBooking.setText("Looking for Provider Location");
                    nRequest.setVisibility(View.VISIBLE);
                    nRequest.setText("CANCEL");


                }
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
                    radius++;
                    getClosestProvider();
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });

    }

    ////////////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////// getProviderLocation
    private Marker nProviderMarker;
    private DatabaseReference providerLocationRef;
    private ValueEventListener providerLocationRefListener;

    public void getProviderLocation() {
        providerLocationRef = FirebaseDatabase.getInstance().getReference().child("providerWorking").child(providerFoundID).child("l");

        providerLocationRefListener = providerLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && requestBol) {
                    List<Object> map = (List<Object>) dataSnapshot.getValue();
                    double locationLat = 0;
                    double locationLng = 0;
                    nMessageBooking.setText("Provider Found");
                    nRequest.setText("CANCEL");

                    if (map.get(0) != null) {
                        locationLat = Double.parseDouble(map.get(0).toString());
                    }

                    if (map.get(1) != null) {
                        locationLng = Double.parseDouble(map.get(1).toString());
                    }

                    LatLng providerLatLng = new LatLng(locationLat, locationLng);

                    if (nProviderMarker != null) {
                        nProviderMarker.remove();
                    }


                    Location loc1 = new Location("");
                    loc1.setLatitude(pickupLocation.latitude);
                    loc1.setLongitude(pickupLocation.longitude);

                    Location loc2 = new Location("");
                    loc2.setLatitude(providerLatLng.latitude);
                    loc2.setLongitude(providerLatLng.longitude);

                    float distance = loc1.distanceTo(loc2);

                    if (distance < 20) {
                        nMessageBooking.setText("Provider is Here : " + String.valueOf(distance) + " meter/s.");
                        nRequest.setText("CANCEL");
                    } else {
                        nMessageBooking.setText("Provider Found: " + String.valueOf(distance) + " meter/s.");
                        nRequest.setText("CANCEL");
                    }


                    nProviderMarker = nMap.addMarker(new MarkerOptions().position(providerLatLng).title("Your Service Provider").icon(BitmapDescriptorFactory.fromResource(R.mipmap.fixit_provider)));       // #13 Change Marker  ++++++++++++


                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    //////////////////////////////////////////////////


    //////////////////////////////////////////////////////////////////////////getProviderInformation
    private void getProviderInformation() {

        nProviderInfo.setVisibility(View.VISIBLE);
        DatabaseReference nConsumerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(providerFoundID);
        nConsumerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    if (map.get("name") != null) {

                        nProviderName.setText(map.get("name").toString());
                    }

                    if (map.get("phone") != null) {

                        nProviderPhone.setText(map.get("phone").toString());

                    }

                    if (map.get("serviceType") != null) {

                        nProviderServiceType.setText(map.get("serviceType").toString());

                    }

                    if (map.get("profileImageUrl") != null) {

                        Glide.with(getApplication()).load(map.get("profileImageUrl").toString()).into(nProviderProfileImage);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //////////////////////////////////////////////////////////////////////////


    //////////////////////////////////////////////onMapReady
    @Override
    public void onMapReady(GoogleMap googleMap) {
        nMap = googleMap;

/*
        // Add a marker in Sydney and move the camera
        LatLng manila = new LatLng(14.5872236, 120.9939596);
        nMap.addMarker(new MarkerOptions().position(manila).title("Marker in Philippines"));
        nMap.moveCamera(CameraUpdateFactory.newLatLng(manila));
*/
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeConsumer.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);

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


    //////////////////////////////////////////////////////////////////////////onLocationChanged
    @Override
    public void onLocationChanged(Location location) {
        nLastLocation = location;
        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
        nMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        nMap.moveCamera(CameraUpdateFactory.zoomTo(16));

        //   String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //   DatabaseReference ref = FirebaseDatabase.getInstance().getReference("providerAvailable");

        //   GeoFire geoFire = new GeoFire(ref);
        //   geoFire.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()));


    }
    //////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////onConnected
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        nLocationRequest = new LocationRequest();
        nLocationRequest.setInterval(1000);
        nLocationRequest.setFastestInterval(1000);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeConsumer.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
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
                    mapFragment.getMapAsync(this);
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
        LocationServices.FusedLocationApi.removeLocationUpdates(nGoogleApiClient, this);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("providerAvailable");

        GeoFire geoFire = new GeoFire(ref);
        geoFire.removeLocation(userId);
    }
    //////////////////////////////////////


    //////////////////////////////////////////////onStop
    @Override
    protected void onStop() {
        super.onStop();
        if (!isLoggingOut) {
            disconnectProvider();

        }


    }

    //////////////////////////////////////////////


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }    }

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

        } else if (id == R.id.nav_logout) {
            signOut();
        }
        //else if (id == R.id.nav_manage) {

        // } else if (id == R.id.nav_share) {

        //  } else if (id == R.id.nav_send) {

        // }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    ////////////////////////////////////////////////signOut
    public void signOut() {

        isLoggingOut = true;
        disconnectProvider();
        //automatic logout
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(HomeConsumer.this, ConsumerLoginActivity.class);
        startActivity(intent);
        finish();
        return;
    }
//////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////********************************accountConsumer
    public void accountConsumer() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("PERSONAL ACCOUNT");
        //  dialog.setMessage("Please use your email to sign in");

        LayoutInflater inflater = LayoutInflater.from(this);
        View account_layout = inflater.inflate(R.layout.activity_consumer_settings, null);

         tvNameField = account_layout.findViewById(R.id.name);
          tvPhoneField = account_layout.findViewById(R.id.phoneNumber);
          nProfileImage = account_layout.findViewById(R.id.profileImage);

        dialog.setView(account_layout);


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




        dialog.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();


                ////////////////////////getConsumerInformation

                saveConsumerInformation();
                startActivity(new Intent(HomeConsumer.this, HomeConsumer.class));





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