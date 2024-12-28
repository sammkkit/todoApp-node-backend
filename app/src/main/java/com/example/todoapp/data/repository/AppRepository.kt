package com.example.todoapp.data.repository

import android.util.Log
import com.example.todoapp.data.api.AuthApiService
import com.example.todoapp.data.api.taskApiService
import com.example.todoapp.data.model.AuthResponse
import com.example.todoapp.data.model.LoginRequest
import com.example.todoapp.data.model.SignupRequest
import com.example.todoapp.data.model.ToDoServerModel
import com.example.todoapp.domain.model.Task
import retrofit2.Response

class AppRepository(private val apiService: taskApiService
,private val authApiService: AuthApiService
) {
    //Auth related function
    suspend fun handleSignUp(signupRequest : SignupRequest) : Response<AuthResponse>{
        return authApiService.signup(signupRequest)
    }
    suspend fun handleLogin(loginRequest: LoginRequest) : Response<AuthResponse>{
        return authApiService.login(loginRequest)
    }

    //task related function
    suspend fun getTasks(): List<ToDoServerModel>? {
        val response = apiService.getTasks()
//        Log.d("RawResponse", response.body().toString())
        return apiService.getTasks().body()
    }
    suspend fun updateTask(id: String,task: ToDoServerModel): ToDoServerModel {
        return apiService.updateTask(id,task)
    }
    suspend fun addTask(task: Task): ToDoServerModel {
        return apiService.addTask(task)
    }

    suspend fun deleteTask(id: String): Boolean {
        return apiService.deleteTask(id).isSuccessful
    }

    suspend fun getTaskByID(id: String): ToDoServerModel {
        return apiService.getTaskByID(id)
    }
}
