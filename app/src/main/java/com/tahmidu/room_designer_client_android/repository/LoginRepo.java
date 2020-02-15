package com.tahmidu.room_designer_client_android.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.tahmidu.room_designer_client_android.network.api.APIService;
import com.tahmidu.room_designer_client_android.network.api.RetrofitClient;
import com.tahmidu.room_designer_client_android.model.Login;
import com.tahmidu.room_designer_client_android.viewModel.WelcomeViewModel;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
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
    public void retrieveToken(String email, String password)
    {
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);

        final Observable<Response<Void>> tokenObservable = apiService.login(new Login(email, password));
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
    }

    public void authUser(final String email, final String password,
                         final MutableLiveData<String> signUpResponse,
                         final MutableLiveData<Boolean> progressVisibility,
                         final MutableLiveData<Integer> navFragment)
    {
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
        progressVisibility.postValue(true);

        final Observable<Response<String>> responseObservable = apiService.authenticateUser(email,
                password);
        responseObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe called");
                    }

                    @Override
                    public void onNext(Response<String> stringResponse) {
                        Log.d(TAG, "onNext called");
                        signUpResponse.postValue(stringResponse.body());
                        Log.d(TAG, "OnNext " + stringResponse.body());

                        if(stringResponse.body() != null)
                            if(stringResponse.body().equals(""))
                                retrieveToken(email, password);
                            if(stringResponse.body().equals("This Account is not active."))
                                navFragment.postValue(WelcomeViewModel.VERIFY_EMAIL_FRAGMENT);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError called");
                        Log.e(TAG, e.getMessage());
                        progressVisibility.postValue(true);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete called");
                        progressVisibility.postValue(true);
                    }
                });
    }

    public MutableLiveData<String> getToken() {
        return token;
    }
}
