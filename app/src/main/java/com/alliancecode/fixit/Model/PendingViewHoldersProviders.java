package com.alliancecode.fixit.Model;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alliancecode.fixit.R;

//will not use onClickListener for the meantime
//public class PendingViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {
    public class PendingViewHoldersProviders extends RecyclerView.ViewHolder{


    TextView  consumerName, consumerPhone, consumerServiceRequest, houseLocation, dateSched, timeRequest, payment, consumerNote;


    public PendingViewHoldersProviders(View itemView) {
        super(itemView);
     //   itemView.setOnClickListener(this);

        consumerName = (TextView) itemView.findViewById(R.id.consumerName);
        consumerPhone = (TextView) itemView.findViewById(R.id.consumerPhone);
        consumerServiceRequest = (TextView) itemView.findViewById(R.id.consumerServiceRequest);
        houseLocation = (TextView) itemView.findViewById(R.id.houseLocation);
        dateSched = (TextView) itemView.findViewById(R.id.dateSched);
        timeRequest = (TextView) itemView.findViewById(R.id.timeRequest);
        payment = (TextView) itemView.findViewById(R.id.payment);
        consumerNote = (TextView) itemView.findViewById(R.id.consumerNote);

    }

/*   will not use for the meantime
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
*/


}




