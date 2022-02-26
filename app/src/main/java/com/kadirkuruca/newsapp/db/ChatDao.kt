package com.kadirkuruca.newsapp.db

import  androidx.lifecycle.LiveData
import androidx.room.*
import com.kadirkuruca.newsapp.data.model.Message

@Dao
interface ChatDao {

    @Insert
    suspend fun insert(message: Message)

    @Query("SELECT * FROM MessageTable WHERE idUser LIKE :id ORDER BY id DESC")
    fun getAllChat(id: String): LiveData<List<Message>>


}