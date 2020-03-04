package com.tahmidu.room_designer_client_android.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.tahmidu.room_designer_client_android.preferences.PreferenceProvider;
import com.tahmidu.room_designer_client_android.repository.ARRepo;

public class ARViewModel extends AndroidViewModel
{

    private ARRepo arRepo;

    private PreferenceProvider preferenceProvider;

    private MutableLiveData<String> arModelDirLiveData = new MutableLiveData<>();

    public ARViewModel(@NonNull Application application) {
        super(application);

        arRepo = ARRepo.getInstance();

        preferenceProvider = new PreferenceProvider(getApplication().getApplicationContext());
    }

    public void fetchModel()
    {
        arRepo = ARRepo.getInstance();
        arRepo.fetchModel(preferenceProvider.getItem().getModel(), preferenceProvider.getJWTToken(),
                preferenceProvider.getItem(), preferenceProvider.getItemsUser(),
                getApplication().getApplicationContext(), arModelDirLiveData);
    }

    public MutableLiveData<String> getArModelDirLiveData() {
        return arModelDirLiveData;
    }
}
