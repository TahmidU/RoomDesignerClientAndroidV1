package com.tahmidu.room_designer_client_android.fragment;


import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.tahmidu.room_designer_client_android.R;
import com.tahmidu.room_designer_client_android.adapter.MainRecyclerAdapter;
import com.tahmidu.room_designer_client_android.databinding.FragmentMainLibraryBinding;
import com.tahmidu.room_designer_client_android.network.NetworkState;
import com.tahmidu.room_designer_client_android.network.NetworkStatus;
import com.tahmidu.room_designer_client_android.view_model.MainViewModel;
import net.cachapa.expandablelayout.ExpandableLayout;
import java.util.ArrayList;
import static com.tahmidu.room_designer_client_android.constant.Exit.DURATION;
import static com.tahmidu.room_designer_client_android.constant.Exit.TOAST_EXIT_MSG;

/**
 * Main Library.
 */
public class MainLibraryFragment extends Fragment {

    private final String TAG = "MAIN_LIB_FRAGMENT";

    //Model View
    private MainViewModel mainViewModel;

    //Binding
    private FragmentMainLibraryBinding binding;

    //RecyclerView
    private MainRecyclerAdapter mainRecyclerAdapter;

    //Filter
    private ExpandableLayout filterSortExpandable;

    //Exit Application
    private long targetTime = 0;

    public MainLibraryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_main_library, container, false);

        Toolbar toolbar = getActivity().findViewById(R.id.main_toolbar);
        String TOOLBAR_TITLE = "Main Library";
        toolbar.setTitle(TOOLBAR_TITLE);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        //Filter
        LinearLayout filterSortLayout = binding.getRoot().findViewById(R.id.filter_and_sort_layout);
        filterSortExpandable = binding.getRoot().findViewById(R.id.filter_sort_expandable);
        ImageButton closeFilterBtn = binding.getRoot().findViewById(R.id.close_sub_filter_btn);

        //Set up navigation drawer
        DrawerLayout drawerLayout = getActivity().findViewById(R.id.main_drawer);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        //Listeners
        filterSortLayout.setOnClickListener(v -> {
            filterSortExpandable.setVisibility(View.VISIBLE);
            filterSortExpandable.expand();
        });

        closeFilterBtn.setOnClickListener(v ->
        {

            filterSortExpandable.collapse();
            if(mainViewModel != null && mainRecyclerAdapter != null)
                if(mainViewModel.isFilterChanged()){
                    resetRecyclerView();
                }
        });

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
        RecyclerView recyclerView = getActivity().findViewById(R.id.main_lib_recycler_view);

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
        mainViewModel.getNavigateFragment().setValue(MainViewModel.MAIN_LIB_FRAGMENT);

        //Observers
        mainViewModel.getItemsLiveData().observe(getViewLifecycleOwner(),
                items ->{
                    Log.d(TAG, "Observer notified!");
                    mainRecyclerAdapter.updateList(items);
                } );

        mainViewModel.getNavigateFragment().observe(getViewLifecycleOwner(), integer ->
        {
            Log.d(TAG, "Navigation to " + integer);
            if (integer == MainViewModel.ITEM_FRAGMENT) {
                FragmentTransaction transaction = transactionMan.beginTransaction();
                transaction.replace(R.id.fragment_main_container, new ItemFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        //Recycler View
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,
                false));
        mainRecyclerAdapter = new MainRecyclerAdapter(mainViewModel.getItemsLiveData().getValue(),
                getActivity().getApplicationContext(), (view1, position) -> {
                    mainViewModel.clickedItem(position);
                    mainViewModel.getNavigateFragment().postValue(MainViewModel.ITEM_FRAGMENT);
                });
        recyclerView.setAdapter(mainRecyclerAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d(TAG, "Can the user scroll? " + recyclerView.canScrollVertically(1));
                if(!recyclerView.canScrollVertically(1)
                        && NetworkState.getInstance().getStatus().equals(NetworkStatus.DONE))
                {
                    Log.d(TAG, "Adapter item count: "+ mainRecyclerAdapter.getItemCount());
                    mainViewModel.fetchMainLibrary();
                }
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_ar_main, menu);
        MenuItem searchItem = menu.findItem(R.id.main_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "Query Submitted: " + query);
                mainViewModel.setSearchItem(query);
                resetRecyclerView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                Log.d(TAG, "Search new text: " + newText);
                if(newText.equals(""))
                {
                    mainViewModel.setSearchItem(null);
                    resetRecyclerView();
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        resetRecyclerView();
    }

    private void resetRecyclerView()
    {
        mainViewModel.resetPage();
        mainViewModel.getItemsLiveData().setValue(new ArrayList<>());
        mainRecyclerAdapter.resetList();
        mainViewModel.fetchMainLibrary();
        mainViewModel.setFilterChanged(false);
    }
}
