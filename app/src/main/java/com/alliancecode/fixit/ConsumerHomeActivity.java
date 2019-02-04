package com.alliancecode.fixit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.widget.ImageView;


import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;

import java.util.ArrayList;
import java.util.List;

public class ConsumerHomeActivity extends AppCompatActivity {


    private ImageView task1, task2, task3, task4, task5, task6, task7, task8, task9, task10;
  //  private Button tab1, tab2, tab3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer_home);

        task1 = (ImageView) findViewById(R.id.carpenterBtn);
        task2 = (ImageView) findViewById(R.id.plumberBtn);
        task3 = (ImageView)findViewById(R.id.masonBtn);
        task4 = (ImageView)findViewById(R.id.electricianBtn);
        task5 = (ImageView)findViewById(R.id.laundryBtn);
        task6 = (ImageView)findViewById(R.id.painterBtn);
        task7 = (ImageView)findViewById(R.id.applianceBtn);
        task8 = (ImageView)findViewById(R.id.airconAndRefBtn);
        task9 = (ImageView)findViewById(R.id.houseKeepingBtn);
        task10 = (ImageView)findViewById(R.id.computerAndLaptopBtn);


        HorizontalStepView horizontalStepView = (HorizontalStepView)findViewById(R.id.horizontalStepView);

        // Progress Tracker Sources

        List<StepBean> sources = new ArrayList<>();
        sources.add(new StepBean("Service",0));
        sources.add(new StepBean("Details",-1));
        sources.add(new StepBean("Summary",-1));

        horizontalStepView.setStepViewTexts(sources)
                .setTextSize(9)
                .setStepsViewIndicatorCompletedLineColor(R.color.colorMaroon)
                .setStepViewComplectedTextColor(R.color.colorOrangeFixit)
                .setStepsViewIndicatorUnCompletedLineColor(R.color.lightblack)
                .setStepViewUnComplectedTextColor(R.color.lightblack)
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(this,R.drawable.attention))
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(this,R.drawable.complted))
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(this,R.drawable.step_icon));


        task1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent next = new Intent(ConsumerHomeActivity.this, TaskDetails.class);
                Bundle carpentryTask = new Bundle();
                carpentryTask.putInt("TASK", 1);
                next.putExtras(carpentryTask);
                startActivity(next);
                finish();

            }
        });
        task2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent next = new Intent(ConsumerHomeActivity.this, TaskDetails.class);
                Bundle plumbingTask = new Bundle();
                plumbingTask.putInt("TASK", 2);
                next.putExtras(plumbingTask);
                startActivity(next);
                finish();

            }
        });
        task3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent next = new Intent(ConsumerHomeActivity.this, TaskDetails.class);
                Bundle masonTask = new Bundle();
                masonTask.putInt("TASK", 3);
                next.putExtras(masonTask);
                startActivity(next);
                finish();

            }
        });
        task4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent next = new Intent(ConsumerHomeActivity.this, TaskDetails.class);
                Bundle elecTask = new Bundle();
                elecTask.putInt("TASK", 4);
                next.putExtras(elecTask);
                startActivity(next);
                finish();
            }
        });
        task5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent next = new Intent(ConsumerHomeActivity.this, TaskDetails.class);
                Bundle laundryTask = new Bundle();
                laundryTask.putInt("TASK", 5);
                next.putExtras(laundryTask);
                startActivity(next);
                finish();
            }
        });
        task6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent next = new Intent(ConsumerHomeActivity.this, TaskDetails.class);
                Bundle painterTask = new Bundle();
                painterTask.putInt("TASK", 6);
                next.putExtras(painterTask);
                startActivity(next);
                finish();
            }
        });
        task7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent next = new Intent(ConsumerHomeActivity.this, TaskDetails.class);
                Bundle applianceTask = new Bundle();
                applianceTask.putInt("TASK", 7);
                next.putExtras(applianceTask);
                startActivity(next);
                finish();
            }
        });
        task8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent next = new Intent(ConsumerHomeActivity.this, TaskDetails.class);
                Bundle airAndRefTask = new Bundle();
                airAndRefTask.putInt("TASK", 8);
                next.putExtras(airAndRefTask);
                startActivity(next);
                finish();
            }
        });
        task9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent next = new Intent(ConsumerHomeActivity.this, TaskDetails.class);
                Bundle houseKeepingTask = new Bundle();
                houseKeepingTask.putInt("TASK", 9);
                next.putExtras(houseKeepingTask);
                startActivity(next);
                finish();
            }
        });
        task10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent next = new Intent(ConsumerHomeActivity.this, TaskDetails.class);
                Bundle comAndLaptopTask = new Bundle();
                comAndLaptopTask.putInt("TASK", 10);
                next.putExtras(comAndLaptopTask);
                startActivity(next);
                finish();
            }
        });






    }

    @Override
    protected void onStop() {
        super.onStop();
        //      if (!isLoggingOut) {

        //     }

    }
    //////////////////////////////////////////////onStart
    @Override
    protected void onStart() {
        super.onStart();

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ConsumerHomeActivity.this, WelcomeMain.class);
        startActivity(intent);
        super.onBackPressed();
    }

}
