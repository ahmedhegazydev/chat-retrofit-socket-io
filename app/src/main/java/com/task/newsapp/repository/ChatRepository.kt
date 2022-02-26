package com.task.newsapp.repository

import com.task.newsapp.data.model.Message
import com.task.newsapp.data.local.ChatDatabase

class ChatRepository(val db: ChatDatabase) {

    suspend fun insert(message: Message) =
        db.getArticleDao().insert(message)

    fun getAllChat(id: String) = db.getArticleDao().getAllChat(id)

}