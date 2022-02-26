package com.kadirkuruca.newsapp.data.remote

import com.kadirkuruca.newsapp.data.model.AllUsers
import com.kadirkuruca.newsapp.data.model.NewsResponse
import com.kadirkuruca.newsapp.data.model.SignInResponse
import com.kadirkuruca.newsapp.data.model.SignUpResponse
import com.kadirkuruca.newsapp.util.API_KEY
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import java.util.HashMap

interface NewsApi {

    @POST("/api/v1/signin")
    suspend fun getLoginUser(
        @QueryMap queries: HashMap<String, String> ,
//        @Query("page") pageNumber: Int = 1,
//        @Query("apiKey") apiKey: String = API_KEY
    ): Response<SignInResponse>

    @FormUrlEncoded
//    @Multipart
    @POST("/api/v1/signup")
    suspend fun getRegisterUser(
//        @PartMap queries: HashMap<String, RequestBody>,
//        @QueryMap queries: HashMap<String, String> ,
//        @Query("fullname") fullname: String,
//        @Query("phone") phone: String,
//        @Query("password") password: String
        @Field("fullname") fullname: String,
        @Field("phone") phone: String,
        @Field("password") password: String
    ): Response<SignUpResponse>


    @GET("/api/v1/getAll")
    suspend fun getAllUsers(
    ): Response<AllUsers>
}