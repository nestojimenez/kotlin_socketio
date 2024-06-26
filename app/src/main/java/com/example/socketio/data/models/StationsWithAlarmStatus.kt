package com.example.socketio.data.models

data class StationsWithAlarmStatus (
    val AlarmId: Int,
    val employee: Int,
    val user_name: String,
    val id_stations: Int,
    val al_status:Int,
    val createdAt: String,
    val updatedAt: String,
    val station_module: String,
    val id: Int,
    val st_name:String,
    val st_line: String,
    val st_unhappy_oee:String,
    val st_happy_oee: String,
    val created_at:String,
    val updated_at:String,

)