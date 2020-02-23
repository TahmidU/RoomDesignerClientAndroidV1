package com.tahmidu.room_designer_client_android.repository;

import androidx.lifecycle.MutableLiveData;

import com.tahmidu.room_designer_client_android.model.Item;
import com.tahmidu.room_designer_client_android.network.api.APIService;
import com.tahmidu.room_designer_client_android.network.api.RetrofitClient;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Multipart;

public class ImageRepo
{
    private final String TAG = "IMAGE_REPO";

    //Repo instance
    private static ImageRepo instance;

    public static ImageRepo getInstance() {
        if(instance == null)
            return instance = new ImageRepo();
        return instance;
    }

    public void fetchItemsImages(final MutableLiveData<List<Item>> itemsLiveData,
                                 final String JWTToken)
    {
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);

/*        final Observable<Multipart> postObsevable*/
    }
}
