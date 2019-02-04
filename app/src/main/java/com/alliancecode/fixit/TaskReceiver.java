package com.alliancecode.fixit;

import android.Manifest;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
//import com.householdservices.alliancecode.taskselector.DotAutoFillTextView;





public class TaskReceiver extends AppCompatActivity {

    public static String address;
    public static String serviceDate;
    public static String serviceTime;
    public static String payment;
    public static String paymentDigit;
    public static String providerType;
    public static String consumerNote;
    public static String houseType;
    public static String itemOne ;
    public static String itemTwo ;
    public static String  itemThree ;
    public static String  itemFour ;
    public static String  itemFive ;
  //  public static String serviceAdditionalDetails;

    private static String items;
    private Boolean isItem1 = false;
    private Boolean isItem2 = false;
    private Boolean isItem3 = false;
    private Boolean isItem4 = false;
    private Boolean isItem5 = false;







    private int x=0;
    private int c=0;


    TextView dateSchedule, amount, task, timeSchedule, serviceSelected, typeOfHouse, houseTypeAmt, item1,taskAmount1,
            item2, taskAmount2, item3, taskAmount3, item4, taskAmount4, item5, taskAmount5, noteConsumer,
    /*dot_1, dot_2, dot_3, dot_4, dot_5,*/ sumAmount, consumerAddress;
    Button confirmBtn, summaryTab1, summaryTab2, summaryTab3;
    EditText tvNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_receiver);


 //       Toast.makeText(TaskReceiver.this, itemOne, Toast.LENGTH_SHORT).show();
 //       Toast.makeText(TaskReceiver.this, itemTwo, Toast.LENGTH_SHORT).show();
 //       Toast.makeText(TaskReceiver.this, itemThree, Toast.LENGTH_SHORT).show();
  //      Toast.makeText(TaskReceiver.this, itemFour, Toast.LENGTH_SHORT).show();
  //      Toast.makeText(TaskReceiver.this, itemFive, Toast.LENGTH_SHORT).show();





        serviceSelected = (TextView)findViewById(R.id.selectedService);
        typeOfHouse = (TextView)findViewById(R.id.houseType);
        houseTypeAmt = (TextView)findViewById(R.id.houseTypeAmount);
        item1 = (TextView)findViewById(R.id.task1);
        item2 = (TextView)findViewById(R.id.task2);
        item3 = (TextView)findViewById(R.id.task3);
        item4 = (TextView)findViewById(R.id.task4);
        item5 = (TextView)findViewById(R.id.task5);
        taskAmount1 = (TextView)findViewById(R.id.item1Amount);
        taskAmount2 = (TextView)findViewById(R.id.item2Amount);
        taskAmount3 = (TextView)findViewById(R.id.item3Amount);
        taskAmount4 = (TextView)findViewById(R.id.item4Amount);
        taskAmount5 = (TextView)findViewById(R.id.item5Amount);
        /*dot_1 = (TextView)findViewById(R.id.dot1);
        dot_2 = (TextView)findViewById(R.id.dot2);
        dot_3 = (TextView)findViewById(R.id.dot3);
        dot_4 = (TextView)findViewById(R.id.dot4);
        dot_5 = (TextView)findViewById(R.id.dot5);*/
        sumAmount = (TextView)findViewById(R.id.totalAmount);
        noteConsumer = (TextView)findViewById(R.id.ConsumerNote);

     //   summaryTab1 = (Button)findViewById(R.id.summaryBtn1);
     //   summaryTab2 = (Button)findViewById(R.id.summaryBtn2);
     //   summaryTab3 = (Button)findViewById(R.id.summaryBtn3);

        tvNote = (EditText) findViewById(R.id.tvNote);

        consumerAddress = (TextView)findViewById(R.id.address);
        dateSchedule = (TextView) findViewById(R.id.date);
        amount = (TextView) findViewById(R.id.totalAmount);
        task = (TextView) findViewById(R.id.task);
        timeSchedule = (TextView)findViewById(R.id.time);
        confirmBtn = (Button) findViewById(R.id.btnConfirmTask);
///////////////
        HorizontalStepView horizontalStepView = (HorizontalStepView)findViewById(R.id.horizontalStepView);

        // Progress Tracker Sources

        List<StepBean> sources = new ArrayList<>();
        sources.add(new StepBean("Service",1));
        sources.add(new StepBean("Details",1));
        sources.add(new StepBean("Summary",0));

        horizontalStepView.setStepViewTexts(sources)
                .setTextSize(9)
                .setStepsViewIndicatorCompletedLineColor(Color.parseColor("#eca14f"))
                .setStepViewComplectedTextColor(Color.parseColor("#eca14f"))
                .setStepsViewIndicatorUnCompletedLineColor(R.color.lightblack)
                .setStepViewUnComplectedTextColor(R.color.lightblack)
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(this,R.drawable.attention))
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(this,R.drawable.complted))
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(this,R.drawable.step_icon));
/////////////////////////////////
    //    summaryTab1.setEnabled(false);
    //    summaryTab2.setEnabled(false);

        final Intent getDate = getIntent();
        final String selectedDate = getDate.getStringExtra("Selected Date");
        dateSchedule.setText(selectedDate);
        final String selectedTime = getDate.getStringExtra("Selected Time");
        timeSchedule.setText(selectedTime);
        final String houseNumber = getDate.getStringExtra("House Number");
        final String Street = getDate.getStringExtra("Street");
        final String City = getDate.getStringExtra("City");
        consumerAddress.setText(houseNumber + " " + Street + " " + City + " City");

     //   final String totalAmountDigit = getDate.getStringExtra("TotalAmountDigit");
      //  paymentDigit = totalAmountDigit;
      //  paymentDigit = "600.00";
        final String taskType = getDate.getStringExtra("Service Type");
        serviceSelected.setText(taskType);
        final String house = getDate.getStringExtra("House Type");
        typeOfHouse.setText(house);
        final String svcCondition = getDate.getStringExtra("Service Type Amount");
        houseTypeAmt.setText(svcCondition);

        final String task1 = getDate.getStringExtra("Item One");
        item1.setText(task1);
        final String amount1 = getDate.getStringExtra("Amount One");
        taskAmount1.setText(amount1);

        final String task2 = getDate.getStringExtra("Item Two");
        item2.setText(task2);
        final String amount2 = getDate.getStringExtra("Amount Two");
        taskAmount2.setText(amount2);

        final String task3 = getDate.getStringExtra("Item Three");
        item3.setText(task3);
        final String amount3 = getDate.getStringExtra("Amount Three");
        taskAmount3.setText(amount3);

        final String task4 = getDate.getStringExtra("Item Four");
        item4.setText(task4);
        final String amount4 = getDate.getStringExtra("Amount Four");
        taskAmount4.setText(amount4);

        final String task5 = getDate.getStringExtra("Item Five");
        item5.setText(task5);
        final String amount5 = getDate.getStringExtra("Amount Five");
        taskAmount5.setText(amount5);

        final String totalAmount = getDate.getStringExtra("Total Amount");
        final String amountFormat = totalAmount.substring(1);
        amount.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + amountFormat));

        final String totalAmountDigit = getDate.getStringExtra("TotalAmountDigit");
        paymentDigit = totalAmountDigit;
      //  paymentDigit = Float.parseFloat(amountFormat);





        if(amount1.toString().equals("0.00") || amount1.toString().equals(null)) {
            item1.setVisibility(View.GONE);
            isItem1 = false;
            //adjustment codes
            /*dot_1.setVisibility(View.GONE);*/
            taskAmount1.setVisibility(View.GONE);
        }if(amount2.toString().equals("0.00") || amount2.toString().equals(null)){
            item2.setVisibility(View.GONE);
            isItem2 = false;                   //adjustment codes
            /*dot_2.setVisibility(View.GONE);*/
            taskAmount2.setVisibility(View.GONE);
        }if(amount3.toString().equals("0.00") || amount3.toString().equals(null)){
            item3.setVisibility(View.GONE);
            isItem3 = false;                         //adjustment codes
            /*dot_3.setVisibility(View.GONE);*/
            taskAmount3.setVisibility(View.GONE);
        }if(amount4.toString().equals("0.00") || amount4.toString().equals(null)){
            item4.setVisibility(View.GONE);
            isItem4 = false;                         //adjustment codes
            /*dot_4.setVisibility(View.GONE);*/
            taskAmount4.setVisibility(View.GONE);
        }if(amount5.toString().equals("0.00") || amount5.toString().equals(null)){
            item5.setVisibility(View.GONE);
            isItem5 = false;                 //adjustment codes
            /*dot_5.setVisibility(View.GONE);*/
            taskAmount5.setVisibility(View.GONE);
        }if(svcCondition.toString().equals("0.00") || svcCondition.toString().equals(null)){
            typeOfHouse.setVisibility(View.GONE);
            houseTypeAmt.setVisibility(View.GONE);
        }

        noteConsumer.setText(Html.fromHtml("<small><font color=\"red\">NOTE:</font></small><br>" +
                "The stated amount is only for service payment.  " +
                "Kindly pay the amount to our Service Provider.  Thank You."));

       /* AlertDialog.Builder receiverTask = new AlertDialog.Builder(TaskReceiver.this, android.R.style.Theme_Holo_Light);
        receiverTask.setTitle(Html.fromHtml("<h3 align =\"center\"><b><font color = \"black\">TASK BOOKING</font></b><h3>"));
        receiverTask.setMessage(Html.fromHtml("<font color =\"red\" style=\"bold\">SERVICE TYPE: </font>" + taskType + "<br>" + "<font color =\"red\">TOTAL AMOUNT: </font>" + totalAmount + "<br>" + "<font color =\"red\">SCHEDULE: </font>" + selectedDate));




        receiverTask.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent go = new Intent(TaskReceiver.this, MainActivity.class);
                startActivity(go);
                dialogInterface.cancel();
            }
        });
        receiverTask.setNegativeButton("DECLINE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent go = new Intent(TaskReceiver.this, MainActivity.class);
                startActivity(go);
                dialogInterface.cancel();
            }
        });
        AlertDialog alertTask = receiverTask.create();
        alertTask.show();*/

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                consumerNote = tvNote.getText().toString();          //adjustment

                try{


                    if(amount1.toString().equals("0.00") || amount1.toString().equals(null) || taskAmount1.equals("P.0.00")) {
                        isItem1 = false;
                        item1 = null;
                    }else
                        isItem1 = true;


                    if(amount2.toString().equals("0.00") || amount2.toString().equals(null) || taskAmount2.equals("P.0.00")) {
                        isItem2 = false;
                        item2 = null;
                    }else
                        isItem2 = true;

                    if(amount3.toString().equals("0.00") || amount3.toString().equals(null) || taskAmount3.equals("P.0.00")) {
                        isItem3 = false;
                        item3 = null;
                    }else
                        isItem3 = true;

                    if(amount4.toString().equals("0.00") || amount4.toString().equals(null) || taskAmount4.equals("P.0.00")) {
                        isItem4 = false;
                        item4 = null;
                    }else
                        isItem4 = true;



                    if(amount5.toString().equals("0.00") || amount5.toString().equals(null) || taskAmount5.equals("P.0.00")) {
                        isItem5 = false;
                        item5 = null;
                    }else
                        isItem5 = true;


                }catch(Exception e){
                    e.printStackTrace();
                }



                //////////////////////////////////////////
                if (isItem1){
                    isItem1 = false;
                    if(item1.getText().toString().equals("")){
                        item1.setVisibility(View.GONE);
                        taskAmount1.setVisibility(View.GONE);
                        itemOne = "";
                    }else{
                        itemOne = item1.getText().toString();
                     //  Toast.makeText(TaskReceiver.this, itemOne, Toast.LENGTH_SHORT).show();
                    }


                }if (isItem2){
                    isItem2 = false;

                    if(item2.getText().toString().equals("")){
                        item2.setVisibility(View.GONE);
                        taskAmount2.setVisibility(View.GONE);
                        itemTwo = "";
                    }else{
                        itemTwo = item2.getText().toString();       //adjustment codes
                      // Toast.makeText(TaskReceiver.this, itemTwo, Toast.LENGTH_SHORT).show();

                    }

                }if (isItem3){
                    isItem3 = false;
                    if(item3.getText().toString().equals("")){
                        item3.setVisibility(View.GONE);
                        taskAmount3.setVisibility(View.GONE);
                        itemThree = "";

                    }else{
                        itemThree = item3.getText().toString();
                     //  Toast.makeText(TaskReceiver.this, itemThree, Toast.LENGTH_SHORT).show();
                    }

                }if (isItem4){
                    isItem4 = false;

                    if(item4.getText().toString().equals("")){
                        item4.setVisibility(View.GONE);
                        taskAmount4.setVisibility(View.GONE);
                        itemFour = "";

                    }else{
                        itemFour = item4.getText().toString();
                      //  Toast.makeText(TaskReceiver.this, itemFour, Toast.LENGTH_SHORT).show();
                    }

                }if (isItem5){
                    isItem5 = false;

                    if(item5.getText().toString().equals("")){
                        item5.setVisibility(View.GONE);
                        taskAmount5.setVisibility(View.GONE);
                        itemFive = "";
                    }else {
                        itemFive = item5.getText().toString();
                      //  Toast.makeText(TaskReceiver.this, itemFive, Toast.LENGTH_SHORT).show();
                    }


//adjustment codes
                }



             Intent intent = new Intent(TaskReceiver.this, ConsumerMapActivity.class);
              startActivity(intent);
            finish();




            }
        });

      //  note = tvNote.getText().toString();
        address = consumerAddress.getText().toString();
        payment = amount.getText().toString();
        serviceDate = dateSchedule.getText().toString();
        serviceTime = timeSchedule.getText().toString();
        providerType = serviceSelected.getText().toString();
        houseType = typeOfHouse.getText().toString();






        // consumerNote = consuNote;


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

