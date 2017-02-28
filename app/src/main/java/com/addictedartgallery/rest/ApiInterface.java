package com.addictedartgallery.rest;

import com.addictedartgallery.model.AuthenticateGetUser;
import com.addictedartgallery.model.AuthenticatePostUser;
import com.addictedartgallery.model.FacebookResponse;
import com.addictedartgallery.model.Profile;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface ApiInterface {

    @GET("en/Authenticate")
    Call<AuthenticateGetUser> getAuthToken();

    @FormUrlEncoded
    @POST("en/Authenticate")
    Call<AuthenticatePostUser> getAccessToken(@Field("access_expire") String accessExpire, @Field("auth_request") String authRequest, @Field("expire") String expire, @Field("username") String username, @Field("authenticate") String authenticate);

    @FormUrlEncoded
    @POST("en/account/socialmob")
    Call<FacebookResponse> getFacebookAccessToken(@Field("type") String provider, @Field("token") String token, @Field("socialid") String socialId , @Field("access_expire") String accessExpire);

    @GET("en/account/profile")
    Call<Profile> getUserProfile(@Header("Authenticate") String authenticate);


}
