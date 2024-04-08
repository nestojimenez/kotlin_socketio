package com.example.socketio.data.models

data class Stations(

    val id: Int,
    val st_name:String,
    val st_line: String,
    val st_unhappy_oee:String,
    val st_happy_oee: String,
    val created_at:String,
    val updated_at:String
    /*val body: String,
    val email: String,
    val id: Int,
    val name: String,
    val postId: Int*/
)