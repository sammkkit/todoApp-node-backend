package com.example.todoapp.presentation.Screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.todoapp.presentation.AppViewModel
import com.example.todoapp.presentation.Routes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    navController: NavHostController,
    appViewModel:AppViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val token by appViewModel.token.collectAsState()
    val loading by appViewModel.isLoading.collectAsState()
    val error by appViewModel.authError.collectAsState()
    LaunchedEffect(token) {
        if (token != null) {
            // Navigate to home if user is not null
            withContext(Dispatchers.Main) {
                navController.navigate(Routes.Home.route){
                    popUpTo(Routes.Login.route){
                        inclusive = true
                    }
                }
            }
        }
    }
    LaunchedEffect(error) {
        Log.d("LoginScreen", "Error: $error")
        if (error != null && error!!.isNotBlank()) {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Login", fontSize = 30.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            visualTransformation = PasswordVisualTransformation()
        )
        Button(
            onClick = {
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(context, "Please fill all details", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    //todo login
                    appViewModel.login(
                        email,
                        password,
                        onSuccessLogin = {
                            CoroutineScope(Dispatchers.Main).launch {
                                navController.navigate(Routes.Home.route){
                                    popUpTo(Routes.Login.route){
                                        inclusive = true
                                    }
                                }
                            }
                        }
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            if (!loading) {
                Text(text = "Login")
            }else{
                CircularProgressIndicator(color = Color.Red)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Dont have an account? Sign up.",
            modifier = Modifier.clickable {
                //todo sign in navigate
                navController.navigate(Routes.Signup.route)
            }
        )
    }
}