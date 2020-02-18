package com.tahmidu.room_designer_client_android.repository;

import androidx.lifecycle.MutableLiveData;

import com.tahmidu.room_designer_client_android.preferences.PreferenceProvider;

public interface ILoginRepo
{
    void retrieveToken(String email, String password, final MutableLiveData<String> token,
                       final PreferenceProvider preferenceProvider);
    void authUser(final String email, final String password,
                  final MutableLiveData<String> signUpResponse,
                  final MutableLiveData<Boolean> progressVisibility,
                  final MutableLiveData<Integer> navFragment,
                  final MutableLiveData<String> token,
                  final PreferenceProvider preferenceProvider);
}
