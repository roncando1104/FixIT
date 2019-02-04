package com.alliancecode.fixit.Model;

public class PendingObjectProviders {
    private String consumerName, consumerPhone, consumerServiceRequest, houseLocation, dateSched, timeRequest, payment, consumerNote;



    public PendingObjectProviders(String dateSched, String timeRequest, String consumerServiceRequest,  String consumerName, String consumerPhone, String houseLocation, String payment, String consumerNote){
        this.consumerServiceRequest = consumerServiceRequest;
        this.consumerName = consumerName;
        this.consumerPhone = consumerPhone;
        this.houseLocation = houseLocation;
        this.dateSched = dateSched;
        this.timeRequest = timeRequest;
        this.payment = payment;
        this.consumerNote = consumerNote;


    }

        public String getConsumerServiceRequest(){
        return consumerServiceRequest;
    }
    public void setConsumerServiceRequest(String consumerServiceRequest){
        this.consumerServiceRequest = consumerServiceRequest;
    }


    public String getConsumerName(){
        return consumerName;
    }
    public void setConsumerName(String consumerName){
        this.consumerName = consumerName;
    }


    public String getConsumerPhone(){
        return consumerPhone;
    }
    public void setConsumerPhone(String consumerPhone){
        this.consumerPhone = consumerPhone;
    }


    public String getHouseLocation(){
        return houseLocation;
    }
    public void setHouseLocation(String houseLocation){
        this.houseLocation = houseLocation;
    }


    public String getDateSched(){
        return dateSched;
    }
    public void setDateSched(String dateSched){
        this.dateSched = dateSched;
    }


    public String getTimeRequest(){
        return timeRequest;
    }
    public void setTimeRequest(String timeRequest){
        this.timeRequest = timeRequest;
    }


    public String getPayment(){
        return payment;
    }
    public void setPayment(String payment){
        this.payment = payment;
    }


    public String getConsumerNote(){
        return consumerNote;
    }
    public void setConsumerNote(String consumerNote){
        this.consumerNote = consumerNote;
    }








}
