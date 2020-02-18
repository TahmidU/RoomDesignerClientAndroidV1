package com.tahmidu.room_designer_client_android.repository;

import androidx.lifecycle.MutableLiveData;

public interface IPasswordRepo
{
    void sendToken(String email);
    void changePassword(String email, int token, String password,
                        final MutableLiveData<String> passwordVerifyResponse);
}
