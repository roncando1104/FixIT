package com.alliancecode.fixit.Model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alliancecode.fixit.R;

import java.util.List;


public class PendingAdapter extends RecyclerView.Adapter<PendingViewHolders> {

    private List<PendingObject> itemList;   //list that will holds the items
    private Context context;

        //populate the Adapter, and passing the List items and the Context////////////////////////////////
    public PendingAdapter(List<PendingObject> itemList, Context context ){
            this.itemList = itemList;
            this.context = context;

    }

        //pass the layout that will be inflated, that will receive all the information/////////////////////////////
    @Override
    public PendingViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_items, null, false);

        //creating and setting the layout Params to fit the whole screen
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //getting the LayoutParams
        layoutView.setLayoutParams(lp);

        //creating an instance of HistoryViewHolders
        PendingViewHolders rvh = new PendingViewHolders(layoutView);
        return rvh;

    }

    //populating each card TextView items//////////////////////////////
    @Override
    public void onBindViewHolder(PendingViewHolders holder, int position) {

        holder.consumerServiceRequest.setText(itemList.get(position).getConsumerServiceRequest());                          //setting equivalent valueText
        holder.consumerNote.setText(itemList.get(position).getConsumerNote());
        holder.dateSched.setText(itemList.get(position).getDateSched());
        holder.timeRequest.setText(itemList.get(position).getTimeRequest());
        holder.payment.setText(itemList.get(position).getPayment());
        holder.providerName.setText(itemList.get(position).getProviderName());
        holder.providerAge.setText(itemList.get(position).getProviderAge());
        holder.providerPhone.setText(itemList.get(position).getProviderPhone());


    }

    @Override
    public int getItemCount() {
      //  return 100;
        return this.itemList.size();   //to display the actual items in the recyclerView, important!
    }
}



