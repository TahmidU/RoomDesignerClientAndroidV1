package com.tahmidu.room_designer_client_android.userAuthentication.login;

import androidx.lifecycle.MutableLiveData;

public interface ILoginRepo
{
    MutableLiveData<String> retrieveToken(String email, String password);
}
