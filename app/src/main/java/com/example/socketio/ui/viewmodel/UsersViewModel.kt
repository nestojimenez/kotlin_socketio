package com.example.socketio.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socketio.data.models.Users
import com.example.socketio.domain.GetAllUsersUseCase
import kotlinx.coroutines.launch

class UsersViewModel : ViewModel() {

    val getAllUsers = GetAllUsersUseCase()
    private val userModel = MutableLiveData<List<Users>>()

    fun onCreate(){
        viewModelScope.launch {
            Log.d("AllUsers", "En rutina de onCreate")
            val result = getAllUsers()
            Log.d("AllUsers", result.toString())
            userModel.postValue(result)
        }
    }
}