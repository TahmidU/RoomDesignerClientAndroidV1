package com.tahmidu.room_designer_client_android.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.tahmidu.room_designer_client_android.model.Item;
import com.tahmidu.room_designer_client_android.model.User;

public class PreferenceProvider
{
    private SharedPreferences sharedPreferences;

    private final String EMAIL = "EMAIL"; //Save email
    private final String TOKEN = "TOKEN"; //Save password or verification token
    private final String JWT_TOKEN = "JWT TOKEN";
    private final String ITEM = "ITEM";
    private final String ITEMS_USER = "ITEMS USER";

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

    public void saveJWTToken(String token)
    {
        sharedPreferences.edit().putString(JWT_TOKEN, token).apply();
    }

    public String getJWTToken()
    {
        return sharedPreferences.getString(JWT_TOKEN, "");
    }

    public void saveItem(Item item)
    {
        Gson gson = new Gson();
        String itemToJSON = gson.toJson(item);
        sharedPreferences.edit().putString(ITEM, itemToJSON).apply();
    }

    public Item getItem()
    {
        Gson gson = new Gson();
        return gson.fromJson(sharedPreferences.getString(ITEM, ""), Item.class);
    }

    public void saveItemsUser(String userAsJsonString)
    {
        sharedPreferences.edit().putString(ITEMS_USER, userAsJsonString).apply();
    }

    public User getItemsUser()
    {
        Gson gson = new Gson();
        return gson.fromJson(sharedPreferences.getString(ITEMS_USER, ""), User.class);
    }
}
