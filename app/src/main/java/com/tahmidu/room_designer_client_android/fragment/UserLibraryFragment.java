package com.tahmidu.room_designer_client_android.fragment;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tahmidu.room_designer_client_android.R;
import com.tahmidu.room_designer_client_android.adapter.MainRecyclerAdapter;
import com.tahmidu.room_designer_client_android.databinding.FragmentUserLibraryBinding;
import com.tahmidu.room_designer_client_android.network.NetworkState;
import com.tahmidu.room_designer_client_android.network.NetworkStatus;
import com.tahmidu.room_designer_client_android.view_model.MainViewModel;

import java.util.ArrayList;

import static com.tahmidu.room_designer_client_android.constant.Exit.DURATION;
import static com.tahmidu.room_designer_client_android.constant.Exit.TOAST_EXIT_MSG;

/**
 * Users Library.
 */
public class UserLibraryFragment extends Fragment {

    private final String TAG = "USER_LIB_FRAGMENT";

    private FragmentUserLibraryBinding binding;

    //Model View
    private MainViewModel mainViewModel;

    //Recycler View
    private MainRecyclerAdapter mainRecyclerAdapter;

    //Application Exit
    private long targetTime = 0;

    public UserLibraryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_user_library, container, false);

        Log.d(TAG, "called");

        Toolbar toolbar = getActivity().findViewById(R.id.main_toolbar);
        String TOOLBAR_TITLE = "User Library";
        toolbar.setTitle(TOOLBAR_TITLE);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        //Set up navigation drawer
        DrawerLayout drawerLayout = getActivity().findViewById(R.id.main_drawer);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        //Listeners
        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                Log.d(TAG, "Current time: " + System.currentTimeMillis());
                Log.d(TAG, "Target time: " + targetTime);

                if(System.currentTimeMillis() < targetTime)
                    System.exit(1);
                else {
                    targetTime = System.currentTimeMillis() + DURATION;
                    Toast.makeText(getContext(), TOAST_EXIT_MSG, Toast.LENGTH_SHORT).show();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Recycler View
        RecyclerView recyclerView = getActivity().findViewById(R.id.user_lib_recycler_view);

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
        mainViewModel.getNavigateFragment().setValue(MainViewModel.USER_LIB_FRAGMENT);

        //Observers
        mainViewModel.getUserItemsLiveData().observe(getViewLifecycleOwner(), items ->
        {
            Log.d(TAG, "Observer notified!");
            mainRecyclerAdapter.updateList(items);
        });

        mainViewModel.getNavigateFragment().observe(getViewLifecycleOwner(), integer ->
        {
            if(integer == MainViewModel.USER_ITEM_FRAGMENT)
            {
                FragmentTransaction transaction = transactionMan.beginTransaction();
                transaction.replace(R.id.fragment_main_container, new UserItemFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }

            if(integer == MainViewModel.ADD_ITEM_FRAGMENT)
            {
                FragmentTransaction transaction = transactionMan.beginTransaction();
                transaction.replace(R.id.fragment_main_container, new AddItemFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        //Recycler View
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        mainRecyclerAdapter = new MainRecyclerAdapter(mainViewModel.getItemsLiveData().getValue(),
                getActivity().getApplicationContext(), (view1, position) -> {
            mainViewModel.clickedUserItem(position);
            mainViewModel.navigateFragment(MainViewModel.USER_ITEM_FRAGMENT);
        });
        recyclerView.setAdapter(mainRecyclerAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d(TAG, "Can the user scroll down? " + recyclerView.canScrollVertically(1));
                Log.d(TAG, "Can the user scroll up? " + recyclerView.canScrollVertically(-1));
                if(!recyclerView.canScrollVertically(1)
                        && NetworkState.getInstance().getStatus().equals(NetworkStatus.DONE))
                {
                    Log.d(TAG, "RecyclerView bottom dragged!");
                    Log.d(TAG, "Adapter item count: "+ mainRecyclerAdapter.getItemCount());
                    mainViewModel.fetchUserLibrary();
                }

                if(!recyclerView.canScrollVertically(-1) &&
                        NetworkState.getInstance().getStatus().equals(NetworkStatus.DONE))
                {
                    Log.d(TAG, "RecyclerView top dragged!");
                    resetRecyclerView();
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, java.time.LocalTime.now() + " OnResume called");
        resetRecyclerView();
    }

    private void resetRecyclerView()
    {
        Log.d(TAG, java.time.LocalTime.now() + " Resetting Recycler View");
        mainViewModel.resetPage();
        mainViewModel.getItemsLiveData().setValue(new ArrayList<>());
        mainRecyclerAdapter.resetList();
        mainViewModel.fetchUserLibrary();
    }
}
