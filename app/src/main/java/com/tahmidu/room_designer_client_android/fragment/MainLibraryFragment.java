package com.tahmidu.room_designer_client_android.fragment;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tahmidu.room_designer_client_android.R;
import com.tahmidu.room_designer_client_android.databinding.FragmentMainLibraryBinding;
import com.tahmidu.room_designer_client_android.viewModel.MainViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainLibraryFragment extends Fragment {

    private MainViewModel mainViewModel;

    private FragmentMainLibraryBinding binding;

    private RecyclerView recyclerView;

    public MainLibraryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_main_library, container, false);

        Toolbar toolbar = getActivity().findViewById(R.id.main_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Create the View Model.
        ViewModelProvider.Factory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication());
        //View Model
        mainViewModel = new ViewModelProvider(this, factory)
                .get(MainViewModel.class);

        binding.setVM(mainViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

    }
}
