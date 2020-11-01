package com.nadim.gbe_gbe_final.API;

import com.nadim.gbe_gbe_final.Model.Login;
import com.nadim.gbe_gbe_final.Model.MyProfileModelClass;
import com.nadim.gbe_gbe_final.OrderActivity.Adeptar.OurDataSet;
import com.google.gson.JsonObject;
import com.nadim.gbe_gbe_final.PostOrderObjectClass.OrderProductClass;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Api {

    @POST("api/GbegbeUserResistration")
    Call<String> postReg(@Body JsonObject ussdObject);

    @POST("api/GbegbeUserResistration/PhoneNumberExist")
    Call<String> postCheckUser(@Body JsonObject ussdObject);

    @POST("api/GbegbeUserLogin")
    Call<Login> postLogin(@Body JsonObject ussdObject);


   @POST("api/OrderProduct/OrderProductTest")
   Call<String> productPostOrder(@Body OrderProductClass orderProductClass);



    @POST("api/GbegbeUserResistration/ForgotPassword")
    Call<String> postForget(@Body JsonObject ussdObject);


    @GET("OrderList/{userId}")
    Call<List<OurDataSet>> getDataSet(@Path("userId") int userId);

    @GET("api/GbegbeUserResistration/GetUser/{userId}")
    Call<MyProfileModelClass> getUserDataSet(@Path("userId") int userId);

    @POST("api/GbegbeUserResistration")
    Call<String> postProfileEdit(@Body JsonObject ussdObject);
}
