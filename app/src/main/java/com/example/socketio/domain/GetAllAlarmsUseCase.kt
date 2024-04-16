package com.example.socketio.domain

import com.example.socketio.data.StationsRepository
import com.example.socketio.data.models.Alarms
import com.example.socketio.data.models.Stations

class GetAllAlarmsUseCase {
    private val repository = StationsRepository()

    suspend operator fun invoke():List<Alarms>? = repository.getAllAlarms()
}