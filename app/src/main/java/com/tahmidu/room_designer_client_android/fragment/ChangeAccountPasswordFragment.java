package com.tahmidu.room_designer_client_android.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tahmidu.room_designer_client_android.R;
import com.tahmidu.room_designer_client_android.databinding.FragmentChangeAccountDetailsBinding;
import com.tahmidu.room_designer_client_android.databinding.FragmentChangeAccountPasswordBinding;
import com.tahmidu.room_designer_client_android.databinding.FragmentChangePasswordBinding;
import com.tahmidu.room_designer_client_android.view_model.MainViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeAccountPasswordFragment extends Fragment {

    private final String TAG = "CHANGE_ACC_PASSWORD_FRAGMENT";
    private final String TOOLBAR_TITLE = "My Account";

    //Model View
    private MainViewModel mainViewModel;

    private FragmentChangeAccountPasswordBinding binding;

    public ChangeAccountPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_change_account_password, container,
                        false);

        Toolbar toolbar = getActivity().findViewById(R.id.main_toolbar);
        toolbar.setTitle(TOOLBAR_TITLE);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        //Set up navigation drawer
        DrawerLayout drawerLayout = getActivity().findViewById(R.id.main_drawer);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Create the View Model.
        ViewModelProvider.Factory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication());
        //View Model
        mainViewModel = new ViewModelProvider(this, factory)
                .get(MainViewModel.class);

        binding.setVM(mainViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        //Fragment transaction setup
        FragmentManager transactionMan = getActivity().getSupportFragmentManager();
        mainViewModel.getNavigateFragment().setValue(MainViewModel.CHANGE_ACC_PASSWORD_FRAGMENT);

        //Observer
        mainViewModel.getNavigateFragment().observe(getActivity(), integer ->
        {
            if(integer == MainViewModel.MY_ACCOUNT_FRAGMENT)
                transactionMan.popBackStack();
        });
    }
}
