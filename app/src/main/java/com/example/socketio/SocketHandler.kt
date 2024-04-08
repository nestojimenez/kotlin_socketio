package com.example.socketio

import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

object SocketHandler {
    lateinit var mSocket: Socket

    @Synchronized
    fun setSocket (){
        try {
            mSocket = IO.socket("http://10.105.168.234:3000")
        }catch (e: URISyntaxException){

        }
    }

    @Synchronized
    fun getSocket() : Socket {
        return mSocket
    }
}