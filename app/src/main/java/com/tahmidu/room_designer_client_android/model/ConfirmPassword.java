package com.tahmidu.room_designer_client_android.model;

import androidx.databinding.Bindable;
import androidx.databinding.Observable;

public class ConfirmPassword implements Observable
{
    private String password;
    private String reEnterPassword;

    public ConfirmPassword() {
    }

    public ConfirmPassword(String password, String reEnterPassword) {
        this.password = password;
        this.reEnterPassword = reEnterPassword;
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Bindable
    public String getReEnterPassword() {
        return reEnterPassword;
    }

    public void setReEnterPassword(String reEnterPassword) {
        this.reEnterPassword = reEnterPassword;
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {

    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {

    }
}
