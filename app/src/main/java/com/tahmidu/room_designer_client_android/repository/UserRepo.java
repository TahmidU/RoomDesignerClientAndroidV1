package com.tahmidu.room_designer_client_android.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;
import com.tahmidu.room_designer_client_android.model.User;
import com.tahmidu.room_designer_client_android.network.NetworkState;
import com.tahmidu.room_designer_client_android.network.NetworkStatus;
import com.tahmidu.room_designer_client_android.network.api.APIService;
import com.tahmidu.room_designer_client_android.network.api.RetrofitClient;
import com.tahmidu.room_designer_client_android.preferences.PreferenceProvider;

import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UserRepo
{
    private final String TAG = "USER_REPO";

    //Repo instance
    private static UserRepo instance;

    public static UserRepo getInstance()
    {
        if(instance == null)
            return instance = new UserRepo();
        return instance;
    }

    public void fetchUserDetails(PreferenceProvider preferenceProvider, MutableLiveData<User> userLiveData)
    {
        final String METHOD_TAG = "fetchUserDetails";
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);

        String JWTToken = preferenceProvider.getJWTToken();

        NetworkState.getInstance().setStatus(NetworkStatus.PENDING);
        Observable<JsonObject> getObservable = apiService.retrieveUserDetails((long) -1 , JWTToken);
        getObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe called");
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        Log.d(TAG, "onNext called");

                        userLiveData.postValue(new User(jsonObject.get("firstName").getAsString(),
                                jsonObject.get("lastName").getAsString(),
                                jsonObject.get("phoneNum").getAsString(),
                                jsonObject.get("email").getAsString()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError called");
                        Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete called");
                    }
                });
    }

    public void deleteUser()
    {

    }
}
