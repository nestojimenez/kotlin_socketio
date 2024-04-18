package com.example.socketio.domain

import com.example.socketio.data.StationsRepository
import com.example.socketio.data.models.Alarms
import com.example.socketio.data.models.Users

class GetAllUsersUseCase {
    private val repository = StationsRepository()

    suspend operator fun invoke():List<Users> = repository.getAllUsers()
}