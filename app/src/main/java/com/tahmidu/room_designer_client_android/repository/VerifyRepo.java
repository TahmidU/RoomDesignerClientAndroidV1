package com.tahmidu.room_designer_client_android.repository;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.tahmidu.room_designer_client_android.network.api.APIService;
import com.tahmidu.room_designer_client_android.network.api.RetrofitClient;
import com.tahmidu.room_designer_client_android.preferences.PreferenceProvider;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


public class VerifyRepo
{
    private final String TAG = "VERIFY_REPO";

    //Expected Response Body
    public static final String RESPONSE_OK = "";
    public static final String RESPONSE_ERROR = "ERROR";

    private static VerifyRepo instance;


    public static VerifyRepo getInstance() {
        if(instance == null)
            return instance = new VerifyRepo();
        return instance;
    }

    public void verify(Context context, String code, final MutableLiveData<String> verifyResponse)
    {
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
        PreferenceProvider preferenceProvider = new PreferenceProvider(context);

        String email = preferenceProvider.getEmail();

        final Observable<Response<String>> postObservable = apiService.verify(Integer.parseInt(code),
                email);
        postObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<String>>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe called");
            }

            @Override
            public void onNext(Response<String> stringResponse) {
                Log.d(TAG, "onNext called");
                verifyResponse.postValue(stringResponse.body());
                Log.d(TAG, "OnNext " + stringResponse.body());
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError called");
                Log.e(TAG, e.getMessage());
                verifyResponse.postValue(RESPONSE_ERROR);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete called");
            }
        });
    }

    public void resendToken(Context context)
    {
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
        PreferenceProvider preferenceProvider = new PreferenceProvider(context);

        String email = preferenceProvider.getEmail();

        final Completable postCompletable = apiService.resendToken(email);
        postCompletable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe called");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete called");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError called");
                        Log.e(TAG, e.getMessage());
                    }
                });
    }
}
