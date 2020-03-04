package com.tahmidu.room_designer_client_android.repository;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.tahmidu.room_designer_client_android.model.Item;
import com.tahmidu.room_designer_client_android.model.Model;
import com.tahmidu.room_designer_client_android.model.User;
import com.tahmidu.room_designer_client_android.network.api.APIService;
import com.tahmidu.room_designer_client_android.network.api.RetrofitClient;
import com.tahmidu.room_designer_client_android.repository.database.DatabaseConnector;
import com.tahmidu.room_designer_client_android.util.CustomFileUtil;
import com.tahmidu.room_designer_client_android.util.CustomZip;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class ARRepo
{
    private final String TAG = "AR_REPO";
    private final String MODEL = "MODEL";

    //Repo instance
    private static ARRepo instance;

    public static ARRepo getInstance() {
        if(instance == null)
            return instance = new ARRepo();
        return instance;
    }

    public void fetchModel(Long modelId, String JWTToken, Item item, User user, Context context,
                           final MutableLiveData<String> arModelDirLiveData)
    {
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);

        final Observable<ResponseBody> getObservable = apiService.retrieveModel(modelId, JWTToken);
/*        getObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe called");
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                Log.d(TAG, "onNext called");

                CustomZip.unzip(item.getItemId()+"\\"+ MODEL, "response");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError called");
                Log.e(TAG, Objects.requireNonNull(e.getMessage()));
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete called");
            }
        });*/

        String storagePath = context.getFilesDir().getAbsolutePath() + "/" + item.getItemId()+"/"+ MODEL;

        getObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(responseBody -> CustomZip.unzip( responseBody.bytes(), "response", storagePath))
                .doOnComplete(() -> {
                    DatabaseConnector dbc = new DatabaseConnector(context);
                    dbc.insertUser(new User(user.getUserId(), user.getFirstName(), user.getLastName(),
                            user.getPhoneNum(), user.getEmail()));
                    getGLTFModelInDir(storagePath, arModelDirLiveData);
                })
                .subscribe();
    }

    private void getGLTFModelInDir(String directory, final MutableLiveData<String> arModelDirLiveData)
    {
        Log.d(TAG, "Accessing: "+directory);
        Completable.fromRunnable(() -> {
            String[] fileNames = new File(directory).list();
            if(fileNames != null)
                for(String fileName : fileNames)
                {
                    Log.d(TAG, "Found Files: " + fileName);
                    String newDir = directory + "/" + fileName;
                    if(CustomFileUtil.getExtension(newDir).equals(".gltf"))
                    {
                        arModelDirLiveData.postValue(newDir);
                        break;
                    }
                }
        }).subscribeOn(Schedulers.io()).subscribe();
    }
}
