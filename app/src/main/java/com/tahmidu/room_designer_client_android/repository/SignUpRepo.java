package com.tahmidu.room_designer_client_android.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.tahmidu.room_designer_client_android.network.api.APIService;
import com.tahmidu.room_designer_client_android.network.api.RetrofitClient;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SignUpRepo implements ISignUpRepo
{
    private final String TAG = "SIGNUP_REPO";

    private static SignUpRepo instance;

    private final MutableLiveData<Void> callBackSignUp = new MutableLiveData<>();

    public static SignUpRepo getInstance() {
        if(instance == null)
            return instance = new SignUpRepo();
        return instance;
    }

    @Override
    public void signUp(String firstName, String lastName, String password, String email,
                       String phoneNum)
    {
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);

        final Observable<Void> postObservable = apiService.signUp(firstName,lastName,password,email,
                phoneNum);
        postObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Void>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe called");
            }

            @Override
            public void onNext(Void aVoid) {
                Log.d(TAG, "onNext called");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError called");
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete called");
            }
        });
    }

    public MutableLiveData<Void> getCallBackSignUp() {
        return callBackSignUp;
    }
}
