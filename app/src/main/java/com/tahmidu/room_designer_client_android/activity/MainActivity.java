package com.tahmidu.room_designer_client_android.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;


import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.tahmidu.room_designer_client_android.R;
import com.tahmidu.room_designer_client_android.fragment.MainLibraryFragment;
import com.tahmidu.room_designer_client_android.fragment.MyAccountFragment;
import com.tahmidu.room_designer_client_android.fragment.UserLibraryFragment;
import com.tahmidu.room_designer_client_android.preferences.PreferenceProvider;
import com.tahmidu.room_designer_client_android.util.network.BasicAuthInterceptor;

import java.util.Objects;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MAIN_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        //Picasso setup
        final String token = new PreferenceProvider(getApplicationContext()).getJWTToken();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(token))
                .build();


        Picasso picasso = new Picasso.Builder(getApplicationContext())
                .downloader(new OkHttp3Downloader(client))
                .build();

        Picasso.setSingletonInstance(picasso);
        Picasso.get().setLoggingEnabled(true);

        //Set up navigation drawer
        DrawerLayout drawerLayout = findViewById(R.id.main_drawer);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        FragmentManager transactionMan = getSupportFragmentManager();
        navigationView.setCheckedItem(R.id.drawer_main_lib);
        navigationView.setNavigationItemSelectedListener(menuItem -> {

            int id = menuItem.getItemId();

            if(id == R.id.drawer_main_lib)
            {
                FragmentTransaction transaction = transactionMan.beginTransaction();
                transaction.replace(R.id.fragment_main_container, new MainLibraryFragment());
                transaction.addToBackStack(null);
                transaction.commit();
                return true;
            }

            if(id == R.id.drawer_user_lib)
            {
                FragmentTransaction transaction = transactionMan.beginTransaction();
                transaction.replace(R.id.fragment_main_container, new UserLibraryFragment());
                transaction.addToBackStack(null);
                transaction.commit();
                return true;
            }

            if(id == R.id.drawer_my_account)
            {
                FragmentTransaction transaction = transactionMan.beginTransaction();
                transaction.replace(R.id.fragment_main_container, new MyAccountFragment());
                transaction.addToBackStack(null);
                transaction.commit();
                return true;
            }
            return true;
        });

        //Begin first fragment transaction
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_main_container, new MainLibraryFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
/*        FragmentManager transactionManager = getSupportFragmentManager();
        transactionManager.popBackStack();*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            Picasso.get().shutdown();
        }catch (UnsupportedOperationException e)
        {
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
        }
        finish();
    }

}
