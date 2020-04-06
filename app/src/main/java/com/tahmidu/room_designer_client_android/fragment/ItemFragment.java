package com.tahmidu.room_designer_client_android.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tahmidu.room_designer_client_android.R;
import com.tahmidu.room_designer_client_android.activity.ARActivity;
import com.tahmidu.room_designer_client_android.activity.MainActivity;
import com.tahmidu.room_designer_client_android.adapter.view_pager.ItemImagesViewPager;
import com.tahmidu.room_designer_client_android.databinding.FragmentItemBinding;
import com.tahmidu.room_designer_client_android.view_model.MainViewModel;

import org.w3c.dom.Text;

public class ItemFragment extends Fragment {

    private final String TAG = "ITEM_FRAGMENT";
    private final String TOOLBAR_TITLE = "Item";

    //Model View
    private MainViewModel mainViewModel;

    //Binding
    private FragmentItemBinding binding;

    //Recycler View
    private ViewPager imageSelectViewPager;
    private ItemImagesViewPager itemImagesViewPagerAdapter;

    //Navigation Controller
    //private NavController navController;

    public ItemFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_item, container, false);

        Toolbar toolbar = getActivity().findViewById(R.id.main_toolbar);
        toolbar.setTitle(TOOLBAR_TITLE);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = getActivity().findViewById(R.id.main_drawer);

        //Set Drawer
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //navController = Navigation.findNavController(view);

        imageSelectViewPager = getActivity().findViewById(R.id.item_images_view_pager);

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
        mainViewModel.getNavigateFragment().setValue(MainViewModel.ITEM_FRAGMENT);

        //View Pager
        itemImagesViewPagerAdapter = new ItemImagesViewPager(getActivity().getApplicationContext(),
                mainViewModel.getSelectedItem().getImages());
        imageSelectViewPager.setAdapter(itemImagesViewPagerAdapter);

        //Increment Stat
        mainViewModel.incrementView();

        //Details Retrieval
        mainViewModel.retrieveContactInfo();

        //Observers
        mainViewModel.getNavigateFragment().observe(getViewLifecycleOwner(), integer ->
        {
            if (integer == MainViewModel.AR_ACTIVITY) {
                if(mainViewModel != null)
                {
                    boolean hasModel =  mainViewModel.currentItemHasModel();
                    if(hasModel) {
                        Intent intent = new Intent(getActivity(), ARActivity.class);
                        intent.putExtra("download", true);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(getActivity(), "This item does not have a model!",
                                Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
