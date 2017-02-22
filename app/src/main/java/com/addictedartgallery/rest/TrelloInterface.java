package com.addictedartgallery.rest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface TrelloInterface {

    @FormUrlEncoded
    @POST("1/cards/?key=cf8a12f35a7519e304c4361063c50570&token=3016983b751667f37d296e02570ad6cdcffa9b1bb1c5590ef6b695a14d8ac401")
    Call<ResponseBody> reportIssues(@Field("name") String name, @Field("desc") String description, @Field("due") String due, @Field("idList") String idList);
}
