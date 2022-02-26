package com.kadirkuruca.newsapp.ui.chat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kadirkuruca.newsapp.data.model.Message
import com.kadirkuruca.newsapp.db.ChatDatabase
import com.kadirkuruca.newsapp.repository.ChatRepository
import kotlinx.coroutines.launch

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val chatRepository =
        ChatRepository(ChatDatabase(application.applicationContext))
    fun insert(message: Message) = viewModelScope.launch {
        chatRepository.insert(message)
    }
    fun getAllChat(id: String) = chatRepository.getAllChat(id)

}