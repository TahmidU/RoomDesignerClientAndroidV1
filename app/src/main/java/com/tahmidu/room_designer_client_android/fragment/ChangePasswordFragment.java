package com.tahmidu.room_designer_client_android.fragment;


import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tahmidu.room_designer_client_android.R;
import com.tahmidu.room_designer_client_android.databinding.FragmentChangePasswordBinding;
import com.tahmidu.room_designer_client_android.model.ConfirmPassword;
import com.tahmidu.room_designer_client_android.view_model.WelcomeViewModel;

public class ChangePasswordFragment extends Fragment {

    private final String TAG = "LOGIN_FRAGMENT";

    //Binding
    private FragmentChangePasswordBinding binding;

    //View Model
    private WelcomeViewModel welcomeViewModel;

    //Navigation Controller
    private NavController navController;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_change_password, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Create the View Model.
        ViewModelProvider.Factory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication());
        welcomeViewModel = new ViewModelProvider(this, factory)
                .get(WelcomeViewModel.class);
        //Bind to view.
        binding.setVM(welcomeViewModel);
        binding.setPassword(new ConfirmPassword());
        binding.setLifecycleOwner(getViewLifecycleOwner());

        //Handle back press.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                navController.navigate(R.id.action_changePasswordFragment_to_passwordTokenFragment);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        //Subscriptions to observables.
        welcomeViewModel.getPasswordVerifyResponse().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.equals(""))
                    navController.navigate(R.id.action_changePasswordFragment_to_loginFragment);
            }
        });

        //Navigation
        welcomeViewModel.getNavigateFragment().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.d(TAG, integer.toString());
                if (integer == WelcomeViewModel.SIGN_IN_FRAGMENT) {
                    navController.navigate(R.id.action_changePasswordFragment_to_loginFragment);
                }
            }
        });
    }
}
