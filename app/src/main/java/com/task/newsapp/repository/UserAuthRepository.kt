package com.task.newsapp.repository

import com.task.newsapp.data.local.ChatDao
import com.task.newsapp.data.model.*
import com.task.newsapp.data.remote.NewsApi
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserAuthRepository @Inject constructor(
    private val newsApi: NewsApi,
    private val chatDao: ChatDao
) {


    suspend fun getLoginUser(queries: HashMap<String, String>): Response<SignInResponse> {
        return newsApi.getLoginUser(queries)
    }

    suspend fun getUserRegister(
        fullname: String,
        phone: String,
        password: String
    ): Response<SignUpResponse> {
        return newsApi.getRegisterUser(
            fullname,
            phone, password
        )
    }

    suspend fun getAllUsers(
    ): Response<AllUsers> {
        return newsApi.getAllUsers(
        )
    }

}