package com.tahmidu.room_designer_client_android.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.tahmidu.room_designer_client_android.model.GalleryItem;
import com.tahmidu.room_designer_client_android.model.Item;
import com.tahmidu.room_designer_client_android.network.NetworkState;
import com.tahmidu.room_designer_client_android.network.NetworkStatus;
import com.tahmidu.room_designer_client_android.network.api.APIService;
import com.tahmidu.room_designer_client_android.network.api.RetrofitClient;
import com.tahmidu.room_designer_client_android.util.file.CustomFileUtil;
import com.tahmidu.room_designer_client_android.util.file.CustomZip;
import java.io.File;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Repository that handles any AR related interactions/requests.
 */
public class ARRepo {
    private final String TAG = "AR_REPO";
    private final String MODEL = "MODEL";

    //Repo instance
    private static ARRepo instance;

    public static ARRepo getInstance() {
        if (instance == null)
            return instance = new ARRepo();
        return instance;
    }

    /**
     * Fetch the specified 3D model from the server.
     * @param modelId Model Id
     * @param JWTToken Authorization token
     * @param item Item
     * @param context App context
     * @param galleryItemsLiveData Live data containing a list of Gallery Items
     */
    public void fetchModel(Long modelId, String JWTToken, Item item, Context context,
                           final MutableLiveData<List<GalleryItem>> galleryItemsLiveData) {
        String storagePath = context.getFilesDir().getAbsolutePath() + "/" + item.getItemId()
                + "/" + MODEL;

        NetworkState.getInstance().setStatus(NetworkStatus.PENDING);
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
        final Observable<ResponseBody> getObservable = apiService.retrieveModel(modelId, JWTToken);
        getObservable
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .concatMap(responseBody -> {
                    Log.d(TAG, Thread.currentThread().getName());
                    return CustomZip.unzip(responseBody.bytes(), "response", storagePath);
                })
                .concatMap(aBoolean -> getGLTFModelInDir(storagePath, item, galleryItemsLiveData))
                .doOnComplete(() -> NetworkState.getInstance().setStatus(NetworkStatus.DONE))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }

    /**
     * Retrieve GLTF model from the specified directory.
     * @param directory directory
     * @param item Item
     * @param galleryItemsLiveData Live data containing a list of Gallery Items.
     * @return
     */
    private Observable<Boolean> getGLTFModelInDir(String directory, Item item,
                                                  final MutableLiveData<List<GalleryItem>> galleryItemsLiveData) {
        Log.d(TAG, Thread.currentThread().getName());
        Log.d(TAG, "Accessing: " + directory);

        String[] fileNames = new File(directory).list();
        if (fileNames != null)
        {
            for (String fileName : fileNames) {
                Log.d(TAG, "Found Files: " + fileName);
                String newDir = directory + "/" + fileName;
                if (CustomFileUtil.getExtension(newDir).equals(".gltf")) {
                    GalleryItem galleryItem = new GalleryItem(item,
                            newDir);
                    Log.d(TAG, galleryItem.toString());
                    List<GalleryItem> galleryItems = galleryItemsLiveData.getValue();
                    galleryItems.add(galleryItem);
                    galleryItemsLiveData.postValue(galleryItems);
                    break;
                }
            }
            return Observable.just(true);
        }
        return Observable.just(false);
    }
}
