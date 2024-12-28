package com.example.todoapp.presentation

sealed class Routes(val route:String) {
    object Login: Routes("login")
    object Signup: Routes("signup")
    object Home: Routes("home")
}