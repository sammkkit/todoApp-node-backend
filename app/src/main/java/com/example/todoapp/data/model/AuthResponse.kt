package com.example.todoapp.data.model

data class AuthResponse(
    val message: String,
    val user: User,
    val token: String
)

data class User(
    val name: String,
    val email: String,
    val password: String,
    val _id: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)


