package com.tahmidu.room_designer_client_android.fragment;


import android.os.Bundle;
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
import android.view.WindowManager;
import android.widget.Toast;
import com.tahmidu.room_designer_client_android.R;
import com.tahmidu.room_designer_client_android.databinding.FragmentLoginBinding;
import com.tahmidu.room_designer_client_android.model.Login;
import com.tahmidu.room_designer_client_android.viewModel.WelcomeViewModel;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment{

    private final String TAG = "LOGIN_FRAGMENT";

    //Binding
    private FragmentLoginBinding binding;

    //Navigation Controller
    private NavController navController;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_login, container, false);

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
        //Bind to view
        binding.setVM(welcomeViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setLogin(new Login("",""));

        //Subscribed to live data.
        welcomeViewModel.getToken().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Objects.requireNonNull(getActivity()).getWindow()
                        .setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                toastToken(s);
            }
        });

        //Navigation
        welcomeViewModel.getNavigateFragment().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.d(TAG, integer.toString());
                switch (integer)
                {
                    case WelcomeViewModel.SIGN_UP_FRAGMENT:
                        navController.navigate(R.id.action_loginFragment_to_signUpFragment);
                        break;
                    case WelcomeViewModel.VERIFY_EMAIL_FRAGMENT:
                        navController.navigate(R.id.action_loginFragment_to_verifyEmailFragment);
                        break;
                    case WelcomeViewModel.FORGOT_PASSWORD_FRAGMENT:
                        navController.navigate(R.id.action_loginFragment_to_passwordFragment);
                        break;
                }
            }
        });
    }

    private void toastToken(String token)
    {
        Toast.makeText(getContext(), token, Toast.LENGTH_LONG).show();
    }

}
