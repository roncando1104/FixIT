package com.alliancecode.fixit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

//public class HistorySingleActivity extends AppCompatActivity implements OnMapReadyCallback{
    public class HistorySingleActivity extends AppCompatActivity {

  //  private GoogleMap nMap;
 //   private SupportMapFragment nMapFragment;

    private String  currentUserId, consumerId, providerId, userProviderOrConsumer;
    private String serviceId;

    private TextView serviceDistance, serviceLocation, serviceDate, userName, userPhone, serviceGiven, paymentPaid, serviceDateDone ;
    private CircleImageView userImage;

    private String serviceGiv;

    private RatingBar nRatingBar;
  //  private RatingBar nRatingStar;

    private LinearLayout nLayoutRating;
    private LinearLayout nLayoutServiceGiven;

    private DatabaseReference historyServiceInformationDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_single);

        serviceId = getIntent().getExtras().getString("serviceId");              //getting Intent Extras Bundle from "HistoryViewHolders"

     //   nMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
     //   nMapFragment.getMapAsync(this);

        serviceDate = (TextView) findViewById(R.id.serviceDate);
        serviceDateDone = (TextView) findViewById(R.id.serviceDateDone);
     //   serviceDistance = (TextView) findViewById(R.id.serviceDistance);
        serviceLocation = (TextView) findViewById(R.id.serviceLocationn);
        paymentPaid = (TextView) findViewById(R.id.paymentPaid);
        userName = (TextView) findViewById(R.id.userName);
        userPhone = (TextView) findViewById(R.id.userPhone);
        serviceGiven = (TextView) findViewById(R.id.serviceGiven);

        userImage = (CircleImageView) findViewById(R.id.userImage);

        nLayoutServiceGiven = (LinearLayout) findViewById(R.id.layoutServiceGiven);
        nLayoutRating = (LinearLayout) findViewById(R.id.layoutRating);
        nRatingBar = (RatingBar) findViewById(R.id.ratingBar);
     //   nRatingStar = (RatingBar) findViewById(R.id.ratingStarBar);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        historyServiceInformationDb = FirebaseDatabase.getInstance().getReference().child("History").child(serviceId);

        getServiceInformation();




    }

    ///////////////////////////////////////////////////////////////////getServiceInformation
    private void getServiceInformation() {
        historyServiceInformationDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){


                        //////////////////////////////////////////////////////////////////consumerOrProvider
                        if(child.getKey().equals("consumer")){
                            consumerId = child.getValue().toString();

                            if(!consumerId.equals(currentUserId)){
                                userProviderOrConsumer = "Providers";                                    //the user is Provider
                                getUserInformation("Consumers", consumerId);          //getting data from Consumers table
                                nLayoutServiceGiven.setVisibility(View.GONE);
                                nLayoutRating.setVisibility(View.GONE);


                            }
                        }

                        if(child.getKey().equals("provider")){
                            providerId = child.getValue().toString();

                            if(!providerId.equals(currentUserId)){
                                userProviderOrConsumer = "Consumers";                                  //the user is Consumer
                                getUserInformation("Providers", providerId);          //getting data from Providers table
                                nLayoutServiceGiven.setVisibility(View.VISIBLE);
                                nLayoutRating.setVisibility(View.VISIBLE);
                        //      displayConsumerRelatedObjects();


                            }
                        }


                        //////////////////////////////////
                //        if(child.getKey().equals("timestamp")){
                //            serviceDate.setText(getDate(Long.valueOf(child.getValue().toString())));     //getting the Value, converted into "Long"

                //        }

                        if(child.getKey().equals("dateSched")){
                            serviceDate.setText(child.getValue().toString()); //getting the Value, converted into "Long"
                        }

                        //////////////////////////////////
                        if(child.getKey().equals("rating")){
                            nRatingBar.setRating(Integer.valueOf(child.getValue().toString()));    //getting the Value, converted into "Integer"
                            nRatingBar.setIsIndicator(true);            //readOnly ratingBar

                        }
                        ////////////////////////////////
                        if(child.getKey().equals("location")){
                            serviceLocation.setText(child.getValue().toString());

                        }
                        ////////////////////////////////
                        //////////////////////////////////////////////////////////////////////////to be filtered

                        ////////////////////////////////
                        if(child.getKey().equals("payment")){
                            paymentPaid.setText(child.getValue().toString());

                        }
                        ////////////////////////////////

                        ////////////////////////////////
                        if(child.getKey().equals("dateDone")){
                            serviceDateDone.setText(child.getValue().toString());

                        }
                        ////////////////////////////////
                        ////////////////////////////////////////////////////////////////////////////

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }); //Creating event Listener for this database reference

    }

    ///////////////////////////////////////////////////////////////////displayConsumerRelatedObjects___Setting the Rating Bar "setOnRatingBarChangeListener"

    private void displayConsumerRelatedObjects() {
    nLayoutRating.setVisibility(View.VISIBLE);
        nRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {
            historyServiceInformationDb.child("rating").setValue(rating);                                          //setting Value to a particular "rating" field based on the "ratingBar"

            DatabaseReference nProviderRatingDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(providerId).child("rating");  //creating new field/table "rating"
            nProviderRatingDb.child(serviceId).setValue(rating);     //creating new field "serviceId" into "rating" field/table, then putting a value into it.
        }
    });
    }
    ///////////////////////////////////////////////////////////////////displayConsumerRelatedObjects_END


    ///////////////////////////////////////////////////////////////////getServiceInformation_END



//////////////////////////////////////////////////////////////////////////////////////////getUserInformation
    private void getUserInformation(String otherUserProviderOrConsumer, String otherUserId) {
        DatabaseReference nOtherUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(otherUserProviderOrConsumer).child(otherUserId);
        nOtherUserDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Map<Object, String> map = (Map<Object, String>) dataSnapshot.getValue();
                    if (map.get("name")!=null){
                        userName.setText(map.get("name").toString());
                    }
                    if (map.get("phone")!=null){
                        userPhone.setText(map.get("phone").toString());
                    }


                    /////////////////////////////////////////
                    if (map.get("serviceType")!=null){

                        serviceGiv = (map.get("serviceType").toString());

                        switch(serviceGiv){
                            case "PLUMBER":
                            serviceGiven.setText("Plumbing Service");
                            break;

                            case "MASONRY":
                                serviceGiven.setText("Masonry Service");
                                break;

                            case "CARPENTER":
                                serviceGiven.setText("Carpentry Service");
                                break;

                            case "ELECTRICIAN":
                                serviceGiven.setText("Electrical Service");
                                break;

                            case "LAUNDRY":
                                serviceGiven.setText("Laundry Service");
                                break;

                            case "PAINTER":
                                serviceGiven.setText("Painting Service");
                                break;

                            case "HOUSE KEEPING":
                                serviceGiven.setText("Housekeeping Service");
                                break;

                            case "AIRCON AND REFRIGERATOR REPAIR":
                                serviceGiven.setText("Aircon and Refrigerator Service");
                                break;

                            case "COMPUTER AND LAPTOP REPAIR":
                                serviceGiven.setText("Computer Repair Service");
                                break;

                            case "APPLIANCE REPAIR":
                                serviceGiven.setText("Appliance Repair Service");
                                break;

                        }

                    }
                    /////////////////////////////////////////

                    if (map.get("profileImageUrl")!=null){
                        Glide.with(getApplication()).load(map.get("profileImageUrl").toString()).into(userImage);
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
//////////////////////////////////////////////////////////////////////////////////////////getUserInformation_END

    //////////////////////////////////////////////////////////getServiceInformation_END

    ///////////////////////////////////////////////////getDate
    private String getDate(Long timestamp) {
        //get instance for the Calendar
        Calendar cal = Calendar.getInstance(Locale.getDefault());   //the default Timezone location of the current user
        cal.setTimeInMillis(timestamp * 1000);                     //getting the exact date and time, note: multiply into 1000
        String date = android.text.format.DateFormat.format("MM-dd-yyyy hh:mm", cal).toString();      //note: use android.text.format.DateFormat.format to remove error in "dd-MM-yyyy hh:mm"
        return date;
    }
    ///////////////////////////////////////////////////

    /////////////////////////////////////////////////////onMapReady
  //  @Override
  //  public void onMapReady(GoogleMap googleMap) {

  //  }


    //////////////////////////////////////////////onStop
    @Override
    protected void onStop() {
        super.onStop();


    }
    //////////////////////////////////////////////onStart
    @Override
    protected void onStart() {
        super.onStart();

    }
    //////////////////////////////////////////////


    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }


}
