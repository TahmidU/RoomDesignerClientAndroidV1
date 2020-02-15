package com.tahmidu.room_designer_client_android.repository;

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

public class SignUpRepo implements ISignUpRepo
{
    private final String TAG = "SIGN_UP_REPO";

    private static SignUpRepo instance;

    public static SignUpRepo getInstance() {
        if(instance == null)
            return instance = new SignUpRepo();
        return instance;
    }

    @Override
    public void signUp(Context context, String firstName, String lastName, String password, String email,
                       String phoneNum)
    {
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
        PreferenceProvider preferenceProvider = new PreferenceProvider(context);

        preferenceProvider.saveEmail(email);

        final Completable postCompletable = apiService.signUp(firstName,lastName,password,email,
                phoneNum);
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
