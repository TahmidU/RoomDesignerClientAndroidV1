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
import com.tahmidu.room_designer_client_android.databinding.FragmentVerifyEmailBinding;
import com.tahmidu.room_designer_client_android.repository.UserRepo;
import com.tahmidu.room_designer_client_android.view_model.WelcomeViewModel;
import java.util.Objects;

/**
 * Verify Account.
 */
public class VerifyEmailFragment extends Fragment {

    private final String TAG = "VERIFY_FRAGMENT";

    //Binding
    private FragmentVerifyEmailBinding binding;

    //Navigation Controller
    private NavController navController;

    public VerifyEmailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
                .getInstance(Objects.requireNonNull(getActivity()).getApplication());
        //View Model
        WelcomeViewModel welcomeViewModel = new ViewModelProvider(this, factory)
                .get(WelcomeViewModel.class);
        //Bind to view.
        binding.setVM(welcomeViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        //On back pressed.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                navController.navigate(R.id.action_verifyEmailFragment_to_loginFragment);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        //Subscribe to live data.
        welcomeViewModel.getVerifyResponse().observe(getViewLifecycleOwner(), s -> {
            if (s.equals(UserRepo.RESPONSE_OK)) {
                navController.navigate(R.id.action_verifyEmailFragment_to_loginFragment);
            }
        });

        //Navigation
        welcomeViewModel.getNavigateFragment().observe(getViewLifecycleOwner(), integer -> {
            Log.d(TAG, integer.toString());
            if (integer == WelcomeViewModel.SIGN_IN_FRAGMENT) {
                navController.navigate(R.id.action_verifyEmailFragment_to_loginFragment);
            }
        });
    }

}
