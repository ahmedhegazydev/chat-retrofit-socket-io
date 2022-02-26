package com.kadirkuruca.newsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kadirkuruca.newsapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
    }
}