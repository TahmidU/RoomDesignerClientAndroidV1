package com.tahmidu.room_designer_client_android.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;
import com.tahmidu.room_designer_client_android.model.User;
import com.tahmidu.room_designer_client_android.network.NetworkState;
import com.tahmidu.room_designer_client_android.network.NetworkStatus;
import com.tahmidu.room_designer_client_android.network.api.APIService;
import com.tahmidu.room_designer_client_android.network.api.RetrofitClient;
import com.tahmidu.room_designer_client_android.util.preferences.PreferenceProvider;

import java.util.Objects;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Repository which handles any user related interactions/requests.
 */
public class UserRepo
{
    private final String TAG = "USER_REPO";

    //Expected Response Body
    public static final String RESPONSE_OK = "";
    private static final String RESPONSE_ERROR = "ERROR";
    private final String ERROR = "Token not found";

    //Repo instance
    private static UserRepo instance;

    public static UserRepo getInstance()
    {
        if(instance == null)
            return instance = new UserRepo();
        return instance;
    }

    /**
     * Retrieve JWT Token.
     * @param email email
     * @param password password
     * @param token mutable live data containing JWT Token
     */
    public void retrieveToken(String email, String password, final MutableLiveData<String> token,
                              final PreferenceProvider preferenceProvider,
                              final MutableLiveData<String> signInResponse)
    {
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);

        preferenceProvider.saveEmail(email);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email", email);
        jsonObject.addProperty("password", password);

        NetworkState.getInstance().setStatus(NetworkStatus.PENDING);
        final Observable<Response<Void>> tokenObservable = apiService.login(jsonObject);
        tokenObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Response<Void>>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe called");
            }

            @Override
            public void onNext(Response<Void> voidResponse) {
                Log.d(TAG, "onNext called");

                String message = voidResponse.headers().get("Message");
                assert message != null;
                if(message.equals("Success"))
                {
                    String responseHeader = voidResponse.headers().get("Authorization");
                    preferenceProvider.saveJWTToken(responseHeader);
                    token.postValue(responseHeader);
                }else
                    signInResponse.postValue(message);

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError called");
                Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                NetworkState.getInstance().setStatus(NetworkStatus.ERROR);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete called");
                NetworkState.getInstance().setStatus(NetworkStatus.PENDING);
            }
        });
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

    /**
     * Verify the account using the token and email (from preference) provided by user.
     * @param context application context
     * @param code verify code
     * @param verifyResponse mutable live data
     */
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

    /**
     * Send password token.
     * @param email email
     */
    public void sendToken(String email)
    {
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);

        NetworkState.getInstance().setStatus(NetworkStatus.PENDING);
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

        NetworkState.getInstance().setStatus(NetworkStatus.PENDING);
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
     * Fetch User Details using the JWT Token.
     * @param preferenceProvider preference provider
     * @param userLiveData Live data containing User
     */
    public void fetchUserDetails(PreferenceProvider preferenceProvider, MutableLiveData<User> userLiveData)
    {
        final String METHOD_TAG = "fetchUserDetails";
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);

        String JWTToken = preferenceProvider.getJWTToken();

        NetworkState.getInstance().setStatus(NetworkStatus.PENDING);
        Observable<JsonObject> getObservable = apiService.retrieveMyDetails(JWTToken);
        getObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, METHOD_TAG + " onSubscribe called");
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        Log.d(TAG, METHOD_TAG + " onNext called");

                        userLiveData.postValue(new User(jsonObject.get("userId").getAsLong(),
                                jsonObject.get("firstName").getAsString(),
                                jsonObject.get("lastName").getAsString(),
                                jsonObject.get("phoneNum").getAsString(),
                                jsonObject.get("email").getAsString()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, METHOD_TAG + " onError called");
                        Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, METHOD_TAG + " onComplete called");
                    }
                });
    }

    /**
     * Change User details. JWT token will be used to authorise this change.
     * @param preferenceProvider preference provider
     * @param firstName first name
     * @param lastName last name
     * @param password password
     * @param phoneNum phone number
     */
    public void changeUserDetails(PreferenceProvider preferenceProvider, String firstName,
                                  String lastName, String password, String phoneNum)
    {
        final String METHOD_TAG = "changeUserDetails";
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);

        String JWTToken = preferenceProvider.getJWTToken();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("firstName", firstName);
        jsonObject.addProperty("lastName", lastName);
        jsonObject.addProperty("password", password);
        jsonObject.addProperty("phoneNum", phoneNum);

        NetworkState.getInstance().setStatus(NetworkStatus.PENDING);
        Observable<ResponseBody> getObservable = apiService.changeUserDetails(jsonObject, JWTToken);
        getObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, METHOD_TAG + " onSubscribe called");
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        Log.d(TAG, METHOD_TAG + " onNext called");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, METHOD_TAG + " onError called");
                        Log.d(TAG, METHOD_TAG + " " + e.getMessage());
                        NetworkState.getInstance().setStatus(NetworkStatus.ERROR);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, METHOD_TAG + " onComplete called");
                        NetworkState.getInstance().setStatus(NetworkStatus.DONE);
                    }
                });
    }

    /**
     * Delete user. JWT token will be used to authorise deletion.
     * @param preferenceProvider preference provider
     */
    public void deleteUser(PreferenceProvider preferenceProvider)
    {
        final String METHOD_TAG = "changeUserDetails";
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);

        String JWTToken = preferenceProvider.getJWTToken();

        NetworkState.getInstance().setStatus(NetworkStatus.PENDING);
        Observable<ResponseBody> getObservable = apiService.deleteUser(JWTToken);
        getObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, METHOD_TAG + " onSubscribe called");
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        Log.d(TAG, METHOD_TAG + " onNext called");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, METHOD_TAG + " onError called");
                        Log.d(TAG, METHOD_TAG + " " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, METHOD_TAG + " onComplete called");
                    }
                });
    }


}
