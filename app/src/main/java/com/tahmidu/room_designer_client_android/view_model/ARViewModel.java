package com.tahmidu.room_designer_client_android.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.tahmidu.room_designer_client_android.model.GalleryItem;
import com.tahmidu.room_designer_client_android.model.Item;
import com.tahmidu.room_designer_client_android.preferences.PreferenceProvider;
import com.tahmidu.room_designer_client_android.repository.ARRepo;
import com.tahmidu.room_designer_client_android.repository.LibraryRepo;

import java.util.ArrayList;
import java.util.List;

public class ARViewModel extends AndroidViewModel
{
    private PreferenceProvider preferenceProvider;

    //New items directory that's not in cache.
    private MutableLiveData<List<GalleryItem>> galleryItemsLiveData = new MutableLiveData<>();
    private ARRepo arRepo;

    private final MutableLiveData<List<Item>> itemsLiveData = new MutableLiveData<>();
    private final LibraryRepo libraryRepo;

    public ARViewModel(@NonNull Application application) {
        super(application);

        arRepo = ARRepo.getInstance();
        libraryRepo = LibraryRepo.getInstance();

        preferenceProvider = new PreferenceProvider(getApplication().getApplicationContext());

        galleryItemsLiveData.setValue(new ArrayList<>());
        itemsLiveData.setValue(new ArrayList<>());
    }

    //-----------------------------Model-----------------------------

    public void fetchModel()
    {
        arRepo.fetchModel(preferenceProvider.getItem().getModel(), preferenceProvider.getJWTToken(),
                preferenceProvider.getItem(), getApplication().getApplicationContext(),
                galleryItemsLiveData);
    }

    public void fetchModel(Item item)
    {
        arRepo.fetchModel(item.getModel(), preferenceProvider.getJWTToken(), item,
                getApplication().getApplicationContext(), galleryItemsLiveData);
    }

    //-----------------------------Item-----------------------------

    public void fetchItems()
    {
        libraryRepo.fetchMainLibrary(itemsLiveData, null, null, null,
                preferenceProvider);
    }

    //-----------------------------Aux-----------------------------

    public void resetPage()
    {
        preferenceProvider.savePage(0);
    }

    //-----------------------------Getters and Setters-----------------------------

    public MutableLiveData<List<GalleryItem>> getGalleryItemsLiveData() {
        return galleryItemsLiveData;
    }

    public MutableLiveData<List<Item>> getItemsLiveData() {
        return itemsLiveData;
    }
}
