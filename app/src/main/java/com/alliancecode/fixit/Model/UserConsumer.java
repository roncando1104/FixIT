package com.alliancecode.fixit.Model;


public class UserConsumer {

    private String email,password,name,phone, profileImageUrl, profileImage;
    private int cancelCount;

    public UserConsumer(){

    }

    public UserConsumer(String email, String password, String name, String phone, String profileImageUrl){
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.profileImage = profileImageUrl;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

     public void profileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public int getCancelCount() {
        return cancelCount;
    }
    public int setCancelCount(Integer cancelCount) {
        this.cancelCount = cancelCount;
        return cancelCount;
    }
}
