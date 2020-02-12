package com.tahmidu.room_designer_client_android.network.api;

import com.tahmidu.room_designer_client_android.model.Login;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService
{
    @HTTP(method = "POST", path = "/login", hasBody = true)
    Observable<Response<Void>> login(@Body Login login);

    @FormUrlEncoded
    @POST("/sign-up/v1")
    Observable<Void> signUp(@Field("firstName") String firstName, @Field("lastName") String lastName,
                            @Field("password") String password, @Field("email") String email,
                            @Field("phoneNum") String phoneNum);
}
