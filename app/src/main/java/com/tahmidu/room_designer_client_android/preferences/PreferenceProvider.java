package com.tahmidu.room_designer_client_android.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class PreferenceProvider
{
    private SharedPreferences sharedPreferences;

    private final String EMAIL = "EMAIL"; //Save email
    private final String TOKEN = "TOKEN"; //Save password or verification token

    public PreferenceProvider(Context applicationContext)
    {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
    }

    public void saveEmail(String email)
    {
        sharedPreferences.edit().putString(EMAIL, email).apply();
    }

    public String getEmail()
    {
        return sharedPreferences.getString(EMAIL, "");
    }

    public void saveToken(int token)
    {
        sharedPreferences.edit().putInt(TOKEN, token).apply();
    }

    public int getToken()
    {
        return sharedPreferences.getInt(TOKEN, 0);
    }
}
