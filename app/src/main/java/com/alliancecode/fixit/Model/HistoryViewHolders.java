package com.alliancecode.fixit.Model;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alliancecode.fixit.ConsumerMapActivity;
import com.alliancecode.fixit.HistorySingleActivity;
import com.alliancecode.fixit.R;

import org.w3c.dom.Text;

public class HistoryViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {



    TextView serviceId;
    TextView dateTime;

    public HistoryViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        serviceId = (TextView) itemView.findViewById(R.id.serviceId);
        dateTime = (TextView) itemView.findViewById(R.id.dateTime);



    }


    //view of single history
    @Override
    public void onClick(View v) {    // v represent every card in the recyclerView
        Intent intent = new Intent(v.getContext(), HistorySingleActivity.class);     //getting the context of every card
  //     Intent intent2 = new Intent(v.getContext(), ConsumerMapActivity.class);
        Bundle b = new Bundle();                                         //passing the serviceId or the Information of every card into the Bundle, Bundle allows to pass multiple pieces of data Between the Activities
        b.putString("serviceId", serviceId.getText().toString());                  //Bundle putting the data into String
        intent.putExtras(b);
  //      intent2.putExtras(b);
        v.getContext().startActivity(intent);
    }



}




