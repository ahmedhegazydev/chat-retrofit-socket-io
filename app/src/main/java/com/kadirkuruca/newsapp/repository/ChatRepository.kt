package com.kadirkuruca.newsapp.repository

import com.kadirkuruca.newsapp.data.model.Message
import com.kadirkuruca.newsapp.data.local.ChatDatabase

class ChatRepository(val db: ChatDatabase) {

    suspend fun insert(message: Message) =
        db.getArticleDao().insert(message)

    fun getAllChat(id: String) = db.getArticleDao().getAllChat(id)

}