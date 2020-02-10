package com.tahmidu.room_designer_client_android.userAuthentication.login;

import android.util.Log;

import androidx.databinding.Bindable;
import androidx.databinding.Observable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.disposables.CompositeDisposable;

public class LoginViewModel extends ViewModel implements ILoginViewModel
{
    //Repository
    private LoginRepo loginRepo;

    public LoginViewModel()
    {
        loginRepo = LoginRepo.getInstance();
    }


    @Override
    public void Login(String email, String password)
    {
        loginRepo.retrieveToken(email, password);
    }

    @Override
    public void goToVerify() {

    }

    @Override
    public void goToSignUp() {

    }

    @Override
    public void goToForgotPassword() {

    }

    public MutableLiveData<String> getToken()
    {
        return loginRepo.getToken();
    }
}
