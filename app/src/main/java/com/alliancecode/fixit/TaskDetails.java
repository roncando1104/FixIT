package com.alliancecode.fixit;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class TaskDetails extends AppCompatActivity{

    int mYear, mMonth, mDay;
    DatabaseReference taskServices;
    Firebase myFirebase;
    DecimalFormat df = new DecimalFormat("##,###.##");
    String AmPm;


    //widgets
    AutoCompleteTextView conAddressCity;
    Button back, next, detailTab1, detailTab2, detailTab3;
    EditText conAddress, conAddressStreet;
    EditText L1pieces, L2pieces, L3pieces, L4pieces;
    TextView L1pricePerPiece, L2pricePerPiece, L3pricePerPiece, L4pricePerPiece;
    EditText P1sqm, P2sqm, P3sqm, P4sqm;
    EditText piecesEfan, brandEfan, piecesTV, brandTV, brandWM, brandES, brandMW;
    TextView tvBrandEfan, priceEfan, tvBrandTelevision, priceTV, priceWM, tvBrandWM, priceES, tvBrandES, priceMW, tvBrandMW;
    TextView priceP1, priceP2, priceP3, priceP4;
    TextView refBrandtv, freezerBrandtv, ACsplitTypePricetv, ACsplitBrandtv, ACwindowTypePricetv, ACwindowBrandTv;
    EditText etBrandRef, etBrandFreezer, etBrandACsplit, etACsplitTypePcs, etbrandACwindow, etACwindowTypePcs;
    RadioGroup carpentryRad, plumbingRad, masonryRad, electricRad, laundryRad, painterRad, houseKeepingRad;
    ImageButton imgSetDate, imgSetTime;
    TextView tvSetDate, tvSetTime;
    TextView desktopMotherBoardPrice, desktopHardDrivePrice, desktopPowerSupplyPrice, desktopMonitorPrice;
    TextView laptopMotherBoardPrice, laptopHDPrice, laptopPowerSupplyPrice, laptopMonitorPrice;
    TextView SWoperatingSystemPrice, SWreformatPrice, SWstorageFileRetPrice, SWramProbPrice;
    //Radio buttons
    RadioButton carpentryRB1, carpentryRB2, carpentryRB3;
    RadioButton plumbingRB1, plumbingRB2, plumbingRB3;
    RadioButton masonRB1, masonRB2, masonRB3;
    RadioButton elecRB1, elecRB2, elecRB3;
    RadioButton laundryRB1, laundryRB2, laundryRB3;
    RadioButton painterRB1, painterRB2, painterRB3;
    RadioButton houseKeepingRB1, houseKeepingRB2, houseKeepingRB3;
    //Checkboxes
    CheckBox carpentryCheck1, carpentryCheck2, carpentryCheck3, carpentryCheck4, carpentryCheck5;
    CheckBox plumbingCheck1, plumbingCheck2, plumbingCheck3, plumbingCheck4, plumbingCheck5;
    CheckBox masonCheck1, masonCheck2, masonCheck3, masonCheck4, masonCheck5;
    CheckBox elecCheck1, elecCheck2, elecCheck3, elecCheck4;
    CheckBox laundryCheck1, laundryCheck2, laundryCheck3, laundryCheck4;
    CheckBox painterCheck1, painterCheck2, painterCheck3, painterCheck4;
    CheckBox applianceCheck1, applianceCheck2, applianceCheck3, applianceCheck4, applianceCheck5;
    CheckBox ACrefCheck1, ACrefCheck2, ACrefCheck3, ACrefCheck4;
    CheckBox houseKeepingCheck1, houseKeepingCheck2, houseKeepingCheck3;
    CheckBox computerLaptopCheck1, computerLaptopCheck2, computerLaptopCheck3;
    CheckBox desktopCB1, desktopCB2, desktopCB3, desktopCB4;
    CheckBox laptopCB1, laptopCB2, laptopCB3, laptopCB4;
    CheckBox softwareCB1, softwareCB2, softwareCB3, softwareCB4;
    ScrollView carpentryTask, plumbingTask, masonryTask, electricTask, laundryTask, painterTask, applianceRepTask, airconAndRefRepair, houseKeeping, compAndLaptopRepair;
    Spinner laundryType, laundryType2, laundryType3, laundryType4;
    Spinner ElectricFanType, teleType, washingMachingType;
    TextView Val, item1, item2, item3, item4, item5, serviceType, svcCondition;
    TextView itemTxt1, itemTxt2, itemTxt3, itemTxt4, itemTxt5;
    //data types and value holders
    String selectedType="", taskSelected, indicatedApp1, indicatedApp2, indicatedApp3, indicatedApp4, indicatedApp5;
    String allowSched;
    String pickedDate;
    int allowableDaysCalendar, numberOfDays;
    String url = "https://fixit-dbae9.firebaseio.com/";
    int count = 0;
    double price1, price2, price3, price4, price5, totalAmount, typeVal;
    //variable of computer task
    double desktopVal, desktopVal2, desktopVal3, desktopVal4, laptopVal, laptopVal2, laptopVal3, laptopVal4, softwareVal, softwareVal2, softwareVal3, softwareVal4;
    double desktopP1, desktopP2, desktopP3, desktopP4;
    double laptopP1, laptopP2, laptopP3, laptopP4;
    double softP1, softP2, softP3, softP4;
    double valueL1, valueL2, valueL3, valueL4;
    double valueP1, valueP2, valueP3, valueP4;
    double valueEfan, valueTV, valueWM, valueMwave, valueEstove;
    double valueACref1, valueACref2, valueACref3, valueACref4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);


        //spinner for laundry task
        laundryType = (Spinner)findViewById(R.id.laundryChoices);
        laundryType2 = (Spinner)findViewById(R.id.laundryChoices2);
        laundryType3 = (Spinner)findViewById(R.id.laundryChoices3);
        laundryType4 = (Spinner)findViewById(R.id.laundryChoices4);
        //spinner for appliance repair
        ElectricFanType = (Spinner)findViewById(R.id.elecFanType);
        teleType = (Spinner)findViewById(R.id.tvType);
        washingMachingType = (Spinner)findViewById(R.id.washingType);

        //spinner for laundry choices
        //spinner array for color type
        ArrayAdapter<CharSequence>adapterType = ArrayAdapter.createFromResource(this, R.array.laundryArray, android.R.layout.simple_spinner_item);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        laundryType.setAdapter(adapterType);
        laundryType2.setAdapter(adapterType);
        laundryType3.setAdapter(adapterType);
        laundryType4.setAdapter(adapterType);

        //spinner array for electric fan type and pieces
        ArrayAdapter<CharSequence>FanType = ArrayAdapter.createFromResource(this, R.array.ElectricFanArray, android.R.layout.simple_spinner_item);
        FanType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ElectricFanType.setAdapter(FanType);

        //spinner array for television type and pieces
        ArrayAdapter<CharSequence>tvType = ArrayAdapter.createFromResource(this, R.array.televisionArray, android.R.layout.simple_spinner_item);
        tvType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teleType.setAdapter(tvType);

        //spinner array for television type and pieces
        ArrayAdapter<CharSequence>washingType = ArrayAdapter.createFromResource(this, R.array.washingMachineArray, android.R.layout.simple_spinner_item);
        washingType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        washingMachingType.setAdapter(washingType);

        conAddressCity = (AutoCompleteTextView) findViewById(R.id.consumerAddress3);
        ArrayAdapter<CharSequence>City = ArrayAdapter.createFromResource(this, R.array.ArrayCity, android.R.layout.simple_list_item_1);
        conAddressCity.setAdapter(City);
        conAddressCity.setThreshold(1);
        conAddressCity.setAdapter(City);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        //button for navigation
        conAddress = (EditText)findViewById(R.id.consumerAddress);
        conAddressStreet = (EditText)findViewById(R.id.consumerAddress2);
        imgSetDate = (ImageButton) findViewById(R.id.imageSetDate);
        imgSetTime = (ImageButton) findViewById(R.id.imageSetTime);
        tvSetDate = (TextView)findViewById(R.id.tvSetDate);
        tvSetTime = (TextView)findViewById(R.id.tvSetTime);
        back = (Button)findViewById(R.id.ServiceDetailBack);
        next = (Button)findViewById(R.id.ServiceDetailNext);

        L1pieces = (EditText)findViewById(R.id.piecesL1);
        L2pieces = (EditText)findViewById(R.id.piecesL2);
        L3pieces = (EditText)findViewById(R.id.piecesL3);
        L4pieces = (EditText)findViewById(R.id.piecesL4);
        L1pricePerPiece = (TextView)findViewById(R.id.L1price);
        L2pricePerPiece = (TextView)findViewById(R.id.L2price);
        L3pricePerPiece = (TextView)findViewById(R.id.L3price);
        L4pricePerPiece = (TextView)findViewById(R.id.L4price);
        priceP1 = (TextView)findViewById(R.id.P1price);
        priceP2 = (TextView)findViewById(R.id.P2price);
        priceP3 = (TextView)findViewById(R.id.P3price);
        priceP4 = (TextView)findViewById(R.id.P4price);
        P1sqm = (EditText)findViewById(R.id.sqmP1);
        P2sqm = (EditText)findViewById(R.id.sqmP2);
        P3sqm = (EditText)findViewById(R.id.sqmP3);
        P4sqm = (EditText)findViewById(R.id.sqmP4);

        piecesEfan = (EditText)findViewById(R.id.EfanPieces);
        piecesTV = (EditText)findViewById(R.id.teleVisionPieces);
        brandTV = (EditText)findViewById(R.id.TelevisionBrand);
        brandEfan = (EditText)findViewById(R.id.EfanBrand);
        brandWM = (EditText)findViewById(R.id.washingMachinceBrand);
        brandES = (EditText)findViewById(R.id.ElectricStoveBrand);
        brandMW = (EditText)findViewById(R.id.MicrowaveBrand);

        priceEfan = (TextView)findViewById(R.id.Efanprice);
        priceTV = (TextView)findViewById(R.id.Televisionprice);
        tvBrandEfan = (TextView)findViewById(R.id.EfanBrandtv);
        tvBrandTelevision = (TextView)findViewById(R.id.TelevisionBrandtv);
        priceWM = (TextView)findViewById(R.id.WMprice);
        tvBrandWM = (TextView)findViewById(R.id.WMBrandtv);
        priceES = (TextView)findViewById(R.id.ElectricStoveprice);
        tvBrandES = (TextView)findViewById(R.id.ElectricStoveBrandtv);
        priceMW = (TextView)findViewById(R.id.Microwaveprice);
        tvBrandMW = (TextView)findViewById(R.id.MicrowaveBrandtv);

        //for ac and ref
        refBrandtv = (TextView)findViewById(R.id.tvRefbrand);
        freezerBrandtv = (TextView)findViewById(R.id.tvFreezerbrand);
        ACsplitTypePricetv = (TextView)findViewById(R.id.ACsplitTypePrice);
        ACsplitBrandtv  = (TextView)findViewById(R.id.tvACsplitBrand);
        ACwindowTypePricetv = (TextView)findViewById(R.id.ACwindowTypePrice);
        ACwindowBrandTv = (TextView)findViewById(R.id.tvACwindowBrand);
        etBrandRef = (EditText)findViewById(R.id.brandref);
        etBrandFreezer = (EditText)findViewById(R.id.brandfreezer);
        etBrandACsplit = (EditText)findViewById(R.id.brandACsplit);
        etACsplitTypePcs = (EditText)findViewById(R.id.ACsplitTypePcs);
        etbrandACwindow = (EditText)findViewById(R.id.brandACwindow);
        etACwindowTypePcs = (EditText)findViewById(R.id.ACwindowTypePcs);
        //id for summary
        Val = (TextView)findViewById(R.id.value);
        item1 = (TextView)findViewById(R.id.price1);
        item2 = (TextView)findViewById(R.id.price2);
        item3 = (TextView)findViewById(R.id.price3);
        item4 = (TextView)findViewById(R.id.price4);
        item5 = (TextView)findViewById(R.id.price5);
        itemTxt1 = (TextView)findViewById(R.id.svcName1);
        itemTxt2 = (TextView)findViewById(R.id.svcName2);
        itemTxt3 = (TextView)findViewById(R.id.svcName3);
        itemTxt4 = (TextView)findViewById(R.id.svcName4);
        itemTxt5 = (TextView)findViewById(R.id.svcName5);
        serviceType = (TextView)findViewById(R.id.serType);
        svcCondition = (TextView)findViewById(R.id.serviceCondition);
        //id for carpentry task
        carpentryRad = (RadioGroup)findViewById(R.id.CapentryRadGrp);
        carpentryRB1 = (RadioButton)findViewById(R.id.carpentryRad1);
        carpentryRB2 = (RadioButton)findViewById(R.id.carpentryRad2);
        carpentryRB3 = (RadioButton)findViewById(R.id.carpentryRad3);
        carpentryCheck1 = (CheckBox)findViewById(R.id.carpentryCB1);
        carpentryCheck2 = (CheckBox)findViewById(R.id.carpentryCB2);
        carpentryCheck3 = (CheckBox)findViewById(R.id.carpentryCB3);
        carpentryCheck4 = (CheckBox)findViewById(R.id.carpentryCB4);
        carpentryCheck5 = (CheckBox)findViewById(R.id.carpentryCB5);
        //id for plumbing task
        plumbingRad = (RadioGroup)findViewById(R.id.PlumberRadGrp);
        plumbingRB1 = (RadioButton)findViewById(R.id.plumberRad1);
        plumbingRB2 = (RadioButton)findViewById(R.id.plumberRad2);
        plumbingRB3 = (RadioButton)findViewById(R.id.plumberRad3);
        plumbingCheck1 = (CheckBox)findViewById(R.id.plumberCB1);
        plumbingCheck2 = (CheckBox)findViewById(R.id.plumberCB2);
        plumbingCheck3 = (CheckBox)findViewById(R.id.plumberCB3);
        plumbingCheck4 = (CheckBox)findViewById(R.id.plumberCB4);
        plumbingCheck5 = (CheckBox)findViewById(R.id.plumberCB5);
        //id for mason task
        masonryRad = (RadioGroup)findViewById(R.id.MasonRadGrp);
        masonRB1 = (RadioButton)findViewById(R.id.masonRad1);
        masonRB2 = (RadioButton)findViewById(R.id.masonRad2);
        masonRB3 = (RadioButton)findViewById(R.id.masonRad3);
        masonCheck1 = (CheckBox)findViewById(R.id.masonCB1);
        masonCheck2 = (CheckBox)findViewById(R.id.masonCB2);
        masonCheck3 = (CheckBox)findViewById(R.id.masonCB3);
        masonCheck4 = (CheckBox)findViewById(R.id.masonCB4);
        masonCheck5 = (CheckBox)findViewById(R.id.masonCB5);
        //id for electrician task
        electricRad = (RadioGroup)findViewById(R.id.ElectricianRadGrp);
        elecRB1 = (RadioButton)findViewById(R.id.electricianRad1);
        elecRB2 = (RadioButton)findViewById(R.id.electricianRad2);
        elecRB3 = (RadioButton)findViewById(R.id.electricianRad3);
        elecCheck1 = (CheckBox)findViewById(R.id.electricianCB1);
        elecCheck2 = (CheckBox)findViewById(R.id.electricianCB2);
        elecCheck3 = (CheckBox)findViewById(R.id.electricianCB3);
        elecCheck4 = (CheckBox)findViewById(R.id.electricianCB4);
        //id for laundry task
        laundryRad = (RadioGroup)findViewById(R.id.LaundryRadGrp);
        laundryRB1 = (RadioButton)findViewById(R.id.laundryRad1);
        laundryRB2 = (RadioButton)findViewById(R.id.laundryRad2);
        laundryRB3 = (RadioButton)findViewById(R.id.laundryRad3);
        laundryCheck1 = (CheckBox)findViewById(R.id.laundryCB1);
        laundryCheck2 = (CheckBox)findViewById(R.id.laundryCB2);
        laundryCheck3 = (CheckBox)findViewById(R.id.laundryCB3);
        laundryCheck4 = (CheckBox)findViewById(R.id.laundryCB4);
        //id for painter task
        painterRad = (RadioGroup)findViewById(R.id.PainterRadGrp);
        painterRB1 = (RadioButton)findViewById(R.id.PainterRad1);
        painterRB2 = (RadioButton)findViewById(R.id.PainterRad2);
        painterRB3 = (RadioButton)findViewById(R.id.PainterRad3);
        painterCheck1 = (CheckBox)findViewById(R.id.painterCB1);
        painterCheck2 = (CheckBox)findViewById(R.id.painterCB2);
        painterCheck3 = (CheckBox)findViewById(R.id.painterCB3);
        painterCheck4 = (CheckBox)findViewById(R.id.painterCB4);
        //id for appliance task
        applianceCheck1 = (CheckBox)findViewById(R.id.applianceCB1);
        applianceCheck2 = (CheckBox)findViewById(R.id.applianceCB2);
        applianceCheck3 = (CheckBox)findViewById(R.id.applianceCB3);
        applianceCheck4 = (CheckBox)findViewById(R.id.applianceCB4);
        applianceCheck5 = (CheckBox)findViewById(R.id.applianceCB5);
        //id for aircon and ref task
        ACrefCheck1 = (CheckBox)findViewById(R.id.airconAndRefCB1);
        ACrefCheck2 = (CheckBox)findViewById(R.id.airconAndRefCB2);
        ACrefCheck3 = (CheckBox)findViewById(R.id.airconAndRefCB3);
        ACrefCheck4 = (CheckBox)findViewById(R.id.airconAndRefCB4);
        //id for house keeping task
        houseKeepingRad = (RadioGroup)findViewById(R.id.houseKeepingRadGrp);
        houseKeepingRB1 = (RadioButton)findViewById(R.id.houseKeepingRad1);
        houseKeepingRB2 = (RadioButton)findViewById(R.id.houseKeepingRad2);
        houseKeepingRB3 = (RadioButton)findViewById(R.id.houseKeepingRad3);
        houseKeepingCheck1 = (CheckBox)findViewById(R.id.houseKeepingCB1);
        houseKeepingCheck2 = (CheckBox)findViewById(R.id.houseKeepingCB2);
        houseKeepingCheck3 = (CheckBox)findViewById(R.id.houseKeepingCB3);
        //id for computer repair task
        computerLaptopCheck1 = (CheckBox)findViewById(R.id.computerLaptopCB1);
        computerLaptopCheck2 = (CheckBox)findViewById(R.id.computerLaptopCB2);
        computerLaptopCheck3 = (CheckBox)findViewById(R.id.computerLaptopCB3);
        desktopCB1 = (CheckBox)findViewById(R.id.DesktopPartCbox1);
        desktopCB2 = (CheckBox)findViewById(R.id.DesktopPartCbox2);
        desktopCB3 = (CheckBox)findViewById(R.id.DesktopPartCbox3);
        desktopCB4 = (CheckBox)findViewById(R.id.DesktopPartCbox4);
        laptopCB1 = (CheckBox)findViewById(R.id.LaptopPartCbox1);
        laptopCB2 = (CheckBox)findViewById(R.id.LaptopPartCbox2);
        laptopCB3 = (CheckBox)findViewById(R.id.LaptopPartCbox3);
        laptopCB4 = (CheckBox)findViewById(R.id.LaptopPartCbox4);
        softwareCB1 = (CheckBox)findViewById(R.id.softwareCbox1);
        softwareCB2 = (CheckBox)findViewById(R.id.softwareCbox2);
        softwareCB3 = (CheckBox)findViewById(R.id.softwareCbox3);
        softwareCB4 = (CheckBox)findViewById(R.id.softwareCbox4);
        desktopMotherBoardPrice = (TextView)findViewById(R.id.desktopMotherBoardVal);
        desktopHardDrivePrice = (TextView)findViewById(R.id.desktopHardDriveVal);
        desktopPowerSupplyPrice = (TextView)findViewById(R.id.desktopPowerSupplyVal);
        desktopMonitorPrice = (TextView)findViewById(R.id.desktopMonitorVal);
        laptopMotherBoardPrice = (TextView)findViewById(R.id.laptopMotherBoardVal);
        laptopHDPrice = (TextView)findViewById(R.id.laptopHDVal);
        laptopPowerSupplyPrice = (TextView)findViewById(R.id.laptopPowerSupplyVal);
        laptopMonitorPrice = (TextView)findViewById(R.id.laptopMonitorVal);
        SWoperatingSystemPrice = (TextView)findViewById(R.id.SWoperatingSystemVal);
        SWreformatPrice = (TextView)findViewById(R.id.SWreformatVal);
        SWstorageFileRetPrice = (TextView)findViewById(R.id.SWstorageFileRetVal);
        SWramProbPrice = (TextView)findViewById(R.id.SWramProbVal);

        //Scrollview for 10 task and services
        carpentryTask = (ScrollView)findViewById(R.id.carpenterSV);
        plumbingTask = (ScrollView)findViewById(R.id.plumberSV);
        masonryTask = (ScrollView)findViewById(R.id.masonSV);
        electricTask = (ScrollView)findViewById(R.id.electricianSV);
        laundryTask = (ScrollView)findViewById(R.id.laundrySV);
        painterTask = (ScrollView)findViewById(R.id.painterSV);
        applianceRepTask = (ScrollView)findViewById(R.id.applianceSV);
        airconAndRefRepair = (ScrollView)findViewById(R.id.airconAndRefSV);
        houseKeeping = (ScrollView)findViewById(R.id.houseKeepingSV);
        compAndLaptopRepair = (ScrollView)findViewById(R.id.computerAndLaptopSV);


        itemTxt1 = (TextView)findViewById(R.id.svcName1);
        itemTxt2 = (TextView)findViewById(R.id.svcName2);
        itemTxt3 = (TextView)findViewById(R.id.svcName3);
        itemTxt4 = (TextView)findViewById(R.id.svcName4);
        itemTxt5 = (TextView)findViewById(R.id.svcName5);
        item1 = (TextView)findViewById(R.id.price1);
        item2 = (TextView)findViewById(R.id.price2);
        item3 = (TextView)findViewById(R.id.price3);
        item4 = (TextView)findViewById(R.id.price4);
        item5 = (TextView)findViewById(R.id.price5);


        if(itemTxt1!=null || item1 != null){
            itemTxt1.setText("");
            item1.setText("");
        }
        if(itemTxt2!=null){
            itemTxt2.setText("");
            item2.setText("");

        }
        if(itemTxt3!=null){
            itemTxt3.setText("");
            item3.setText("");

        }
        if(itemTxt4!=null){
            itemTxt4.setText("");
            item4.setText("");

        }
        if(itemTxt5!=null){
            itemTxt5.setText("");
            item5.setText("");

        }


//////////////////
        HorizontalStepView horizontalStepView = (HorizontalStepView)findViewById(R.id.horizontalStepView);

        // Progress Tracker Sources

        List<StepBean> sources = new ArrayList<>();
        sources.add(new StepBean("Service",1));
        sources.add(new StepBean("Details",0));
        sources.add(new StepBean("Summary",-1));


        horizontalStepView.setStepViewTexts(sources)
                .setTextSize(9)
                .setStepsViewIndicatorCompletedLineColor(Color.parseColor("#eca14f"))
                .setStepViewComplectedTextColor(Color.parseColor("#eca14f"))
                .setStepsViewIndicatorUnCompletedLineColor(R.color.lightblack)
                .setStepViewUnComplectedTextColor(R.color.lightblack)
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(this,R.drawable.attention))
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(this,R.drawable.complted))
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(this,R.drawable.step_icon));

////////////////////////////
        taskServices = FirebaseDatabase.getInstance().getReference("Task");


        //intent for setting to visibility/gone of every detailed task
        Bundle fromMain = getIntent().getExtras();
        int taskNum = fromMain.getInt("TASK");
        if(taskNum == 1){
            carpentryTask.setVisibility(View.VISIBLE);
            taskSelected = "CARPENTRY";
        }else if(taskNum == 2){
            plumbingTask.setVisibility(View.VISIBLE);
            taskSelected = "PLUMBER";
        }else if(taskNum == 3){
            masonryTask.setVisibility(View.VISIBLE);
            taskSelected = "MASONRY";
        }else if(taskNum == 4){
            electricTask.setVisibility(View.VISIBLE);
            taskSelected = "ELECTRICIAN";
        }else if(taskNum == 5){
            laundryTask.setVisibility(View.VISIBLE);
            taskSelected = "LAUNDRY";
        }else if(taskNum == 6){
            painterTask.setVisibility(View.VISIBLE);
            taskSelected = "PAINTER";
        }else if(taskNum == 7){
            applianceRepTask.setVisibility(View.VISIBLE);
            taskSelected = "APPLIANCE REPAIR";
            serviceType.setVisibility(View.GONE);
            svcCondition.setVisibility(View.GONE);
        }else if(taskNum == 8){
            airconAndRefRepair.setVisibility(View.VISIBLE);
            taskSelected = "AIRCON AND REFRIGERATOR REPAIR";
            serviceType.setVisibility(View.GONE);
            svcCondition.setVisibility(View.GONE);
        }else if(taskNum == 9){
            houseKeeping.setVisibility(View.VISIBLE);
            taskSelected = "HOUSE KEEPING";
        }else if(taskNum == 10){
            compAndLaptopRepair.setVisibility(View.VISIBLE);
            taskSelected = "COMPUTER AND LAPTOP REPAIR";
            serviceType.setVisibility(View.GONE);
            svcCondition.setVisibility(View.GONE);
        }

        conAddressCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                 /*Toast.makeText(TaskDetails.this, "Enter only the City Name", Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(conAddressCity.getText().toString().contains(" City") || conAddressCity.getText().toString().contains(" city")){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetails.this);
                    builder.setMessage("Please remove the word 'City' on the city field");
                    builder.setCancelable(true);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert11 = builder.create();
                    alert11.show();
                }
            }
        });

        //button to go back to 10 services offered
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(TaskDetails.this, ConsumerHomeActivity.class);
                startActivity(back);
                return;
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String val1 = conAddress.getText().toString();
                final String val2 = conAddressStreet.getText().toString();
                final String val3 = conAddressCity.getText().toString();

                if(TextUtils.isEmpty(val1) || (TextUtils.isEmpty(val2) || (TextUtils.isEmpty(val3)))){
                    if(TextUtils.isEmpty(val1)){
                        conAddress.requestFocus();
                        Toast.makeText(TaskDetails.this, "Please enter house number", Toast.LENGTH_SHORT).show();
                    }else if(TextUtils.isEmpty(val2)){
                        conAddressStreet.requestFocus();
                        Toast.makeText(TaskDetails.this, "Please enter street", Toast.LENGTH_SHORT).show();
                    }else if(TextUtils.isEmpty(val3)){
                        conAddressCity.requestFocus();
                        Toast.makeText(TaskDetails.this, "Please enter city", Toast.LENGTH_SHORT).show();
                    }//end of text util for address to check if null
                    //edittext for task to check if not null
                }else if(P1sqm.getVisibility()==View.VISIBLE && TextUtils.isEmpty(P1sqm.getText().toString())){
                    P1sqm.requestFocus();
                    Toast.makeText(TaskDetails.this, "Square meter is required", Toast.LENGTH_SHORT).show();
                }else if(P2sqm.getVisibility()==View.VISIBLE && TextUtils.isEmpty(P2sqm.getText().toString())){
                    P2sqm.requestFocus();
                    Toast.makeText(TaskDetails.this, "Square meter is required", Toast.LENGTH_SHORT).show();
                }else if(P3sqm.getVisibility()==View.VISIBLE && TextUtils.isEmpty(P3sqm.getText().toString())){
                    P3sqm.requestFocus();
                    Toast.makeText(TaskDetails.this, "Square meter is required", Toast.LENGTH_SHORT).show();
                }else if(P4sqm.getVisibility()==View.VISIBLE && TextUtils.isEmpty(P4sqm.getText().toString())){
                    P4sqm.requestFocus();
                    Toast.makeText(TaskDetails.this, "Square meter is required", Toast.LENGTH_SHORT).show();
                }//end of text util for painter
                else if(L1pieces.getVisibility()==View.VISIBLE && TextUtils.isEmpty(L1pieces.getText().toString())){
                    L1pieces.requestFocus();
                    Toast.makeText(TaskDetails.this, "Quantity is required", Toast.LENGTH_SHORT).show();
                }else if(L2pieces.getVisibility()==View.VISIBLE && TextUtils.isEmpty(L2pieces.getText().toString())){
                    L2pieces.requestFocus();
                    Toast.makeText(TaskDetails.this, "Quantity is required", Toast.LENGTH_SHORT).show();
                }else if(L3pieces.getVisibility()==View.VISIBLE && TextUtils.isEmpty(L3pieces.getText().toString())){
                    L3pieces.requestFocus();
                    Toast.makeText(TaskDetails.this, "Quantity is required", Toast.LENGTH_SHORT).show();
                }else if(L4pieces.getVisibility()==View.VISIBLE && TextUtils.isEmpty(L4pieces.getText().toString())){
                    L4pieces.requestFocus();
                    Toast.makeText(TaskDetails.this, "Weight in kg. is required", Toast.LENGTH_SHORT).show();
                }//end of text util for laundry
                else if(piecesEfan.getVisibility()==View.VISIBLE && TextUtils.isEmpty(piecesEfan.getText().toString())){
                    piecesEfan.requestFocus();
                    Toast.makeText(TaskDetails.this, "Quantity is required", Toast.LENGTH_SHORT).show();
                } else if(piecesTV.getVisibility()==View.VISIBLE && TextUtils.isEmpty(piecesTV.getText().toString())){
                    piecesTV.requestFocus();
                    Toast.makeText(TaskDetails.this, "Quantity is required", Toast.LENGTH_SHORT).show();
                } else if(etACsplitTypePcs.getVisibility()==View.VISIBLE && TextUtils.isEmpty(etACsplitTypePcs.getText().toString())){
                    etACsplitTypePcs.requestFocus();
                    Toast.makeText(TaskDetails.this, "Quantity is required", Toast.LENGTH_SHORT).show();
                } else if(etACwindowTypePcs.getVisibility()==View.VISIBLE && TextUtils.isEmpty(etACwindowTypePcs.getText().toString())){
                    etACwindowTypePcs.requestFocus();
                    Toast.makeText(TaskDetails.this, "Quantity is required", Toast.LENGTH_SHORT).show();
                }//end of text util for quantity of appliances and aircon and ref
                else if(brandTV.getVisibility()==View.VISIBLE && TextUtils.isEmpty(brandTV.getText().toString())){
                    brandTV.requestFocus();
                    Toast.makeText(TaskDetails.this, "Brand is required", Toast.LENGTH_SHORT).show();
                }else if(brandEfan.getVisibility()==View.VISIBLE && TextUtils.isEmpty(brandEfan.getText().toString())){
                    brandEfan.requestFocus();
                    Toast.makeText(TaskDetails.this, "Brand is required", Toast.LENGTH_SHORT).show();
                }else if(brandWM.getVisibility()==View.VISIBLE && TextUtils.isEmpty(brandWM.getText().toString())){
                    brandWM.requestFocus();
                    Toast.makeText(TaskDetails.this, "Brand is required", Toast.LENGTH_SHORT).show();
                }else if(brandES.getVisibility()==View.VISIBLE && TextUtils.isEmpty(brandES.getText().toString())){
                    brandES.requestFocus();
                    Toast.makeText(TaskDetails.this, "Brand is required", Toast.LENGTH_SHORT).show();
                }else if(brandMW.getVisibility()==View.VISIBLE && TextUtils.isEmpty(brandMW.getText().toString())){
                    brandMW.requestFocus();
                    Toast.makeText(TaskDetails.this, "Brand is required", Toast.LENGTH_SHORT).show();
                }else if(etBrandRef.getVisibility()==View.VISIBLE && TextUtils.isEmpty(etBrandRef.getText().toString())){
                    etBrandRef.requestFocus();
                    Toast.makeText(TaskDetails.this, "Brand is required", Toast.LENGTH_SHORT).show();
                }else if(etBrandFreezer.getVisibility()==View.VISIBLE && TextUtils.isEmpty(etBrandFreezer.getText().toString())){
                    etBrandFreezer.requestFocus();
                    Toast.makeText(TaskDetails.this, "Brand is required", Toast.LENGTH_SHORT).show();
                }else if(etBrandACsplit.getVisibility()==View.VISIBLE && TextUtils.isEmpty(etBrandACsplit.getText().toString())){
                    etBrandACsplit.requestFocus();
                    Toast.makeText(TaskDetails.this, "Brand is required", Toast.LENGTH_SHORT).show();
                }else if(etbrandACwindow .getVisibility()==View.VISIBLE && TextUtils.isEmpty(etbrandACwindow .getText().toString())){
                    etbrandACwindow .requestFocus();
                    Toast.makeText(TaskDetails.this, "Brand is required", Toast.LENGTH_SHORT).show();
                }//end of text util for appliances brand

                else if(conAddressCity.getText().toString().contains(" City") || conAddressCity.getText().toString().contains(" city")){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetails.this);
                    builder.setMessage("Please remove the word 'City' on the city field");
                    builder.setCancelable(true);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert11 = builder.create();
                    alert11.show();
              }else if(tvSetDate.getText().toString().equalsIgnoreCase("Set Date")){
                    Toast.makeText(TaskDetails.this, "Please choose a Date", Toast.LENGTH_SHORT).show();
                  tvSetDate.setTextColor(Color.RED);


                }else if(tvSetTime.getText().toString().equalsIgnoreCase("Set Time")){
                    Toast.makeText(TaskDetails.this, "Please choose a Time", Toast.LENGTH_SHORT).show();
                    tvSetTime.setTextColor(Color.RED);
                }
               else {
        //            tvSetDate.setText("08-20-2018");

                    //intent to next activity that will received the info and scheduled date for the task
                    Intent go = new Intent(TaskDetails.this, TaskReceiver.class);
                    go.putExtra("Selected Date", tvSetDate.getText().toString());
                    go.putExtra("Selected Time", tvSetTime.getText().toString());
                    go.putExtra("House Number", conAddress.getText().toString());
                    go.putExtra("Street", conAddressStreet.getText().toString());
                    go.putExtra("City", conAddressCity.getText().toString());
                    go.putExtra("Service Type", taskSelected.toString());
                    go.putExtra("House Type", serviceType.getText().toString());
                    go.putExtra("Service Type Amount", svcCondition.getText().toString());
                    go.putExtra("Item One", itemTxt1.getText().toString());
                    go.putExtra("Amount One", item1.getText().toString());
                    go.putExtra("Item Two", itemTxt2.getText().toString());
                    go.putExtra("Amount Two", item2.getText().toString());
                    go.putExtra("Item Three", itemTxt3.getText().toString());
                    go.putExtra("Amount Three", item3.getText().toString());
                    go.putExtra("Item Four", itemTxt4.getText().toString());
                    go.putExtra("Amount Four", item4.getText().toString());
                    go.putExtra("Item Five", itemTxt5.getText().toString());
                    go.putExtra("Amount Five", item5.getText().toString());
                    go.putExtra("Total Amount", Val.getText().toString());

                    final String totalAmtDigit = String.valueOf(totalAmount);
                    go.putExtra("TotalAmountDigit", totalAmtDigit);
                    startActivity(go);
                }

            }
        });


        /*Animation mAnimation = new AlphaAnimation(1, 0);
        mAnimation.setDuration(200);
        mAnimation.setInterpolator(new LinearInterpolator());
        mAnimation.setRepeatCount(Animation.INFINITE);
        mAnimation.setRepeatMode(Animation.REVERSE);
        imgSetDate.startAnimation(mAnimation);
        *//*imgSetTime.startAnimation(mAnimation);*/
      /*  ////////////
        imgSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
                String currentTime = formatter.format(cal.getTime());

                final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
                Date currentLocalTime = calendar.getTime();
                final DateFormat date = new SimpleDateFormat("hh:mm a");
                date.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                String localTime = date.format(currentLocalTime);
                int hour = Integer.parseInt(localTime.substring(0, 2));
                int minute = Integer.parseInt(localTime.substring(3, 5));
                String pm = localTime.substring(6);

                //scheduler
                final DatePickerDialog dpd = new DatePickerDialog(TaskDetails.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        c.set(year, month, dayOfMonth);
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);
                    }
                }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                //will not allow the user to select the date today
                final Calendar d = Calendar.getInstance();
                d.add(Calendar.DAY_OF_MONTH, 0);
                dpd.getDatePicker().setMinDate(d.getTimeInMillis());
                //d.add(Calendar.MONTH,1);
                //will only allow the user to select within 10 days from tomorrow
                d.add(Calendar.DAY_OF_MONTH, 9);
                dpd.getDatePicker().setMaxDate(d.getTimeInMillis());
                dpd.show();

                //will handle the selecting of date of user/consumer and will toast the specific date selected
                dpd.getDatePicker().init(d.get(Calendar.MONTH), d.get(Calendar.DAY_OF_MONTH), d.get(Calendar.YEAR), new
                        DatePicker.OnDateChangedListener() {
                            @Override
                            public void onDateChanged(DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {
                                //using the OK button of date picker dialog
                                Button dpdOKbutton = (Button)dpd.getButton(dpd.BUTTON_POSITIVE);
                                dpdOKbutton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        d.set(year, monthOfYear, dayOfMonth);
                                        d.getTime();
                                        final String pickedDate = new SimpleDateFormat("MM-dd-yyyy").format(d.getTime());
                                        Toast.makeText(TaskDetails.this, "Your selected date is: " + pickedDate, Toast.LENGTH_LONG).show();
                                        tvSetDate.setText(pickedDate);
                                        tvSetDate.setTextColor(Color.BLACK);
                                        dpd.cancel();
                                    }
                                });
                            }
                        });
            }
        });
        //////// */
      ///////////


        imgSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Calendar cal = Calendar.getInstance();
                SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
                String currentTime = formatter.format(cal.getTime());

                final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
                Date currentLocalTime = calendar.getTime();
                final DateFormat date = new SimpleDateFormat("hh:mm a");
                date.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                String localTime = date.format(currentLocalTime);

                //firebase to retrieve number of selectable days in calendar
                Firebase.setAndroidContext(getApplicationContext());
                myFirebase = new Firebase(url);

                myFirebase.child("SchedulingConfiguration").child("NumberOfFutureDays").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            allowableDaysCalendar = Integer.valueOf(dataSnapshot.getValue(Integer.class));
                            numberOfDays = allowableDaysCalendar;

                            //scheduler
                            final DatePickerDialog dpd = new DatePickerDialog(TaskDetails.this, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    c.set(year, month, dayOfMonth);

                                    mYear = c.get(Calendar.YEAR);
                                    mMonth = c.get(Calendar.MONTH);
                                    mDay = c.get(Calendar.DAY_OF_MONTH);

                                }
                            }, mYear, mMonth, mDay);
                            dpd.getDatePicker().setMinDate(System.currentTimeMillis());

                            final Calendar d = Calendar.getInstance();
                            //will not allow the user to select the date today//////////////////////////////////////////////////////////

                            d.add(Calendar.DAY_OF_MONTH, 0);
                            dpd.getDatePicker().setMinDate(d.getTimeInMillis());

                            //will not allow the user to select the date today//////////////////////////////////////////////////////////
                            //d.add(Calendar.MONTH,1);
                            //will only allow the user to select number of days set on the database from today
                            d.add(Calendar.DAY_OF_MONTH, numberOfDays);
                            dpd.getDatePicker().setMaxDate(d.getTimeInMillis());
                            dpd.show();

                            //will handle the selecting of date of user/consumer and will toast the specific date selected
                            dpd.getDatePicker().init(d.get(Calendar.MONTH), d.get(Calendar.DAY_OF_MONTH), d.get(Calendar.YEAR), new
                                    DatePicker.OnDateChangedListener() {
                                        @Override
                                        public void onDateChanged(DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {
                                            //using the OK button of date picker dialog
                                            Button dpdOKbutton = (Button)dpd.getButton(dpd.BUTTON_POSITIVE);
                                            dpdOKbutton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    d.set(year, monthOfYear, dayOfMonth);
                                                    d.getTime();
                                                    pickedDate = new SimpleDateFormat("MM-dd-yyyy").format(d.getTime());
                                                    Toast.makeText(TaskDetails.this, "Your selected date is: " + pickedDate, Toast.LENGTH_LONG).show();
                                                    tvSetDate.setText(pickedDate);
                                                    tvSetDate.setTextColor(Color.BLACK);
                                        /*view.clearAnimation();*/
                                                    dpd.cancel();
                                                }
                                            });
                                        }
                                    });
                           /* Toast.makeText(TaskDetails.this, "Allowed days: " + numberOfDays, Toast.LENGTH_LONG).show();*/
                        }else{
                            numberOfDays = 10;
                            /*Toast.makeText(TaskDetails.this, "No value", Toast.LENGTH_LONG).show();*/

                            //scheduler
                            final DatePickerDialog dpd = new DatePickerDialog(TaskDetails.this, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    c.set(year, month, dayOfMonth);

                                    mYear = c.get(Calendar.YEAR);
                                    mMonth = c.get(Calendar.MONTH);
                                    mDay = c.get(Calendar.DAY_OF_MONTH);

                                }
                            }, mYear, mMonth, mDay);
                            dpd.getDatePicker().setMinDate(System.currentTimeMillis());

                            final Calendar d = Calendar.getInstance();
                            //will not allow the user to select the date today//////////////////////////////////////////////////////////

                            d.add(Calendar.DAY_OF_MONTH, 0);
                            dpd.getDatePicker().setMinDate(d.getTimeInMillis());

                            //will not allow the user to select the date today//////////////////////////////////////////////////////////
                            //d.add(Calendar.MONTH,1);
                            //will only allow the user to select number of days set on the database from today
                            d.add(Calendar.DAY_OF_MONTH, numberOfDays);
                            dpd.getDatePicker().setMaxDate(d.getTimeInMillis());
                            dpd.show();

                            //will handle the selecting of date of user/consumer and will toast the specific date selected
                            dpd.getDatePicker().init(d.get(Calendar.MONTH), d.get(Calendar.DAY_OF_MONTH), d.get(Calendar.YEAR), new
                                    DatePicker.OnDateChangedListener() {
                                        @Override
                                        public void onDateChanged(DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {
                                            //using the OK button of date picker dialog
                                            Button dpdOKbutton = (Button)dpd.getButton(dpd.BUTTON_POSITIVE);
                                            dpdOKbutton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    d.set(year, monthOfYear, dayOfMonth);
                                                    d.getTime();
                                                    pickedDate = new SimpleDateFormat("MM-dd-yyyy").format(d.getTime());
                                                    Toast.makeText(TaskDetails.this, "Your selected date is: " + pickedDate, Toast.LENGTH_LONG).show();
                                                    tvSetDate.setText(pickedDate);
                                                    tvSetDate.setTextColor(Color.BLACK);
                                        /*view.clearAnimation();*/
                                                    dpd.cancel();
                                                }
                                            });
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });


            }
        });
        /////////////

        ///////////////////
        imgSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                final SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
                final String currentTime = formatter.format(cal.getTime());
                //formatter.setLenient(false);

                final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
                Date currentLocalTime = calendar.getTime();

                final SimpleDateFormat date = new SimpleDateFormat("MM-dd-yyyy hh:mm a");
                date.setLenient(false);
                date.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                final String localTime = date.format(currentLocalTime);
                final int hour = Integer.parseInt(localTime.substring(0, 2));
                final int minutes = Integer.parseInt(localTime.substring(3, 5));
                final String ampm = localTime.substring(6);



                //firebase to allow the past time of not in scheduling
                Firebase.setAndroidContext(getApplicationContext());
                myFirebase = new Firebase(url);

                myFirebase.child("SchedulingConfiguration").child("AllowPastSched").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            allowSched = String.valueOf(dataSnapshot.getValue(String.class));
                           /* Toast.makeText(TaskDetails.this, "value is " + allowSched, Toast.LENGTH_LONG).show();*/
                            if(allowSched.equals("No")){

                                final TimePickerDialog tpd = new TimePickerDialog(TaskDetails.this, new
                                        TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                                Date datePicked = null;
                                                Date datePicked2 = null;

                                                Calendar dateTime = Calendar.getInstance();
                                                dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                                dateTime.set(Calendar.MINUTE, minute);

                                                //////////////////////////////////date now/////////////////////////////////////////////
                                                Date c = Calendar.getInstance().getTime();
                                                SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy hh:mm a");
                                                String dateToday = df.format(c);
                                                try{
                                                    datePicked2 = date.parse(dateToday);
                                                    Toast.makeText(TaskDetails.this, "Today is " + datePicked2, Toast.LENGTH_LONG).show();
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                    Toast.makeText(TaskDetails.this, "Error: " +e.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                                //////////////////////////////////date now/////////////////////////////////////////////

                                                //////////////////////////////////date selected/////////////////////////////////////////////
                                                String HrsToShow = (dateTime.get(Calendar.HOUR) == 0) ?"12":dateTime.get(Calendar.HOUR)+"";
                                                String MinToShow = (dateTime.get(Calendar.MINUTE)<10)?"0"+minute:dateTime.get(Calendar.MINUTE)+"";
                                                String AM_PM = "";

                                                if(dateTime.get(Calendar.AM_PM) == Calendar.AM){
                                                    AM_PM = "AM";
                                                }else if(dateTime.get(Calendar.AM_PM) == Calendar.PM){
                                                    AM_PM = "PM";
                                                }

                                                try {

                                                    String time = pickedDate.toString() + " " + HrsToShow+":"+MinToShow+" "+AM_PM;
                                                    datePicked = date.parse(time);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                    Toast.makeText(TaskDetails.this, "Error: " +e.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                                //////////////////////////////////date selected/////////////////////////////////////////////

                                                //compare date if date selected is before date now
                                                if(datePicked.compareTo(datePicked2) < 0){
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetails.this);
                                                    builder.setMessage("Selected time has already occurred. Please select a " +
                                                            "time not less than " + datePicked2);
                                                    builder.setCancelable(true);
                                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            dialogInterface.cancel();
                                                            tvSetTime.setText("Set Time");
                                                            tvSetTime.setTextColor(Color.RED);
                                                        }
                                                    });
                                                    AlertDialog alert11 = builder.create();
                                                    alert11.show();
                                                }else{
                                                    if(dateTime.get(Calendar.AM_PM) == Calendar.AM){
                                                        AmPm = "AM";
                                                        if(dateTime.get(Calendar.HOUR) >= 12 || (dateTime.get(Calendar.HOUR) <= 7)) {
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetails.this);
                                                            builder.setMessage("You can only schedule a service between 8:00 AM to 2:59 PM?");
                                                            builder.setCancelable(true);
                                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    dialogInterface.cancel();
                                                                    tvSetTime.setText("Set Time");
                                                                    tvSetTime.setTextColor(Color.RED);
                                                                }
                                                            });
                                                            AlertDialog alert11 = builder.create();
                                                            alert11.show();

                                                        }
                                                    }else if(dateTime.get(Calendar.AM_PM) == Calendar.PM){
                                                        AmPm = "PM";

                                                        if(dateTime.get(Calendar.HOUR) >= 3) {
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetails.this);
                                                            builder.setMessage("You can only schedule a service between 8:00 AM to 2:59 PM?");
                                                            builder.setCancelable(true);
                                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    dialogInterface.cancel();
                                                                    tvSetTime.setText("Set Time");
                                                                    tvSetTime.setTextColor(Color.RED);
                                                                }
                                                            });
                                                            AlertDialog alert11 = builder.create();
                                                            alert11.show();
                                                        }else if(dateTime.get(Calendar.HOUR) < hour /*&& dateTime.get(Calendar.MINUTE) <= minute*/ && dateTime.get(Calendar.AM_PM) == Calendar.AM){

                                                        }
                                                    }
                                                }

                                                String strHrsToShow = (dateTime.get(Calendar.HOUR) == 0) ?"12":dateTime.get(Calendar.HOUR)+"";
                                                String strMinToShow = (dateTime.get(Calendar.MINUTE)<10)?"0"+minute:dateTime.get(Calendar.MINUTE)+"";
                                                tvSetTime.setText(strHrsToShow+":"+strMinToShow+" "+AmPm);
                                                tvSetTime.setTextColor(Color.BLACK);
                                            }
                                        }, hour, minutes, false);
                                tpd.show();

                            }else if(allowSched.equals("Yes")){
                                final TimePickerDialog tpd = new TimePickerDialog(TaskDetails.this, new
                                        TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                                Calendar dateTime = Calendar.getInstance();
                                                dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                                dateTime.set(Calendar.MINUTE, minute);

                                                if(dateTime.get(Calendar.AM_PM) == Calendar.AM){
                                                    AmPm = "AM";
                                                    if(dateTime.get(Calendar.HOUR) >= 12 || (dateTime.get(Calendar.HOUR) <= 7)) {
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetails.this);
                                                        builder.setMessage("You can only schedule a service between 8:00 AM to 2:59 PM?");
                                                        builder.setCancelable(true);
                                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                dialogInterface.cancel();
                                                                tvSetTime.setText("Set Time");
                                                                tvSetTime.setTextColor(Color.RED);
                                                            }
                                                        });
                                                        AlertDialog alert11 = builder.create();
                                                        alert11.show();

                                                    }
                                                }else if(dateTime.get(Calendar.AM_PM) == Calendar.PM){
                                                    AmPm = "PM";

                                                    if(dateTime.get(Calendar.HOUR) >= 3) {
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetails.this);
                                                        builder.setMessage("You can only schedule a service between 8:00 AM to 2:59 PM?");
                                                        builder.setCancelable(true);
                                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                dialogInterface.cancel();
                                                                tvSetTime.setText("Set Time");
                                                                tvSetTime.setTextColor(Color.RED);
                                                            }
                                                        });
                                                        AlertDialog alert11 = builder.create();
                                                        alert11.show();
                                                    }else if(dateTime.get(Calendar.HOUR) < hour /*&& dateTime.get(Calendar.MINUTE) <= minute*/ && dateTime.get(Calendar.AM_PM) == Calendar.AM){

                                                    }
                                                }


                                                String strHrsToShow = (dateTime.get(Calendar.HOUR) == 0) ?"12":dateTime.get(Calendar.HOUR)+"";
                                                String strMinToShow = (dateTime.get(Calendar.MINUTE)<10)?"0"+minute:dateTime.get(Calendar.MINUTE)+"";
                                                tvSetTime.setText(strHrsToShow+":"+strMinToShow+" "+AmPm);
                                                tvSetTime.setTextColor(Color.BLACK);
                                            }
                                        }, hour, minutes, false);
                                tpd.show();
                            }
                        }else {
                            Toast.makeText(TaskDetails.this, "No value", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });


                /*Toast.makeText(TaskDetails.this, "Hour is " + hour, Toast.LENGTH_LONG).show();*/
                /*Toast.makeText(TaskDetails.this, "ampm is " + ampm, Toast.LENGTH_LONG).show();*/

                //time scheduler

            }
        });
        ////////////////

   /*     ////////////////
        imgSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
                String currentTime = formatter.format(cal.getTime());

                final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
                Date currentLocalTime = calendar.getTime();
                final DateFormat date = new SimpleDateFormat("hh:mm a");
                date.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                final String localTime = date.format(currentLocalTime);
                int hour = Integer.parseInt(localTime.substring(0, 2));
                int minute = Integer.parseInt(localTime.substring(3, 5));
                String pm = localTime.substring(6);

                //time scheduler
                final TimePickerDialog tpd = new TimePickerDialog(TaskDetails.this, new
                        TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                Calendar dateTime = Calendar.getInstance();
                                dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                dateTime.set(Calendar.MINUTE, minute);

                                if(dateTime.get(Calendar.AM_PM) == Calendar.AM){
                                    AmPm = "AM";
                                    if(dateTime.get(Calendar.HOUR) >= 12 || (dateTime.get(Calendar.HOUR) <= 7)) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetails.this);
                                        builder.setMessage("You can only schedule a service between 8:00 AM to 2:59 PM?");
                                        builder.setCancelable(true);
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                                tvSetTime.setText("Set Time");
                                                tvSetTime.setTextColor(Color.RED);
                                            }
                                        });
                                        AlertDialog alert11 = builder.create();
                                        alert11.show();
                                    }
                                }else if(dateTime.get(Calendar.AM_PM) == Calendar.PM){
                                    AmPm = "PM";
                                    if(dateTime.get(Calendar.HOUR) >= 3) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetails.this);
                                        builder.setMessage("You can only schedule a service between 8:00 AM to 2:59 PM?");
                                        builder.setCancelable(true);
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                                tvSetTime.setText("Set Time");
                                                tvSetTime.setTextColor(Color.RED);
                                            }
                                        });
                                        AlertDialog alert11 = builder.create();
                                        alert11.show();
                                    }
                                }

                                String strHrsToShow = (dateTime.get(Calendar.HOUR) == 0) ?"12":dateTime.get(Calendar.HOUR)+"";
                                String strMinToShow = (dateTime.get(Calendar.MINUTE)<10)?"0"+minute:dateTime.get(Calendar.MINUTE)+"";
                                tvSetTime.setText(strHrsToShow+":"+strMinToShow+" "+AmPm);
                                tvSetTime.setTextColor(Color.BLACK);
                            }
                        }, hour, minute, false);
                tpd.show();
            }
        });
////////////////////// */


        //calling of all the methods for every detailed tasks

        carpenterTask();
        plumbingTask();
        masonTask();
        electricianTask();
        laundryTask();
        painterTask();
        applianceRepairTask();
        AirconAndRefTask();
        HouseKeepingTask();
        ComputerLaptopRepair();
    }
   /* @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("carpenterRad1", carpentryRB1.isChecked());
        savedInstanceState.putBoolean("carpenterRad2", carpentryRB2.isChecked());
        savedInstanceState.putBoolean("carpenterRad2", carpentryRB3.isChecked());
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        carpentryRB1.setChecked(savedInstanceState.getBoolean("carpenterRad1"));
        carpentryRB2.setChecked(savedInstanceState.getBoolean("carpenterRad2"));
        carpentryRB3.setChecked(savedInstanceState.getBoolean("carpenterRad3"));
    }*/

    //METHODS//

    public void disablingNextBtn(){
        //will enable the next button if totalAmount is set into minimum amount
        if(totalAmount >= 250){
            next.setEnabled(true);
        }else if(totalAmount <200){
            next.setEnabled(false);
        }
    }



    //method for computing carpentry task #1
    public void carpenterTask(){

        carpentryRad.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkStructure) {
                //retriever of price in firebase
                Firebase.setAndroidContext(getApplicationContext());
                myFirebase = new Firebase(url);
                if(checkStructure==R.id.carpentryRad1){
                    myFirebase.child("Services").child("AdditionalPayment").child("HouseStructure").child("Condo").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                selectedType = carpentryRB1.getText().toString();
                                typeVal = Double.valueOf(dataSnapshot.getValue(double.class));
                                serviceType.setText("Condo: ");
                                svcCondition.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",typeVal)));
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));

                                disablingNextBtn();
                            }else{
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }else if(checkStructure==R.id.carpentryRad2){
                    myFirebase.child("Services").child("AdditionalPayment").child("HouseStructure").child("Storey").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                selectedType = carpentryRB2.getText().toString();
                                typeVal = Double.valueOf(dataSnapshot.getValue(double.class));
                                serviceType.setText("Apartment: ");
                                svcCondition.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",typeVal)));
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }else{
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }else if(checkStructure==R.id.carpentryRad3){
                    myFirebase.child("Services").child("AdditionalPayment").child("HouseStructure").child("Bungalow").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                selectedType = carpentryRB3.getText().toString();
                                typeVal = Double.valueOf(dataSnapshot.getValue(double.class));
                                serviceType.setText("Bungalow: ");
                                svcCondition.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",typeVal)));
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }else{
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
            }
        });
        //checkboxes for detailed task (carpenter)
        carpentryCheck1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    count++;
                    Toast.makeText(TaskDetails.this, "You have selected: " + carpentryCheck1.getText().toString(), Toast.LENGTH_SHORT).show();
                    //retriever of price in firebase
                    Firebase.setAndroidContext(getApplicationContext());
                    myFirebase = new Firebase(url);
                    myFirebase.child("Services").child("Carpenter").child("Roof").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.exists()){
                                price1 = Double.valueOf(dataSnapshot.getValue(double.class));
                                itemTxt1.setText("Roof/Ceiling: ");
                                item1.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price1)));
                                itemTxt1.setVisibility(View.VISIBLE);
                                item1.setVisibility(View.VISIBLE);
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }else{
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                    if(count > 3){
                        AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetails.this);
                        builder.setMessage("You Can Only Select Up To 3 Items. Proceed with the 3 item selected?");
                        builder.setCancelable(true);

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                carpentryCheck1.setChecked(false);
                                Intent go = new Intent(TaskDetails.this, MainActivity.class);
                                startActivity(go);
                                dialogInterface.cancel();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                carpentryCheck1.setChecked(false);
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alert11 = builder.create();
                        alert11.show();
                    }
                }else if(!isChecked){
                    count--;
                    price1 = 0;
                    itemTxt1.setText("Roof/Ceiling: ");
                    item1.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price1)));
                    itemTxt1.setVisibility(View.GONE);
                    item1.setVisibility(View.GONE);
                    totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                    disablingNextBtn();
                }
            }
        });
        carpentryCheck2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    count++;

                    Firebase.setAndroidContext(getApplicationContext());
                    myFirebase = new Firebase(url);
                    myFirebase.child("Services").child("Carpenter").child("Cabinet").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.exists()) {
                                price2 = Double.valueOf(dataSnapshot.getValue().toString());
                                Toast.makeText(TaskDetails.this, "You have selected: " + carpentryCheck2.getText().toString(), Toast.LENGTH_SHORT).show();
                                itemTxt2.setText("Cabinet Overhead: ");
                                item2.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price2)));
                                itemTxt2.setVisibility(View.VISIBLE);
                                item2.setVisibility(View.VISIBLE);
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                                disablingNextBtn();
                            }else {
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    if(count > 3){
                        AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetails.this);
                        builder.setMessage("You Can Only Select Up To 3 Items. Proceed with the 3 item selected?");
                        builder.setCancelable(true);

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                carpentryCheck2.setChecked(false);
                                Intent go = new Intent(TaskDetails.this, MainActivity.class);
                                startActivity(go);
                                dialogInterface.cancel();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                carpentryCheck2.setChecked(false);
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alert11 = builder.create();
                        alert11.show();
                    }
                }else if(!isChecked){
                    count--;
                    price2 = 0;
                    itemTxt2.setText("Cabinet Overhead: ");
                    item2.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price2)));
                    itemTxt2.setVisibility(View.GONE);
                    item2.setVisibility(View.GONE);
                    totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                    disablingNextBtn();
                }
            }
        });
        carpentryCheck3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    count++;

                    Firebase.setAndroidContext(getApplicationContext());
                    myFirebase = new Firebase(url);
                    myFirebase.child("Services").child("Carpenter").child("Walls").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                price3 = Double.valueOf(dataSnapshot.getValue().toString());
                                Toast.makeText(TaskDetails.this, "You have selected: " + carpentryCheck3.getText().toString(), Toast.LENGTH_SHORT).show();
                                itemTxt3.setText("Walls/Dividers: ");
                                item3.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price3)));
                                itemTxt3.setVisibility(View.VISIBLE);
                                item3.setVisibility(View.VISIBLE);
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                                disablingNextBtn();
                            }else {
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    if(count > 3){
                        AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetails.this);
                        builder.setMessage("You Can Only Select Up To 3 Items. Proceed with the 3 item selected?");
                        builder.setCancelable(true);

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                carpentryCheck3.setChecked(false);
                                Intent go = new Intent(TaskDetails.this, MainActivity.class);
                                startActivity(go);
                                dialogInterface.cancel();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                carpentryCheck3.setChecked(false);
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alert11 = builder.create();
                        alert11.show();
                    }
                }else if(!isChecked){
                    count--;
                    price3 = 0;
                    itemTxt3.setText("Walls/Dividers: ");
                    item3.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price3)));
                    itemTxt3.setVisibility(View.GONE);
                    item3.setVisibility(View.GONE);
                    totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                    disablingNextBtn();
                }
            }
        });
        carpentryCheck4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    count++;

                    Firebase.setAndroidContext(getApplicationContext());
                    myFirebase = new Firebase(url);
                    myFirebase.child("Services").child("Carpenter").child("Flooring").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                price4 = Double.valueOf(dataSnapshot.getValue().toString());
                                Toast.makeText(TaskDetails.this, "You have selected: " + carpentryCheck4.getText().toString(), Toast.LENGTH_SHORT).show();
                                itemTxt4.setText("Cabinet Overhead: ");
                                item4.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price4)));
                                itemTxt4.setVisibility(View.VISIBLE);
                                item4.setVisibility(View.VISIBLE);
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                                disablingNextBtn();
                            }else {
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    if(count > 3){
                        AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetails.this);
                        builder.setMessage("You Can Only Select Up To 3 Items. Proceed with the 3 item selected?");
                        builder.setCancelable(true);

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                carpentryCheck4.setChecked(false);
                                Intent go = new Intent(TaskDetails.this, MainActivity.class);
                                startActivity(go);
                                dialogInterface.cancel();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                carpentryCheck4.setChecked(false);
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alert11 = builder.create();
                        alert11.show();
                    }
                }else if(!isChecked){
                    count--;
                    price4 = 0;
                    itemTxt4.setText("Flooring/Tiles: ");
                    item4.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price4)));
                    itemTxt4.setVisibility(View.GONE);
                    item4.setVisibility(View.GONE);
                    totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                    disablingNextBtn();
                }
            }
        });
        carpentryCheck5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    count++;

                    Firebase.setAndroidContext(getApplicationContext());
                    myFirebase = new Firebase(url);
                    myFirebase.child("Services").child("Carpenter").child("FurnitureRepair").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                price5 = Double.valueOf(dataSnapshot.getValue().toString());
                                Toast.makeText(TaskDetails.this, "You have selected: " + carpentryCheck5.getText().toString(), Toast.LENGTH_SHORT).show();
                                itemTxt5.setText("Cabinet Overhead: ");
                                item5.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price5)));
                                itemTxt5.setVisibility(View.VISIBLE);
                                item5.setVisibility(View.VISIBLE);
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                                disablingNextBtn();
                            }else {
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    if(count > 3){
                        AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetails.this);
                        builder.setMessage("You Can Only Select Up To 3 Items. Proceed with the 3 item selected?");
                        builder.setCancelable(true);

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                carpentryCheck5.setChecked(false);
                                Intent go = new Intent(TaskDetails.this, MainActivity.class);
                                startActivity(go);
                                dialogInterface.cancel();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                carpentryCheck5.setChecked(false);
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alert11 = builder.create();
                        alert11.show();
                    }
                }else if(!isChecked){
                    count--;
                    price5 = 0;
                    itemTxt5.setText("Furniture: ");
                    item5.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price5)));
                    itemTxt5.setVisibility(View.GONE);
                    item5.setVisibility(View.GONE);
                    totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                    disablingNextBtn();
                }
            }
        });
    }
    //method for computing plumber task #2
    public void plumbingTask(){

        plumbingRad.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkStructure) {
                //retriever of price in firebase
                Firebase.setAndroidContext(getApplicationContext());
                myFirebase = new Firebase(url);

                if(checkStructure==R.id.plumberRad1){
                    myFirebase.child("Services").child("AdditionalPayment").child("HouseStructure").child("Condo").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                selectedType = plumbingRB1.getText().toString();
                                typeVal = Double.valueOf(dataSnapshot.getValue(double.class));
                                serviceType.setText("Condo: ");
                                svcCondition.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",typeVal)));
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }else{
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }else if(checkStructure==R.id.plumberRad2){
                    myFirebase.child("Services").child("AdditionalPayment").child("HouseStructure").child("Storey").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                selectedType = plumbingRB2.getText().toString();
                                typeVal = Double.valueOf(dataSnapshot.getValue(double.class));
                                serviceType.setText("Apartment: ");
                                svcCondition.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",typeVal)));
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }else{
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }else if(checkStructure==R.id.plumberRad3){
                    myFirebase.child("Services").child("AdditionalPayment").child("HouseStructure").child("Bungalow").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                selectedType = plumbingRB3.getText().toString();
                                typeVal = Double.valueOf(dataSnapshot.getValue(double.class));
                                serviceType.setText("Bungalow: ");
                                svcCondition.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",typeVal)));
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }else{
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
            }
        });
        //checkboxes for detailed task (plumber)
        plumbingCheck1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    count++;
                    Toast.makeText(TaskDetails.this, "You have selected: " + plumbingCheck1.getText().toString(), Toast.LENGTH_SHORT).show();

                    //retriever of price in firebase
                    Firebase.setAndroidContext(getApplicationContext());
                    myFirebase = new Firebase(url);
                    myFirebase.child("Services").child("Plumber").child("CRSink").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                price1 = Double.valueOf(dataSnapshot.getValue(double.class));
                                itemTxt1.setText("CR Sink/Sewerage: ");
                                item1.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price1)));
                                itemTxt1.setVisibility(View.VISIBLE);
                                item1.setVisibility(View.VISIBLE);
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }else{
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    if(count > 3){
                        AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetails.this);
                        builder.setMessage("You Can Only Select Up To 3 Items. Proceed with the 3 item selected?");
                        builder.setCancelable(false);

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                plumbingCheck1.setChecked(false);
                            //    Intent go = new Intent(TaskDetails.this, MainActivity.class);
                             //   startActivity(go);
                                dialogInterface.cancel();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                plumbingCheck1.setChecked(false);
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alert11 = builder.create();
                        alert11.show();
                    }
                }else if(!isChecked){
                    count--;
                    price1 = 0;
                    itemTxt1.setText("CR Sink/Sewerage: ");
                    item1.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price1)));
                    itemTxt1.setVisibility(View.GONE);
                    item1.setVisibility(View.GONE);
                    totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                    disablingNextBtn();
                }
            }
        });
        plumbingCheck2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    count++;
                    Toast.makeText(TaskDetails.this, "You have selected: " + plumbingCheck2.getText().toString(), Toast.LENGTH_SHORT).show();

                    Firebase.setAndroidContext(getApplicationContext());
                    myFirebase = new Firebase(url);
                    myFirebase.child("Services").child("Plumber").child("ToiletBowl").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                price2 = Double.valueOf(dataSnapshot.getValue().toString());
                                itemTxt2.setText("Toilet Bowl/Sewerage: ");
                                item2.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price2)));
                                itemTxt2.setVisibility(View.VISIBLE);
                                item2.setVisibility(View.VISIBLE);
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                                disablingNextBtn();
                            }else {
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    if(count > 3){
                        AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetails.this);
                        builder.setMessage("You Can Only Select Up To 3 Items. Proceed with the 3 item selected?");
                        builder.setCancelable(true);

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                plumbingCheck2.setChecked(false);
                                Intent go = new Intent(TaskDetails.this, MainActivity.class);
                                startActivity(go);
                                dialogInterface.cancel();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                plumbingCheck2.setChecked(false);
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alert11 = builder.create();
                        alert11.show();
                    }
                }else if(!isChecked){
                    count--;
                    price2 = 0;
                    itemTxt2.setText("Toilet Bowl/Sewerage: ");
                    item2.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price2)));
                    itemTxt2.setVisibility(View.GONE);
                    item2.setVisibility(View.GONE);
                    totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                    disablingNextBtn();
                }
            }
        });
        plumbingCheck3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    count++;
                    Toast.makeText(TaskDetails.this, "You have selected: " + plumbingCheck3.getText().toString(), Toast.LENGTH_SHORT).show();

                    Firebase.setAndroidContext(getApplicationContext());
                    myFirebase = new Firebase(url);
                    myFirebase.child("Services").child("Plumber").child("KitchenSink").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                price3 = Double.valueOf(dataSnapshot.getValue().toString());
                                itemTxt3.setText("Kitchen Sink/Sewerage: ");
                                item3.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price3)));
                                itemTxt3.setVisibility(View.VISIBLE);
                                item3.setVisibility(View.VISIBLE);
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                                disablingNextBtn();
                            }else {
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    if(count > 3){
                        AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetails.this);
                        builder.setMessage("You Can Only Select Up To 3 Items. Proceed with the 3 item selected?");
                        builder.setCancelable(true);

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                plumbingCheck3.setChecked(false);
                                Intent go = new Intent(TaskDetails.this, MainActivity.class);
                                startActivity(go);
                                dialogInterface.cancel();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                plumbingCheck3.setChecked(false);
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alert11 = builder.create();
                        alert11.show();
                    }
                }else if(!isChecked){
                    count--;
                    price3 = 0;
                    itemTxt3.setText("Kitchen Sink/Sewerage: ");
                    item3.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price3)));
                    itemTxt3.setVisibility(View.GONE);
                    item3.setVisibility(View.GONE);
                    totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                    disablingNextBtn();
                }
            }
        });
        plumbingCheck4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    count++;
                    Toast.makeText(TaskDetails.this, "You have selected: " + plumbingCheck4.getText().toString(), Toast.LENGTH_SHORT).show();
                    Firebase.setAndroidContext(getApplicationContext());
                    myFirebase = new Firebase(url);
                    myFirebase.child("Services").child("Plumber").child("RoofDrainage").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                price4 = Double.valueOf(dataSnapshot.getValue().toString());
                                itemTxt4.setText("Roof Drainage: ");
                                item4.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price4)));
                                itemTxt4.setVisibility(View.VISIBLE);
                                item4.setVisibility(View.VISIBLE);
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                                disablingNextBtn();
                            }else {
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    if(count > 3){
                        AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetails.this);
                        builder.setMessage("You Can Only Select Up To 3 Items. Proceed with the 3 item selected?");
                        builder.setCancelable(true);

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                plumbingCheck4.setChecked(false);
                                Intent go = new Intent(TaskDetails.this, MainActivity.class);
                                startActivity(go);
                                dialogInterface.cancel();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                plumbingCheck4.setChecked(false);
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alert11 = builder.create();
                        alert11.show();
                    }
                }else if(!isChecked){
                    count--;
                    price4 = 0;
                    itemTxt4.setText("Roof Drainage: ");
                    item4.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price4)));
                    itemTxt4.setVisibility(View.GONE);
                    item4.setVisibility(View.GONE);
                    totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                    disablingNextBtn();
                }
            }
        });
        plumbingCheck5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    count++;
                    Toast.makeText(TaskDetails.this, "You have selected: " + plumbingCheck5.getText().toString(), Toast.LENGTH_SHORT).show();

                    Firebase.setAndroidContext(getApplicationContext());
                    myFirebase = new Firebase(url);
                    myFirebase.child("Services").child("Plumber").child("LaundrySewerage").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                price5 = Double.valueOf(dataSnapshot.getValue().toString());
                                itemTxt5.setText("Laundry Sewerage: ");
                                item5.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price5)));
                                itemTxt5.setVisibility(View.VISIBLE);
                                item5.setVisibility(View.VISIBLE);
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                                disablingNextBtn();
                            }else {
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    if(count > 3){
                        AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetails.this);
                        builder.setMessage("You Can Only Select Up To 3 Items. Proceed with the 3 item selected?");
                        builder.setCancelable(true);

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                plumbingCheck5.setChecked(false);
                                Intent go = new Intent(TaskDetails.this, MainActivity.class);
                                startActivity(go);
                                dialogInterface.cancel();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                plumbingCheck5.setChecked(false);
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alert11 = builder.create();
                        alert11.show();
                    }
                }else if(!isChecked){
                    count--;
                    price5 = 0;
                    itemTxt5.setText("Laundry Sewerage: ");
                    item5.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price5)));
                    itemTxt5.setVisibility(View.GONE);
                    item5.setVisibility(View.GONE);
                    totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                    disablingNextBtn();
                }
            }
        });
    }
    //method for computing mason task #3
    public void masonTask(){
        masonryRad.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkStructure) {
                //retriever of price in firebase
                Firebase.setAndroidContext(getApplicationContext());
                myFirebase = new Firebase(url);
                if(checkStructure==R.id.masonRad1){
                    myFirebase.child("Services").child("AdditionalPayment").child("HouseStructure").child("Condo").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                selectedType = masonRB1.getText().toString();
                                typeVal = Double.valueOf(dataSnapshot.getValue(double.class));
                                serviceType.setText("Condo: ");
                                svcCondition.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",typeVal)));
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }else{
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }else if(checkStructure==R.id.masonRad2){
                    myFirebase.child("Services").child("AdditionalPayment").child("HouseStructure").child("Storey").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                selectedType = masonRB2.getText().toString();
                                typeVal = Double.valueOf(dataSnapshot.getValue(double.class));
                                serviceType.setText("Apartment: ");
                                svcCondition.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",typeVal)));
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }else{
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }else if(checkStructure==R.id.masonRad3){
                    myFirebase.child("Services").child("AdditionalPayment").child("HouseStructure").child("Bungalow").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                selectedType = masonRB3.getText().toString();
                                typeVal = Double.valueOf(dataSnapshot.getValue(double.class));
                                serviceType.setText("Bungalow: ");
                                svcCondition.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",typeVal)));
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }else{
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
            }
        });
        //check for detail task (mason)
        masonCheck1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    count++;
                    Toast.makeText(TaskDetails.this, "You have selected: " + masonCheck1.getText().toString(), Toast.LENGTH_SHORT).show();

                    //retriever of price in firebase
                    Firebase.setAndroidContext(getApplicationContext());
                    myFirebase = new Firebase(url);
                    myFirebase.child("Services").child("Masonry").child("CeilingRepair").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                price1 = Double.valueOf(dataSnapshot.getValue(double.class));
                                itemTxt1.setText("Ceiling: ");
                                item1.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price1)));
                                itemTxt1.setVisibility(View.VISIBLE);
                                item1.setVisibility(View.VISIBLE);
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }else{
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    if(count > 3){
                        AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetails.this);
                        builder.setMessage("You Can Only Select Up To 3 Items. Proceed with the 3 item selected?");
                        builder.setCancelable(true);

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                masonCheck1.setChecked(false);
                                Intent go = new Intent(TaskDetails.this, MainActivity.class);
                                startActivity(go);
                                dialogInterface.cancel();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                masonCheck1.setChecked(false);
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alert11 = builder.create();
                        alert11.show();
                    }
                }else if(!isChecked){
                    count--;
                    price1 = 0;
                    itemTxt1.setText("Ceiling: ");
                    item1.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price1)));
                    itemTxt1.setVisibility(View.GONE);
                    item1.setVisibility(View.GONE);
                    totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                    disablingNextBtn();
                }
            }
        });
        masonCheck2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    count++;
                    Toast.makeText(TaskDetails.this, "You have selected: " + masonCheck2.getText().toString(), Toast.LENGTH_SHORT).show();

                    Firebase.setAndroidContext(getApplicationContext());
                    myFirebase = new Firebase(url);
                    myFirebase.child("Services").child("Masonry").child("Walls").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                price2 = Double.valueOf(dataSnapshot.getValue().toString());
                                itemTxt2.setText("Walls/Stairs: ");
                                item2.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price2)));
                                itemTxt2.setVisibility(View.VISIBLE);
                                item2.setVisibility(View.VISIBLE);
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                                disablingNextBtn();
                            }else {
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    if(count > 3){
                        AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetails.this);
                        builder.setMessage("You Can Only Select Up To 3 Items. Proceed with the 3 item selected?");
                        builder.setCancelable(true);

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                masonCheck2.setChecked(false);
                                Intent go = new Intent(TaskDetails.this, MainActivity.class);
                                startActivity(go);
                                dialogInterface.cancel();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                masonCheck2.setChecked(false);
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alert11 = builder.create();
                        alert11.show();
                    }
                }else if(!isChecked){
                    count--;
                    price2 = 0;
                    itemTxt2.setText("Walls/Stairs: ");
                    item2.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price2)));
                    itemTxt2.setVisibility(View.GONE);
                    item2.setVisibility(View.GONE);
                    totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                    disablingNextBtn();
                }
            }
        });
        masonCheck3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    count++;
                    Toast.makeText(TaskDetails.this, "You have selected: " + masonCheck3.getText().toString(), Toast.LENGTH_SHORT).show();

                    Firebase.setAndroidContext(getApplicationContext());
                    myFirebase = new Firebase(url);
                    myFirebase.child("Services").child("Masonry").child("KitchenSink").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                price3 = Double.valueOf(dataSnapshot.getValue().toString());
                                itemTxt3.setText("Kitchen/Sink: ");
                                item3.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price3)));
                                itemTxt3.setVisibility(View.VISIBLE);
                                item3.setVisibility(View.VISIBLE);
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                                disablingNextBtn();
                            }else {
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    if(count > 3){
                        AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetails.this);
                        builder.setMessage("You Can Only Select Up To 3 Items. Proceed with the 3 item selected?");
                        builder.setCancelable(true);

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                masonCheck3.setChecked(false);
                                Intent go = new Intent(TaskDetails.this, MainActivity.class);
                                startActivity(go);
                                dialogInterface.cancel();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                masonCheck3.setChecked(false);
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alert11 = builder.create();
                        alert11.show();
                    }
                }else if(!isChecked){
                    count--;
                    price3 = 0;
                    itemTxt3.setText("Kitchen/Sink: ");
                    item3.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price3)));
                    itemTxt3.setVisibility(View.GONE);
                    item3.setVisibility(View.GONE);
                    totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                    disablingNextBtn();
                }
            }
        });
        masonCheck4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    count++;
                    Toast.makeText(TaskDetails.this, "You have selected: " + masonCheck4.getText().toString(), Toast.LENGTH_SHORT).show();

                    Firebase.setAndroidContext(getApplicationContext());
                    myFirebase = new Firebase(url);
                    myFirebase.child("Services").child("Masonry").child("FloorRepair").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                price4 = Double.valueOf(dataSnapshot.getValue().toString());
                                itemTxt4.setText("Living Area/Room Floor: ");
                                item4.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price4)));
                                itemTxt4.setVisibility(View.VISIBLE);
                                item4.setVisibility(View.VISIBLE);
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                                disablingNextBtn();
                            }else {
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    if(count > 3){
                        AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetails.this);
                        builder.setMessage("You Can Only Select Up To 3 Items. Proceed with the 3 item selected?");
                        builder.setCancelable(true);

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                masonCheck4.setChecked(false);
                                Intent go = new Intent(TaskDetails.this, MainActivity.class);
                                startActivity(go);
                                dialogInterface.cancel();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                masonCheck4.setChecked(false);
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alert11 = builder.create();
                        alert11.show();
                    }
                }else if(!isChecked){
                    count--;
                    price4 = 0;
                    itemTxt4.setText("Living Area/Room Floor: ");
                    item4.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price4)));
                    itemTxt4.setVisibility(View.GONE);
                    item4.setVisibility(View.GONE);
                    totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                    disablingNextBtn();
                }
            }
        });
        masonCheck5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    count++;
                    Toast.makeText(TaskDetails.this, "You have selected: " + masonCheck5.getText().toString(), Toast.LENGTH_SHORT).show();

                    Firebase.setAndroidContext(getApplicationContext());
                    myFirebase = new Firebase(url);
                    myFirebase.child("Services").child("Masonry").child("CRFloor").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                price5 = Double.valueOf(dataSnapshot.getValue().toString());
                                itemTxt5.setText("CR Floor: ");
                                item5.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price5)));
                                itemTxt5.setVisibility(View.VISIBLE);
                                item5.setVisibility(View.VISIBLE);
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                                disablingNextBtn();
                            }else {
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    if(count > 3){
                        AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetails.this);
                        builder.setMessage("You Can Only Select Up To 3 Items. Proceed with the 3 item selected?");
                        builder.setCancelable(true);

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                masonCheck5.setChecked(false);
                                Intent go = new Intent(TaskDetails.this, MainActivity.class);
                                startActivity(go);
                                dialogInterface.cancel();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                masonCheck5.setChecked(false);
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alert11 = builder.create();
                        alert11.show();
                    }
                }else if(!isChecked){
                    count--;
                    price5 = 0;
                    itemTxt5.setText("CR Floor: ");
                    itemTxt5.setText("Furniture: ");
                    item5.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price5)));
                    itemTxt5.setVisibility(View.GONE);
                    item5.setVisibility(View.GONE);
                    totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                    disablingNextBtn();
                }
            }
        });
    }
    //method for electrician task #4
    public void electricianTask(){

        electricRad.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkStructure) {
                //retriever of price in firebase
                Firebase.setAndroidContext(getApplicationContext());
                myFirebase = new Firebase(url);
                if(checkStructure==R.id.electricianRad1){
                    myFirebase.child("Services").child("AdditionalPayment").child("HouseStructure").child("Condo").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                selectedType = elecRB1.getText().toString();
                                typeVal = Double.valueOf(dataSnapshot.getValue(double.class));
                                serviceType.setText("Condo: ");
                                svcCondition.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",typeVal)));
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }else{
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }else if(checkStructure==R.id.electricianRad2){
                    myFirebase.child("Services").child("AdditionalPayment").child("HouseStructure").child("Storey").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                selectedType = elecRB2.getText().toString();
                                typeVal = Double.valueOf(dataSnapshot.getValue(double.class));
                                serviceType.setText("Apartment: ");
                                svcCondition.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",typeVal)));
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }else{
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }else if(checkStructure==R.id.electricianRad3){
                    myFirebase.child("Services").child("AdditionalPayment").child("HouseStructure").child("Bungalow").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                selectedType = elecRB3.getText().toString();
                                typeVal = Double.valueOf(dataSnapshot.getValue(double.class));
                                serviceType.setText("Bungalow: ");
                                svcCondition.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",typeVal)));
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }else{
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
            }
        });
        //check for details electrician task
        elecCheck1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    count++;
                    Toast.makeText(TaskDetails.this, "You have selected: " + elecCheck1.getText().toString(), Toast.LENGTH_SHORT).show();

                    //retriever of price in firebase
                    Firebase.setAndroidContext(getApplicationContext());
                    myFirebase = new Firebase(url);
                    myFirebase.child("Services").child("Electrician").child("Cabling").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                price1 = Double.valueOf(dataSnapshot.getValue(double.class));
                                itemTxt1.setText("Cabling/Wiring: ");
                                item1.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price1)));
                                itemTxt1.setVisibility(View.VISIBLE);
                                item1.setVisibility(View.VISIBLE);
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }else{
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    if(count > 3){
                        AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetails.this);
                        builder.setMessage("You Can Only Select Up To 3 Items. Proceed with the 3 item selected?");
                        builder.setCancelable(true);

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                elecCheck1.setChecked(false);
                                Intent go = new Intent(TaskDetails.this, MainActivity.class);
                                startActivity(go);
                                dialogInterface.cancel();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                elecCheck1.setChecked(false);
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alert11 = builder.create();
                        alert11.show();
                    }
                }else if(!isChecked){
                    count--;
                    price1 = 0;
                    itemTxt1.setText("Cabling/Wiring: ");
                    item1.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price1)));
                    itemTxt1.setVisibility(View.GONE);
                    item1.setVisibility(View.GONE);
                    totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                    disablingNextBtn();
                }
            }
        });
        elecCheck2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    count++;
                    Toast.makeText(TaskDetails.this, "You have selected: " + elecCheck2.getText().toString(), Toast.LENGTH_SHORT).show();

                    Firebase.setAndroidContext(getApplicationContext());
                    myFirebase = new Firebase(url);
                    myFirebase.child("Services").child("Electrician").child("CircuitBreaker").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                price2 = Double.valueOf(dataSnapshot.getValue().toString());
                                itemTxt2.setText("Circuit Breaker: ");
                                item2.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price2)));
                                itemTxt2.setVisibility(View.VISIBLE);
                                item2.setVisibility(View.VISIBLE);
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                                disablingNextBtn();
                            }else {
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    if(count > 3){
                        AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetails.this);
                        builder.setMessage("You Can Only Select Up To 3 Items. Proceed with the 3 item selected?");
                        builder.setCancelable(true);

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                elecCheck2.setChecked(false);
                                Intent go = new Intent(TaskDetails.this, MainActivity.class);
                                startActivity(go);
                                dialogInterface.cancel();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                elecCheck2.setChecked(false);
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alert11 = builder.create();
                        alert11.show();
                    }
                }else if(!isChecked){
                    count--;
                    price2 = 0;
                    itemTxt2.setText("Circuit Breaker: ");
                    item2.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price2)));
                    itemTxt2.setVisibility(View.GONE);
                    item2.setVisibility(View.GONE);
                    totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                    disablingNextBtn();
                }
            }
        });
        elecCheck3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    count++;
                    Toast.makeText(TaskDetails.this, "You have selected: " + elecCheck3.getText().toString(), Toast.LENGTH_SHORT).show();

                    Firebase.setAndroidContext(getApplicationContext());
                    myFirebase = new Firebase(url);
                    myFirebase.child("Services").child("Electrician").child("CRshowerHeater").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                price3 = Double.valueOf(dataSnapshot.getValue().toString());
                                itemTxt3.setText("Shower Heater: ");
                                item3.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price3)));
                                itemTxt3.setVisibility(View.VISIBLE);
                                item3.setVisibility(View.VISIBLE);
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                                disablingNextBtn();
                            }else {
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    if(count > 3){
                        AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetails.this);
                        builder.setMessage("You Can Only Select Up To 3 Items. Proceed with the 3 item selected?");
                        builder.setCancelable(true);

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                elecCheck3.setChecked(false);
                                Intent go = new Intent(TaskDetails.this, MainActivity.class);
                                startActivity(go);
                                dialogInterface.cancel();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                elecCheck3.setChecked(false);
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alert11 = builder.create();
                        alert11.show();
                    }
                }else if(!isChecked){
                    count--;
                    price3 = 0;
                    itemTxt3.setText("Shower Heater: ");
                    item3.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price3)));
                    itemTxt3.setVisibility(View.GONE);
                    item3.setVisibility(View.GONE);
                    totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                    disablingNextBtn();
                }
            }
        });
        elecCheck4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    count++;
                    Toast.makeText(TaskDetails.this, "You have selected: " + elecCheck4.getText().toString(), Toast.LENGTH_SHORT).show();

                    Firebase.setAndroidContext(getApplicationContext());
                    myFirebase = new Firebase(url);
                    myFirebase.child("Services").child("Electrician").child("BrokenPlug").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                price4 = Double.valueOf(dataSnapshot.getValue().toString());
                                itemTxt4.setText("Plug/Wire Repair: ");
                                item4.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price4)));
                                itemTxt4.setVisibility(View.VISIBLE);
                                item4.setVisibility(View.VISIBLE);
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                                disablingNextBtn();
                            }else {
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    if(count > 3){
                        AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetails.this);
                        builder.setMessage("You Can Only Select Up To 3 Items. Proceed with the 3 item selected?");
                        builder.setCancelable(true);

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                elecCheck4.setChecked(false);
                                Intent go = new Intent(TaskDetails.this, MainActivity.class);
                                startActivity(go);
                                dialogInterface.cancel();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                elecCheck4.setChecked(false);
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alert11 = builder.create();
                        alert11.show();
                    }
                }else if(!isChecked){
                    count--;
                    price4 = 0;
                    itemTxt4.setText("Plug/Wire Repair: ");
                    item4.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price4)));
                    itemTxt4.setVisibility(View.GONE);
                    item4.setVisibility(View.GONE);
                    totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                    disablingNextBtn();
                }
            }
        });
    }
    //method for laundry task #5
    public void laundryTask() {
        laundryRad.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkStructure) {
                //retriever of price in firebase
                Firebase.setAndroidContext(getApplicationContext());
                myFirebase = new Firebase(url);

                if (checkStructure == R.id.laundryRad1) {
                    myFirebase.child("Services").child("AdditionalPayment").child("WashType").child("MachineWash").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                selectedType = laundryRB1.getText().toString();
                                typeVal = Double.valueOf(dataSnapshot.getValue(double.class));
                                serviceType.setText("Machine Wash: ");
                                svcCondition.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",typeVal)));
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }else{
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                } else if (checkStructure == R.id.laundryRad2) {
                    myFirebase.child("Services").child("AdditionalPayment").child("WashType").child("HandWash").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                selectedType = laundryRB2.getText().toString();
                                typeVal = Double.valueOf(dataSnapshot.getValue(double.class));
                                serviceType.setText("Hand Wash: ");
                                svcCondition.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",typeVal)));
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }else{
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                } else if (checkStructure == R.id.laundryRad3) {
                    myFirebase.child("Services").child("AdditionalPayment").child("WashType").child("MachineHandWash").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                selectedType = laundryRB3.getText().toString();
                                typeVal = Double.valueOf(dataSnapshot.getValue(double.class));
                                serviceType.setText("Machine and Hand Wash: ");
                                svcCondition.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",typeVal)));
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }else{
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
            }
        });
        laundryCheck1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (laundryCheck1.isChecked()) {
                    //retriever of price in firebase
                    Firebase.setAndroidContext(getApplicationContext());
                    myFirebase = new Firebase(url);
                    myFirebase.child("Services").child("Laundry").child("LargeThickCurtain").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                valueL1 = Double.valueOf(dataSnapshot.getValue(double.class));
                                L1pricePerPiece.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", valueL1)+" /pc."));
                               /* L1pieces.setText(Integer.toString(pcs));*/

                                TextWatcher textWatcher = new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        Editable editableValue1 = L1pieces.getText();
                                        double value;
                                        if(editableValue1 != null && editableValue1.length() >= 1){
                                            value = Double.parseDouble(editableValue1.toString());
                                            price1 = valueL1 * value;
                                            item1.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price1)));
                                            itemTxt1.setVisibility(View.VISIBLE);
                                            item1.setVisibility(View.VISIBLE);
                                            itemTxt1.setText("Large Curtain: ");
                                            totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                            Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                                            disablingNextBtn();
                                        }else if(editableValue1 != null){

                                        }
                                    }
                                };
                                L1pieces.addTextChangedListener(textWatcher);
                            }else{
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                    L1pieces.setVisibility(View.VISIBLE);
                    laundryType.setVisibility(View.VISIBLE);
                    laundryRB1.setChecked(true);
                    laundryRB2.setEnabled(false);
                    laundryRB3.setEnabled(false);
                    Toast.makeText(TaskDetails.this, "This type of materials is only available for " + laundryRB1.getText().toString(), Toast.LENGTH_LONG).show();


                } else if (!laundryCheck1.isChecked()) {
                    //needs to reset the corresponding spinner and item price if unchecked. It will be implemented on all laundry CB.
                    laundryType.setVisibility(View.GONE);
                    L1pieces.setVisibility(View.GONE);
                    L1pieces.setText("");
                    laundryType.setSelection(0);
                    L1pricePerPiece.setText("--");
                    laundryRB2.setEnabled(true);
                    laundryRB3.setEnabled(true);
                    price1 = 0;
                    itemTxt1.setText("---: ");
                    item1.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price1)));
                    itemTxt1.setVisibility(View.GONE);
                    item1.setVisibility(View.GONE);
                    totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                    disablingNextBtn();
                }
            }
        });
        laundryCheck2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (laundryCheck2.isChecked()) {
                    count++;

                    Firebase.setAndroidContext(getApplicationContext());
                    myFirebase = new Firebase(url);
                    myFirebase.child("Services").child("Laundry").child("MediumThinCurtain").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                valueL2 = Double.valueOf(dataSnapshot.getValue().toString());
                                L2pricePerPiece.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", valueL2)+" /pc."));
                                /*L2pieces.setText(Integer.toString(pcs));*/

                                TextWatcher textWatcher = new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        Editable editableValue1 = L2pieces.getText();
                                        double value;
                                        if(editableValue1 != null && editableValue1.length() >= 1){
                                            value = Double.parseDouble(editableValue1.toString());
                                            price2 = valueL2 * value;
                                            item2.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price2)));
                                            itemTxt2.setVisibility(View.VISIBLE);
                                            itemTxt2.setText("Medium Curtain: ");
                                            item2.setVisibility(View.VISIBLE);
                                            totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                            Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                                            disablingNextBtn();
                                        }else if(editableValue1 != null){

                                        }
                                    }
                                };
                                L2pieces.addTextChangedListener(textWatcher);
                            }else {
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }//end of datasnopshot
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    L2pieces.setVisibility(View.VISIBLE);
                    laundryType2.setVisibility(View.VISIBLE);
                    laundryRB1.setChecked(true);
                    laundryRB2.setEnabled(false);
                    laundryRB3.setEnabled(false);
                    Toast.makeText(TaskDetails.this, "This type of materials is only available for " + laundryRB1.getText().toString(), Toast.LENGTH_LONG).show();

                } else if (!laundryCheck2.isChecked()) {
                    L2pieces.setVisibility(View.GONE);
                    laundryType2.setVisibility(View.GONE);
                    L2pieces.setText("");
                    laundryType2.setSelection(0);
                    L2pricePerPiece.setText("--");
                    laundryRB2.setEnabled(true);
                    laundryRB3.setEnabled(true);
                    price2 = 0;
                    itemTxt2.setText("---: ");
                    item2.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price2)));
                    itemTxt2.setVisibility(View.GONE);
                    item2.setVisibility(View.GONE);
                    totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                    disablingNextBtn();
                }

            }
        });
        laundryCheck3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(laundryCheck3.isChecked()){

                    Firebase.setAndroidContext(getApplicationContext());
                    myFirebase = new Firebase(url);
                    myFirebase.child("Services").child("Laundry").child("BedsheetComforter").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {

                                valueL3 = Double.valueOf(dataSnapshot.getValue(double.class));
                                L3pricePerPiece.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", valueL3)+" /pc."));
                                /*L1pieces.setText(Integer.toString(pcs));*/

                                TextWatcher textWatcher = new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        Editable editableValue1 = L3pieces.getText();
                                        double value;
                                        if(editableValue1 != null && editableValue1.length() >= 1){
                                            value = Double.parseDouble(editableValue1.toString());
                                            price3 = valueL3 * value;
                                            item3.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price3)));
                                            itemTxt3.setVisibility(View.VISIBLE);
                                            item3.setVisibility(View.VISIBLE);
                                            itemTxt3.setText("Comforter/Bedsheet: ");
                                            totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                            Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                                            disablingNextBtn();
                                        }else if(editableValue1 != null){

                                        }
                                    }
                                };
                                L1pieces.addTextChangedListener(textWatcher);
                            }else {
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    L3pieces.setVisibility(View.VISIBLE);
                    laundryType3.setVisibility(View.VISIBLE);
                    laundryRB1.setChecked(true);
                    laundryRB2.setEnabled(false);
                    laundryRB3.setEnabled(false);
                    Toast.makeText(TaskDetails.this, "This type of materials is only available for " + laundryRB1.getText().toString(), Toast.LENGTH_LONG).show();

                    //spinner color laundry type3 was here and deleted

                }else if (!laundryCheck3.isChecked()) {

                    laundryType3.setVisibility(View.GONE);
                    L3pieces.setVisibility(View.GONE);
                    L3pieces.setText("");
                    laundryType3.setSelection(0);
                    L3pricePerPiece.setText("---");
                    laundryRB2.setEnabled(true);
                    laundryRB3.setEnabled(true);
                    price3 = 0;
                    itemTxt3.setText("---: ");
                    item3.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price3)));
                    itemTxt3.setVisibility(View.GONE);
                    item3.setVisibility(View.GONE);
                    totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                    disablingNextBtn();
                }
            }
        });
        laundryCheck4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(laundryCheck4.isChecked()){

                    laundryType4.setVisibility(View.VISIBLE);

                    if(laundryCheck1.isChecked() || laundryCheck2.isChecked() || laundryCheck3.isChecked()){

                        AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetails.this);
                        builder.setMessage("NOTE:\nYou can change the laundry type to Machine and Hand wash since you selected" +
                                "this item together with curtains and bedsheets. Change it now?");
                        builder.setCancelable(true);

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                laundryRB2.setEnabled(false);
                                laundryRB1.setEnabled(false);
                                laundryRB3.setEnabled(false);
                                laundryRB3.setChecked(true);
                                dialogInterface.cancel();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alert11 = builder.create();
                        alert11.show();

                        //spinner color laundry type4 was here and deleted

                    }

                    Firebase.setAndroidContext(getApplicationContext());
                    myFirebase = new Firebase(url);
                    myFirebase.child("Services").child("Laundry").child("Clothes").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {

                                valueL4 = Double.valueOf(dataSnapshot.getValue(double.class));
                                L4pricePerPiece.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", valueL4)+" /kg."));
                                /*L4pieces.setText(Integer.toString(pcs));*/

                                TextWatcher textWatcher = new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        Editable editableValue1 = L4pieces.getText();
                                        double value;
                                        if(editableValue1 != null && editableValue1.length() >= 1){
                                            value = Double.parseDouble(editableValue1.toString());
                                            price4 = valueL4 * value;
                                            item4.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price4)));
                                            itemTxt4.setVisibility(View.VISIBLE);
                                            itemTxt4.setText("Regular Clothes: ");
                                            item4.setVisibility(View.VISIBLE);
                                            totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                            Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                                            disablingNextBtn();
                                        }else if(editableValue1 != null){

                                        }
                                    }
                                };
                                L4pieces.addTextChangedListener(textWatcher);


                            }else {
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    L4pieces.setVisibility(View.VISIBLE);
                    laundryType4.setVisibility(View.VISIBLE);

                }else if (!laundryCheck4.isChecked()) {
                    laundryType4.setVisibility(View.GONE);
                    L4pieces.setVisibility(View.GONE);
                    L4pieces.setText("");
                    laundryType4.setSelection(0);
                    L4pricePerPiece.setText("--");
                    price4 = 0;
                    itemTxt4.setText("---: ");
                    item4.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price4)));
                    itemTxt4.setVisibility(View.GONE);
                    item4.setVisibility(View.GONE);
                    totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                    disablingNextBtn();
                }/*else{
                    laundryType4.setVisibility(View.VISIBLE);
                }*/
            }
        });
    }


    // method for painter task #6
    public void painterTask(){

        painterRad.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkStructure) {
                //retriever of price in firebase
                Firebase.setAndroidContext(getApplicationContext());
                myFirebase = new Firebase(url);
                if(checkStructure==R.id.PainterRad1){
                    myFirebase.child("Services").child("AdditionalPayment").child("HouseStructure").child("Condo").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                selectedType = carpentryRB1.getText().toString();
                                typeVal = Double.valueOf(dataSnapshot.getValue(double.class));
                                serviceType.setText("Condo: ");
                                svcCondition.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",typeVal)));
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }else{
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }else if(checkStructure==R.id.PainterRad2){
                    myFirebase.child("Services").child("AdditionalPayment").child("HouseStructure").child("Storey").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                selectedType = carpentryRB2.getText().toString();
                                typeVal = Double.valueOf(dataSnapshot.getValue(double.class));
                                serviceType.setText("Apartment: ");
                                svcCondition.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",typeVal)));
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }else{
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                }else if(checkStructure==R.id.PainterRad3){
                    myFirebase.child("Services").child("AdditionalPayment").child("HouseStructure").child("Bungalow").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                selectedType = carpentryRB3.getText().toString();
                                typeVal = Double.valueOf(dataSnapshot.getValue(double.class));
                                serviceType.setText("Bungalow: ");
                                svcCondition.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",typeVal)));
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }else{
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
            }
        });
        //check for detail of painter task
        painterCheck1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //retriever of price in firebase
                    Firebase.setAndroidContext(getApplicationContext());
                    myFirebase = new Firebase(url);
                    myFirebase.child("Services").child("Painting").child("InteriorWH").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                valueP1 = Double.valueOf(dataSnapshot.getValue(double.class));
                                priceP1.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", valueP1)+" /sqm."));

                                TextWatcher textWatcher = new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        Editable editableValue1 = P1sqm.getText();
                                        double value;
                                        if(editableValue1 != null && editableValue1.length() >= 1){
                                            value = Double.parseDouble(editableValue1.toString());
                                            price1 = valueP1 * value;
                                            item1.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price1)));
                                            itemTxt1.setVisibility(View.VISIBLE);
                                            itemTxt1.setText("Interior: ");
                                            item1.setVisibility(View.VISIBLE);
                                            totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                            Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                                            disablingNextBtn();
                                        }else if(editableValue1 != null){

                                        }
                                    }
                                };
                                P1sqm.addTextChangedListener(textWatcher);

                            }else{
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                    count++;
                    Toast.makeText(TaskDetails.this, "You have selected: " + painterCheck1.getText().toString(), Toast.LENGTH_SHORT).show();
                    P1sqm.setVisibility(View.VISIBLE);
                    priceP1.setVisibility(View.VISIBLE);

                    if(count > 3){
                        AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetails.this);
                        builder.setMessage("You Can Only Select Up To 3 Items. Proceed with the 3 item selected?");
                        builder.setCancelable(true);

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                painterCheck1.setChecked(false);
                                Intent go = new Intent(TaskDetails.this, MainActivity.class);
                                startActivity(go);
                                dialogInterface.cancel();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                painterCheck1.setChecked(false);
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alert11 = builder.create();
                        alert11.show();
                    }
                }else if(!isChecked){
                    count--;
                    P1sqm.setVisibility(View.GONE);
                    P1sqm.setText("");
                    priceP1.setVisibility(View.GONE);
                    priceP1.setText("---");
                    price1 = 0;
                    itemTxt1.setText("");
                    item1.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + Double.toString(price1)));
                    itemTxt1.setVisibility(View.GONE);
                    item1.setVisibility(View.GONE);
                    totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" +Double.toString(totalAmount)));
                    disablingNextBtn();
                }
            }
        });
        painterCheck2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //retriever of price in firebase
                    Firebase.setAndroidContext(getApplicationContext());
                    myFirebase = new Firebase(url);
                    myFirebase.child("Services").child("Painting").child("ExteriorWH").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                valueP2 = Double.valueOf(dataSnapshot.getValue(double.class));
                                priceP2.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", valueP2)+" /sqm."));

                                TextWatcher textWatcher = new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        Editable editableValue1 = P2sqm.getText();
                                        double value;
                                        if(editableValue1 != null && editableValue1.length() >= 1){
                                            value = Double.parseDouble(editableValue1.toString());
                                            price2 = valueP2 * value;
                                            item2.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price2)));
                                            itemTxt2.setVisibility(View.VISIBLE);
                                            itemTxt2.setText("Exterior: ");
                                            item2.setVisibility(View.VISIBLE);
                                            totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                            Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                                            disablingNextBtn();
                                        }else if(editableValue1 != null){

                                        }
                                    }
                                };
                                P2sqm.addTextChangedListener(textWatcher);

                            }else {
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                    count++;
                    Toast.makeText(TaskDetails.this, "You have selected: " + painterCheck2.getText().toString(), Toast.LENGTH_SHORT).show();
                    P2sqm.setVisibility(View.VISIBLE);
                    priceP2.setVisibility(View.VISIBLE);
                    if(count > 3){
                        AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetails.this);
                        builder.setMessage("You Can Only Select Up To 3 Items. Proceed with the 3 item selected?");
                        builder.setCancelable(true);

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                painterCheck2.setChecked(false);
                                Intent go = new Intent(TaskDetails.this, MainActivity.class);
                                startActivity(go);
                                dialogInterface.cancel();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                painterCheck2.setChecked(false);
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alert11 = builder.create();
                        alert11.show();
                    }
                }else if(!isChecked){
                    count--;
                    P2sqm.setVisibility(View.GONE);
                    priceP2.setVisibility(View.GONE);
                    P2sqm.setText("");
                    priceP2.setText("---");
                    price2 = 0;
                    itemTxt2.setText("");
                    item2.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + Double.toString(price2)));
                    itemTxt2.setVisibility(View.GONE);
                    item2.setVisibility(View.GONE);
                    totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" +Double.toString(totalAmount)));
                    disablingNextBtn();
                }
            }
        });
        painterCheck3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //retriever of price in firebase
                    Firebase.setAndroidContext(getApplicationContext());
                    myFirebase = new Firebase(url);
                    myFirebase.child("Services").child("Painting").child("SingleArea").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                valueP3 = Double.valueOf(dataSnapshot.getValue(double.class));
                                priceP3.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", valueP3)+" /sqm."));

                                TextWatcher textWatcher = new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        Editable editableValue1 = P3sqm.getText();
                                        double value;
                                        if(editableValue1 != null && editableValue1.length() >= 1){
                                            value = Double.parseDouble(editableValue1.toString());
                                            price3 = valueP3 * value;
                                            item3.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price3)));
                                            itemTxt3.setVisibility(View.VISIBLE);
                                            itemTxt3.setText("Single Area: ");
                                            item3.setVisibility(View.VISIBLE);
                                            totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                            Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                                            disablingNextBtn();
                                        }else if(editableValue1 != null){

                                        }
                                    }
                                };
                                P3sqm.addTextChangedListener(textWatcher);

                            }else{
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                    count++;
                    Toast.makeText(TaskDetails.this, "You have selected: " + painterCheck3.getText().toString(), Toast.LENGTH_SHORT).show();
                    P3sqm.setVisibility(View.VISIBLE);
                    priceP3.setVisibility(View.VISIBLE);
                    if(count > 3){
                        AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetails.this);
                        builder.setMessage("You Can Only Select Up To 3 Items. Proceed with the 3 item selected?");
                        builder.setCancelable(true);

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                painterCheck3.setChecked(false);
                                Intent go = new Intent(TaskDetails.this, MainActivity.class);
                                startActivity(go);
                                dialogInterface.cancel();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                painterCheck3.setChecked(false);
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alert11 = builder.create();
                        alert11.show();
                    }
                }else if(!isChecked){
                    count--;
                    P3sqm.setVisibility(View.GONE);
                    priceP3.setVisibility(View.GONE);
                    P3sqm.setText("");
                    priceP3.setText("---");
                    price3 = 0;
                    itemTxt3.setText("");
                    item3.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + Double.toString(price3)));
                    itemTxt3.setVisibility(View.GONE);
                    item3.setVisibility(View.GONE);
                    totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" +Double.toString(totalAmount)));
                    disablingNextBtn();
                }
            }
        });
        painterCheck4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //retriever of price in firebase
                    Firebase.setAndroidContext(getApplicationContext());
                    myFirebase = new Firebase(url);
                    myFirebase.child("Services").child("Painting").child("Roof").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                valueP4 = Double.valueOf(dataSnapshot.getValue(double.class));
                                priceP4.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", valueP4)+" /sqm."));

                                TextWatcher textWatcher = new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        Editable editableValue1 = P4sqm.getText();
                                        double value;
                                        if(editableValue1 != null && editableValue1.length() >= 1){
                                            value = Double.parseDouble(editableValue1.toString());
                                            price4 = valueP4 * value;
                                            item4.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price4)));
                                            itemTxt4.setVisibility(View.VISIBLE);
                                            itemTxt4.setText("Roof: ");
                                            item4.setVisibility(View.VISIBLE);
                                            totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                            Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                                            disablingNextBtn();
                                        }else if(editableValue1 != null){

                                        }

                                    }
                                };
                                P4sqm.addTextChangedListener(textWatcher);
                            }else{
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                    count++;
                    Toast.makeText(TaskDetails.this, "You have selected: " + painterCheck4.getText().toString(), Toast.LENGTH_SHORT).show();
                    P4sqm.setVisibility(View.VISIBLE);
                    priceP4.setVisibility(View.VISIBLE);
                    if(count > 3){
                        AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetails.this);
                        builder.setMessage("You Can Only Select Up To 3 Items. Proceed with the 3 item selected?");
                        builder.setCancelable(true);

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                painterCheck4.setChecked(false);
                                Intent go = new Intent(TaskDetails.this, MainActivity.class);
                                startActivity(go);
                                dialogInterface.cancel();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                painterCheck4.setChecked(false);
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alert11 = builder.create();
                        alert11.show();
                    }
                }else if(!isChecked){
                    count--;
                    P4sqm.setVisibility(View.GONE);
                    P4sqm.setText("");
                    priceP4.setVisibility(View.GONE);
                    priceP4.setText("---");
                    price4 = 0;
                    itemTxt4.setText("");
                    item4.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + Double.toString(price2)));
                    itemTxt4.setVisibility(View.GONE);
                    item4.setVisibility(View.GONE);
                    totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" +Double.toString(totalAmount)));
                    disablingNextBtn();
                }
            }
        });
    }
    //task for Appliance Repair task #7
    public void applianceRepairTask(){

        applianceCheck1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (applianceCheck1.isChecked()) {
                    count++;
                    ElectricFanType.setVisibility(View.VISIBLE);
                    piecesEfan.setVisibility(View.VISIBLE);
                    brandEfan.setVisibility(View.VISIBLE);
                    tvBrandEfan.setVisibility(View.VISIBLE);

                    /*if(count>3){
                        if(count > 3){
                            AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetails.this);
                            builder.setMessage("You Can Only Select Up To 3 Items. Proceed with the 3 item selected?");
                            builder.setCancelable(true);

                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    applianceCheck1.setChecked(false);
                                    Intent go = new Intent(TaskDetails.this, MainActivity.class);
                                    startActivity(go);
                                    dialogInterface.cancel();
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    applianceCheck1.setChecked(false);
                                    dialogInterface.cancel();
                                }
                            });
                            AlertDialog alert11 = builder.create();
                            alert11.show();
                        }
                    }*/

                    ElectricFanType.setOnItemSelectedListener(new OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            if (ElectricFanType.getSelectedItem().equals("Industrial Fan")) {//datasnopshot is on every selection of the spinner including the textwatcher for computation
                                piecesEfan.setText("");
                                brandEfan.setText("");
                                item1.setText("");
                                indicatedApp1 = "Industrial Fan";
                                itemTxt1.setVisibility(View.VISIBLE);
                                itemTxt1.setText(indicatedApp1);
                                //retriever of price in firebase
                                Firebase.setAndroidContext(getApplicationContext());
                                myFirebase = new Firebase(url);
                                myFirebase.child("Services").child("ApplianceRepair").child("IndustrialEfan").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            valueEfan = Double.valueOf(dataSnapshot.getValue(double.class));
                                            priceEfan.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", valueEfan)+" /pc."));

                                            TextWatcher textWatcher = new TextWatcher() {
                                                @Override
                                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                }

                                                @Override
                                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                                }

                                                @Override
                                                public void afterTextChanged(Editable s) {
                                                    Editable editableValue1 = piecesEfan.getText();
                                                    Editable etBrand = brandEfan.getText();
                                                    double value;
                                                    if(editableValue1 != null && editableValue1.length() >= 1){
                                                        value = Double.parseDouble(editableValue1.toString());
                                                        price1 = valueEfan * value;
                                                        item1.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price1)));
                                                        item1.setVisibility(View.VISIBLE);
                                                        itemTxt1.setText("Industrial Fan/" + piecesEfan.getText().toString() + " pc(s): ");
                                                        totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                                        Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                                                        disablingNextBtn();
                                                    }//end of editableValue1
                                                    if(etBrand != null && etBrand.length() >= 1){
                                                        itemTxt1.setText("Industrial Fan: "  + "\n" + piecesEfan.getText().toString() + " pc(s)/"
                                                                + etBrand.toString());
                                                    }//end of etBrand

                                                }
                                            };
                                            piecesEfan.addTextChangedListener(textWatcher);
                                            brandEfan.addTextChangedListener(textWatcher);
                                        }else{
                                            Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });

                            } else if (ElectricFanType.getSelectedItem().equals("Ceiling Fan")) {
                                piecesEfan.setText("");
                                brandEfan.setText("");
                                item1.setText("");
                                indicatedApp1 = "Ceiling Fan";
                                itemTxt1.setVisibility(View.VISIBLE);
                                itemTxt1.setText(indicatedApp1);
                                Firebase.setAndroidContext(getApplicationContext());
                                myFirebase = new Firebase(url);
                                myFirebase.child("Services").child("ApplianceRepair").child("CeilingEfan").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            valueEfan = Double.valueOf(dataSnapshot.getValue(double.class));
                                            priceEfan.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", valueEfan)+" /pc."));

                                            TextWatcher textWatcher = new TextWatcher() {
                                                @Override
                                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                }

                                                @Override
                                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                                }

                                                @Override
                                                public void afterTextChanged(Editable s) {
                                                    Editable editableValue1 = piecesEfan.getText();
                                                    Editable etBrand = brandEfan.getText();
                                                    double value;
                                                    if(editableValue1 != null && editableValue1.length() >= 1){
                                                        value = Double.parseDouble(editableValue1.toString());
                                                        price1 = valueEfan * value;
                                                        item1.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price1)));
                                                        itemTxt1.setVisibility(View.VISIBLE);
                                                        itemTxt1.setText("Ceiling Fan/" + piecesEfan.getText().toString() + " pc(s): ");
                                                        item1.setVisibility(View.VISIBLE);
                                                        totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                                        Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                                                        disablingNextBtn();
                                                    }//end of editableValue1
                                                    if(etBrand != null && etBrand.length() >= 1){
                                                        itemTxt1.setText("Industrial Fan: "  + "\n" + piecesEfan.getText().toString() + " pc(s)/"
                                                                + etBrand.toString());
                                                    }//end of etBrand
                                                }
                                            };
                                            piecesEfan.addTextChangedListener(textWatcher);
                                            brandEfan.addTextChangedListener(textWatcher);
                                        }else {
                                            Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });


                            } else if (ElectricFanType.getSelectedItem().equals("Wall Fan")) {
                                piecesEfan.setText("");
                                item1.setText("");
                                brandEfan.setText("");
                                indicatedApp1 = "Wall Fan";
                                itemTxt1.setVisibility(View.VISIBLE);
                                itemTxt1.setText(indicatedApp1);
                                Firebase.setAndroidContext(getApplicationContext());
                                myFirebase = new Firebase(url);
                                myFirebase.child("Services").child("ApplianceRepair").child("WallEfan").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            valueEfan = Double.valueOf(dataSnapshot.getValue(double.class));
                                            priceEfan.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", valueEfan)+" /pc."));

                                            TextWatcher textWatcher = new TextWatcher() {
                                                @Override
                                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                }

                                                @Override
                                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                                }

                                                @Override
                                                public void afterTextChanged(Editable s) {
                                                    Editable editableValue1 = piecesEfan.getText();
                                                    Editable etBrand = brandEfan.getText();
                                                    double value;
                                                    if(editableValue1 != null && editableValue1.length() >= 1){
                                                        value = Double.parseDouble(editableValue1.toString());
                                                        price1 = valueEfan * value;
                                                        item1.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price1)));
                                                        itemTxt1.setVisibility(View.VISIBLE);
                                                        itemTxt1.setText("Wall Fan/" + piecesEfan.getText().toString() + " pc(s): ");
                                                        item1.setVisibility(View.VISIBLE);
                                                        totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                                        Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                                                        disablingNextBtn();
                                                    }//end of editableValue1
                                                    if(etBrand != null && etBrand.length() >= 1){
                                                        itemTxt1.setText("Industrial Fan: "  + "\n" + piecesEfan.getText().toString() + " pc(s)/"
                                                                + etBrand.toString());
                                                    }//end of etBrand

                                                }
                                            };
                                            piecesEfan.addTextChangedListener(textWatcher);
                                            brandEfan.addTextChangedListener(textWatcher);
                                        }else{
                                            Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });

                            } else if (ElectricFanType.getSelectedItem().equals("Stand Fan")) {
                                piecesEfan.setText("");
                                item1.setText("");
                                brandEfan.setText("");
                                indicatedApp1 = "Stand Fan";
                                itemTxt1.setVisibility(View.VISIBLE);
                                itemTxt1.setText(indicatedApp1);
                                //retriever of price in firebase
                                Firebase.setAndroidContext(getApplicationContext());
                                myFirebase = new Firebase(url);
                                myFirebase.child("Services").child("ApplianceRepair").child("StandEfan").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            valueEfan = Double.valueOf(dataSnapshot.getValue(double.class));
                                            priceEfan.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", valueEfan)+" /pc."));

                                            TextWatcher textWatcher = new TextWatcher() {
                                                @Override
                                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                }

                                                @Override
                                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                                }

                                                @Override
                                                public void afterTextChanged(Editable s) {
                                                    Editable editableValue1 = piecesEfan.getText();
                                                    Editable etBrand = brandEfan.getText();
                                                    double value;
                                                    if(editableValue1 != null && editableValue1.length() >= 1){
                                                        value = Double.parseDouble(editableValue1.toString());
                                                        price1 = valueEfan * value;
                                                        item1.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price1)));
                                                        itemTxt1.setVisibility(View.VISIBLE);
                                                        itemTxt1.setText("Stand Fan/" + piecesEfan.getText().toString() + " pc(s): ");
                                                        item1.setVisibility(View.VISIBLE);
                                                        totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                                        Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                                                        disablingNextBtn();
                                                    }//end of editableValue1
                                                    if(etBrand != null && etBrand.length() >= 1){
                                                        itemTxt1.setText("Industrial Fan: "  + "\n" + piecesEfan.getText().toString() + " pc(s)/"
                                                                + etBrand.toString());
                                                    }//end of etBrand

                                                }
                                            };
                                            piecesEfan.addTextChangedListener(textWatcher);
                                            brandEfan.addTextChangedListener(textWatcher);
                                        }else {
                                            Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });

                            }else if(ElectricFanType.getSelectedItem().equals("Type:")){
                                /*Toast.makeText(TaskDetails.this, "Invalid selection.", Toast.LENGTH_SHORT).show();*/
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            //place some validation in here
                        }
                    });
                }else if(!applianceCheck1.isChecked()){
                    count --;
                    ElectricFanType.setVisibility(View.GONE);
                    /*ElectricFanType.getSelectedItem().equals("Type:");*/
                    ElectricFanType.setSelection(0);
                    brandEfan.setVisibility(View.GONE);
                    brandEfan.setText("");
                    tvBrandEfan.setVisibility(View.GONE);
                    piecesEfan.setVisibility(View.GONE);
                    piecesEfan.setText("");
                    priceEfan.setText("---");
                    price1 = 0;
                    itemTxt1.setText("---: ");
                    item1.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + Double.toString(price1)));
                    itemTxt1.setVisibility(View.GONE);
                    item1.setVisibility(View.GONE);
                    totalAmount = price1 + price2 + price3 + price4 + price5;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" +Double.toString(totalAmount)));
                    disablingNextBtn();
                }

            }
        });

        applianceCheck2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(applianceCheck2.isChecked()){
                    teleType.setVisibility(View.VISIBLE);
                    piecesTV.setVisibility(View.VISIBLE);
                    brandTV.setVisibility(View.VISIBLE);
                    tvBrandTelevision.setVisibility(View.VISIBLE);
                    count++;

                    teleType.setOnItemSelectedListener(new OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(teleType.getSelectedItem().equals("Flat Screen LCD")){
                                indicatedApp2 = "Flat Screen TV";
                                piecesTV.setText("");
                                brandTV.setText("");
                                item2.setText("");
                                itemTxt2.setVisibility(View.VISIBLE);
                                itemTxt2.setText(indicatedApp2);
                                //retriever of price in firebase
                                Firebase.setAndroidContext(getApplicationContext());
                                myFirebase = new Firebase(url);
                                myFirebase.child("Services").child("ApplianceRepair").child("FlatTV").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            valueTV = Double.valueOf(dataSnapshot.getValue(double.class));
                                            priceTV.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", valueTV)+" /pc."));

                                            TextWatcher textWatcher = new TextWatcher() {
                                                @Override
                                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                }

                                                @Override
                                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                                }

                                                @Override
                                                public void afterTextChanged(Editable s) {
                                                    Editable editableValue1 = piecesTV.getText();
                                                    Editable etBrand = brandTV.getText();
                                                    double value;
                                                    if(editableValue1 != null && editableValue1.length() >= 1){
                                                        value = Double.parseDouble(editableValue1.toString());
                                                        price2 = valueTV * value;
                                                        item2.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price2)));
                                                        itemTxt2.setVisibility(View.VISIBLE);
                                                        itemTxt2.setText("Flat Screen TV/" + piecesTV.getText().toString() + " pc(s): ");
                                                        item2.setVisibility(View.VISIBLE);
                                                        totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                                        Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                                                        disablingNextBtn();
                                                    }//end of editableValue1
                                                    if(etBrand != null && etBrand.length() >= 1){
                                                        itemTxt2.setText("Flat Screen TV: "  + "\n" + piecesTV.getText().toString() + " pc(s)/"
                                                                + etBrand.toString());
                                                    }//end of etBrand
                                                }
                                            };
                                            piecesTV.addTextChangedListener(textWatcher);
                                            brandTV.addTextChangedListener(textWatcher);
                                        }else {
                                            Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });

                            }else if(teleType.getSelectedItem().equals("Old Type w/ Picture Tube")){
                                indicatedApp2 = "Old Type TV";
                                piecesTV.setText("");
                                brandTV.setText("");
                                item2.setText("");
                                itemTxt2.setVisibility(View.VISIBLE);
                                itemTxt2.setText(indicatedApp2);
                                //retriever of price in firebase
                                Firebase.setAndroidContext(getApplicationContext());
                                myFirebase = new Firebase(url);
                                myFirebase.child("Services").child("ApplianceRepair").child("PictureTubeTV").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            valueTV = Double.valueOf(dataSnapshot.getValue(double.class));
                                            priceTV.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", valueTV)+" /pc."));

                                            TextWatcher textWatcher = new TextWatcher() {
                                                @Override
                                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                }

                                                @Override
                                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                                }

                                                @Override
                                                public void afterTextChanged(Editable s) {
                                                    Editable editableValue1 = piecesTV.getText();
                                                    Editable etBrand = brandTV.getText();
                                                    double value;
                                                    if(editableValue1 != null && editableValue1.length() >= 1){
                                                        value = Double.parseDouble(editableValue1.toString());
                                                        price2 = valueTV * value;
                                                        item2.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price2)));
                                                        itemTxt2.setVisibility(View.VISIBLE);
                                                        itemTxt2.setText("Old Type TV/" + piecesTV.getText().toString() + " pc(s): ");
                                                        item2.setVisibility(View.VISIBLE);
                                                        totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                                        Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                                                        disablingNextBtn();
                                                    }//end of editableValue1
                                                    if(etBrand != null && etBrand.length() >= 1){
                                                        itemTxt2.setText("Old Type TV: "  + "\n" + piecesEfan.getText().toString() + " pc(s)/"
                                                                + etBrand.toString());
                                                    }//end of etBrand
                                                }
                                            };
                                            piecesTV.addTextChangedListener(textWatcher);
                                            brandTV.addTextChangedListener(textWatcher);
                                        }else {
                                            Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });

                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if(!applianceCheck2.isChecked()){
                    count --;
                    teleType.setVisibility(View.GONE);
                    teleType.setSelection(0);
                    brandTV.setVisibility(View.GONE);
                    tvBrandTelevision.setVisibility(View.GONE);
                    brandTV.setText("");
                    piecesTV.setVisibility(View.GONE);
                    piecesTV.setText("");
                    priceTV.setText("--");
                    price2 = 0;
                    itemTxt2.setText("---: ");
                    item2.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + Double.toString(price2)));
                    itemTxt2.setVisibility(View.GONE);
                    item2.setVisibility(View.GONE);
                    totalAmount = price1 + price2 + price3 + price4 + price5;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" +Double.toString(totalAmount)));
                    disablingNextBtn();
                }

            }
        });
        applianceCheck3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(applianceCheck3.isChecked()){
                    washingMachingType.setVisibility(View.VISIBLE);
                    washingMachingType.setVisibility(View.VISIBLE);
                    tvBrandWM.setVisibility(View.VISIBLE);
                    brandWM.setVisibility(View.VISIBLE);
                    count++;


                    washingMachingType.setOnItemSelectedListener(new OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(washingMachingType.getSelectedItem().equals("Analog Washing Machine")){
                                indicatedApp3 = "Analog Washing Machine";
                                brandWM.setText("");
                                itemTxt3.setVisibility(View.VISIBLE);
                                itemTxt3.setText(indicatedApp3);
                                //retriever of price in firebase
                                Firebase.setAndroidContext(getApplicationContext());
                                myFirebase = new Firebase(url);
                                myFirebase.child("Services").child("ApplianceRepair").child("AnalogWM").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            valueWM = Double.valueOf(dataSnapshot.getValue(double.class));
                                            priceWM.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", valueWM)));
                                            price3 = valueWM;
                                            item3.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price3)));
                                            item3.setVisibility(View.VISIBLE);
                                            totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                            Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                                            disablingNextBtn();

                                            TextWatcher textWatcher = new TextWatcher() {
                                                @Override
                                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                }

                                                @Override
                                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                                }

                                                @Override
                                                public void afterTextChanged(Editable s) {

                                                    Editable etBrand = brandWM.getText();

                                                    if(etBrand != null && etBrand.length() >= 1){
                                                        itemTxt3.setText("Washing Machine: " + "\n" + etBrand.toString() + "/Analog");
                                                    }//end of etBrand
                                                }
                                            };
                                            brandWM.addTextChangedListener(textWatcher);
                                        }else {
                                            Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });

                            }else if(washingMachingType.getSelectedItem().equals("Digital Washing Machine")){
                                brandWM.setText("");
                                indicatedApp3 = "Digital Washing Machine";
                                itemTxt3.setVisibility(View.VISIBLE);
                                itemTxt3.setText(indicatedApp3);

                                //retriever of price in firebase
                                Firebase.setAndroidContext(getApplicationContext());
                                myFirebase = new Firebase(url);
                                myFirebase.child("Services").child("ApplianceRepair").child("DigitalWM").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            valueWM = Double.valueOf(dataSnapshot.getValue(double.class));
                                            priceWM.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", valueWM)));
                                            price3 = valueWM;
                                            item3.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price3)));
                                            item3.setVisibility(View.VISIBLE);
                                            totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                            Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                                            disablingNextBtn();

                                            TextWatcher textWatcher = new TextWatcher() {
                                                @Override
                                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                }

                                                @Override
                                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                                }

                                                @Override
                                                public void afterTextChanged(Editable s) {
                                                    Editable etBrand = brandWM.getText();
                                                    if(etBrand != null && etBrand.length() >= 1){
                                                        itemTxt3.setText("Washing Machine: " + "\n" + etBrand.toString()+"/Digital");
                                                    }else if(etBrand != null){

                                                    }
                                                }
                                            };
                                            brandWM.addTextChangedListener(textWatcher);
                                        }else {
                                            Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });

                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if(!applianceCheck3.isChecked()){
                    count --;
                    washingMachingType.setVisibility(View.GONE);
                    washingMachingType.setSelection(0);
                    priceWM.setText("");
                    brandWM.setText("");
                    tvBrandWM.setVisibility(View.GONE);
                    brandWM.setVisibility(View.GONE);
                    price3 = 0;
                    itemTxt3.setText("---: ");
                    item3.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + Double.toString(price3)));
                    itemTxt3.setVisibility(View.GONE);
                    item3.setVisibility(View.GONE);
                    totalAmount = price1 + price2 + price3 + price4 + price5;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" +Double.toString(totalAmount)));
                    disablingNextBtn();
                }

            }
        });
        applianceCheck4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(applianceCheck4.isChecked()){
                    count++;
                    brandES.setText("");
                    brandES.setVisibility(View.VISIBLE);
                    tvBrandES.setVisibility(View.VISIBLE);
                    indicatedApp4 = "Electric Stove";
                    itemTxt4.setVisibility(View.VISIBLE);
                    itemTxt4.setText(indicatedApp4);

                    /*if(count>3){
                        if(count > 3){
                            AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetails.this);
                            builder.setMessage("You Can Only Select Up To 3 Items. Proceed with the 3 item selected?");
                            builder.setCancelable(true);

                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    applianceCheck4.setChecked(false);
                                    Intent go = new Intent(TaskDetails.this, MainActivity.class);
                                    startActivity(go);
                                    dialogInterface.cancel();
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    applianceCheck4.setChecked(false);
                                    dialogInterface.cancel();
                                }
                            });
                            AlertDialog alert11 = builder.create();
                            alert11.show();
                        }
                    }*/
                    //retriever of price in firebase
                    Firebase.setAndroidContext(getApplicationContext());
                    myFirebase = new Firebase(url);
                    myFirebase.child("Services").child("ApplianceRepair").child("ElectricStove").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                valueEstove = Double.valueOf(dataSnapshot.getValue(double.class));
                                priceES.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", valueEstove)));
                                price4 = valueEstove;
                                item4.setVisibility(View.VISIBLE);
                                item4.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price4)));
                                totalAmount = price1 + price2 + price3 + price4 + price5;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();

                                TextWatcher textWatcher = new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        Editable etBrand = brandES.getText();
                                        if(etBrand != null && etBrand.length() >= 1){
                                            itemTxt4.setText("Electric Stove: "  + "\n" + etBrand.toString());
                                        }//end of etBrand
                                    }
                                };
                                brandES.addTextChangedListener(textWatcher);
                            }else {
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }else if(!applianceCheck4.isChecked()){
                    count --;
                    price4= 0;
                    brandES.setText("");
                    brandES.setVisibility(View.GONE);
                    tvBrandES.setVisibility(View.GONE);
                    itemTxt4.setText("---: ");
                    item4.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price4)));
                    itemTxt4.setVisibility(View.GONE);
                    item4.setVisibility(View.GONE);
                    totalAmount = price1 + price2 + price3 + price4 + price5;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                    disablingNextBtn();
                }

            }
        });
        applianceCheck5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(applianceCheck5.isChecked()){
                    count++;
                    brandMW.setText("");
                    brandMW.setVisibility(View.VISIBLE);
                    tvBrandMW.setVisibility(View.VISIBLE);
                    indicatedApp5 = "Microwave";
                    itemTxt5.setVisibility(View.VISIBLE);
                    itemTxt5.setText(indicatedApp5);

                   /* if(count>3){
                        if(count > 3){
                            AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetails.this);
                            builder.setMessage("You Can Only Select Up To 3 Items. Proceed with the 3 item selected?");
                            builder.setCancelable(true);

                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    applianceCheck5.setChecked(false);
                                    Intent go = new Intent(TaskDetails.this, MainActivity.class);
                                    startActivity(go);
                                    dialogInterface.cancel();
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    applianceCheck5.setChecked(false);
                                    dialogInterface.cancel();
                                }
                            });
                            AlertDialog alert11 = builder.create();
                            alert11.show();
                        }
                    }*/
                    //retriever of price in firebase
                    Firebase.setAndroidContext(getApplicationContext());
                    myFirebase = new Firebase(url);
                    myFirebase.child("Services").child("ApplianceRepair").child("Microwave").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                valueMwave = Double.valueOf(dataSnapshot.getValue(double.class));
                                priceMW.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", valueMwave)));
                                price5 = valueMwave;
                                item5.setVisibility(View.VISIBLE);
                                item5.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price5)));
                                totalAmount = price1 + price2 + price3 + price4 + price5;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();

                                TextWatcher textWatcher = new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        Editable etBrand = brandMW.getText();
                                        if(etBrand != null && etBrand.length() >= 1){
                                            itemTxt5.setText("Microwave: "  + "\n" + etBrand.toString());
                                        }//end of etBrand
                                    }
                                };
                                brandMW.addTextChangedListener(textWatcher);
                            }else {
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                }else if(!applianceCheck5.isChecked()){
                    count --;
                    price5 = 0;
                    brandMW.setText("");
                    brandMW.setVisibility(View.GONE);
                    tvBrandMW.setVisibility(View.GONE);
                    itemTxt5.setText("---: ");
                    item5.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price5)));
                    itemTxt5.setVisibility(View.GONE);
                    item5.setVisibility(View.GONE);
                    totalAmount = price1 + price2 + price3 + price4 + price5;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                    disablingNextBtn();
                }
            }
        });
    }

    // method for aircon and ref task #8
    public void AirconAndRefTask(){

        ACrefCheck1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    refBrandtv.setVisibility(View.VISIBLE);
                    etBrandRef.setText("");
                    etBrandRef.setVisibility(View.VISIBLE);
                    itemTxt1.setVisibility(View.VISIBLE);
                    itemTxt1.setText("Refrigerator");

                    //retriever of price in firebase
                    Firebase.setAndroidContext(getApplicationContext());
                    myFirebase = new Firebase(url);
                    myFirebase.child("Services").child("AirconRefRepair").child("Refrigerator").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                valueACref1 = Double.valueOf(dataSnapshot.getValue(double.class));
                                item1.setVisibility(View.VISIBLE);
                                item1.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", valueACref1)));
                                price1 = valueACref1;
                                item1.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price1)));
                                totalAmount = price1 + price2 + price3 + price4;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                                disablingNextBtn();

                                TextWatcher textWatcher = new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        Editable etBrand = etBrandRef.getText();
                                        if(etBrand != null && etBrand.length() >= 1){
                                            itemTxt1.setText("Refrigerator: "  + "\n" + etBrand.toString());
                                        }//end of etBrand
                                    }
                                };
                                etBrandRef.addTextChangedListener(textWatcher);
                            }else {
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }else if(!isChecked){
                    refBrandtv.setVisibility(View.GONE);
                    etBrandRef.setVisibility(View.GONE);
                    etBrandRef.setText("");
                    price1 = 0;
                    itemTxt1.setText("---: ");
                    item1.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",  price1)));
                    itemTxt1.setVisibility(View.GONE);
                    item1.setVisibility(View.GONE);
                    totalAmount = price1 + price2 + price3 + price4 + price5;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                    disablingNextBtn();
                }

            }
        });
        ACrefCheck2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    freezerBrandtv.setVisibility(View.VISIBLE);
                    etBrandFreezer.setVisibility(View.VISIBLE);
                    etBrandFreezer.setText("");
                    itemTxt2.setVisibility(View.VISIBLE);
                    itemTxt2.setText("Stand Alone Freezer");

                    //retriever of price in firebase
                    Firebase.setAndroidContext(getApplicationContext());
                    myFirebase = new Firebase(url);
                    myFirebase.child("Services").child("AirconRefRepair").child("Freezer").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                valueACref2 = Double.valueOf(dataSnapshot.getValue(double.class));
                                item2.setVisibility(View.VISIBLE);
                                item2.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", valueACref2)));
                                price2 = valueACref2;
                                item2.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price2)));
                                totalAmount = price1 + price2 + price3 + price4;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                                disablingNextBtn();

                                TextWatcher textWatcher = new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        Editable etBrand = etBrandFreezer.getText();
                                        if(etBrand != null && etBrand.length() >= 1){
                                            itemTxt2.setText("Stand Alone Freezer: "  + "\n" + etBrand.toString());
                                        }//end of etBrand
                                    }
                                };
                                etBrandFreezer.addTextChangedListener(textWatcher);
                            }else {
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }else if(!isChecked){
                    freezerBrandtv.setVisibility(View.GONE);
                    etBrandFreezer.setText("");
                    etBrandFreezer.setVisibility(View.GONE);
                    price2 = 0;
                    itemTxt2.setText("---: ");
                    item2.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price2)));
                    itemTxt2.setVisibility(View.GONE);
                    item2.setVisibility(View.GONE);
                    totalAmount = price1 + price2 + price3 + price4 + price5;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                    disablingNextBtn();
                }
            }
        });
        ACrefCheck3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ACsplitBrandtv.setVisibility(View.VISIBLE);
                    etBrandACsplit.setText("");
                    etBrandACsplit.setVisibility(View.VISIBLE);
                    etACsplitTypePcs.setText("");
                    etACsplitTypePcs.setVisibility(View.VISIBLE);
                    itemTxt3.setVisibility(View.VISIBLE);
                    itemTxt3.setText("AC Split Type");

                    //retriever of price in firebase
                    Firebase.setAndroidContext(getApplicationContext());
                    myFirebase = new Firebase(url);
                    myFirebase.child("Services").child("AirconRefRepair").child("ACSplitType").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                valueACref3 = Double.valueOf(dataSnapshot.getValue(double.class));
                                ACsplitTypePricetv.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", valueACref3) + "/pc(s)."));

                                TextWatcher textWatcher = new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        Editable editableValue = etACsplitTypePcs.getText();
                                        Editable etBrand = etBrandACsplit.getText();
                                        double value;
                                        if(editableValue != null && editableValue.length() >= 1){
                                            value = Double.parseDouble(editableValue.toString());
                                            price3 = valueACref3 * value;
                                            item3.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price3)));
                                            item3.setVisibility(View.VISIBLE);
                                            itemTxt3.setText("AC Split Type/" + etACsplitTypePcs.getText().toString() + "pc(s).");
                                            totalAmount = price1 + price2 + price3 + price4 + price5;
                                            Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                                            disablingNextBtn();
                                        }//end of editableValue
                                        if(etBrand != null && etBrand.length() >= 1){
                                            itemTxt3.setText("AC Split Type/" + "\n" + etACsplitTypePcs.getText().toString() + "pc(s)/"
                                                    + etBrandACsplit.getText().toString());
                                        }

                                    }
                                };
                                etACsplitTypePcs.addTextChangedListener(textWatcher);
                                etBrandACsplit.addTextChangedListener(textWatcher);
                            }else{
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }else if(!isChecked){
                    ACsplitTypePricetv.setText("--");
                    ACsplitBrandtv.setVisibility(View.GONE);
                    etBrandACsplit.setText("");
                    etACsplitTypePcs.setText("");
                    etBrandACsplit.setVisibility(View.GONE);
                    etACsplitTypePcs.setVisibility(View.GONE);
                    itemTxt3.setVisibility(View.GONE);
                    item3.setVisibility(View.GONE);
                    item3.setText("");
                    itemTxt3.setText("--");
                    price3 = 0;
                    item3.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price3)));
                    totalAmount = price1 + price2 + price3 + price4;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                    disablingNextBtn();
                }
            }
        });
        ACrefCheck4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ACwindowBrandTv.setVisibility(View.VISIBLE);
                    etbrandACwindow.setText("");
                    etACwindowTypePcs.setText("");
                    etbrandACwindow.setVisibility(View.VISIBLE);
                    etACwindowTypePcs.setVisibility(View.VISIBLE);
                    itemTxt4.setVisibility(View.VISIBLE);
                    itemTxt4.setText("AC Window Type");

                    //retriever of price in firebase
                    Firebase.setAndroidContext(getApplicationContext());
                    myFirebase = new Firebase(url);
                    myFirebase.child("Services").child("AirconRefRepair").child("ACWindowType").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                valueACref4 = Double.valueOf(dataSnapshot.getValue(double.class));
                                ACwindowTypePricetv.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", valueACref4) + "/pc(s)."));

                                TextWatcher textWatcher = new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        Editable editableValue = etACwindowTypePcs.getText();
                                        Editable etBrand = etbrandACwindow.getText();
                                        double value;
                                        if(editableValue != null && editableValue.length() >= 1){
                                            value = Double.parseDouble(editableValue.toString());
                                            price4 = valueACref4 * value;
                                            item4.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", price4)));
                                            item4.setVisibility(View.VISIBLE);
                                            itemTxt4.setText("AC Window Type/" + etACwindowTypePcs.getText().toString() + "pc(s).");
                                            totalAmount = price1 + price2 + price3 + price4 + price5;
                                            Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", totalAmount)));
                                            disablingNextBtn();
                                        }//end of editableValue
                                        if(etBrand != null && etBrand.length() >= 1){
                                            itemTxt4.setText("AC Window Type/" + "\n" + etACwindowTypePcs.getText().toString() + "pc(s)/"
                                                    + etbrandACwindow.getText().toString());
                                        }
                                    }
                                };
                                etACwindowTypePcs.addTextChangedListener(textWatcher);
                                etbrandACwindow.addTextChangedListener(textWatcher);
                            }else{
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }else if(!isChecked){
                    ACwindowTypePricetv.setText("--");
                    ACwindowBrandTv.setVisibility(View.GONE);
                    etbrandACwindow.setText("");
                    etACwindowTypePcs.setText("");
                    etbrandACwindow.setVisibility(View.GONE);
                    etACwindowTypePcs.setVisibility(View.GONE);
                    itemTxt4.setVisibility(View.GONE);
                    item4.setVisibility(View.GONE);
                    item4.setText("");
                    itemTxt4.setText("---");
                    price4 = 0;
                    item4.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + Double.toString(price4)));
                    totalAmount = price1 + price2 + price3 + price4;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" +Double.toString(totalAmount)));
                    disablingNextBtn();
                }
            }
        });
    }

    //method for house keeping task #9
    public void HouseKeepingTask(){

        houseKeepingRad.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkStructure) {
                //retriever of price in firebase
                Firebase.setAndroidContext(getApplicationContext());
                myFirebase = new Firebase(url);
                if(checkStructure==R.id.houseKeepingRad1){
                    myFirebase.child("Services").child("AdditionalPayment").child("HouseStructure").child("Condo").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                selectedType = houseKeepingRB1.getText().toString();
                                typeVal = Double.valueOf(dataSnapshot.getValue(double.class));
                                serviceType.setText("Condo: ");
                                svcCondition.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",typeVal)));
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }else{
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }else if(checkStructure==R.id.houseKeepingRad2){
                    myFirebase.child("Services").child("AdditionalPayment").child("HouseStructure").child("Storey").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                selectedType = houseKeepingRB2.getText().toString();
                                typeVal = Double.valueOf(dataSnapshot.getValue(double.class));
                                serviceType.setText("Apartment: ");
                                svcCondition.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",typeVal)));
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }else{
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }else if(checkStructure==R.id.houseKeepingRad3){
                    myFirebase.child("Services").child("AdditionalPayment").child("HouseStructure").child("Bungalow").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                selectedType = houseKeepingRB3.getText().toString();
                                typeVal = Double.valueOf(dataSnapshot.getValue(double.class));
                                serviceType.setText("Bungalow: ");
                                svcCondition.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",typeVal)));
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }else{
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
            }
        });
        //checkboxes for detailed task (house keeping)
        houseKeepingCheck1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){

                    Toast.makeText(TaskDetails.this, "You have selected: " + houseKeepingCheck1.getText().toString(), Toast.LENGTH_SHORT).show();
                    //retriever of price in firebase
                    Firebase.setAndroidContext(getApplicationContext());
                    myFirebase = new Firebase(url);
                    myFirebase.child("Services").child("HouseKeeping").child("HouseCleaning").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.exists()){
                                price1 = Double.valueOf(dataSnapshot.getValue(double.class));
                                itemTxt1.setText("House Cleaning: ");
                                item1.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price1)));
                                itemTxt1.setVisibility(View.VISIBLE);
                                item1.setVisibility(View.VISIBLE);
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }else{
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                        }
                    });
                }else if(!isChecked){

                    price1 = 0;
                    itemTxt1.setText("House Cleaning: ");
                    item1.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price1)));
                    itemTxt1.setVisibility(View.GONE);
                    item1.setVisibility(View.GONE);
                    totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                    disablingNextBtn();
                }

            }
        });
        houseKeepingCheck2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){

                    Toast.makeText(TaskDetails.this, "You have selected: " + houseKeepingCheck2.getText().toString(), Toast.LENGTH_SHORT).show();
                    //retriever of price in firebase
                    Firebase.setAndroidContext(getApplicationContext());
                    myFirebase = new Firebase(url);
                    myFirebase.child("Services").child("HouseKeeping").child("GrassGardenCleaning").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.exists()){
                                price2 = Double.valueOf(dataSnapshot.getValue(double.class));
                                itemTxt2.setText("Grass/Garden Cleaning: ");
                                item2.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price2)));
                                itemTxt2.setVisibility(View.VISIBLE);
                                item2.setVisibility(View.VISIBLE);
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }else{
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                        }
                    });

                }else if(!isChecked){

                    price2 = 0;
                    itemTxt2.setText("Grass/Garden Cleaning: ");
                    item2.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price2)));
                    itemTxt2.setVisibility(View.GONE);
                    item2.setVisibility(View.GONE);
                    totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                    disablingNextBtn();
                }

            }
        });
        houseKeepingCheck3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){

                    Toast.makeText(TaskDetails.this, "You have selected: " + houseKeepingCheck3.getText().toString(), Toast.LENGTH_SHORT).show();
                    //retriever of price in firebase
                    Firebase.setAndroidContext(getApplicationContext());
                    myFirebase = new Firebase(url);
                    myFirebase.child("Services").child("HouseKeeping").child("StorageCleaning").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.exists()){
                                price3 = Double.valueOf(dataSnapshot.getValue(double.class));
                                itemTxt3.setText("Storage Cleaning: ");
                                item3.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price3)));
                                itemTxt3.setVisibility(View.VISIBLE);
                                item3.setVisibility(View.VISIBLE);
                                totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }else{
                                Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                        }
                    });
                }else if(!isChecked){

                    price3 = 0;
                    itemTxt3.setText("Storage Cleaning: ");
                    item3.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price3)));
                    itemTxt3.setVisibility(View.GONE);
                    item3.setVisibility(View.GONE);
                    totalAmount = price1 + price2 + price3 + price4 + price5 + typeVal;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                    disablingNextBtn();
                }

            }
        });
    }
    //method for computer and laptop repair task #10
    public void ComputerLaptopRepair(){

        computerLaptopCheck1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    desktopCB1.setVisibility(View.VISIBLE);
                    desktopCB2.setVisibility(View.VISIBLE);
                    desktopCB3.setVisibility(View.VISIBLE);
                    desktopCB4.setVisibility(View.VISIBLE);

                    desktopCB1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                                Firebase.setAndroidContext(getApplicationContext());
                                myFirebase = new Firebase(url);
                                myFirebase.child("Services").child("ComputerLaptopRepair").child("MotherBoardDT").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            //firebase value retriever
                                            desktopVal = Double.valueOf(dataSnapshot.getValue(double.class));
                                            desktopP1 = desktopVal; //will pass this value along desktopCB1 value
                                            //will convert the desktopCB1 value + price
                                           /* desktopCB1.setText(desktopCB1.getText().toString());*/
                                            desktopMotherBoardPrice.setVisibility(View.VISIBLE);
                                            desktopMotherBoardPrice.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", desktopP1)));
                                            //will visible item and value for summary
                                            itemTxt1.setVisibility(View.VISIBLE);
                                            item1.setVisibility(View.VISIBLE);
                                            //price1 will be equal to all checkbox under desktop repair
                                            price1 = desktopP1 + desktopP2 + desktopP3 + desktopP4;
                                            itemTxt1.setText(computerLaptopCheck1.getText().toString()+":");
                                            item1.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price1)));
                                            totalAmount = price1 + price2 + price3;
                                            Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                            disablingNextBtn();
                                        }else{
                                            Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });
                            }else if(!isChecked){
                                desktopP1 = 0;
                                /*desktopCB1.setText(desktopCB1.getText().toString());*/
                                desktopMotherBoardPrice.setText(String.format("%,.02f", desktopP1));
                                desktopMotherBoardPrice.setVisibility(View.GONE);
                                price1 = desktopP1 + desktopP2 + desktopP3 + desktopP4;
                                itemTxt1.setText(computerLaptopCheck1.getText().toString()+":");
                                item1.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price1)));
                                totalAmount = price1 + price2 + price3;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }
                        }
                    });
                    desktopCB2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                                Firebase.setAndroidContext(getApplicationContext());
                                myFirebase = new Firebase(url);
                                myFirebase.child("Services").child("ComputerLaptopRepair").child("HardDriveDT").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            //firebase value retriever
                                            desktopVal2 = Double.valueOf(dataSnapshot.getValue(double.class));
                                            desktopP2 = desktopVal2; //will pass this value along desktopCB1 value
                                            //will convert the desktopCB1 value + price
                                            desktopHardDrivePrice.setVisibility(View.VISIBLE);
                                            desktopHardDrivePrice.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", desktopP2)));
                                            //will visible item and value for summary
                                            itemTxt1.setVisibility(View.VISIBLE);
                                            item1.setVisibility(View.VISIBLE);
                                            //price1 will be equal to all checkbox under desktop repair
                                            price1 = desktopP1 + desktopP2 + desktopP3 + desktopP4;
                                            itemTxt1.setText(computerLaptopCheck1.getText().toString()+":");
                                            item1.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price1)));
                                            totalAmount = price1 + price2 + price3;
                                            Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                            disablingNextBtn();
                                        }else{
                                            Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });
                            }else if(!isChecked){
                                desktopP2 = 0;
                                desktopHardDrivePrice.setText("");
                                desktopHardDrivePrice.setVisibility(View.GONE);
                                itemTxt1.setText(computerLaptopCheck1.getText().toString()+":");
                                price1 = desktopP1 + desktopP2 + desktopP3 + desktopP4;
                                item1.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price1)));
                                totalAmount = price1 + price2 + price3;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }
                        }
                    });
                    desktopCB3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                                Firebase.setAndroidContext(getApplicationContext());
                                myFirebase = new Firebase(url);
                                myFirebase.child("Services").child("ComputerLaptopRepair").child("PowerSupplyDT").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            desktopVal3 = Double.valueOf(dataSnapshot.getValue(double.class));
                                            desktopP3 = desktopVal3;
                                            desktopPowerSupplyPrice.setVisibility(View.VISIBLE);
                                            desktopPowerSupplyPrice.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", desktopP3)));
                                            itemTxt1.setVisibility(View.VISIBLE);
                                            item1.setVisibility(View.VISIBLE);
                                            price1 = desktopP1 + desktopP2 + desktopP3 + desktopP4;
                                            itemTxt1.setText(computerLaptopCheck1.getText().toString()+":");
                                            item1.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price1)));
                                            totalAmount = price1 + price2 + price3;
                                            Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                            disablingNextBtn();
                                        }else {
                                            Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });

                            }else if(!isChecked){
                                desktopP3 = 0;
                                desktopPowerSupplyPrice.setText("");
                                desktopPowerSupplyPrice.setVisibility(View.GONE);
                                itemTxt1.setText(computerLaptopCheck1.getText().toString()+":");
                                price1 = desktopP1 + desktopP2 + desktopP3 + desktopP4;
                                item1.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price1)));
                                totalAmount = price1 + price2 + price3;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }
                        }
                    });
                    desktopCB4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                                Firebase.setAndroidContext(getApplicationContext());
                                myFirebase = new Firebase(url);
                                myFirebase.child("Services").child("ComputerLaptopRepair").child("MonitorDT").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            desktopVal4 = Double.valueOf(dataSnapshot.getValue(double.class));
                                            desktopP4 = desktopVal4;
                                            desktopMonitorPrice.setVisibility(View.VISIBLE);
                                            desktopMonitorPrice.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", desktopP2)));
                                            itemTxt1.setVisibility(View.VISIBLE);
                                            item1.setVisibility(View.VISIBLE);
                                            price1 = desktopP1 + desktopP2 + desktopP3 + desktopP4;
                                            itemTxt1.setText(computerLaptopCheck1.getText().toString()+":");
                                            item1.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price1)));
                                            totalAmount = price1 + price2 + price3;
                                            Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                            disablingNextBtn();
                                        }else {
                                            Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();

                                        }
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });

                            }else if(!isChecked){
                                desktopP4 = 0;
                                desktopMonitorPrice.setText("");
                                desktopMonitorPrice.setVisibility(View.GONE);
                                itemTxt1.setText(computerLaptopCheck1.getText().toString()+":");
                                price1 = desktopP1 + desktopP2 + desktopP3 + desktopP4;
                                item1.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price1)));
                                totalAmount = price1 + price2 + price3;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }
                        }
                    });
                }else if(!isChecked){

                    desktopCB1.setVisibility(View.GONE);
                    desktopCB2.setVisibility(View.GONE);
                    desktopCB3.setVisibility(View.GONE);
                    desktopCB4.setVisibility(View.GONE);
                    desktopCB1.setChecked(false);
                    desktopCB2.setChecked(false);
                    desktopCB3.setChecked(false);
                    desktopCB4.setChecked(false);
                    desktopMotherBoardPrice.setText("");
                    desktopHardDrivePrice.setText("");
                    desktopPowerSupplyPrice.setText("");
                    desktopMonitorPrice.setText("");
                    desktopMotherBoardPrice.setVisibility(View.GONE);
                    desktopHardDrivePrice.setVisibility(View.GONE);
                    desktopPowerSupplyPrice.setVisibility(View.GONE);
                    desktopMonitorPrice.setVisibility(View.GONE);

                    //will gone textview if main checkbox is unchecked
                    itemTxt1.setVisibility(View.GONE);
                    item1.setVisibility(View.GONE);
                    //price1 will be equal to zero
                    price1 = 0;
                    itemTxt1.setText("---");
                    item1.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price1)));
                    totalAmount = price1 + price2 + price3;
                    Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                    disablingNextBtn();

                }
            }
        });
        computerLaptopCheck2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    laptopCB1.setVisibility(View.VISIBLE);
                    laptopCB2.setVisibility(View.VISIBLE);
                    laptopCB3.setVisibility(View.VISIBLE);
                    laptopCB4.setVisibility(View.VISIBLE);

                    laptopCB1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                                Firebase.setAndroidContext(getApplicationContext());
                                myFirebase = new Firebase(url);
                                myFirebase.child("Services").child("ComputerLaptopRepair").child("MotherBoardLT").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            laptopVal = Double.valueOf(dataSnapshot.getValue(double.class));
                                            laptopP1 = laptopVal;
                                            laptopMotherBoardPrice.setVisibility(View.VISIBLE);
                                            laptopMotherBoardPrice.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", laptopP1)));
                                            price2 = laptopP1 + laptopP2 + laptopP3 + laptopP4;
                                            itemTxt2.setText(computerLaptopCheck2.getText().toString()+":");
                                            itemTxt2.setVisibility(View.VISIBLE);
                                            item2.setVisibility(View.VISIBLE);
                                            item2.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price2)));
                                            totalAmount = price1 + price2 + price3;
                                            Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                            disablingNextBtn();

                                        }else {
                                            Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });
                            }else if(!isChecked){
                                laptopP1 = 0;
                                /*desktopCB1.setText(desktopCB1.getText().toString());*/
                                laptopMotherBoardPrice.setText(String.format("%,.02f", laptopP1));
                                laptopMotherBoardPrice.setVisibility(View.GONE);
                                price2 = laptopP1 + laptopP2 + laptopP3 + laptopP4;
                                itemTxt2.setText(computerLaptopCheck2.getText().toString()+":");
                                item2.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price2)));
                                totalAmount = price1 + price2 + price3;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }

                        }
                    });
                    laptopCB2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                                Firebase.setAndroidContext(getApplicationContext());
                                myFirebase = new Firebase(url);
                                myFirebase.child("Services").child("ComputerLaptopRepair").child("HardDriveLT").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            laptopVal2 = Double.valueOf(dataSnapshot.getValue(double.class));
                                            laptopP2 = laptopVal2;
                                            laptopHDPrice.setVisibility(View.VISIBLE);
                                            laptopHDPrice.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", laptopP2)));
                                            price2 = laptopP1 + laptopP2 + laptopP3 + laptopP4;
                                            itemTxt2.setText(computerLaptopCheck2.getText().toString()+":");
                                            itemTxt2.setVisibility(View.VISIBLE);
                                            item2.setVisibility(View.VISIBLE);
                                            item2.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price2)));
                                            totalAmount = price1 + price2 + price3;
                                            Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                            disablingNextBtn();
                                        }else {
                                            Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });

                            }else if(!isChecked){
                                laptopP2 = 0;
                                laptopHDPrice.setText(String.format("%,.02f", laptopP2));
                                laptopHDPrice.setVisibility(View.GONE);
                                price2 = laptopP1 + laptopP2 + laptopP3 + laptopP4;
                                itemTxt2.setText(computerLaptopCheck2.getText().toString()+":");
                                item2.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price2)));
                                totalAmount = price1 + price2 + price3;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }
                        }
                    });
                    laptopCB3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                                Firebase.setAndroidContext(getApplicationContext());
                                myFirebase = new Firebase(url);
                                myFirebase.child("Services").child("ComputerLaptopRepair").child("PowerSupplyLT").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            laptopVal3 = Double.valueOf(dataSnapshot.getValue(double.class));
                                            laptopP3 = laptopVal3;
                                            laptopPowerSupplyPrice.setVisibility(View.VISIBLE);
                                            laptopPowerSupplyPrice.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", laptopP3)));
                                            price2 = laptopP1 + laptopP2 + laptopP3 + laptopP4;
                                            itemTxt2.setText(computerLaptopCheck2.getText().toString()+":");
                                            itemTxt2.setVisibility(View.VISIBLE);
                                            item2.setVisibility(View.VISIBLE);
                                            item2.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price2)));
                                            totalAmount = price1 + price2 + price3;
                                            Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                            disablingNextBtn();
                                        }else {
                                            Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });
                            }else if(!isChecked){
                                laptopP3 = 0;
                                laptopPowerSupplyPrice.setText(String.format("%,.02f", laptopP3));
                                laptopPowerSupplyPrice.setVisibility(View.GONE);
                                price2 = laptopP1 + laptopP2 + laptopP3 + laptopP4;
                                itemTxt2.setText(computerLaptopCheck2.getText().toString()+":");
                                item2.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price2)));
                                totalAmount = price1 + price2 + price3;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }
                        }
                    });
                    laptopCB4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                                Firebase.setAndroidContext(getApplicationContext());
                                myFirebase = new Firebase(url);
                                myFirebase.child("Services").child("ComputerLaptopRepair").child("MonitorLT").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            laptopVal4 = Double.valueOf(dataSnapshot.getValue(double.class));
                                            laptopP4 = laptopVal4;
                                            laptopMonitorPrice.setVisibility(View.VISIBLE);
                                            laptopMonitorPrice.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", laptopP4)));
                                            price2 = laptopP1 + laptopP2 + laptopP3 + laptopP4;
                                            itemTxt2.setVisibility(View.VISIBLE);
                                            item2.setVisibility(View.VISIBLE);
                                            itemTxt2.setText(computerLaptopCheck2.getText().toString()+":");
                                            item2.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price2)));
                                            totalAmount = price1 + price2 + price3;
                                            Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                            disablingNextBtn();
                                        }else {
                                            Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });
                            }else if(!isChecked){
                                laptopP4 = 0;
                                laptopMonitorPrice.setText(String.format("%,.02f", laptopP4));
                                laptopMonitorPrice.setVisibility(View.GONE);
                                price2 = laptopP1 + laptopP2 + laptopP3 + laptopP4;
                                itemTxt2.setText(computerLaptopCheck2.getText().toString()+":");
                                item2.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price2)));
                                totalAmount = price1 + price2 + price3;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }
                        }
                    });
                }else if(!isChecked){

                    laptopCB1.setVisibility(View.GONE);
                    laptopCB2.setVisibility(View.GONE);
                    laptopCB3.setVisibility(View.GONE);
                    laptopCB4.setVisibility(View.GONE);
                    laptopCB1.setChecked(false);
                    laptopCB2.setChecked(false);
                    laptopCB3.setChecked(false);
                    laptopCB4.setChecked(false);
                    laptopMotherBoardPrice.setVisibility(View.GONE);
                    laptopHDPrice.setVisibility(View.GONE);
                    laptopPowerSupplyPrice.setVisibility(View.GONE);
                    laptopMonitorPrice.setVisibility(View.GONE);
                    laptopMotherBoardPrice.setText("--");
                    laptopHDPrice.setText("--");
                    laptopPowerSupplyPrice.setText("--");
                    laptopMonitorPrice.setText("--");
                    itemTxt2.setVisibility(View.GONE);
                    item2.setVisibility(View.GONE);
                    itemTxt2.setText("");
                    item2.setText("");

                }
            }
        });
        computerLaptopCheck3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    softwareCB1.setVisibility(View.VISIBLE);
                    softwareCB2.setVisibility(View.VISIBLE);
                    softwareCB3.setVisibility(View.VISIBLE);
                    softwareCB4.setVisibility(View.VISIBLE);

                    softwareCB1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                                Firebase.setAndroidContext(getApplicationContext());
                                myFirebase = new Firebase(url);
                                myFirebase.child("Services").child("ComputerLaptopRepair").child("OperatingSystem").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            softwareVal = Double.valueOf(dataSnapshot.getValue(double.class));
                                            softP1 = softwareVal;
                                            SWoperatingSystemPrice.setVisibility(View.VISIBLE);
                                            SWoperatingSystemPrice.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", softP1)));
                                            price3 = softP1 + softP2 + softP3 + softP4;
                                            itemTxt3.setVisibility(View.VISIBLE);
                                            item3.setVisibility(View.VISIBLE);
                                            itemTxt3.setText(computerLaptopCheck3.getText().toString()+":");
                                            item3.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price3)));
                                            totalAmount = price1 + price2 + price3;
                                            Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                            disablingNextBtn();
                                        }else {
                                            Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });
                            }else if(!isChecked){
                                softP1 = 0;
                                SWoperatingSystemPrice.setText(String.format("%,.02f", softP1));
                                SWoperatingSystemPrice.setVisibility(View.GONE);
                                price3 = softP1 + softP2 + softP3 + softP4;
                                itemTxt3.setText(computerLaptopCheck3.getText().toString()+":");
                                item3.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price3)));
                                totalAmount = price1 + price2 + price3;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }
                        }
                    });
                    softwareCB2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                                Firebase.setAndroidContext(getApplicationContext());
                                myFirebase = new Firebase(url);
                                myFirebase.child("Services").child("ComputerLaptopRepair").child("Reformat").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            softwareVal2 = Double.valueOf(dataSnapshot.getValue(double.class));
                                            softP2 = softwareVal2;
                                            SWreformatPrice.setVisibility(View.VISIBLE);
                                            SWreformatPrice.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", softP2)));
                                            price3 = softP1 + softP2 + softP3 + softP4;
                                            itemTxt3.setVisibility(View.VISIBLE);
                                            item3.setVisibility(View.VISIBLE);
                                            itemTxt3.setText(computerLaptopCheck3.getText().toString()+":");
                                            item3.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price3)));
                                            totalAmount = price1 + price2 + price3;
                                            Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                            disablingNextBtn();
                                        }else{
                                            Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });
                            }else if(!isChecked){
                                softP2 = 0;
                                SWreformatPrice.setText(String.format("%,.02f", softP2));
                                SWreformatPrice.setVisibility(View.GONE);
                                price3 = softP1 + softP2 + softP3 + softP4;
                                itemTxt3.setText(computerLaptopCheck3.getText().toString()+":");
                                item3.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price3)));
                                totalAmount = price1 + price2 + price3;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }
                        }
                    });
                    softwareCB3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                                Firebase.setAndroidContext(getApplicationContext());
                                myFirebase = new Firebase(url);
                                myFirebase.child("Services").child("ComputerLaptopRepair").child("FileRetrieval").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            softwareVal3 = Double.valueOf(dataSnapshot.getValue(double.class));
                                            softP3 = softwareVal3;
                                            SWstorageFileRetPrice.setVisibility(View.VISIBLE);
                                            SWstorageFileRetPrice.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", softP3)));
                                            price3 = softP1 + softP2 + softP3 + softP4;
                                            itemTxt3.setVisibility(View.VISIBLE);
                                            item3.setVisibility(View.VISIBLE);
                                            itemTxt3.setText(computerLaptopCheck3.getText().toString()+":");
                                            item3.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price3)));
                                            totalAmount = price1 + price2 + price3;
                                            Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                            disablingNextBtn();
                                        }else {
                                            Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });
                            }else if(!isChecked){
                                softP3 = 0;
                                SWstorageFileRetPrice.setText(String.format("%,.02f", softP3));
                                SWstorageFileRetPrice.setVisibility(View.GONE);
                                price3 = softP1 + softP2 + softP3 + softP4;
                                itemTxt3.setText(computerLaptopCheck3.getText().toString()+":");
                                item3.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price3)));
                                totalAmount = price1 + price2 + price3;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }
                        }
                    });
                    softwareCB4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                                Firebase.setAndroidContext(getApplicationContext());
                                myFirebase = new Firebase(url);
                                myFirebase.child("Services").child("ComputerLaptopRepair").child("RAMProblem").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            softwareVal4 = Double.valueOf(dataSnapshot.getValue(double.class));
                                            softP4 = softwareVal4;
                                            SWramProbPrice.setVisibility(View.VISIBLE);
                                            SWramProbPrice.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f", softP4)));
                                            price3 = softP1 + softP2 + softP3 + softP4;
                                            itemTxt3.setVisibility(View.VISIBLE);
                                            item3.setVisibility(View.VISIBLE);
                                            itemTxt3.setText(computerLaptopCheck3.getText().toString()+":");
                                            item3.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price3)));
                                            totalAmount = price1 + price2 + price3;
                                            Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                            disablingNextBtn();
                                        }else {
                                            Toast.makeText(TaskDetails.this, "System Error. Please try again. ", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });
                            }else if(!isChecked){
                                softP4 = 0;
                                SWramProbPrice.setText(String.format("%,.02f", softP4));
                                SWramProbPrice.setVisibility(View.GONE);
                                price3 = softP1 + softP2 + softP3 + softP4;
                                itemTxt3.setText(computerLaptopCheck2.getText().toString()+":");
                                item3.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",price3)));
                                totalAmount = price1 + price2 + price3;
                                Val.setText(Html.fromHtml("<small><font color=\"red\">P</font></small>" + String.format("%,.02f",totalAmount)));
                                disablingNextBtn();
                            }
                        }
                    });
                }else if(!isChecked){

                    softwareCB1.setVisibility(View.GONE);
                    softwareCB2.setVisibility(View.GONE);
                    softwareCB3.setVisibility(View.GONE);
                    softwareCB4.setVisibility(View.GONE);
                    softwareCB1.setChecked(false);
                    softwareCB2.setChecked(false);
                    softwareCB3.setChecked(false);
                    softwareCB4.setChecked(false);
                    SWoperatingSystemPrice.setVisibility(View.GONE);
                    SWreformatPrice.setVisibility(View.GONE);
                    SWstorageFileRetPrice.setVisibility(View.GONE);
                    SWramProbPrice.setVisibility(View.GONE);
                    SWoperatingSystemPrice.setText("--");
                    SWreformatPrice.setText("--");
                    SWstorageFileRetPrice.setText("--");
                    SWramProbPrice.setText("--");
                    itemTxt3.setVisibility(View.GONE);
                    item3.setVisibility(View.GONE);
                    itemTxt3.setText("");
                    item3.setText("");

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {

        if(itemTxt1!=null || item1 != null){
            itemTxt1.setText("");
            item1.setText("");
        }
        if(itemTxt2!=null){
            itemTxt2.setText("");
            item2.setText("");

        }
        if(itemTxt3!=null){
            itemTxt3.setText("");
            item3.setText("");

        }
        if(itemTxt4!=null){
            itemTxt4.setText("");
            item4.setText("");

        }
        if(itemTxt5!=null){
            itemTxt5.setText("");
            item5.setText("");

        }

        super.onResume();
    }

    @Override
    protected void onStart() {

        if(itemTxt1!=null || item1 != null){
            itemTxt1.setText("");
            item1.setText("");
        }
        if(itemTxt2!=null){
            itemTxt2.setText("");
            item2.setText("");

        }
        if(itemTxt3!=null){
            itemTxt3.setText("");
            item3.setText("");

        }
        if(itemTxt4!=null){
            itemTxt4.setText("");
            item4.setText("");

        }
        if(itemTxt5!=null){
            itemTxt5.setText("");
            item5.setText("");

        }

        super.onStart();
    }
}


