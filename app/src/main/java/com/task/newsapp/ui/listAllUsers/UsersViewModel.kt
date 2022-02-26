package com.task.newsapp.ui.listAllUsers

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.task.newsapp.data.model.SocketUser

class UsersViewModel(application: Application) : AndroidViewModel(application) {
    private val _getUserViewModel = MutableLiveData<List<SocketUser>>()
    private val getSocketUserViewModel: LiveData<List<SocketUser>> = _getUserViewModel
    fun addListUsers(listSocketUser: List<SocketUser>) {
        _getUserViewModel.postValue(listSocketUser)
    }

    fun getAllUser() = getSocketUserViewModel
}