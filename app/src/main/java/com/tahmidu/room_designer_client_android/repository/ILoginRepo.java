package com.tahmidu.room_designer_client_android.repository;

import androidx.lifecycle.MutableLiveData;

public interface ILoginRepo
{
    void retrieveToken(String email, String password);
}
