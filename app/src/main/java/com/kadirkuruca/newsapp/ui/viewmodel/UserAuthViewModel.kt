package com.kadirkuruca.newsapp.ui.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kadirkuruca.newsapp.data.local.ChatDao
import com.kadirkuruca.newsapp.data.model.AllUsers
import com.kadirkuruca.newsapp.data.model.SignInResponse
import com.kadirkuruca.newsapp.data.model.SignUpResponse
import com.kadirkuruca.newsapp.repository.UserAuthRepository
import com.kadirkuruca.newsapp.util.Resource
import com.kadirkuruca.newsapp.util.hasInternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class UserAuthViewModel @Inject constructor(
    private val userAuthRepository: UserAuthRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val loginUser: MutableLiveData<Resource<SignInResponse>> = MutableLiveData()
    val registerUser: MutableLiveData<Resource<SignUpResponse>> = MutableLiveData()
    val allUsers: MutableLiveData<Resource<AllUsers>> = MutableLiveData()

    init {
    }

    fun getLoginUser(queries: HashMap<String, String>) = viewModelScope.launch {
        safeLoginUserCall(queries)
    }

    private suspend fun safeLoginUserCall(queries: HashMap<String, String>) {
        loginUser.postValue(Resource.Loading())
        try {
            if (hasInternetConnection(context)) {
                val response = userAuthRepository.getLoginUser(queries)
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
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleRegisterResponse(response: Response<SignUpResponse>): Resource<SignUpResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleAllUsersResponse(response: Response<AllUsers>): Resource<AllUsers> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun getRegisterUser(
        fullname: String,
        phone: String,
        password: String
    ) = viewModelScope.launch {
        registerUser.postValue(Resource.Loading())
        try {
            if (hasInternetConnection(context)) {
                val response = userAuthRepository.getUserRegister(
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
                val response = userAuthRepository.getAllUsers(
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