package com.alliancecode.fixit;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.alliancecode.fixit.Model.HistoryAdapter;
import com.alliancecode.fixit.Model.HistoryObject;
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
import java.util.TimeZone;


public class HistoryActivity extends AppCompatActivity {

    private RecyclerView nHistoryRecyclerView;
    private RecyclerView.Adapter nHistoryAdapter;
    private RecyclerView.LayoutManager nHistoryLayoutManager;
   // private String result;
    private String consumerOrProvider, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        nHistoryRecyclerView = (RecyclerView) findViewById(R.id.historyRecyclerView);

                /////////RcyclerView setting-up
        nHistoryRecyclerView.setNestedScrollingEnabled(false);
        nHistoryRecyclerView.setHasFixedSize(true);
        nHistoryLayoutManager = new LinearLayoutManager(HistoryActivity.this);

        //attaching layoutManager into Adapter
        nHistoryRecyclerView.setLayoutManager(nHistoryLayoutManager);
        nHistoryAdapter = new HistoryAdapter(getDataSetHistory(), HistoryActivity.this);    //instance of HistoryAdapter   ...inside  parameters is the Method "getDataSetHistory()"

        nHistoryRecyclerView.setAdapter(nHistoryAdapter);

    //    HistoryObject obj = new HistoryObject("1234");
     //   resultsHistory.add(obj);


        consumerOrProvider = getIntent().getExtras().getString("consumerOrProvider");    //getting Intent Extras Bundle from  "both ConsumerMapActivity and ProviderMapActivity"

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        getUserHistoryIds();

   //     for(int i=0; i<100; i++){
   //         HistoryObject obj = new HistoryObject(Integer.toString(i));
   //         resultsHistory.add(obj);
   //     }

   //    nHistoryAdapter.notifyDataSetChanged();
    }
    ///////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////getUserHistoryIds
    private void getUserHistoryIds() {
        DatabaseReference userHistoryDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(consumerOrProvider).child(userId).child("history");
        userHistoryDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot historyVar : dataSnapshot.getChildren()){       //getChildren and not getValue, all children will get.

                        fetchServiceTransInformation(historyVar.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////getUserHistoryIds_END

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////fetchServiceTransInformation
    private void fetchServiceTransInformation(String serviceKey) {      //////// //getting each services history data in the main "History" that based to the "serviceKey" which from userHistoryDatabase

        DatabaseReference historyDatabase = FirebaseDatabase.getInstance().getReference().child("History").child(serviceKey);
        historyDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String serviceId = dataSnapshot.getKey();        //getting only the Key data in the "History"
                    Long timestamp = 0L;                        //this is how to initialize the timestamp

                    //getting all "timeStamp" data in the entire History table, then passing it to variable timestamp
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if (child.getKey().equals("timestamp")){
                            timestamp = Long.valueOf(child.getValue().toString());
                        }
                    }
                 HistoryObject obj = new HistoryObject(serviceId, getDate(timestamp));     //creating HistoryObject instances...with actual parameter, note: use funtion getDate(timestamp) to convert into date format

                   resultsHistory.add(obj);                              //adding the items in the recylerView

                    nHistoryAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////fetchServiceTransInformation_END

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////getDate   ..converting the Long timstamp into Date format
    private String getDate(Long timestamp) {
        //get instance for the Calendar
        Calendar cal = Calendar.getInstance(Locale.getDefault());   //the default Timezone location of the current user
        cal.setTimeInMillis(timestamp * 1000);                     //getting the exact date and time, note: multiply into 1000
        String date = android.text.format.DateFormat.format("MM-dd-yyyy hh:mm", cal).toString();      //note: use android.text.format.DateFormat.format to remove error in "dd-MM-yyyy hh:mm"
        return date;

     //   String date = android.text.format.DateFormat.format("dd-MM-yyyy hh:mm", cal).toString();      //note: use android.text.format.DateFormat.format to remove error in "dd-MM-yyyy hh:mm"
     //   String date = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();
      //  String date = new SimpleDateFormat("MM-dd-yyyy").format(d.getTime());          /// date  string
      //  String date = new SimpleDateFormat("MM-dd-yyyy hh:mm").format(cal.toString());          /// date  string
     //   final String date = new SimpleDateFormat("MM-dd-yyyy").format(cal.toString());

      /*  /////Working date conversation
        DateFormat objFormatter = new SimpleDateFormat("MM-dd-yyyy hh:mm");
        objFormatter.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));//use this to get ph timezone

        Calendar objCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        objCalendar.setTimeInMillis(timestamp*1000);
        String result = objFormatter.format(objCalendar.getTime());
        objCalendar.clear();
        return result;
        //////  */
    }
    ////////////////////////////////////////////////////////////////////////////////////////getDataSetHistory


    private ArrayList resultsHistory = new ArrayList<HistoryObject>();
    private ArrayList<HistoryObject> getDataSetHistory() {
        return resultsHistory;
    }





    //////////////////////////////////////////////onStop
    @Override
    protected void onStop() {
        super.onStop();


    }
    //////////////////////////////////////////////onStart
  //  @Override
   // protected void onStart() {
   //     super.onStart();

  //  }
    //////////////////////////////////////////////

    @Override
    public void onBackPressed() {
      //  super.onBackPressed();
/*
 if (consumerOrProvider.equals("Providers")){
     Intent intent = new Intent(HistoryActivity.this, ProviderMapActivity.class);
     startActivity(intent);
 }else{
     Intent intent = new Intent(HistoryActivity.this, WelcomeMain.class);
     startActivity(intent);
 }
 */
        super.onBackPressed();
      }



}


