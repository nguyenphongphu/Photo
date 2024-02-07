package com.tp.photo.Api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tp.photo.Model.Photo;
import com.tp.photo.Model.User;
import com.tp.photo.Utility.Constants;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterface {
    Gson gson=new GsonBuilder().setDateFormat("yyyy MM dd HH:mm:ss").create();
    ApiInterface API_INTERFACE =new Retrofit.Builder()
            .baseUrl(Constants.KEY_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiInterface.class);
    @Multipart
    @POST("register")
    Call<User> register(
            @Part(Constants.KEY_NAME)RequestBody username,
            @Part(Constants.KEY_EMAIL) RequestBody email,
            @Part(Constants.KEY_PASSWORD) RequestBody password,
            @Part MultipartBody.Part image);
    @FormUrlEncoded
    @POST("login")
    Call<User> login(
            @Field(Constants.KEY_EMAIL) String email,
            @Field(Constants.KEY_PASSWORD) String password
    );

    @Multipart
    @POST("upload")
    Call<Photo> upload(
            @Part(Constants.KEY_EMAIL) RequestBody email,
            @Part MultipartBody.Part image);


}
