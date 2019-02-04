package com.alliancecode.fixit.Model;


public class UserProvider {

    private String email, password, name, address, age, phone, serviceType;

    public UserProvider (){

    }

    public UserProvider(String email, String pasword, String name, String address, String age, String phone, String serviceType){

        this.email = email;
        this.password = pasword;
        this.name = name;
        this.address = address;
        this.age = age;
        this.phone = phone;
        this.serviceType = serviceType;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}

