package com.tahmidu.room_designer_client_android.view_model;

public interface IWelcomeViewModel
{
    void authenticateUser(String email, String password);
    void signUp(String firstName, String lastName, String password, String rePassword, String email,
                String phoneNum);
    void forgotPassword(String email, boolean sendToken);
    void navigateFragment(int id);
    void verifyCode(String code);
    void resendVerificationCode();
    void verifyPasswordToken(String token);
    void forgotPasswordTokenResend();
    void changePassword(String password, String reEnterPassword);
}
