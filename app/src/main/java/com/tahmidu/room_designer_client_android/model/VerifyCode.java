package com.tahmidu.room_designer_client_android.model;

import androidx.databinding.Bindable;
import androidx.databinding.Observable;

public class VerifyCode implements Observable
{
    private String verifyCode;

    public VerifyCode() {
    }

    public VerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    @Bindable
    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {

    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {

    }
}
