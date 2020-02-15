package com.tahmidu.room_designer_client_android.repository;

import android.content.Context;

public interface ISignUpRepo
{
    void signUp(Context context, String firstName, String lastName, String password, String email, String phoneNum);
}
