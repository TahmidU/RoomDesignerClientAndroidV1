package com.tahmidu.room_designer_client_android.repository;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;

import com.tahmidu.room_designer_client_android.network.NetworkState;
import com.tahmidu.room_designer_client_android.network.NetworkStatus;
import com.tahmidu.room_designer_client_android.network.api.APIService;
import com.tahmidu.room_designer_client_android.network.api.RetrofitClient;
import com.tahmidu.room_designer_client_android.preferences.PreferenceProvider;
import java.util.Objects;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


public class VerifyRepo implements IVerifyRepo
{
    private final String TAG = "VERIFY_REPO";

    //Expected Response Body
    public static final String RESPONSE_OK = "";
    private static final String RESPONSE_ERROR = "ERROR";

    //Repo instance
    private static VerifyRepo instance;

    public static VerifyRepo getInstance() {
        if(instance == null)
            return instance = new VerifyRepo();
        return instance;
    }

    /**
     * Verify the account using the token and email (from preference) provided by user.
     * @param context application context
     * @param code verify code
     * @param verifyResponse mutable live data
     */
    @Override
    public void verify(Context context, String code, final MutableLiveData<String> verifyResponse)
    {
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
        PreferenceProvider preferenceProvider = new PreferenceProvider(context);

        String email = preferenceProvider.getEmail();

        NetworkState.getInstance().setStatus(NetworkStatus.PENDING);
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
                Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                verifyResponse.postValue(RESPONSE_ERROR);
                NetworkState.getInstance().setStatus(NetworkStatus.ERROR);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete called");
                NetworkState.getInstance().setStatus(NetworkStatus.DONE);
            }
        });
    }

    /**
     * Resend token.
     * @param context application context
     */
    @Override
    public void resendToken(Context context)
    {
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
        PreferenceProvider preferenceProvider = new PreferenceProvider(context);

        String email = preferenceProvider.getEmail();

        NetworkState.getInstance().setStatus(NetworkStatus.PENDING);
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
