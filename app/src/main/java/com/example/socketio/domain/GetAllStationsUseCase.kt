package com.example.socketio.domain

import com.example.socketio.data.StationsRepository
import com.example.socketio.data.models.Stations

class GetAllStationsUseCase {
    private val repository = StationsRepository()

    suspend operator fun invoke():List<Stations>? = repository.getAllComments()
}