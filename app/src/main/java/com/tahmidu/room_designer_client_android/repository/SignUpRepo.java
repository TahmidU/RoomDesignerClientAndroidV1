package com.tahmidu.room_designer_client_android.repository;

import android.content.Context;
import android.util.Log;

import com.tahmidu.room_designer_client_android.network.NetworkState;
import com.tahmidu.room_designer_client_android.network.NetworkStatus;
import com.tahmidu.room_designer_client_android.network.api.APIService;
import com.tahmidu.room_designer_client_android.network.api.RetrofitClient;
import com.tahmidu.room_designer_client_android.preferences.PreferenceProvider;

import java.util.Objects;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SignUpRepo implements ISignUpRepo
{
    private final String TAG = "SIGN_UP_REPO";

    //Repo instance.
    private static SignUpRepo instance;

    public static SignUpRepo getInstance() {
        if(instance == null)
            return instance = new SignUpRepo();
        return instance;
    }

    /**
     * Sign-up user.
     * @param context application context
     * @param firstName first name
     * @param lastName last name
     * @param password password
     * @param email email
     * @param phoneNum phone number
     */
    @Override
    public void signUp(Context context, String firstName, String lastName, String password, String email,
                       String phoneNum)
    {
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
        PreferenceProvider preferenceProvider = new PreferenceProvider(context);

        preferenceProvider.saveEmail(email);

        NetworkState.getInstance().setStatus(NetworkStatus.PENDING);
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
                        NetworkState.getInstance().setStatus(NetworkStatus.DONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError called");
                        Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                        NetworkState.getInstance().setStatus(NetworkStatus.ERROR);
                    }
                });
    }
}
