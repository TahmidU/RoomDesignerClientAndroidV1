package com.tahmidu.room_designer_client_android.userAuthentication.login;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.tahmidu.room_designer_client_android.api.APIService;
import com.tahmidu.room_designer_client_android.api.RetrofitClient;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepo implements ILoginRepo
{
    private static LoginRepo instance;
    private final String TAG = "LOGIN_REPO";

    private final MutableLiveData<String> token = new MutableLiveData<>();

    public static LoginRepo getInstance() {
        if(instance == null)
            return instance = new LoginRepo();
        return instance;
    }

    @Override
    public MutableLiveData<String> retrieveToken(String email, String password)
    {
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
        token.setValue("");

        Observable<Response<Void>> tokenObservable = apiService.login(new Login(email, password));
        tokenObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Response<Void>>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe called");
            }

            @Override
            public void onNext(Response<Void> voidResponse) {
                Log.d(TAG, "onNext called");

                String responseHeader = voidResponse.headers().get("Authorization");
                if(responseHeader != null)
                    token.postValue(responseHeader);
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

        return token;
    }

    public MutableLiveData<String> getToken() {
        return token;
    }
}
