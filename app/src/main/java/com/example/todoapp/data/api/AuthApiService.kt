package com.example.todoapp.data.api

import com.example.todoapp.data.model.AuthResponse
import com.example.todoapp.data.model.LoginRequest
import com.example.todoapp.data.model.SignupRequest
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.Body

interface AuthApiService {

    @POST("/user/signup")
    suspend fun signup(
        @Body signupRequest: SignupRequest
    ): Response<AuthResponse>

    @POST("/user/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<AuthResponse>
}
