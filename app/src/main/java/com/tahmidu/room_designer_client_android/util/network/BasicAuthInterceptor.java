package com.tahmidu.room_designer_client_android.util.network;

import android.content.Context;

import com.tahmidu.room_designer_client_android.util.preferences.PreferenceProvider;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BasicAuthInterceptor implements Interceptor
{
    private volatile String token;

    public BasicAuthInterceptor(Context applicationContext) {
        this.token = new PreferenceProvider(applicationContext).getJWTToken();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request newRequest = chain.request().newBuilder()
                .addHeader("Authorization", token)
                .build();
        return chain.proceed(newRequest);
    }
}
