package com.alliancecode.fixit.Model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alliancecode.fixit.R;

import java.util.List;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolders> {

    private List<HistoryObject> itemList;   //list that will holds the items
    private Context context;

        //populate the Adapter, and passing the List items and the Context////////////////////////////////
    public HistoryAdapter(List<HistoryObject> itemList, Context context ){
            this.itemList = itemList;
            this.context = context;

    }

        //pass the layout that will be inflated, that will receive all the information/////////////////////////////
    @Override
    public HistoryViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_items, null, false);

        //creating and setting the layout Params to fit the whole screen
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //getting the LayoutParams
        layoutView.setLayoutParams(lp);

        //creating an instance of HistoryViewHolders
        HistoryViewHolders rvh = new HistoryViewHolders(layoutView);
        return rvh;

    }

    //populating each card TextView items//////////////////////////////
    @Override
    public void onBindViewHolder(HistoryViewHolders holder, int position) {

        holder.serviceId.setText(itemList.get(position).getServiceId());                          //setting equivalent valueText
        holder.dateTime.setText(itemList.get(position).getDateTime());


    }

    @Override
    public int getItemCount() {
      //  return 100;
        return this.itemList.size();   //to display the actual items in the recyclerView, important!
    }
}
