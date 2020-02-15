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
import android.widget.EditText;

import com.tahmidu.room_designer_client_android.R;
import com.tahmidu.room_designer_client_android.databinding.FragmentVerifyEmailBinding;
import com.tahmidu.room_designer_client_android.model.SignUp;
import com.tahmidu.room_designer_client_android.model.VerifyCode;
import com.tahmidu.room_designer_client_android.repository.VerifyRepo;
import com.tahmidu.room_designer_client_android.viewModel.WelcomeViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class VerifyEmailFragment extends Fragment {

    private final String TAG = "VERIFY_FRAGMENT";

    //Messages
    public static final String OK_VERIFY_MSG = "";
    public static final String ERROR_VERIFY_MSG = "Something went wrong.";
    public static final String INCORRECT_VERIFY_MSG = "The token you have entered is incorrect.";
    public static final String EXPIRED_VERIFY_MSG = "The token you have entered is expired. Click resend token.";

    //Binding
    private FragmentVerifyEmailBinding binding;

    //View Model
    private WelcomeViewModel welcomeViewModel;

    //Navigation Controller
    private NavController navController;

    public VerifyEmailFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_verify_email, container, false);

        // Inflate the layout for this fragment
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
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setVerify(new VerifyCode());

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                navController.navigate(R.id.action_verifyEmailFragment_to_loginFragment);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        welcomeViewModel.getVerifyResponse().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals(VerifyRepo.RESPONSE_OK)) {
                    navController.navigate(R.id.action_verifyEmailFragment_to_loginFragment);
                }
            }
        });

        welcomeViewModel.getNavigateFragment().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.d(TAG, integer.toString());
                if (integer == WelcomeViewModel.SIGN_IN_FRAGMENT) {
                    navController.navigate(R.id.action_verifyEmailFragment_to_loginFragment);
                }
            }
        });
    }

}
