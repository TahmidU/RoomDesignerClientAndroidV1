package com.tahmidu.room_designer_client_android.util.network;

import android.content.Context;
import android.util.Log;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.tahmidu.room_designer_client_android.network.NetworkState;
import com.tahmidu.room_designer_client_android.network.NetworkStatus;

import okhttp3.OkHttpClient;

public class PicassoSetup
{
    private final String TAG = "PICASSO SETUP";

    private static PicassoSetup instance;
    private static PicassoStatus picassoStatus;
    private static Context applicationContext;

    public static PicassoSetup getInstance(Context applicationContext) {
        if(instance == null)
        {
            picassoStatus = PicassoStatus.NOT_SET;
            PicassoSetup.applicationContext = applicationContext;
            return instance = new PicassoSetup();
        }

        return instance;
    }

    public void configurePicasso()
    {
        if(picassoStatus == PicassoStatus.NOT_SET) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new BasicAuthInterceptor(applicationContext))
                    .build();

            Picasso picasso = new Picasso.Builder(applicationContext)
                    .downloader(new OkHttp3Downloader(client))
                    .build();

            Picasso.setSingletonInstance(picasso);
            Picasso.get().setLoggingEnabled(true);

            picassoStatus = PicassoStatus.SET;
        }
    }

    public static PicassoStatus getPicassoStatus() {
        return picassoStatus;
    }
}
