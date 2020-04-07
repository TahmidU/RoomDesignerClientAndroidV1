package com.tahmidu.room_designer_client_android.fragment;


import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tahmidu.room_designer_client_android.R;
import com.tahmidu.room_designer_client_android.databinding.FragmentChangePasswordBinding;
import com.tahmidu.room_designer_client_android.view_model.WelcomeViewModel;

/**
 * Change Password once logged in.
 */
public class ChangePasswordFragment extends Fragment {

    private final String TAG = "LOGIN_FRAGMENT";

    //Binding
    private FragmentChangePasswordBinding binding;

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

        //View Model
        WelcomeViewModel welcomeViewModel = new ViewModelProvider(this, factory)
                .get(WelcomeViewModel.class);

        //Bind to view.
        binding.setVM(welcomeViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        //Handle back press.
        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                navController.navigate(R.id.action_changePasswordFragment_to_passwordTokenFragment);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        //Observables
        welcomeViewModel.getPasswordVerifyResponse().observe(getViewLifecycleOwner(), s -> {
            if(s.equals(""))
                navController.navigate(R.id.action_changePasswordFragment_to_loginFragment);
        });

        //Navigation
        welcomeViewModel.getNavigateFragment().observe(getViewLifecycleOwner(), integer -> {
            Log.d(TAG, integer.toString());
            if (integer == WelcomeViewModel.SIGN_IN_FRAGMENT) {
                navController.navigate(R.id.action_changePasswordFragment_to_loginFragment);
            }
        });
    }
}
