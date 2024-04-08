package com.example.socketio.domain

import com.example.socketio.data.StationsRepository
import com.example.socketio.data.models.Alarms
import com.example.socketio.data.models.Stations

class PostAlarmSupportCase() {
    private val repository = StationsRepository()

    suspend operator fun invoke(body: Alarms):Alarms? = repository.postAlarmSupport(body)
}