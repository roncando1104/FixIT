package com.alliancecode.fixit.Model;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alliancecode.fixit.R;

import java.util.List;


public class PendingAdapterProviders extends RecyclerView.Adapter<PendingViewHoldersProviders> {

    private List<PendingObjectProviders> itemList;   //list that will holds the items
    private Context context;


    //populate the Adapter, and passing the List items and the Context////////////////////////////////
    public PendingAdapterProviders(List<PendingObjectProviders> itemList, Context context ){
            this.itemList = itemList;
            this.context = context;

    }

        //pass the layout that will be inflated, that will receive all the information/////////////////////////////
    @Override
    public PendingViewHoldersProviders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_items_providers, null, false);

        //creating and setting the layout Params to fit the whole screen
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //getting the LayoutParams
        layoutView.setLayoutParams(lp);

        //creating an instance of HistoryViewHolders
        PendingViewHoldersProviders rvh = new PendingViewHoldersProviders(layoutView);
        return rvh;

    }

    //populating each card TextView items//////////////////////////////
    @Override
    public void onBindViewHolder(PendingViewHoldersProviders holder, int position) {

     //   dateRequest.setText(Html.fromHtml(redString));
      //  dateRequest = "Date Request: ";
     //  Spannable spannable = new SpannableString(dateRequest);
     //  spannable.setSpan(new ForegroundColorSpan(Color.WHITE));
     //  spannable.setSpan(new ForegroundColorSpan(Color.RED), dateRequest.length(), dateRequest.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      //  dateRequest = getClass().getString(R.string.dateRequest);

    //  holder.dateSched.setText("Date Request: "+itemList.get(position).getDateSched());
     //   holder.dateSched.setText(spannable+itemList.get(position).getDateSched(), TextView.BufferType.SPANNABLE);
     //   holder.dateSched.setText((Html.fromHtml(dateRequest))+itemList.get(position).getConsumerServiceRequest());                          //setting equivalent valueText
       // holder.dateSched.setTextColor("Date Request: "+itemList.get(position).getConsumerServiceRequest());                          //setting equivalent valueText

        holder.dateSched.setText(itemList.get(position).getDateSched());
        holder.consumerServiceRequest.setText(itemList.get(position).getConsumerServiceRequest());                          //setting equivalent valueText
        holder.consumerName.setText(itemList.get(position).getConsumerName());
        holder.consumerPhone.setText(itemList.get(position).getConsumerPhone());
        holder.consumerNote.setText(itemList.get(position).getConsumerNote());
        holder.houseLocation.setText(itemList.get(position).getHouseLocation());
        holder.timeRequest.setText(itemList.get(position).getTimeRequest());
        holder.payment.setText(itemList.get(position).getPayment());



    }

    @Override
    public int getItemCount() {
      //  return 100;
        return this.itemList.size();   //to display the actual items in the recyclerView, important!
    }


}



