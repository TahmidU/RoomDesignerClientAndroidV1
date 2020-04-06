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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tahmidu.room_designer_client_android.R;
import com.tahmidu.room_designer_client_android.adapter.view_pager.ItemImagesViewPager;
import com.tahmidu.room_designer_client_android.databinding.FragmentMyAccountBinding;
import com.tahmidu.room_designer_client_android.databinding.FragmentUserItemBinding;
import com.tahmidu.room_designer_client_android.view_model.MainViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAccountFragment extends Fragment {

    private final String TAG = "MY_ACCOUNT_FRAGMENT";
    private final String TOOLBAR_TITLE = "My Account";

    //Model View
    private MainViewModel mainViewModel;

    private FragmentMyAccountBinding binding;

    public MyAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_my_account, container, false);

        Toolbar toolbar = getActivity().findViewById(R.id.main_toolbar);
        toolbar.setTitle(TOOLBAR_TITLE);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        //Set up navigation drawer
        DrawerLayout drawerLayout = getActivity().findViewById(R.id.main_drawer);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        //Create the View Model.
        ViewModelProvider.Factory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication());
        //View Model
        mainViewModel = new ViewModelProvider(this, factory)
                .get(MainViewModel.class);

        //Set to binding
        binding.setVM(mainViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        //Fragment transaction setup
        mainViewModel.getNavigateFragment().setValue(MainViewModel.MY_ACCOUNT_FRAGMENT);
    }
}
