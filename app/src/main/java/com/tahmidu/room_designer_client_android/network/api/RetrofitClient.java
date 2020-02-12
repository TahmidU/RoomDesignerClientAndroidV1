package com.tahmidu.room_designer_client_android.network.api;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient extends Application
{
    private static Retrofit retrofit = null;
    private final static String URL = "http://192.168.0.8:8080/";

    public static Retrofit getRetrofit()
    {
        if(retrofit == null)
        {
            okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder().build();

            retrofit = new Retrofit.Builder()
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(URL)
                    .build();
        }
        return retrofit;
    }
}
