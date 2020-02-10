package com.tahmidu.room_designer_client_android.api;

import com.tahmidu.room_designer_client_android.userAuthentication.login.Login;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;

public interface APIService
{
    @HTTP(method = "POST", path = "/login", hasBody = true)
    Observable<Response<Void>> login(@Body Login login);
}
