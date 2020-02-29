package com.tahmidu.room_designer_client_android.network.api;

import com.tahmidu.room_designer_client_android.model.Item;
import com.tahmidu.room_designer_client_android.model.Login;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService
{
    @HTTP(method = "POST", path = "/login", hasBody = true)
    Observable<Response<Void>> login(@Body Login login);

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

    @GET("/user/authenticate")
    Observable<Response<String>> authenticateUser(@Query("email") String email,
                                                  @Query("password") String password);

    @FormUrlEncoded
    @POST("/password/recovery")
    Completable sendPasswordToken(@Field("email") String email);

    @FormUrlEncoded
    @POST("/password/recovery/change")
    Observable<Response<String>> changePassword(@Field("email") String email,
                                                @Field("token") int token,
                                                @Field("password") String password);

    @GET("/item/fetch-all")
    Observable<List<Item>> fetchItem(@Query("pageNum") int pageNum, @Query("itemName") String itemName,
                                     @Query("catId") Integer catId, @Query("typeId") Integer typeId,
                                     @Query("hasModel") Boolean hasModel,
                                     @Header("Authorization") String authorization);

    @GET("/user/contact-info")
    Observable<Response<String>> retrieveContactInfo(@Query("userId") Long itemId,
                                                     @Header("Authorization") String authorization);

/*    @FormUrlEncoded
    @POST("image/fetch-thumbnail")
    Observable<Multipart> fetchItemImage(@Field("itemId") int itemId,
                                         @Header("Authorization") String authorization);*/
}
