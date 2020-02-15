package com.tahmidu.room_designer_client_android.viewModel;

import androidx.lifecycle.MutableLiveData;

public interface IWelcomeViewModel
{
    void authenticateUser(String email, String password);
    void login(String email, String password);
    void signUp(String firstName, String lastName, String password, String rePassword, String email,
                String phoneNum);
    void forgotPassword(String email);
    void navigateFragment(int id);
}
