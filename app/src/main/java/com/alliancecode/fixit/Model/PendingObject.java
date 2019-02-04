package com.alliancecode.fixit.Model;

public class PendingObject {
    private String consumerId, consumerName, consumerPhone, consumerServiceRequest, houseLocation, dateSched, timeRequest, payment, consumerNote,   providerId, providerName, providerAge,  providerPhone;



    public PendingObject(String dateSched, String timeRequest, String consumerServiceRequest, String providerName, String providerPhone, String providerAge, String payment, String consumerNote){
        this.consumerServiceRequest = consumerServiceRequest;
     //   this.consumerId = consumerId;
      //  this.consumerName = consumerName;
     //   this.consumerPhone = consumerPhone;
     //   this.houseLocation = houseLocation;
        this.dateSched = dateSched;
        this.timeRequest = timeRequest;
        this.payment = payment;
        this.consumerNote = consumerNote;
     //   this.providerId = providerId;
        this.providerName = providerName;
        this.providerAge = providerAge;
        this.providerPhone = providerPhone;

    }

        public String getConsumerServiceRequest(){
        return consumerServiceRequest;
    }
    public void setConsumerServiceRequest(String consumerServiceRequest){
        this.consumerServiceRequest = consumerServiceRequest;
    }


    public String getConsumerId(){
        return consumerId;
    }
    public void setConsumerId(String consumerId){
        this.consumerId = consumerId;
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


    public String getProviderId(){
        return providerId;
    }
    public void setProviderId(String providerId){
        this.providerId = providerId;
    }


    public String getProviderName(){
        return providerName;
    }
    public void setProviderName(String providerName){
        this.providerName = providerName;
    }

    public String getProviderAge(){
        return providerAge;
    }
    public void setProviderAge(String providerAge){
        this.providerAge = providerAge;
    }



    public String getProviderPhone(){
        return providerPhone;
    }
    public void setProviderPhone(String providerPhone){
        this.providerPhone = providerPhone;
    }






}
