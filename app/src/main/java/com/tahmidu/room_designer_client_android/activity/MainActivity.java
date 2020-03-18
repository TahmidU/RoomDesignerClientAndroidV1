package com.tahmidu.room_designer_client_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;


import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.tahmidu.room_designer_client_android.R;
import com.tahmidu.room_designer_client_android.preferences.PreferenceProvider;
import com.tahmidu.room_designer_client_android.util.BasicAuthInterceptor;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        final String token = new PreferenceProvider(getApplicationContext()).getJWTToken();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(token))
                .build();


        Picasso picasso = new Picasso.Builder(getApplicationContext())
                .downloader(new OkHttp3Downloader(client))
                .build();

        Picasso.setSingletonInstance(picasso);
        Picasso.get().setLoggingEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Picasso.get().shutdown();
        finish();
    }
}
