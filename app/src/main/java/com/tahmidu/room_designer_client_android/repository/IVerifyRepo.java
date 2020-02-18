package com.tahmidu.room_designer_client_android.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

public interface IVerifyRepo
{
    void verify(Context context, String code, final MutableLiveData<String> verifyResponse);
    void resendToken(Context context);
}
