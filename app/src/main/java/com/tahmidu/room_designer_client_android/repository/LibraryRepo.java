package com.tahmidu.room_designer_client_android.repository;

import android.content.ContentResolver;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;
import com.jaiselrahman.filepicker.model.MediaFile;
import com.tahmidu.room_designer_client_android.model.Item;
import com.tahmidu.room_designer_client_android.model.User;
import com.tahmidu.room_designer_client_android.network.NetworkState;
import com.tahmidu.room_designer_client_android.network.NetworkStatus;
import com.tahmidu.room_designer_client_android.network.api.APIService;
import com.tahmidu.room_designer_client_android.network.api.RetrofitClient;
import com.tahmidu.room_designer_client_android.preferences.PreferenceProvider;
import com.tahmidu.room_designer_client_android.util.file.CustomFileUtil;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class LibraryRepo
{
    private final String TAG = "LIBRARY_REPO";
    private String cachePath = "";

    //Repo instance
    private static LibraryRepo instance;

    public static LibraryRepo getInstance() {
        if(instance == null)
            return instance = new LibraryRepo();
        return instance;
    }

    public void fetchMainLibrary(final MutableLiveData<List<Item>> itemsLiveData,
                                 String itemName, List<Integer> catIds, List<Integer> typeIds,
                                 PreferenceProvider preferenceProvider)
    {
        final String METHOD_TAG = "fetchMainLibrary";
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);

        Integer pageNum = preferenceProvider.getPage();
        String JWTToken = preferenceProvider.getJWTToken();

        NetworkState.getInstance().setStatus(NetworkStatus.PENDING);
        final Observable<List<Item>> getObservable = apiService.fetchAllItem(pageNum,
                itemName, catIds, typeIds, null, JWTToken);
        getObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<Item>>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, METHOD_TAG + " onSubscribe called");
            }

            @Override
            public void onNext(List<Item> items) {
                Log.d(TAG, METHOD_TAG + " onNext called");
                if(items != null)
                {
                    if(items.size() > 0) {
                        Log.d(TAG, "onNext adding new items");
                        List<Item> newItems = itemsLiveData.getValue();

                        if (newItems == null)
                            newItems = new ArrayList<>();

                        newItems.addAll(items);
                        itemsLiveData.postValue(newItems);
                        preferenceProvider.savePage(pageNum+9);
                    }else
                        Log.d(TAG, "onNext no new items");
                }else
                    Log.d(TAG, "onNext no new items");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, METHOD_TAG + " onError called");
                Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                NetworkState.getInstance().setStatus(NetworkStatus.ERROR);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, METHOD_TAG + " onComplete called");
                NetworkState.getInstance().setStatus(NetworkStatus.DONE);
            }
        });
    }

    public void fetchUserLibrary(final MutableLiveData<List<Item>> userItemsLiveData,
                                 PreferenceProvider preferenceProvider)
    {
        final String METHOD_TAG = "fetchUserLibrary";
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);

        Integer pageNum = preferenceProvider.getPage();
        String JWTToken = preferenceProvider.getJWTToken();

        NetworkState.getInstance().setStatus(NetworkStatus.PENDING);

        Observable<List<Item>> getObservable = apiService.fetchUserItem(pageNum, null,
                null, null, null, JWTToken);
        getObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<Item>>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, METHOD_TAG + " onSubscribe called");
            }

            @Override
            public void onNext(List<Item> items) {
                Log.d(TAG, METHOD_TAG + "onNext called");
                if(items != null)
                {
                    if(items.size() > 0) {
                        Log.d(TAG, "onNext adding new items");
                        List<Item> newItems = userItemsLiveData.getValue();

                        if (newItems == null)
                            newItems = new ArrayList<>();

                        newItems.addAll(items);
                        userItemsLiveData.postValue(newItems);
                        preferenceProvider.savePage(pageNum+9);
                    }else
                        Log.d(TAG, "onNext no new items");
                }else
                    Log.d(TAG, "onNext no new items");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, METHOD_TAG + " onError called");
                Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                NetworkState.getInstance().setStatus(NetworkStatus.ERROR);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, METHOD_TAG + "onComplete called");
                NetworkState.getInstance().setStatus(NetworkStatus.DONE);
            }
        });
    }

    public void retrieveContactInfo(final long userId, final MutableLiveData<String> contactInfo,
                                    final String JWTToken, final PreferenceProvider preferenceProvider)
    {
        final String METHOD_TAG = "retrieveContactInfo";
        Log.d(TAG, METHOD_TAG + " called");
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);

        NetworkState.getInstance().setStatus(NetworkStatus.PENDING);
        final Observable<JsonObject> getObservable = apiService.retrieveUserDetails(userId, JWTToken);
        getObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, METHOD_TAG+" onSubscribe called");
                    }

                    @Override
                    public void onNext(JsonObject jsonObject)
                    {
                        Log.d(TAG, METHOD_TAG + " onNext called");

                        User user = new User(userId, jsonObject.get("firstName").getAsString(),
                                jsonObject.get("lastName").getAsString(),
                                jsonObject.get("phoneNum").getAsString(),
                                jsonObject.get("email").getAsString());

                        preferenceProvider.saveItemsUser(jsonObject.toString());

                        contactInfo.postValue("First Name: " + user.getFirstName() + "\n" +
                                "Last Name: " + user.getLastName() + "\n" +
                                "Email: " + user.getEmail() + "\n" +
                                "Phone Number: " + user.getPhoneNum());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, METHOD_TAG + " onError called");
                        Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                        contactInfo.postValue("N/A");
                        NetworkState.getInstance().setStatus(NetworkStatus.ERROR);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, METHOD_TAG + " onComplete called");
                        Log.d(TAG, "onComplete " + contactInfo.getValue());
                        NetworkState.getInstance().setStatus(NetworkStatus.DONE);
                    }
                });
    }

    public void submitItem(final PreferenceProvider preferenceProvider,
                           final String title,
                           final String desc,
                           final String catName,
                           final String typeName,
                           final List<MediaFile> previewImages,
                           final MediaFile thumbnail, final List<MediaFile> modelFiles,
                           ContentResolver contentResolver)
    {
        final String METHOD_TAG = "submitItem";
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);

        String JWTToken = preferenceProvider.getJWTToken();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        boolean hasModel = false;

        if(modelFiles != null && !modelFiles.isEmpty())
            hasModel = true;

        JsonObject jsonObject = new JsonObject();

        Log.d(TAG, "title: " + title);
        Log.d(TAG, "desc: " + desc);
        Log.d(TAG, "date: " + date);
        Log.d(TAG, "hasModel: " + hasModel);

        jsonObject.addProperty("name", title);
        jsonObject.addProperty("description", desc);
        jsonObject.addProperty("date", date);
        jsonObject.addProperty("hasModel", hasModel);

        NetworkState.getInstance().setStatus(NetworkStatus.PENDING);
        final Observable<JsonObject> getObservable = apiService.addItem(jsonObject, catName,
                typeName, JWTToken);

        getObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, METHOD_TAG + " onSubscribe called");
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        Log.d(TAG, METHOD_TAG + " onNext called");

                        if(jsonObject != null)
                        {
                            long itemId = jsonObject.get("itemId").getAsLong();
                            Log.d(TAG, "Item created: " + itemId);
                            if(thumbnail != null)
                                submitThumbnail(itemId, thumbnail, JWTToken, contentResolver);
                            if(previewImages.size() > 0)
                                submitImages(itemId, previewImages, JWTToken, contentResolver);
                            if(modelFiles.size() > 0)
                                submitModels(itemId, modelFiles, JWTToken, contentResolver);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, METHOD_TAG + " onError called");
                        Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                        NetworkState.getInstance().setStatus(NetworkStatus.ERROR);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, METHOD_TAG + " onComplete called");
                        NetworkState.getInstance().setStatus(NetworkStatus.DONE);
                    }
                });
    }

    public void editItem(final PreferenceProvider preferenceProvider,
                         final String title,
                         final String desc,
                         final String catName,
                         final String typeName,
                         final List<MediaFile> previewImages,
                         final MediaFile thumbnail, final List<MediaFile> modelFiles,
                         ContentResolver contentResolver)
    {
        final String METHOD_TAG = "editItem";
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);

        String JWTToken = preferenceProvider.getJWTToken();
        long itemId = preferenceProvider.getItem().getItemId();

        JsonObject jsonObject = new JsonObject();

        Log.d(TAG, "title: " + title);
        Log.d(TAG, "desc: " + desc);

        jsonObject.addProperty("itemId", itemId);
        jsonObject.addProperty("name", title);
        jsonObject.addProperty("description", desc);
        jsonObject.addProperty("date", "2020-01-01");
        jsonObject.addProperty("hasModel", false);

        NetworkState.getInstance().setStatus(NetworkStatus.PENDING);
        final Observable<ResponseBody> getObservable = apiService.editItem(jsonObject, catName,
                typeName, JWTToken);

        getObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, METHOD_TAG + " onSubscribe called");
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        Log.d(TAG, METHOD_TAG + " onNext called");

                        if(thumbnail != null)
                            submitThumbnail(itemId, thumbnail, JWTToken, contentResolver);
                        if(previewImages.size() > 0)
                            submitImages(itemId, previewImages, JWTToken, contentResolver);
                        if(modelFiles.size() > 0)
                            submitModels(itemId, modelFiles, JWTToken, contentResolver);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, METHOD_TAG + " onError called");
                        Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                        NetworkState.getInstance().setStatus(NetworkStatus.ERROR);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, METHOD_TAG + " onComplete called");
                        NetworkState.getInstance().setStatus(NetworkStatus.DONE);
                    }
                });
    }

    public void removeItem(long itemId, PreferenceProvider preferenceProvider)
    {
        final String METHOD_TAG = "removeItem";
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);

        String JWTToken = preferenceProvider.getJWTToken();

        NetworkState.getInstance().setStatus(NetworkStatus.PENDING);
        Observable<ResponseBody> getObservable = apiService.removeItem(itemId, JWTToken);
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
                        Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                        NetworkState.getInstance().setStatus(NetworkStatus.ERROR);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, METHOD_TAG + " onComplete called");
                        NetworkState.getInstance().setStatus(NetworkStatus.DONE);
                    }
                });

    }

    public void removeModel(PreferenceProvider preferenceProvider)
    {
        final String METHOD_TAG = "removeModel";
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);

        String JWTToken = preferenceProvider.getJWTToken();
        Long itemId = preferenceProvider.getItem().getItemId();
        Long modelId = preferenceProvider.getItem().getModel();

        NetworkState.getInstance().setStatus(NetworkStatus.PENDING);
        Observable<ResponseBody> getObservable = apiService.deleteModel(modelId, itemId, JWTToken);
        getObservable.doOnError(throwable ->
        {
            Log.d(TAG, METHOD_TAG + " onError called");
            Log.e(TAG, Objects.requireNonNull(throwable.getMessage()));
            NetworkState.getInstance().setStatus(NetworkStatus.ERROR);
        }).doOnComplete(() ->
        {
            Log.d(TAG, METHOD_TAG + " onComplete called");
            NetworkState.getInstance().setStatus(NetworkStatus.DONE);
        })
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void removeThumbnail(PreferenceProvider preferenceProvider)
    {
        final String METHOD_TAG = "removeThumbnail";

        String JWTToken = preferenceProvider.getJWTToken();
        long itemId = preferenceProvider.getItem().getItemId();

        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
        NetworkState.getInstance().setStatus(NetworkStatus.PENDING);

        Observable<Response<Long>> getObservable = apiService.fetchThumbnailId(itemId, JWTToken);
        getObservable.doOnNext(longResponse -> {
            Log.d(TAG, METHOD_TAG + " onNext called");
            Long imageId = longResponse.body();
            Log.d(TAG, METHOD_TAG + "imageId: " + imageId + ", itemId: " + itemId +
                    " JWTToken: " + JWTToken);
            if(imageId != -1)
            {
                apiService.deleteImage(imageId, true, itemId, JWTToken)
                        .doOnError(throwable ->
                        {
                            Log.d(TAG, METHOD_TAG + " onError called");
                            Log.e(TAG, Objects.requireNonNull(throwable.getMessage()));
                            NetworkState.getInstance().setStatus(NetworkStatus.ERROR);
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
            }
        }).doOnComplete(() -> {
            Log.d(TAG, METHOD_TAG + " onComplete called");
            NetworkState.getInstance().setStatus(NetworkStatus.DONE);
        }).doOnError(throwable ->
        {
            Log.d(TAG, METHOD_TAG + " onError called");
            Log.e(TAG, Objects.requireNonNull(throwable.getMessage()));
            NetworkState.getInstance().setStatus(NetworkStatus.ERROR);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void removeImages(PreferenceProvider preferenceProvider)
    {
        final String METHOD_TAG = "removeImages";

        String JWTToken = preferenceProvider.getJWTToken();
        long itemId = preferenceProvider.getItem().getItemId();
        List<Long> imageIds = preferenceProvider.getItem().getImages();

        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
        NetworkState.getInstance().setStatus(NetworkStatus.PENDING);

        Observable<Response<Long>> getObservable = apiService.fetchThumbnailId(itemId, JWTToken);
        getObservable.doOnNext(longResponse -> {
            Log.d(TAG, METHOD_TAG + " onNext called");
            Long imageId = longResponse.body();
            for(long i : imageIds) {
                Log.d(TAG, METHOD_TAG + "imageId: " + imageId + ", itemId: " + itemId +
                        " JWTToken: " + JWTToken);
                if (imageId != -1) {
                    if(imageId != i) {
                        apiService.deleteImage(i, false, itemId, JWTToken)
                                .doOnComplete(() -> {
                                    Log.d(TAG, METHOD_TAG + " onComplete called");
                                })
                                .doOnError(throwable ->
                                {
                                    Log.d(TAG, METHOD_TAG + " onError called");
                                    Log.e(TAG, Objects.requireNonNull(throwable.getMessage()));
                                    NetworkState.getInstance().setStatus(NetworkStatus.ERROR);
                                })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe();
                    }
                }else
                    {
                        apiService.deleteImage(i, false, itemId, JWTToken)
                                .doOnComplete(() -> {
                                    Log.d(TAG, METHOD_TAG + " onComplete called");
                                })
                                .doOnError(throwable ->
                                {
                                    Log.d(TAG, METHOD_TAG + " onError called");
                                    Log.e(TAG, Objects.requireNonNull(throwable.getMessage()));
                                    NetworkState.getInstance().setStatus(NetworkStatus.ERROR);
                                })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe();
                    }
            }
        }).doOnComplete(() -> {
            Log.d(TAG, METHOD_TAG + " onComplete called");
            NetworkState.getInstance().setStatus(NetworkStatus.DONE);
        }).doOnError(throwable ->
        {
            Log.d(TAG, METHOD_TAG + " onError called");
            Log.e(TAG, METHOD_TAG + " " + Objects.requireNonNull(throwable.getMessage()));
            NetworkState.getInstance().setStatus(NetworkStatus.ERROR);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void submitThumbnail(long itemId, MediaFile thumbnail, String JWTToken, ContentResolver contentResolver)
    {
        final String METHOD_TAG = "submitThumbnail";
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
        NetworkState.getInstance().setStatus(NetworkStatus.PENDING);

        Log.d(TAG, METHOD_TAG + " file path: "+thumbnail.getUri());
        RequestBody requestFile = null;
        try {

            File file = new File(cachePath+"/"+thumbnail.getName());
            CustomFileUtil.copyInputStreamToFile(contentResolver.openInputStream(thumbnail.getUri()), file);
            requestFile = RequestBody.create(file, MediaType.parse("multipart/form-data"));
        }catch (IOException e) {
            e.printStackTrace();
        }

        MultipartBody.Part file = MultipartBody.Part.createFormData("file", thumbnail.getName(),
                requestFile);
        MultipartBody.Part[] files = new MultipartBody.Part[1];
        files[0] = file;

        final Observable<ResponseBody> getObservable =
                apiService.uploadImages(files, itemId,
                        true, JWTToken);
        getObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, METHOD_TAG + "onSubscribe called");
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        Log.d(TAG, METHOD_TAG + "onNext called");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, METHOD_TAG + "onError called");
                        Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                        NetworkState.getInstance().setStatus(NetworkStatus.ERROR);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, METHOD_TAG + "onComplete called");
                        NetworkState.getInstance().setStatus(NetworkStatus.DONE);
                    }
                });
    }

    private void submitImages(long itemId, List<MediaFile> previewImages, String JWTToken, ContentResolver contentResolver)
    {
        final String METHOD_TAG = "submitImages";
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
        NetworkState.getInstance().setStatus(NetworkStatus.PENDING);

        Log.d(TAG, METHOD_TAG + " previewImagesFiles size: " + previewImages.size());
        MultipartBody.Part[] files = new MultipartBody.Part[previewImages.size()];
        int count = 0;
        for(MediaFile previewImage : previewImages)
        {
            Log.d(TAG, METHOD_TAG + " file path: "+previewImage.getUri());

            RequestBody requestFile = null;
            try {
                File file = new File(cachePath+"/"+previewImage.getName());
                CustomFileUtil.copyInputStreamToFile(contentResolver.openInputStream(previewImage.getUri()), file);
                requestFile = RequestBody.create(file, MediaType.parse("multipart/form-data"));

            }catch (IOException e) {
                e.printStackTrace();
            }
            MultipartBody.Part file = MultipartBody.Part.createFormData("file", previewImage.getName(),
                    requestFile);
            files[count] = file;
            count++;
        }
        Log.d(TAG, METHOD_TAG + " files size: " + files.length);

        final Observable<ResponseBody> getObservable =
                apiService.uploadImages(files, itemId,
                        false, JWTToken);
        getObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, METHOD_TAG +"onSubscribe called");
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        Log.d(TAG, METHOD_TAG + "onNext called");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, METHOD_TAG + "onError called");
                        Log.e(TAG, METHOD_TAG + Objects.requireNonNull(e.getMessage()));
                        NetworkState.getInstance().setStatus(NetworkStatus.ERROR);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, METHOD_TAG + "onComplete called");
                        NetworkState.getInstance().setStatus(NetworkStatus.DONE);
                    }
                });
    }

    private void submitModels(long itemId, List<MediaFile> modelFiles, String JWTToken, ContentResolver contentResolver)
    {
        final String METHOD_TAG = "submitModels";
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
        NetworkState.getInstance().setStatus(NetworkStatus.PENDING);

        Log.d(TAG, METHOD_TAG + " modelFiles size: " + modelFiles.size());
        MultipartBody.Part[] files = new MultipartBody.Part[modelFiles.size()];
        int count = 0;
        for(MediaFile modelFile : modelFiles)
        {
            Log.d(TAG, METHOD_TAG + " file path: "+ modelFile.getUri());
            RequestBody requestFile = null;
            try {
                File file = new File(cachePath+"/"+modelFile.getName());
                CustomFileUtil.copyInputStreamToFile(contentResolver.openInputStream(modelFile.getUri()), file);
                requestFile = RequestBody.create(file, MediaType.parse("multipart/form-data"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            MultipartBody.Part file = MultipartBody.Part.createFormData("file", modelFile.getName(),
                    requestFile);
            files[count] = file;
            count++;
        }
        Log.d(TAG, METHOD_TAG + " files size: " + files.length);

        final Observable<ResponseBody> getObservable = apiService.uploadModel(files, itemId, JWTToken);
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
                        Log.d(TAG,  METHOD_TAG + " onError called");
                        Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                        NetworkState.getInstance().setStatus(NetworkStatus.ERROR);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, METHOD_TAG + " onComplete called");
                        NetworkState.getInstance().setStatus(NetworkStatus.DONE);
                    }
                });
    }

    public String getCachePath() {
        return cachePath;
    }

    public void setCachePath(String cachePath) {
        this.cachePath = cachePath;
        File file = new File(cachePath);
        if(!file.exists())
            Log.d(TAG, "Cache path directory created? " + file.mkdirs());
    }
}
