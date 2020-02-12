package com.tahmidu.room_designer_client_android.viewModel;

import androidx.lifecycle.MutableLiveData;

public interface IWelcomeViewModel
{
    void Login(String email, String password);
    void navigateFragment(int id);
    void goToVerify();
    void goToSignUp();
    void goToForgotPassword();
}
