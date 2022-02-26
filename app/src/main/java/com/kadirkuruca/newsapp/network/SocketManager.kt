package com.kadirkuruca.newsapp.network

import android.content.Context
import android.util.Log
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket


const val TAG = "SocketManager"

class SocketManager private constructor(private var context: Context) {

    private var socket: Socket? = null

    //            private val BASE_URL = "http://10.0.0.1:5000"
//    private val BASE_URL = "https://10.0.2.16:5000"
//    private val BASE_URL = "https://socket-io-chat.now.sh/"
//    private val BASE_URL = "http://10.0.2.15:5000"
    private val BASE_URL = "http://10.0.2.2:5000"
//    private val BASE_URL = "http://10.0.2.3:5000"
//    private val BASE_URL = "http://10.0.2.1:5000"
//    private val BASE_URL = "http://localhost:5000"
//    private val BASE_URL = "http://192.168.1.5:5000"
//    private val BASE_URL = "http://192.168.1.1:5000"
//    private val BASE_URL = "http://192.168.1.8:5000"
    //    private val BASE_URL = "http://192.168.0.103:5000"
//    private val BASE_URL = "http://127.0.0.1:5000"
//    private val BASE_URL = "https://task-ksa.herokuapp.com"


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
//            if (socket == null){
//                Log.e(TAG, "socket = null", )
//            }else{
//
//                socket?.id()?.let {
//                    Log.e("success", it)
//                }
//            }
            socket?.connect()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("fail", "Failed to connect")
        }

    }


}