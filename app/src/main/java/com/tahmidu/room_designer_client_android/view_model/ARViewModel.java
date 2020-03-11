package com.tahmidu.room_designer_client_android.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.tahmidu.room_designer_client_android.preferences.PreferenceProvider;
import com.tahmidu.room_designer_client_android.repository.ARRepo;
import com.tahmidu.room_designer_client_android.util.SingleLiveEvent;

public class ARViewModel extends AndroidViewModel
{

    private ARRepo arRepo;

    private PreferenceProvider preferenceProvider;

    //New items directory that's not in cache.
    private MutableLiveData<String> arModelDirLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isTrackingInit = new MutableLiveData<>();

    public ARViewModel(@NonNull Application application) {
        super(application);

        arRepo = ARRepo.getInstance();

        preferenceProvider = new PreferenceProvider(getApplication().getApplicationContext());
    }

    public void fetchModel()
    {
        arRepo.fetchModel(preferenceProvider.getItem().getModel(), preferenceProvider.getJWTToken(),
                preferenceProvider.getItem(), preferenceProvider.getItemsUser(),
                getApplication().getApplicationContext(), arModelDirLiveData);
    }

    public MutableLiveData<String> getArModelDirLiveData() {
        return arModelDirLiveData;
    }

    public MutableLiveData<Boolean> getIsTracking() {
        return isTrackingInit;
    }

    public void setIsTracking(boolean isTracking)
    {
        if(isTrackingInit != null)
        {
            this.isTrackingInit.setValue(isTracking);
            this.isTrackingInit = null;
        }
    }
}
