package com.example.todoapp.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.todoapp.presentation.Screens.LoginScreen
import com.example.todoapp.presentation.Screens.SignUpScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavGraph(navController: NavHostController) {
    val appViewModel:AppViewModel = koinViewModel()
    NavHost(navController = navController, startDestination = Routes.Login.route) {
        composable(Routes.Login.route){
            LoginScreen(navController,appViewModel)
        }
        composable(Routes.Signup.route){
            SignUpScreen(navController,appViewModel)
        }
        composable(Routes.Home.route){
            TaskScreen(
                appViewModel,
                onNavigateToLogin = {
                    navController.navigate(Routes.Login.route)
                }
            )
        }

    }
}