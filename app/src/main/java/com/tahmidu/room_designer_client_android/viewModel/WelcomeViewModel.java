package com.tahmidu.room_designer_client_android.viewModel;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.databinding.Observable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.tahmidu.room_designer_client_android.fragment.VerifyEmailFragment;
import com.tahmidu.room_designer_client_android.model.VerifyCode;
import com.tahmidu.room_designer_client_android.preferences.PreferenceProvider;
import com.tahmidu.room_designer_client_android.repository.LoginRepo;
import com.tahmidu.room_designer_client_android.repository.PasswordRepo;
import com.tahmidu.room_designer_client_android.repository.SignUpRepo;
import com.tahmidu.room_designer_client_android.repository.VerifyRepo;

public class WelcomeViewModel extends AndroidViewModel implements IWelcomeViewModel
{
    private final String TAG = "WELCOME_VIEW_MODEL";

    //Fragment Navigation
    public final static int SIGN_IN_FRAGMENT = 0;
    public final static int SIGN_UP_FRAGMENT = 1;
    public final static int VERIFY_EMAIL_FRAGMENT = 2;
    public final static int FORGOT_PASSWORD_FRAGMENT = 3;
    public final static int VERIFY_PASSWORD_FRAGMENT = 4;
    public final static int CONFIRM_PASSWORD_FRAGMENT = 5;

    //Repository
    private LoginRepo loginRepo;
    private SignUpRepo signUpRepo;
    private VerifyRepo verifyRepo;
    private PasswordRepo passwordRepo;

    //Visibility
    //This is not a toggle but a notifier to the view to change the visibility.
    private final MutableLiveData<Boolean> progressVisibility = new MutableLiveData<>();

    //Navigation
    private MutableLiveData<Integer> navigateFragment = new MutableLiveData<>();

    //Responses
    private final MutableLiveData<String> verifyResponse = new MutableLiveData<>();
    private final MutableLiveData<String> signInResponse = new MutableLiveData<>();
    private final MutableLiveData<String> passwordVerifyResponse = new MutableLiveData<>();

    //Preferences
    private PreferenceProvider preferenceProvider;

    public WelcomeViewModel(@NonNull Application application) {
        super(application);
        loginRepo = LoginRepo.getInstance();
        signUpRepo = SignUpRepo.getInstance();
        verifyRepo = VerifyRepo.getInstance();
        passwordRepo = PasswordRepo.getInstance();

        preferenceProvider = new PreferenceProvider(getApplication().getApplicationContext());

        progressVisibility.setValue(true);
    }

    @Override
    public void authenticateUser(String email, String password) {

        final String COMPLETE_FIELD = "Complete all fields.";
        if(email.equals("") || password.equals(""))
        {
            signInResponse.postValue(COMPLETE_FIELD);
            return;
        }
        loginRepo.authUser(email, password, signInResponse, progressVisibility, navigateFragment);
    }

    @Override
    public void login(String email, String password)
    {

        loginRepo.retrieveToken(email, password);

    }

    @Override
    public void signUp(String firstName, String lastName, String password, String rePassword, String email, String phoneNum)
    {
        if (firstName == null || lastName == null || password == null || rePassword == null
                || email == null || phoneNum == null)
            return;

        if(password.equals(rePassword))
        {
            signUpRepo.signUp(getApplication().getApplicationContext(),firstName, lastName, password,
                    email, phoneNum);
            navigateFragment(VERIFY_EMAIL_FRAGMENT);
        }
    }

    public void verifyCode(String code)
    {
       verifyRepo.verify(getApplication().getApplicationContext(), code, verifyResponse);
    }

    public void resendVerificationCode()
    {
        verifyRepo.resendToken(getApplication().getApplicationContext());
    }

    @Override
    public void navigateFragment(int id)
    {
        navigateFragment.postValue(id);
    }

    @Override
    public void forgotPassword(String email)
    {
        preferenceProvider.saveEmail(email);
        passwordRepo.sendToken(email);
        navigateFragment(VERIFY_PASSWORD_FRAGMENT);
    }

    public void verifyPasswordToken(String token)
    {
        preferenceProvider.saveToken(Integer.parseInt(token));
        navigateFragment(CONFIRM_PASSWORD_FRAGMENT);
    }

    public void forgotPasswordTokenResend()
    {
        forgotPassword(preferenceProvider.getEmail());
    }

    public void changePassword(String password, String reEnterPassword)
    {
        if(password == null || reEnterPassword == null)
            return;

        if(password.equals(reEnterPassword))
            passwordRepo.changePassword(preferenceProvider.getEmail(),preferenceProvider.getToken(),
                    password, verifyResponse);

        navigateFragment(SIGN_IN_FRAGMENT);

    }

    public MutableLiveData<Boolean> getProgressVisibility() {
        return progressVisibility;
    }

    public MutableLiveData<String> getToken()
    {
        return loginRepo.getToken();
    }

    public MutableLiveData<String> getVerifyResponse()
    {
        return verifyResponse;
    }

    public MutableLiveData<String> getSignUpResponse() {
        return signInResponse;
    }

    public MutableLiveData<String> getPasswordVerifyResponse() {
        return passwordVerifyResponse;
    }

    public MutableLiveData<Integer> getNavigateFragment() {
        return navigateFragment;
    }

}
