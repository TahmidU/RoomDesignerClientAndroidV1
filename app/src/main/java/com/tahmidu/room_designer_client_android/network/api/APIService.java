package com.tahmidu.room_designer_client_android.network.api;

import com.google.gson.JsonObject;
import com.tahmidu.room_designer_client_android.model.Item;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface APIService
{
    //-----------------------------Login-----------------------------

    @POST("/user/authenticate")
    Observable<Response<String>> authenticateUser(@Body JsonObject login);

    @POST("/login")
    Observable<Response<Void>> login(@Body JsonObject login);

    //-----------------------------SignUp-----------------------------

    @FormUrlEncoded
    @POST("/sign-up/v1")
    Completable signUp(@Field("firstName") String firstName, @Field("lastName") String lastName,
                             @Field("password") String password, @Field("email") String email,
                             @Field("phoneNum") String phoneNum);

    @FormUrlEncoded
    @POST("/sign-up/confirmation")
    Observable<Response<String>> verify(@Field("token") int token, @Field("email") String email);

    @FormUrlEncoded
    @POST("sign-up/resend-token")
    Completable resendToken(@Field("email") String email);

    //-----------------------------Password Recovery-----------------------------

    @FormUrlEncoded
    @POST("/password/recovery")
    Completable sendPasswordToken(@Field("email") String email);

    @FormUrlEncoded
    @POST("/password/recovery/change")
    Observable<Response<String>> changePassword(@Field("email") String email,
                                                @Field("token") int token,
                                                @Field("password") String password);

    //-----------------------------Item-----------------------------

    @GET("/item/fetch-all")
    Observable<List<Item>> fetchAllItem(@Query("pageNum") int pageNum, @Query("itemName") String itemName,
                                        @Query("catIds") List<Integer> catIds, @Query("typeIds") List<Integer> typeIds,
                                        @Query("hasModel") Boolean hasModel,
                                        @Header("Authorization") String authorization);

    @GET("/item/fetch/my")
    Observable<List<Item>> fetchUserItem(@Query("pageNum") int pageNum, @Query("itemName") String itemName,
                                         @Query("catIds") List<Integer> catIds, @Query("typeIds") List<Integer> typeIds,
                                         @Query("hasModel") Boolean hasModel,
                                         @Header("Authorization") String authorization);

    @Headers("Content-Type: application/json")
    @POST("/item/add")
    Observable<JsonObject> addItem(@Body JsonObject item, @Query("catName") String catName,
                                   @Query("typeName") String typeName,
                                   @Header("Authorization") String authorization);

    @DELETE("/item/remove")
    Observable<ResponseBody> removeItem(@Query("id") Long id,
                                        @Header("Authorization") String authorization);

    @PUT("/item/update")
    Observable<ResponseBody> editItem(@Body JsonObject item, @Query("catName") String catName,
                                      @Query("typeName") String typeName,
                                      @Header("Authorization") String authorization);

    //-----------------------------User-----------------------------

    @GET("/user/me")
    Observable<JsonObject> retrieveMyDetails(@Header("Authorization") String authorization);

    @PUT("/user/change-details")
    Observable<ResponseBody> changeUserDetails(@Body JsonObject user,
                                               @Header("Authorization") String authorization);

    @DELETE("/user/delete")
    Observable<ResponseBody> deleteUser(@Header("Authorization") String authorization);

    @GET("/user/details")
    Observable<JsonObject> retrieveUserDetails(@Query("userId") Long itemId,
                                               @Header("Authorization") String authorization);

    //-----------------------------Model-----------------------------

    @Streaming
    @GET("/model/fetch")
    Observable<ResponseBody> retrieveModel(@Query("modelId") Long modelId,
                                           @Header("Authorization") String authorization);

    @Multipart
    @POST("/model/upload")
    Observable<ResponseBody> uploadModel(@Part MultipartBody.Part[] files, @Query("itemId") Long itemId,
                                         @Header("Authorization") String authorization);

    @DELETE("/model/remove")
    Observable<ResponseBody> deleteModel(@Query("modelId") Long modelId,
                                         @Query("itemId") Long itemId,
                                         @Header("Authorization") String authorization);

    //-----------------------------Image-----------------------------

    @Multipart
    @POST("/image/upload")
    Observable<ResponseBody> uploadImages(@Part MultipartBody.Part[] files,
                                          @Query("itemId") Long itemId,
                                          @Query("isThumbnail") Boolean isThumbnail,
                                          @Header("Authorization") String authorization);

    @DELETE("/image/delete")
    Observable<ResponseBody> deleteImage(@Query("imageId") Long imageId,
                                         @Query("isThumbnail") Boolean isThumbnail,
                                         @Query("itemId") Long itemId,
                                         @Header("Authorization") String authorization);

    @GET("/image/fetch-thumbnail-id")
    Observable<Response<Long>> fetchThumbnailId(@Query("itemId") Long itemId,
                                              @Header("Authorization") String authorization);

    //-----------------------------Statistics-----------------------------

    @GET("/item-view/get-views")
    Observable<Response<Integer>> fetchViewStat(@Query("itemId") Long itemId,
                                                @Header("Authorization") String authorization);

    @GET("/item-download/aggregate")
    Observable<Response<Integer>> fetchDownloadStat(@Query("itemId") Long itemId,
                                                    @Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("/item-view/increment")
    Observable<ResponseBody> incrementView(@Field("itemId") Long itemId,
                                           @Header("Authorization") String authorization);

}