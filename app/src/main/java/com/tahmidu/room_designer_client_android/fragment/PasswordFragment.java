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
import com.tahmidu.room_designer_client_android.databinding.FragmentPasswordBinding;
import com.tahmidu.room_designer_client_android.model.Login;
import com.tahmidu.room_designer_client_android.view_model.WelcomeViewModel;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class PasswordFragment extends Fragment {

    private final String TAG = "PASSWORD_FRAGMENT";

    //Binding
    private FragmentPasswordBinding binding;

    //Navigation Controller
    private NavController navController;

    public PasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_password, container, false);

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
        binding.setLogin(new Login());
        binding.setLifecycleOwner(getViewLifecycleOwner());

        //On back pressed.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                navController.navigate(R.id.action_passwordFragment_to_loginFragment);
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
                    case WelcomeViewModel.VERIFY_PASSWORD_FRAGMENT:
                        navController.navigate(R.id.action_passwordFragment_to_passwordTokenFragment);
                        break;
                    case WelcomeViewModel.SIGN_UP_FRAGMENT:
                        navController.navigate(R.id.action_passwordFragment_to_signUpFragment);
                        break;
                }
            }
        });
    }

}
