package com.task.newsapp.data.remote

import com.task.newsapp.data.model.AllUsers
import com.task.newsapp.data.model.SignInResponse
import com.task.newsapp.data.model.SignUpResponse
import retrofit2.Response
import retrofit2.http.*
import java.util.HashMap

interface NewsApi {

    @POST("/api/v1/signin")
    suspend fun getLoginUser(
        @QueryMap queries: HashMap<String, String> ,
    ): Response<SignInResponse>

    @FormUrlEncoded
    @POST("/api/v1/signup")
    suspend fun getRegisterUser(
        @Field("fullname") fullname: String,
        @Field("phone") phone: String,
        @Field("password") password: String
    ): Response<SignUpResponse>


    @GET("/api/v1/getAll")
    suspend fun getAllUsers(
    ): Response<AllUsers>
}