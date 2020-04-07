package com.tahmidu.room_designer_client_android.repository;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.tahmidu.room_designer_client_android.network.NetworkState;
import com.tahmidu.room_designer_client_android.network.NetworkStatus;
import com.tahmidu.room_designer_client_android.network.api.APIService;
import com.tahmidu.room_designer_client_android.network.api.RetrofitClient;
import com.tahmidu.room_designer_client_android.preferences.PreferenceProvider;
import java.util.Objects;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Repository that handles any statistics related interactions/requests.
 */
public class StatisticRepo
{
    private final String TAG = "STATISTIC_REPO";

    //Repo instance
    private static StatisticRepo instance;

    public static StatisticRepo getInstance() {
        if(instance == null)
            return instance = new StatisticRepo();
        return instance;
    }

    /**
     * Retrieve statistics for downloads and views.
     * @param statistics statistics contains both view and download
     * @param preferenceProvider preference provider.
     */
    public void retrieveStatistics(final MutableLiveData<String> statistics,
                                   final PreferenceProvider preferenceProvider)
    {
        final String METHOD_TAG = "retrieveStatistics";
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);

        String JWTToken = preferenceProvider.getJWTToken();
        Long itemId = preferenceProvider.getItem().getItemId();

        NetworkState.getInstance().setStatus(NetworkStatus.PENDING);

        Observable<Response<Integer>> getDownloadStatObservable =
                apiService.fetchDownloadStat(itemId, JWTToken);

        getDownloadStatObservable
                .doOnNext(integerResponse ->
                {
                    Log.d(TAG, METHOD_TAG + " onNext called");
                    Log.d(TAG, METHOD_TAG + " OnNext download response: " + integerResponse.body());

                    Observable<Response<Integer>> getViewStatObservable =
                            apiService.fetchViewStat(itemId, JWTToken);

                    getViewStatObservable
                            .doOnNext(integerResponse1 ->
                            {
                                Log.d(TAG, METHOD_TAG + " onNext called");
                                Log.d(TAG, METHOD_TAG + " OnNext view response: "
                                        + integerResponse1.body());
                                statistics.postValue("Views: " + integerResponse1.body()
                                        + "\nDownloads: " + integerResponse.body());
                            })
                            .doOnError(throwable ->
                            {
                                Log.d(TAG, METHOD_TAG + " onError called");
                                Log.d(TAG, Objects.requireNonNull(throwable.getMessage()));
                                NetworkState.getInstance().setStatus(NetworkStatus.ERROR);
                            })
                            .doOnComplete(() ->
                            {
                                Log.d(TAG, METHOD_TAG + " onComplete called");
                                NetworkState.getInstance().setStatus(NetworkStatus.DONE);
                            })
                            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
                })
                .doOnError(throwable ->
                {
                    Log.d(TAG, METHOD_TAG + " onError called");
                    Log.d(TAG, Objects.requireNonNull(throwable.getMessage()));
                    NetworkState.getInstance().setStatus(NetworkStatus.ERROR);
                })
                .doOnComplete(() ->
                {
                    Log.d(TAG, METHOD_TAG + " onComplete called");
                    NetworkState.getInstance().setStatus(NetworkStatus.DONE);
                })
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    /**
     * Increment view counter.
     * @param preferenceProvider preference provider
     */
    public void incrementView(final PreferenceProvider preferenceProvider)
    {
        final String METHOD_TAG = "incrementView";
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);

        String JWTToken = preferenceProvider.getJWTToken();
        Long itemId = preferenceProvider.getItem().getItemId();

        NetworkState.getInstance().setStatus(NetworkStatus.PENDING);
        Observable<ResponseBody> getObservable = apiService.incrementView(itemId, JWTToken);
        getObservable
                .doOnError(throwable -> {
                    Log.d(TAG, METHOD_TAG + " onError called");
                    Log.d(TAG, Objects.requireNonNull(throwable.getMessage()));
                    NetworkState.getInstance().setStatus(NetworkStatus.ERROR);
                })
                .doOnComplete(() ->
                {
                    Log.d(TAG, METHOD_TAG + " onComplete called");
                    NetworkState.getInstance().setStatus(NetworkStatus.DONE);
                })
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }
}
