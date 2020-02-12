package com.tahmidu.room_designer_client_android.model;

import androidx.databinding.Bindable;
import androidx.databinding.Observable;

public class SignUp implements Observable
{
    private String firstName;
    private String lastName;
    private String password;
    private String rePassword;
    private String email;
    private String phoneNum;

    public SignUp() {
    }

    public SignUp(String firstName, String lastName, String password, String rePassword,
                  String email, String phoneNum) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.rePassword = rePassword;
        this.email = email;
        this.phoneNum = phoneNum;
    }

    @Bindable
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Bindable
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Bindable
    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    @Bindable
    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Bindable
    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {

    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {

    }
}
