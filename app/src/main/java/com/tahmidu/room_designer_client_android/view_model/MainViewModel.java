package com.tahmidu.room_designer_client_android.view_model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.tahmidu.room_designer_client_android.model.Item;
import com.tahmidu.room_designer_client_android.preferences.PreferenceProvider;
import com.tahmidu.room_designer_client_android.repository.ImageRepo;
import com.tahmidu.room_designer_client_android.repository.LibraryRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MainViewModel extends AndroidViewModel
{
    private final String TAG = "MAIN_VIEW_MODEL";

    //Navigation
    public final static int MAIN_LIB_FRAGMENT = 0;
    public final static int ITEM_FRAGMENT = 1;
    public final static int AR_FRAGMENT = 2;

    private final MutableLiveData<Integer> navigateFragment = new MutableLiveData<>();
    private final MutableLiveData<Boolean> fromItemToVR = new MutableLiveData<>();

    //Items View
    private final MutableLiveData<List<Item>> itemsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> contactInfoLiveData = new MutableLiveData<>();

    private int mainPageNum = 0;

    private final LibraryRepo libraryRepo;
    private final ImageRepo imageRepo;

    private PreferenceProvider preferenceProvider;

    public MainViewModel(@NonNull Application application)
    {
        super(application);

        itemsLiveData.setValue(new ArrayList<Item>());
        contactInfoLiveData.setValue("N/A");

        libraryRepo = LibraryRepo.getInstance();
        imageRepo = ImageRepo.getInstance();

        preferenceProvider = new PreferenceProvider(getApplication().getApplicationContext());
    }

    public void fetchMainLibrary()
    {
        libraryRepo.fetchMainLibrary(itemsLiveData, preferenceProvider.getJWTToken(), mainPageNum);
        mainPageNum++;
    }

    public void clickedItem(int listPosition) {
        Item item = Objects.requireNonNull(itemsLiveData.getValue()).get(listPosition);
        preferenceProvider.saveItem(item);
    }

    public void retrieveContactInfo()
    {
        libraryRepo.retrieveContactInfo(preferenceProvider.getItem().getUser(), contactInfoLiveData,
                preferenceProvider.getJWTToken(), preferenceProvider);
    }

    public void navigateFragment(int id)
    {
        Log.d(TAG, "Navigate to " + id);
        navigateFragment.postValue(id);
    }

    public void initVR(boolean download)
    {
        Log.d(TAG, "Initialise VR. Switching from Item to VR? " + download);
        navigateFragment.postValue(AR_FRAGMENT);
        fromItemToVR.postValue(download);
    }

    public Item getSelectedItem()
    {
        return preferenceProvider.getItem();
    }

    public MutableLiveData<Boolean> getFromItemToVR() {
        return fromItemToVR;
    }

    public MutableLiveData<Integer> getNavigateFragment() {
        return navigateFragment;
    }

    public MutableLiveData<List<Item>> getItemsLiveData() {
        return itemsLiveData;
    }

    public MutableLiveData<String> getContactInfoLiveData() {
        return contactInfoLiveData;
    }
}
