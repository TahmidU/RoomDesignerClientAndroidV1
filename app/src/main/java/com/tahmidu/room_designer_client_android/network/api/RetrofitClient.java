package com.tahmidu.room_designer_client_android.network.api;

import android.app.Application;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient extends Application
{
    private static Retrofit retrofit = null;
    private final static String URL = "http://192.168.0.8:8080/";

    public static Retrofit getRetrofit()
    {
        if(retrofit == null)
        {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.level(HttpLoggingInterceptor.Level.BODY);
            okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder()
                    .addInterceptor(logging).build();
/*            okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder().build();*/

            retrofit = new Retrofit.Builder()
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(URL)
                    .build();
        }
        return retrofit;
    }
}
