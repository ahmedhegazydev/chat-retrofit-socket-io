package com.kadirkuruca.newsapp.repository

import com.kadirkuruca.newsapp.data.local.ArticleDao
import com.kadirkuruca.newsapp.data.model.*
import com.kadirkuruca.newsapp.data.remote.NewsApi
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val newsApi: NewsApi,
    private val articleDao: ArticleDao
) {


    suspend fun getLoginUser(queries: HashMap<String, String>): Response<SignInResponse> {
        return newsApi.getLoginUser(queries)
    }

    suspend fun getUserRegister(
//        queries: HashMap<String, String>
//        queries: HashMap<String, RequestBody>
        fullname: String,
        phone: String,
        password: String
    ): Response<SignUpResponse> {
        return newsApi.getRegisterUser(
//            queries
            fullname,
            phone, password
        )
    }

    suspend fun getAllUsers(
    ): Response<AllUsers> {
        return newsApi.getAllUsers(
        )
    }

//    suspend fun searchNews(searchQuery: String, pageNumber: Int): Response<NewsResponse>{
//        return newsApi.searchForNews(searchQuery, pageNumber)
//    }

//    suspend fun getBreakingNews(countryCode: String, pageNumber: Int): Response<NewsResponse> {
//        return newsApi.getBreakingNews(countryCode,pageNumber)
//    }
//
//    suspend fun searchNews(searchQuery: String, pageNumber: Int): Response<NewsResponse>{
//        return newsApi.searchForNews(searchQuery, pageNumber)
//    }

//    fun getAllArticles() = articleDao.getArticles()
//
//    suspend fun insertArticle(register: Article) = articleDao.insert(register)
//
//    suspend fun deleteArticle(register: Article) = articleDao.delete(register)
//
//    suspend fun deleteAllArticles() = articleDao.deleteAllArticles()
}