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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tahmidu.room_designer_client_android.R;
import com.tahmidu.room_designer_client_android.activity.ARActivity;
import com.tahmidu.room_designer_client_android.adapter.view_pager.ItemImagesViewPager;
import com.tahmidu.room_designer_client_android.databinding.FragmentUserItemBinding;
import com.tahmidu.room_designer_client_android.view_model.MainViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserItemFragment extends Fragment {

    private final String TAG = "USER_ITEM_FRAGMENT";
    private final String TOOLBAR_TITLE = "User Item";

    //Model View
    private MainViewModel mainViewModel;

    private FragmentUserItemBinding binding;

    //Recycler View
    private ViewPager imageSelectViewPager;
    private ItemImagesViewPager itemImagesViewPagerAdapter;

    //Navigation Controller
    private NavController navController;

    public UserItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "called");

        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_user_item, container, false);

        Toolbar toolbar = getActivity().findViewById(R.id.main_toolbar);
        toolbar.setTitle(TOOLBAR_TITLE);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        TextView arTextView = binding.getRoot().findViewById(R.id.user_item_ar_text_view);
        DrawerLayout drawerLayout = getActivity().findViewById(R.id.main_drawer);
        ImageButton deleteBtn = binding.getRoot().findViewById(R.id.user_item_delete_btn);
        ImageButton editBtn = binding.getRoot().findViewById(R.id.user_item_edit_btn);

        //Set Drawer
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        //Listener
        deleteBtn.setOnClickListener(v ->
        {
            mainViewModel.deleteSelectedItem();
            FragmentManager transactionManager = getActivity().getSupportFragmentManager();
            transactionManager.popBackStack();
        });

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //navController = Navigation.findNavController(view);

        imageSelectViewPager = getActivity().findViewById(R.id.user_item_images_view_pager);

        //Create the View Model.
        ViewModelProvider.Factory factory = ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication());
        //View Model
        mainViewModel = new ViewModelProvider(this, factory)
                .get(MainViewModel.class);

        //Set to binding
        binding.setVM(mainViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        //View Pager
        itemImagesViewPagerAdapter = new ItemImagesViewPager(getActivity().getApplicationContext(),
                mainViewModel.getSelectedItem().getImages());
        imageSelectViewPager.setAdapter(itemImagesViewPagerAdapter);

        //Increment Stat
        mainViewModel.incrementView();

        //Details Retrieval
        mainViewModel.retrieveContactInfo();
        mainViewModel.fetchItemStatistics();

        //Fragment transaction setup
        FragmentManager transactionMan = getActivity().getSupportFragmentManager();
        mainViewModel.getNavigateFragment().setValue(MainViewModel.USER_ITEM_FRAGMENT);

        //Observers
        mainViewModel.getNavigateFragment().observe(getViewLifecycleOwner(), integer ->
        {
            switch (integer)
            {
                case MainViewModel.AR_ACTIVITY:
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
                    break;
                case MainViewModel.EDIT_ITEM_FRAGMENT:
                    FragmentTransaction transaction = transactionMan.beginTransaction();
                    transaction.replace(R.id.fragment_main_container, new EditItemFragment());
                    transaction.addToBackStack(null);
                    transaction.commit();
                    break;
            }
        });

        mainViewModel.getFromItemToVR().observe(getViewLifecycleOwner(), aBoolean ->
        {
            Intent intent = new Intent(getActivity(), ARActivity.class);
            intent.putExtra("download", true);
            startActivity(intent);
        });
    }
}
