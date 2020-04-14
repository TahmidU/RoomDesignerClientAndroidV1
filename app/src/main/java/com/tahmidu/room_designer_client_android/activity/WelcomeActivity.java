package com.tahmidu.room_designer_client_android.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.tahmidu.room_designer_client_android.R;
import com.tahmidu.room_designer_client_android.network.api.RetrofitClient;

public class WelcomeActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
