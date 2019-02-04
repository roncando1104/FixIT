package com.alliancecode.fixit;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class WelcomeMainProvider extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    private DatabaseReference nProviderDatabase;
    private FirebaseDatabase nDbase;
    private String userId;
    private String proName, proPhone, proService, proRating;
    private String nName, nPhone, nAddress, nProfileImageUrl, nServiceType, nAge;
    private String dateToday;
    private String passChanged;
    private String consuNamePast, consuPhonePast, consuAddressPast,scheduleDatePast, serviceTypeRequestPast, paymentPast, pendingId;

    private LinearLayout nScheduleAlert;

    private CircleImageView nProfileImage, imageAvatar, nProImage;

   // private ImageView nProImage;

    private int count;

    private Boolean isAccountHasIssue = false;
    private Boolean isAccountClear = false;
    private Boolean isSuspensionExpire = false;

        private Button nGetStarted;

    private DrawerLayout nRootLayout, drawer;

    private TextView nProviderName, nProviderPhone, nProviderService, nRating;
    private TextView tvNameField, tvAddressField, tvAgeField, tvServiceTypeField, tvPhoneField;
    private TextView txtProviderName, txtStars;

    private Button nBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_main_provider);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nProviderName = (TextView) findViewById(R.id.txtProviderName);
        nProviderPhone = (TextView) findViewById(R.id.txtProviderPhone);
        nProviderService = (TextView) findViewById(R.id.txtProviderService);
        nRating = (TextView) findViewById(R.id.txtRating);

        nGetStarted = (Button) findViewById(R.id.getStarted);

        nProImage = (CircleImageView) findViewById(R.id.imgProImage);

        nRootLayout = (DrawerLayout) findViewById(R.id.drawerProvider_layout);

        drawer = (DrawerLayout) findViewById(R.id.drawerProvider_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_viewProvider);
        navigationView.setNavigationItemSelectedListener(this);
        View navigationHeaderView = navigationView.getHeaderView(0);

        txtProviderName = (TextView) navigationHeaderView.findViewById(R.id.txtProviderName);
        txtStars = (TextView) navigationHeaderView.findViewById(R.id.txtStars);
        imageAvatar = (CircleImageView) navigationHeaderView.findViewById(R.id.profileImage);


        Calendar cal = Calendar.getInstance();
        dateToday = new SimpleDateFormat("MM-dd-yyyy").format(cal.getTime());

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        nDbase = FirebaseDatabase.getInstance();
        nProviderDatabase = nDbase.getReference().child("Users").child("Providers").child(userId);

        nProviderDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {

                    java.util.Map<String, Object> map = (java.util.Map<String, Object>) dataSnapshot.getValue();

                    if (map.get("name") != null) {
                        proName = map.get("name").toString();
                        nProviderName.setText(proName);
                        txtProviderName.setText(proName);
                    }

                    if (map.get("phone") != null) {
                        proPhone = map.get("phone").toString();
                        nProviderPhone.setText(proPhone);

                    }


                    if (map.get("serviceType") != null) {
                        proService = map.get("serviceType").toString();
                        nProviderService.setText(proService);

                    }

                    if (map.get("aveRating") != null) {
                        proRating = map.get("aveRating").toString();
                        nRating.setText(proRating);
                        txtStars.setText(proRating);

                    }


                    if (map.get("profileImageUrl") != null) {

                        if (map.get("profileImageUrl").equals("")) {
                            nProImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_profileimage));
                            imageAvatar.setImageDrawable(getResources().getDrawable(R.mipmap.ic_profileimage));
                        }else{
                            //  schedProProfileImage = map.get("profileImageUrl").toString();
                            Glide.with(getApplication()).load(map.get("profileImageUrl").toString()).into(nProImage);
                            Glide.with(getApplication()).load(map.get("profileImageUrl").toString()).into(imageAvatar);
                        }
                    }else{
                        nProImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_profileimage));
                        imageAvatar.setImageDrawable(getResources().getDrawable(R.mipmap.ic_profileimage));
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        /////////////////////
        nGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    accountStatus();

                    if(isAccountClear){
                        Intent intent = new Intent(WelcomeMainProvider.this, ProviderMapActivity.class);
                        startActivity(intent);
                    }

                }catch (Exception e){
                    e.printStackTrace();

                }




            }
        });


        suspensionExpire();
        accountStatus();

        if(!isAccountHasIssue){
            passedSched();
            workScheduleToday();
            checkWorkSchedule();
        }
    }


    /////////////////////////////////////////suspensionExpire

    private Date suspendDateToday, suspendDateCheck;
    private String suspendDate;
    private Boolean hasAccountSuspension = false;
    private DatabaseReference suspendRef;
    private void suspensionExpire() {
        final DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        final Calendar cal = Calendar.getInstance();
        final String dateNow  = new SimpleDateFormat("MM-dd-yyyy").format(cal.getTime());


        suspendRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(userId);
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
           //                            Toast.makeText(WelcomeMainProvider.this, "isSuspensionExpire Yes: " + suspendDate, Toast.LENGTH_LONG).show();

                        } else {
            //                          Toast.makeText(WelcomeMainProvider.this, "isSuspensionExpire No : " + suspendDate, Toast.LENGTH_LONG).show();
                        }
                    }



                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    ///////////////////////////////////////////////////////
    private void accountStatus() {
   //  DatabaseReference suspendRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(userId).child("cancelCount");
      DatabaseReference suspendRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(userId);

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

    //////////////////////////////////////////////////
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


    ////////////////////////////////////

    ////////////////////////////////////////////////////////passedSched
    private Date datetoday, datepass;
    private String datePass, datePending;
    private DatabaseReference passSche;


    private void passedSched() {
        final DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        final Calendar cal = Calendar.getInstance();
        final String dateNow  = new SimpleDateFormat("MM-dd-yyyy").format(cal.getTime());

        //   monthPas = Integer.parseInt(dateNow.substring(0,2));
        //   dayPas = Integer.parseInt(dateNow.substring(0,2));


        passSche = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(userId).child("WorkSchedule");
        passSche.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //          Map<String, Object> map = (Map<String, Object>) dataSnapshot.getChildren();
           //     List<String> pendingList = new ArrayList<String>();
//

                if (dataSnapshot.exists()){

                    for(DataSnapshot child: dataSnapshot.getChildren()){
                        datePending = child.getKey();
           //             pendingList.add(String.valueOf(child.getKey()));

                        try {
                            datepass = df.parse(datePending);
                            datetoday = df.parse(dateNow);

                        } catch (ParseException e) {          //ParseException issue
                            e.printStackTrace();
                        }


                        if(datepass.before(datetoday)){
                            //      hasPassedRequest = true;
                                datePass  = df.format(datepass).toString();
                                deletePassPending();
        //                    Toast.makeText(WelcomeMainProvider.this, "May pending: "+datePass, Toast.LENGTH_LONG).show();
                        }else{
                            //       hasPassedRequest = false;
                        //    Toast.makeText(WelcomeMainProvider.this, "NO PENDING: "+datepass, Toast.LENGTH_LONG).show();
                        }


                    }

                    //      pendingId = map.get(datePass)
/*
                  try {
                        datepass = df.parse(datePass);
                    datetoday = df.parse(dateNow);

                  } catch (ParseException e) {          //ParseException issue
                       e.printStackTrace();
                   }


                    if(datepass.after(datetoday)){
                  //      hasPassedRequest = true;
                  //      deletePassPending();
                                 Toast.makeText(WelcomeMainProvider.this, "May pending: "+datepass, Toast.LENGTH_LONG).show();
                    }else{
                 //       hasPassedRequest = false;
                                Toast.makeText(WelcomeMainProvider.this, "NO PENDING: "+datetoday, Toast.LENGTH_LONG).show();
                    }

*/

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    ///////////////////////////////////////////////

    /////////////////////////////////////////////////////deletePassPending
    private DatabaseReference passPending;
    private DatabaseReference passPendingMain;
    private  DatabaseReference passWorkSchedule;
    private void deletePassPending() {

        passWorkSchedule = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(userId).child("WorkSchedule").child(datePass);
        passWorkSchedule.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child: dataSnapshot.getChildren()){

                        if(child.getKey().equals("pendingId")){
                            pendingId = child.getValue().toString();

                            passPending = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(userId).child("pending").child(pendingId);
                            passPendingMain = FirebaseDatabase.getInstance().getReference().child("Pending").child(pendingId);


                        }

                        if(child.getKey().equals("consumerName")) {
                            consuNamePast = child.getValue().toString();
                        }

                        if(child.getKey().equals("consumerPhone")) {
                            consuPhonePast = child.getValue().toString();
                        }

                        if(child.getKey().equals("dateSched")) {
                            scheduleDatePast = child.getValue().toString();
                        }


                        if(child.getKey().equals("consumerServiceRequest")) {
                            serviceTypeRequestPast = child.getValue().toString();
                        }

                        if(child.getKey().equals("houseLocation")) {
                            consuAddressPast = child.getValue().toString();
                        }

                        if(child.getKey().equals("payment")) {
                            paymentPast = child.getValue().toString();
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
        View check_layout = inflater.inflate(R.layout.layout_message_overduescheduleprovider, null);

        dialog.setView(check_layout);
        dialog.setCancelable(false);

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
           //     Toast.makeText(WelcomeMainProvider.this, "May pending No.: "+datePass, Toast.LENGTH_LONG).show();

               ServicesUnperformed();
               passWorkSchedule.removeValue();
              passPending.removeValue();
              passPendingMain.removeValue();

            }
        });
        dialog.show();
    }


///////////////////////////////////////////////////////////PendingServicesUnperformed
    private void ServicesUnperformed() {
        DatabaseReference overDue = FirebaseDatabase.getInstance().getReference().child("PendingServicesUnperformed").child(pendingId);
        //  requestId = overDue.push().getKey();
        HashMap map = new HashMap();
        map.put("providerName", proName);
        map.put("providerPhone", proPhone);
        map.put("consumerName", consuNamePast);
        map.put("consumerAddress", consuAddressPast);
        map.put("consumerPhone", consuPhonePast);
        map.put("consumerServiceRequest", serviceTypeRequestPast);
        map.put("dateSched", scheduleDatePast);
        map.put("paymentTobe", paymentPast);

        overDue.updateChildren(map);

    }

////////////////////////////////////////////////////////



    //////////////////////////////////
    private void workScheduleToday() {
         DatabaseReference sched = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(userId).child("WorkSchedule").child(dateToday);
      //DatabaseReference sched = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(userId).child("WorkSchedule").child("08-15-2018");

        sched.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Intent intent = new Intent(WelcomeMainProvider.this, ProviderMapActivity.class);
                    startActivity(intent);

                }else{}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /////////////////////////////////////////////
    private void checkWorkSchedule() {
        DatabaseReference Consumersched = FirebaseDatabase.getInstance().getReference().child("Users").child("Providers").child(userId).child("WorkSchedule");
        Consumersched.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    scheduleNotif();
                  //  autoSms();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    private void scheduleNotif() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
     //   dialog.setTitle("SCHEDULE ALERT!");
     //   dialog.setMessage(proName+", you have pending service request. Please check your 'Pending Service Request' for details.");

        LayoutInflater inflater = LayoutInflater.from(this);
        View check_layout = inflater.inflate(R.layout.layout_messageprovider, null);
   //     nScheduleAlert = (LinearLayout)findViewById(R.id.linearscheduleAlert);
    //    nScheduleAlert.setVisibility(View.VISIBLE);
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

    ////////////////////////////////////////////////////////////////


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
        }
        else if (id == R.id.nav_changePassword) {
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

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////historyDetais
    public void historyDetails(){
        Intent intent = new Intent(WelcomeMainProvider.this, HistoryActivity.class);
        intent.putExtra("consumerOrProvider", "Providers");                 //The value "Consumers" is the Consmers
        startActivity(intent);
      //  finish();
    }
    ////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////pendingDetails
    public void pendingDetails(){
        Intent intent = new Intent(WelcomeMainProvider.this, PendingActivityProviders.class);

        intent.putExtra("consumerOrProvider", "Providers");                 //The value "Consumers" is the Consmers
        startActivity(intent);
      //  finish();
    }
    ////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////=============********************************accountProvider
    public void accountProvider() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
      //  dialog.setTitle("PERSONAL ACCOUNT");

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


      /*  dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }


        });
      */

      //  dialog.show();


        nBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogshow.dismiss();
                return;
            }
        });

    }
    //////////////////////////////////////////////////////////////////////////////////////////********************************accountProvider_end
////////////////////////////////////////////////

    ////////////////////////////////////////////////signOut
    public void signOut() {

       //automatic logout
        FirebaseAuth.getInstance().signOut();
        finishAffinity();

        //     Intent intent = new Intent(WelcomeMainProvider.this, ProviderLoginActivity.class);
  //      startActivity(intent);
    //    finish();
    //    return;
    }
//////////////////////////////////////////////

    @Override
    public void onBackPressed() {
        finishAffinity();

        super.onBackPressed();
    }
}