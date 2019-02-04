package com.alliancecode.fixit.Model;

public class HistoryObject {
    private String serviceId;
    private String dateTime;


    public HistoryObject(String serviceId, String dateTime){
        this.serviceId = serviceId;
        this.dateTime = dateTime;

    }

    public String getServiceId(){
        return serviceId;
    }
    public void setServiceId(String serviceId){
        this.serviceId = serviceId;
    }


    public String getDateTime(){
        return dateTime;
    }
    public void setDateTime(String dateTime){
        this.dateTime = dateTime;
    }
}
