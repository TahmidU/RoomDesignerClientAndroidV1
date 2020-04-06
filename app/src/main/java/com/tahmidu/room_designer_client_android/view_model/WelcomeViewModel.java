package com.tahmidu.room_designer_client_android.view_model;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.tahmidu.room_designer_client_android.preferences.PreferenceProvider;
import com.tahmidu.room_designer_client_android.repository.LoginRepo;
import com.tahmidu.room_designer_client_android.repository.PasswordRepo;
import com.tahmidu.room_designer_client_android.repository.SignUpRepo;
import com.tahmidu.room_designer_client_android.repository.VerifyRepo;

public class WelcomeViewModel extends AndroidViewModel
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
    private final MutableLiveData<String> passwordEmailResponse = new MutableLiveData<>();
    private final MutableLiveData<String> passwordVerifyResponse = new MutableLiveData<>();

    //JWT Token
    private final MutableLiveData<String> jwtToken = new MutableLiveData<>();

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

    /**
     * Authenticate if the user has valid credentials.
     * @param email email
     * @param password password
     */
    public void authenticateUser(String email, String password) {
        //token.postValue("bypass");
        Log.d(TAG, "Authenticate User: " + email);
        final String COMPLETE_FIELD = "Complete all fields.";
        if(email.equals("") || password.equals(""))
        {
            signInResponse.postValue(COMPLETE_FIELD);
            return;
        }
        loginRepo.authUser(email, password, signInResponse, progressVisibility, navigateFragment,
                jwtToken, preferenceProvider);
    }

    /**
     * Sign-up user.
     * @param firstName first name
     * @param lastName last name
     * @param password password
     * @param rePassword re-enter password
     * @param email email
     * @param phoneNum phone number
     */
    public void signUp(String firstName, String lastName, String password, String rePassword, String email, String phoneNum)
    {
        Log.d(TAG, "Sign-up");
        if (firstName == null || lastName == null || password == null || rePassword == null
                || email == null || phoneNum == null)
            return;

        if(password.equals(rePassword))
        {
            signUpRepo.signUp(getApplication().getApplicationContext(),firstName, lastName, password,
                    email, phoneNum);
            Log.d(TAG, "Sign-up: fields valid");
            navigateFragment(VERIFY_EMAIL_FRAGMENT);
        }
    }

    /**
     * Verify account (filter spam accounts) by checking if verification code matches.
     * @param code verification code
     */
    public void verifyCode(String code)
    {
        Log.d(TAG,"Verify code" + code);
       verifyRepo.verify(getApplication().getApplicationContext(), code, verifyResponse);
    }

    /**
     * Resend verification code. Previous code will be deleted and replaced!
     */
    public void resendVerificationCode()
    {
        Log.d(TAG,"Resend verify code.");
        verifyRepo.resendToken(getApplication().getApplicationContext());
    }

    /**
     * Navigate to specific fragment.
     * @param id fragment id
     */
    public void navigateFragment(int id)
    {
        Log.d(TAG, "Navigate to " + id);
        navigateFragment.postValue(id);
    }

    /**
     * Send password reset token to users email (optional), if and only if the user exists.
     * Save email in preference then navigate to verifying password token fragment.
     * @param email email
     * @param sendToken send token?
     */
    public void forgotPassword(String email, boolean sendToken)
    {
        if(email != null)
        {
            if(!email.equals(""))
            {
                preferenceProvider.saveEmail(email);
                if(sendToken)
                    passwordRepo.sendToken(email);
                Log.d(TAG,"Forgot password, send token: " + sendToken);
                navigateFragment(VERIFY_PASSWORD_FRAGMENT);
            }
        }

        passwordEmailResponse.postValue("Please enter your email");
    }

    /**
     * Save password verification token in preference.
     * @param token password token
     */
    public void verifyPasswordToken(String token)
    {
        Log.d(TAG, "Saving password token..." + token);
        preferenceProvider.saveToken(Integer.parseInt(token));
        navigateFragment(CONFIRM_PASSWORD_FRAGMENT);
    }

    /**
     * Resend password verification token.
     */
    public void forgotPasswordTokenResend()
    {
        Log.d(TAG, "Resend password token.");
        forgotPassword(preferenceProvider.getEmail(), true);
    }

    /**
     * Change password. Check if fields match and retrieve email and token from preference.
     * @param password password
     * @param reEnterPassword re-enter password
     */
    public void changePassword(String password, String reEnterPassword)
    {
        Log.d(TAG, "Change password");
        if(password == null || reEnterPassword == null)
            return;

        if(password.equals(reEnterPassword))
            passwordRepo.changePassword(preferenceProvider.getEmail(),preferenceProvider.getToken(),
                    password, passwordVerifyResponse);

    }

    public MutableLiveData<Boolean> getProgressVisibility() {
        return progressVisibility;
    }

    public MutableLiveData<String> getJWTToken()
    {
        return jwtToken;
    }

    public MutableLiveData<String> getVerifyResponse()
    {
        return verifyResponse;
    }

    public MutableLiveData<String> getSignUpResponse() {
        return signInResponse;
    }

    public MutableLiveData<String> getPasswordEmailResponse() {
        return passwordEmailResponse;
    }

    public MutableLiveData<String> getPasswordVerifyResponse() {
        return passwordVerifyResponse;
    }

    public MutableLiveData<Integer> getNavigateFragment() {
        return navigateFragment;
    }

}
