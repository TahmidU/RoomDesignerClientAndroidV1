package com.tahmidu.room_designer_client_android.repository;

public interface ISignUpRepo
{
    void signUp(String firstName, String lastName, String password, String email, String phoneNum);
}
