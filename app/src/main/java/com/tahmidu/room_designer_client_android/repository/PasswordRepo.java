package com.tahmidu.room_designer_client_android.repository;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.tahmidu.room_designer_client_android.network.api.APIService;
import com.tahmidu.room_designer_client_android.network.api.RetrofitClient;
import java.util.Objects;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class PasswordRepo implements IPasswordRepo
{
    private final String TAG = "PASSWORD_REPO";

    //Repo instance.
    private static PasswordRepo instance;

    //Responses
    private final String ERROR = "Token not found";

    public static PasswordRepo getInstance() {
        if(instance == null)
            return instance = new PasswordRepo();
        return instance;
    }

    /**
     * Send password token.
     * @param email email
     */
    public void sendToken(String email)
    {
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);

        Completable postCompletable = apiService.sendPasswordToken(email);
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
                        Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                    }
                });
    }

    /**
     * Change user password.
     * @param email email
     * @param token token
     * @param password password
     * @param passwordVerifyResponse mutable live data response
     */
    public void changePassword(String email, int token, String password,
                               final MutableLiveData<String> passwordVerifyResponse)
    {
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);

        Observable<Response<String>> postObservable = apiService.changePassword(email, token,
                password);
        postObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe called");
                    }

                    @Override
                    public void onNext(Response<String> stringResponse) {
                        Log.d(TAG, "onNext called");
                        Log.d(TAG, "onNext " + stringResponse.body());

                        if(stringResponse.body() != null)
                            passwordVerifyResponse.postValue(stringResponse.body());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError called");
                        Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                        passwordVerifyResponse.postValue(ERROR);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete called");
                    }
                });
    }
}
