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
import com.tahmidu.room_designer_client_android.databinding.FragmentSignUpBinding;
import com.tahmidu.room_designer_client_android.model.SignUp;
import com.tahmidu.room_designer_client_android.view_model.WelcomeViewModel;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {

    private final String TAG = "SIGN_UP_FRAGMENT";

    //Binding
    private FragmentSignUpBinding binding;

    //Navigation Controller
    private NavController navController;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_sign_up, container, false);

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
                .getInstance(Objects.requireNonNull(getActivity()).getApplication());
        //View Model
        WelcomeViewModel welcomeViewModel = new ViewModelProvider(this, factory)
                .get(WelcomeViewModel.class);
        //Bind to view.
        binding.setVM(welcomeViewModel);
        binding.setSignUp(new SignUp());

        //On back pressed.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                navController.navigate(R.id.action_signUpFragment_to_loginFragment);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        //Navigation
        welcomeViewModel.getNavigateFragment().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.d(TAG, integer.toString());
                switch (integer)
                {
                    case WelcomeViewModel.SIGN_IN_FRAGMENT:
                        navController.navigate(R.id.action_signUpFragment_to_loginFragment);
                        break;
                    case WelcomeViewModel.VERIFY_EMAIL_FRAGMENT:
                        navController.navigate(R.id.action_signUpFragment_to_verifyEmailFragment);
                        break;
                }
            }
        });
    }
}
