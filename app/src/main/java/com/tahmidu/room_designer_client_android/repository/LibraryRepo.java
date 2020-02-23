package com.tahmidu.room_designer_client_android.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.tahmidu.room_designer_client_android.model.Item;
import com.tahmidu.room_designer_client_android.network.api.APIService;
import com.tahmidu.room_designer_client_android.network.api.RetrofitClient;
import com.tahmidu.room_designer_client_android.preferences.PreferenceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LibraryRepo
{
    private final String TAG = "LIBRARY_REPO";

    //Repo instance
    private static LibraryRepo instance;

    public static LibraryRepo getInstance() {
        if(instance == null)
            return instance = new LibraryRepo();
        return instance;
    }

    public void fetchMainLibrary(final MutableLiveData<List<Item>> itemsLiveData,
                                 final String JWTToken, int pageNum)
    {
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);

        final Observable<List<Item>> getObservable = apiService.fetchItem(pageNum,
                null, null, null, null, JWTToken);
        getObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<Item>>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe called");
            }

            @Override
            public void onNext(List<Item> items) {
                Log.d(TAG, "onNext called");
                if(items != null)
                {
                    Log.d(TAG, "onNext adding new items");
                    Log.d(TAG, items.get(0).getName());
                    List<Item> newItems = itemsLiveData.getValue();

                    if(newItems == null)
                        newItems = new ArrayList<>();



                    newItems.addAll(items);
                    itemsLiveData.postValue(newItems);
                }else
                    Log.d(TAG, "onNext no new items");
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
        });
    }
}
