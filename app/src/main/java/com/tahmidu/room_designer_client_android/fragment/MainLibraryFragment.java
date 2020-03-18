package com.tahmidu.room_designer_client_android.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tahmidu.room_designer_client_android.R;
import com.tahmidu.room_designer_client_android.activity.ARActivity;
import com.tahmidu.room_designer_client_android.adapter.MainRecyclerAdapter;
import com.tahmidu.room_designer_client_android.databinding.FragmentMainLibraryBinding;
import com.tahmidu.room_designer_client_android.model.Item;
import com.tahmidu.room_designer_client_android.view_model.MainViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainLibraryFragment extends Fragment {

    //Model View
    private MainViewModel mainViewModel;

    //Binding
    private FragmentMainLibraryBinding binding;

    //Recycler View
    private RecyclerView recyclerView;
    private MainRecyclerAdapter mainRecyclerAdapter;

    //Navigation Controller
    private NavController navController;

    public MainLibraryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_main_library, container, false);

        Toolbar toolbar = getActivity().findViewById(R.id.main_toolbar);
        /*LinearLayout linearLayout = main_lib_toolbar.findViewById();
        linearLayout.addView();
        linearLayout.removeView();*/
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        recyclerView = getActivity().findViewById(R.id.main_lib_recycler_view);

        //Create the View Model.
        ViewModelProvider.Factory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication());
        //View Model
        mainViewModel = new ViewModelProvider(this, factory)
                .get(MainViewModel.class);

        binding.setVM(mainViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        mainViewModel.fetchMainLibrary();

        //Observers
        mainViewModel.getItemsLiveData().observe(getViewLifecycleOwner(),
                items -> mainRecyclerAdapter.notifyDataSetChanged());

        mainViewModel.getNavigateFragment().observe(getViewLifecycleOwner(), integer ->
        {
            switch (integer)
            {
                case MainViewModel.ITEM_FRAGMENT:
                    navController.navigate(R.id.action_mainLibraryFragment_to_itemFragment);
            }
        });

        //Recycler View
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        mainRecyclerAdapter = new MainRecyclerAdapter(mainViewModel.getItemsLiveData().getValue(),
                getActivity().getApplicationContext(), (view1, position) -> {
                    mainViewModel.clickedItem(position);
                    mainViewModel.getNavigateFragment().postValue(MainViewModel.ITEM_FRAGMENT);
                });
        recyclerView.setAdapter(mainRecyclerAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }
}
