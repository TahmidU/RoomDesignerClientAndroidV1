package com.tahmidu.room_designer_client_android.userAuthentication.login;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.tahmidu.room_designer_client_android.R;
import com.tahmidu.room_designer_client_android.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity
{
    //Data Binding
    private ActivityLoginBinding binding;

    //View Model
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        //Create the View Model.
        ViewModelProvider.Factory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication());
        loginViewModel = new ViewModelProvider(this, factory)
                .get(LoginViewModel.class);
        binding.setVM(loginViewModel);
        binding.setLogin(new Login("",""));

        loginViewModel.getToken().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                toastToken(s);
            }
        });

    }

    private void toastToken(String token)
    {
        Toast.makeText(this, token, Toast.LENGTH_LONG).show();
    }
}
