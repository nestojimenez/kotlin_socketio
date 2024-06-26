package com.example.socketio.data.models

data class Alarms(
    val id: Int?,
    val employee: Int,
    val id_stations: Int?,
    val al_status: Int?,
    val created_at: String?,
    val updated_at: String?,
    val station_module: String?
)