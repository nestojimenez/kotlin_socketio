package com.example.socketio.domain

import com.example.socketio.data.StationsRepository
import com.example.socketio.data.models.Stations
import com.example.socketio.data.models.StationsWithAlarmStatus

class GetStationWithAlarmStatusListUseCase {
    private val repository = StationsRepository()

    suspend operator fun invoke():List<StationsWithAlarmStatus>? = repository.getAllStationsWithAlarmsStatus()
}