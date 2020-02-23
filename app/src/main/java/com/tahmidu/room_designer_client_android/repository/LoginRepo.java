package com.tahmidu.room_designer_client_android.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.tahmidu.room_designer_client_android.network.api.APIService;
import com.tahmidu.room_designer_client_android.network.api.RetrofitClient;
import com.tahmidu.room_designer_client_android.model.Login;
import com.tahmidu.room_designer_client_android.preferences.PreferenceProvider;
import com.tahmidu.room_designer_client_android.view_model.WelcomeViewModel;

import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class LoginRepo implements ILoginRepo
{
    private final String TAG = "LOGIN_REPO";

    //Repo instance
    private static LoginRepo instance;

    public static LoginRepo getInstance() {
        if(instance == null)
            return instance = new LoginRepo();
        return instance;
    }

    /**
     * Retrieve JWT Token.
     * @param email email
     * @param password password
     * @param token mutable live data containing JWT Token
     */
    @Override
    public void retrieveToken(String email, String password, final MutableLiveData<String> token,
                              final PreferenceProvider preferenceProvider)
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
                {
                    preferenceProvider.saveJWTToken(responseHeader);
                    token.postValue(responseHeader);
                }

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

    /**
     * Authenticate user.
     * @param email email
     * @param password password
     * @param signUpResponse Mutable live data containing sign-up response
     * @param progressVisibility Mutable live data containing progress view visibility change
     * @param navFragment Mutable live data containing navigation
     * @param token Mutable live data containing JWT Token
     */
    public void authUser(final String email, final String password,
                         final MutableLiveData<String> signUpResponse,
                         final MutableLiveData<Boolean> progressVisibility,
                         final MutableLiveData<Integer> navFragment,
                         final MutableLiveData<String> token,
                         final PreferenceProvider preferenceProvider)
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

                        if(stringResponse.body() != null) {
                            if (stringResponse.body().equals(""))
                                retrieveToken(email, password, token, preferenceProvider);
                            if (stringResponse.body().equals("This Account is not active."))
                                navFragment.postValue(WelcomeViewModel.VERIFY_EMAIL_FRAGMENT);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError called");
                        Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                        progressVisibility.postValue(true);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete called");
                        progressVisibility.postValue(true);
                    }
                });
    }
}
