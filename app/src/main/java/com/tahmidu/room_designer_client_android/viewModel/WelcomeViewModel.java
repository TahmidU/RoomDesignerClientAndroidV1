package com.tahmidu.room_designer_client_android.viewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tahmidu.room_designer_client_android.repository.LoginRepo;
import com.tahmidu.room_designer_client_android.repository.SignUpRepo;

public class WelcomeViewModel extends ViewModel implements IWelcomeViewModel
{
    private final String TAG = "WELCOME_VIEW_MODEL";

    //Fragment Navigation
    public final static int SIGN_IN_FRAGMENT = 0;
    public final static int SIGN_UP_FRAGMENT = 1;
    public final static int VERIFY_EMAIL_FRAGMENT = 2;

    //Repository
    private LoginRepo loginRepo;
    private SignUpRepo signUpRepo;

    private MutableLiveData<Integer> navigateFragment = new MutableLiveData<>();

    public WelcomeViewModel()
    {
        loginRepo = LoginRepo.getInstance();
        signUpRepo = SignUpRepo.getInstance();
    }


    @Override
    public void Login(String email, String password)
    {
        loginRepo.retrieveToken(email, password);
    }

    public void signUp(String firstName, String lastName, String password, String rePassword, String email, String phoneNum)
    {
        Log.d(TAG, password + " " + rePassword);
        if(password.equals(rePassword))
        {
            signUpRepo.signUp(firstName, lastName, password, email, phoneNum);
            navigateFragment(VERIFY_EMAIL_FRAGMENT);
        }
    }

    @Override
    public void navigateFragment(int id)
    {
        navigateFragment.postValue(id);
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

    public MutableLiveData<Integer> getNavigateFragment() {
        return navigateFragment;
    }
}
