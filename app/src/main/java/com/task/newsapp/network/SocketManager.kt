package com.task.newsapp.network

import android.content.Context
import android.util.Log
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket


const val TAG = "SocketManager"

class SocketManager private constructor(private var context: Context) {

    private var socket: Socket? = null

    private val BASE_URL = "http://10.0.2.2:5000"


    companion object {
        var instance: SocketManager? = null
        fun getInstance(context: Context): SocketManager? {
            if (instance == null) {
                instance = SocketManager(context)
            }
            return instance
        }
    }

    fun getSocket() = socket

    init {
        //Let's connect to our Chat room! :D
        try {
            socket = IO.socket(BASE_URL)
            socket?.on(
                Socket.EVENT_CONNECT
            ) { val socketID = socket!!.id()
                Log.e(TAG, "socketID =  $socketID", )
            }
            socket?.connect()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("fail", "Failed to connect")
        }

    }


}