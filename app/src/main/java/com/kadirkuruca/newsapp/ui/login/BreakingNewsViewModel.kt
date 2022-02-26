package com.kadirkuruca.newsapp.ui.login

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kadirkuruca.newsapp.data.model.AllUsers
import com.kadirkuruca.newsapp.data.model.NewsResponse
import com.kadirkuruca.newsapp.data.model.SignInResponse
import com.kadirkuruca.newsapp.data.model.SignUpResponse
import com.kadirkuruca.newsapp.repository.NewsRepository
import com.kadirkuruca.newsapp.util.Resource
import com.kadirkuruca.newsapp.util.hasInternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class BreakingNewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    //    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val loginUser: MutableLiveData<Resource<SignInResponse>> = MutableLiveData()
    val registerUser: MutableLiveData<Resource<SignUpResponse>> = MutableLiveData()
    val allUsers: MutableLiveData<Resource<AllUsers>> = MutableLiveData()

    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null

    init {
//        getBreakingNews("tr")
    }

    fun getLoginUser(queries: HashMap<String, String>) = viewModelScope.launch {
        safeBreakingNewsCall(queries)
    }

    private suspend fun safeBreakingNewsCall(queries: HashMap<String, String>) {
        loginUser.postValue(Resource.Loading())
        try {
            if (hasInternetConnection(context)) {
                val response = newsRepository.getLoginUser(queries)
                loginUser.postValue(handleBreakingNewsResponse(response))
            } else {
                loginUser.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (ex: Exception) {
            when (ex) {
                is IOException -> loginUser.postValue(Resource.Error("Network Failure"))
                else -> loginUser.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleBreakingNewsResponse(response: Response<SignInResponse>): Resource<SignInResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
//                breakingNewsPage++
//                if (breakingNewsResponse == null)
//                    breakingNewsResponse = resultResponse
//                else {
//                    val oldArticles = breakingNewsResponse?.articles
//                    val newArticles = resultResponse.articles
//                    oldArticles?.addAll(newArticles)
//                }
//                return Resource.Success(breakingNewsResponse ?: resultResponse)
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleRegisterResponse(response: Response<SignUpResponse>): Resource<SignUpResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
//                breakingNewsPage++
//                if (breakingNewsResponse == null)
//                    breakingNewsResponse = resultResponse
//                else {
//                    val oldArticles = breakingNewsResponse?.articles
//                    val newArticles = resultResponse.articles
//                    oldArticles?.addAll(newArticles)
//                }
//                return Resource.Success(breakingNewsResponse ?: resultResponse)
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleAllUsersResponse(response: Response<AllUsers>): Resource<AllUsers> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
//                breakingNewsPage++
//                if (breakingNewsResponse == null)
//                    breakingNewsResponse = resultResponse
//                else {
//                    val oldArticles = breakingNewsResponse?.articles
//                    val newArticles = resultResponse.articles
//                    oldArticles?.addAll(newArticles)
//                }
//                return Resource.Success(breakingNewsResponse ?: resultResponse)
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun getRegisterUser(
//        queries: HashMap<String, String>
//        queries: HashMap<String, RequestBody>
        fullname: String,
        phone: String,
        password: String
    ) = viewModelScope.launch {
        registerUser.postValue(Resource.Loading())
        try {
            if (hasInternetConnection(context)) {
                val response = newsRepository.getUserRegister(
//                    queries
                    fullname,
                    phone,
                    password
                )
                registerUser.postValue(handleRegisterResponse(response))
            } else {
                registerUser.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (ex: Exception) {
            when (ex) {
                is IOException -> registerUser.postValue(Resource.Error("Network Failure"))
                else -> registerUser.postValue(Resource.Error("Conversion Error"))
            }
        }
    }


    fun getAllUsers(
    ) = viewModelScope.launch {
        allUsers.postValue(Resource.Loading())
        try {
            if (hasInternetConnection(context)) {
                val response = newsRepository.getAllUsers(
                )
                allUsers.postValue(handleAllUsersResponse(response))
            } else {
                allUsers.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (ex: Exception) {
            when (ex) {
                is IOException -> allUsers.postValue(Resource.Error("Network Failure"))
                else -> allUsers.postValue(Resource.Error("Conversion Error"))
            }
        }
    }
}