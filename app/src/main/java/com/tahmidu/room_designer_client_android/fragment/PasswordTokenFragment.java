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
import com.tahmidu.room_designer_client_android.databinding.FragmentPasswordTokenBinding;
import com.tahmidu.room_designer_client_android.model.Login;
import com.tahmidu.room_designer_client_android.model.VerifyCode;
import com.tahmidu.room_designer_client_android.viewModel.WelcomeViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class PasswordTokenFragment extends Fragment {

    private final String TAG = "LOGIN_FRAGMENT";

    //Binding
    private FragmentPasswordTokenBinding binding;

    //View Model
    private WelcomeViewModel welcomeViewModel;

    //Navigation Controller
    private NavController navController;


    public PasswordTokenFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_password_token, container, false);

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
        binding.setVM(welcomeViewModel);
        binding.setVerifyCode(new VerifyCode());
        binding.setLifecycleOwner(getViewLifecycleOwner());

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                navController.navigate(R.id.action_passwordTokenFragment_to_passwordFragment);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        //Navigation
        welcomeViewModel.getNavigateFragment().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.d(TAG, integer.toString());
                if (integer == WelcomeViewModel.CONFIRM_PASSWORD_FRAGMENT) {
                    navController.navigate(R.id.action_passwordTokenFragment_to_changePasswordFragment);
                }
            }
        });
    }
}
