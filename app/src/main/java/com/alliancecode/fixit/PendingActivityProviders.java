package com.alliancecode.fixit;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.alliancecode.fixit.Model.PendingAdapter;
import com.alliancecode.fixit.Model.PendingAdapterProviders;
import com.alliancecode.fixit.Model.PendingObject;
import com.alliancecode.fixit.Model.PendingObjectProviders;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class PendingActivityProviders extends AppCompatActivity {

    private RecyclerView nPendingRecyclerView;
    private RecyclerView.Adapter nPendingAdapter;
    private RecyclerView.LayoutManager nPendingLayoutManager;

    private String consumerOrProvider, userId;
    private String consumerId, consumerName, consumerPhone, consumerServiceRequest, houseLocation, dateSched, timeRequest, payment, consumerNote,   providerId, providerName,  providerAge, providerPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        consumerOrProvider = getIntent().getExtras().getString("consumerOrProvider");    //getting Intent Extras Bundle from  "both ConsumerMapActivity and ProviderMapActivity"

            setContentView(R.layout.activity_pending_providers);
            nPendingRecyclerView = (RecyclerView) findViewById(R.id.pendingRecyclerViewProviders);



        /////////RcyclerView setting-up
        nPendingRecyclerView.setNestedScrollingEnabled(false);
        nPendingRecyclerView.setHasFixedSize(true);
        nPendingLayoutManager = new LinearLayoutManager(PendingActivityProviders.this);

        //attaching layoutManager into Adapter
        nPendingRecyclerView.setLayoutManager(nPendingLayoutManager);


            nPendingAdapter = new PendingAdapterProviders(getDataSetHistoryPro(), PendingActivityProviders.this);    //instance of HistoryAdapter   ...inside  parameters is the Method "getDataSetHistory()"


        nPendingRecyclerView.setAdapter(nPendingAdapter);

        //    HistoryObject obj = new HistoryObject("1234");
        //   resultsHistory.add(obj);


        //  consumerOrProvider = getIntent().getExtras().getString("consumerOrProvider");    //getting Intent Extras Bundle from  "both ConsumerMapActivity and ProviderMapActivity"

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //  getConsumerAndProviderInfoSchedule();
        getUserPendingIds();

        //     for(int i=0; i<100; i++){
        //         HistoryObject obj = new HistoryObject(Integer.toString(i));
        //         resultsHistory.add(obj);
        //     }

        //    nHistoryAdapter.notifyDataSetChanged();
    }

    private void getConsumerAndProviderInfoSchedule() {

    }
    ///////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////getUserHistoryIds
    private void getUserPendingIds() {
        DatabaseReference userPendingDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(consumerOrProvider).child(userId).child("pending");
        userPendingDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot pendingVar : dataSnapshot.getChildren()){       //getChildren and not getValue, all children will get.

                        fetchServiceTransInformation(pendingVar.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////getUserHistoryIds_END

    // remove for the meantime
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////fetchServiceTransInformation
    private void fetchServiceTransInformation(String serviceKey) {      //////// //getting each services history data in the main "History" that based to the "serviceKey" which from userHistoryDatabase

        DatabaseReference pendingDatabase = FirebaseDatabase.getInstance().getReference().child("Pending").child(serviceKey);
        pendingDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    //serviceId not needed for the meantime
                    //    String serviceId = dataSnapshot.getKey();        //getting only the Key data in the "History"
                    Long timestamp = 0L;                        //this is how to initialize the timestamp

                    //getting all "timeStamp" data in the entire History table, then passing it to variable timestamp
                    for(DataSnapshot child : dataSnapshot.getChildren()){

                        //   if (child.getKey().equals("")){
                        //       timestamp = Long.valueOf(child.getValue().toString());
                        //  }



                        if(child.getKey().equals("consumerName")) {
                            consumerName = child.getValue().toString();
                        }

                        if(child.getKey().equals("consumerPhone")) {
                            consumerPhone = child.getValue().toString();
                        }

                        if(child.getKey().equals("consumerServiceRequest")) {
                            consumerServiceRequest = child.getValue().toString();
                        }

                        if(child.getKey().equals("dateSched")) {
                            dateSched = child.getValue().toString();
                        }

                        if(child.getKey().equals("houseLocation")) {
                            houseLocation = child.getValue().toString();
                        }

                        if(child.getKey().equals("payment")) {
                            payment = child.getValue().toString();
                        }

                        if(child.getKey().equals("consumerNote")) {
                            consumerNote = child.getValue().toString();
                        }

                        if(child.getKey().equals("timeRequest")) {
                            timeRequest = child.getValue().toString();
                        }



                    }

                    //   PendingObject obj = new PendingObject(serviceId, getDate(timestamp));     //creating HistoryObject instances...with actual parameter, note: use funtion getDate(timestamp) to convert into date format
                        PendingObjectProviders object = new PendingObjectProviders(dateSched, timeRequest,  consumerServiceRequest, consumerName, consumerPhone, houseLocation, payment, consumerNote);     //creating HistoryObject instances...with actual parameter, note: use funtion getDate(timestamp) to convert into date format
                        resultsPendingPro.add(object);


                    //adding the items in the recylerView

                    nPendingAdapter.notifyDataSetChanged();


                }
            }
////    private String consumerId, consumerName, consumerPhone, , houseLocation, dateSched, timeRequest, payment, consumerNote,   providerId, providerName, providerPhone;

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////fetchServiceTransInformation_END






    private ArrayList resultsPendingPro = new ArrayList<PendingObjectProviders>();
    private ArrayList<PendingObjectProviders> getDataSetHistoryPro() {
        return resultsPendingPro;
    }



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
        //  super.onBackPressed();
        super.onBackPressed();

      //  Intent intent = new Intent(PendingActivityProviders.this, ProviderMapActivity.class);
       // startActivity(intent);
    }
}


