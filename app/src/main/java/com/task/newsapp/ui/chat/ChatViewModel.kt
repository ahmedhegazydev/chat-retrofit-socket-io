package com.task.newsapp.ui.chat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.task.newsapp.data.model.Message
import com.task.newsapp.data.local.ChatDatabase
import com.task.newsapp.repository.ChatRepository
import kotlinx.coroutines.launch

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val chatRepository =
        ChatRepository(ChatDatabase(application.applicationContext))
    fun insert(message: Message) = viewModelScope.launch {
        chatRepository.insert(message)
    }
    fun getAllChat(id: String) = chatRepository.getAllChat(id)

}