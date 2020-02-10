package com.tahmidu.room_designer_client_android.userAuthentication.login;

import androidx.lifecycle.MutableLiveData;

interface ILoginViewModel
{
    void Login(String email, String password);
    void goToVerify();
    void goToSignUp();
    void goToForgotPassword();
}
