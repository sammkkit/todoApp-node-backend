package com.example.todoapp.presentation

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.model.AuthResponse
import com.example.todoapp.data.model.LoginRequest
import com.example.todoapp.data.model.SignupRequest
import com.example.todoapp.data.model.ToDoServerModel
import com.example.todoapp.data.model.User
import com.example.todoapp.data.repository.AppRepository
import com.example.todoapp.domain.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class AppViewModel(
    private val repository: AppRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<ToDoServerModel>>(emptyList())
    val tasks: StateFlow<List<ToDoServerModel>> = _tasks

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _selectedTask = MutableStateFlow<ToDoServerModel?>(null)
    val selectedTask: StateFlow<ToDoServerModel?> = _selectedTask

    // Authentication
    private val _authResponse = MutableStateFlow<AuthResponse?>(null)
    val authResponse: StateFlow<AuthResponse?> = _authResponse

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token

    // User data
    private val _user = MutableStateFlow<String?>(null)
    val user: StateFlow<String?> = _user

    init {
        Log.d("AppViewModel", "in INIT")
        checkIfUserIsLoggedIn()
        loadToken()
        Log.d("AppViewModel", " user is ${_user.value}")
        Log.d("AppViewModel", "token value: ${_token.value}")
        fetchTasks()
    }

    // Check if user is logged in (check if token exists)
    private fun checkIfUserIsLoggedIn() {
        val tokenFromPrefs = sharedPreferences.getString("auth_token", null)
        if (tokenFromPrefs != null) {
            _token.value = tokenFromPrefs
        } else {
            Log.d("AppViewModel", "User not logged in")
        }
    }
    // Authentication functions
    fun signUp(name: String, email: String, password: String,
               onSuccessSignUp: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val signupRequest = SignupRequest(name, email, password)
                val response = repository.handleSignUp(signupRequest)
                if (response.isSuccessful) {
                    _authResponse.value = response.body()
                    response.body()?.let {
                        saveToken(it.token,it.user)
                    }
//                    _user.value = response.body()?.user
                    onSuccessSignUp()
                    Log.d("AppViewModel", "SignUp Success: ${response.body()}")
                    Log.d("AppViewModel", "token value: ${response.body()?.token}")
                } else {
                    _authError.value = response.errorBody()?.string()
                    Log.e("AppViewModel", "SignUp Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _authError.value = e.message
                Log.e("AppViewModel", "SignUp Exception: $e")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun login(email: String, password: String,onSuccessLogin: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val loginRequest = LoginRequest(email, password)
                val response = repository.handleLogin(loginRequest)
                if (response.isSuccessful) {
                    _authResponse.value = response.body()
                    Log.d("AppViewModel", "Response Body: ${response.body()}")
                    response.body()?.let {
                        saveToken(it.token,it.user)
                    }
//                    _user.value = response.body()?.user
                    fetchTasks()
                    onSuccessLogin()
                    Log.d("AppViewModel", "Login Success: ${response.body()}")
                    Log.d("AppViewModel", "token value: ${response.body()?.token}")
                } else {
                    _authError.value = response.errorBody()?.string()
                    Log.e("AppViewModel", "Login Error: ${response}")
                }
            } catch (e: Exception) {
                _authError.value = e.message
                Log.e("AppViewModel", "Login Exception: $e")
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun logout(onLogOut: ()->Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Clear token and user data
                clearToken()
                _user.value = null
                _authResponse.value = null
                onLogOut()
                Log.d("AppViewModel", "User logged out successfully.")
            } catch (e: Exception) {
                Log.e("AppViewModel", "Logout Exception: $e")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun saveToken(token: String,user: User) {
        Log.d("AppViewModel", "user - : $user")
        sharedPreferences.edit().putString("auth_token", token).apply()
        sharedPreferences.edit().putString("user", user.name).apply()
        Log.d("AppViewModel", "Token Saved: $token")

    }
    private fun loadToken() {
        _token.value = sharedPreferences.getString("auth_token", null)
        _user.value = sharedPreferences.getString("user", null)
    }
    fun clearToken() {
        sharedPreferences.edit().remove("auth_token").apply()
        sharedPreferences.edit().remove("user").apply()
        _token.value = null
        Log.d("AppViewModel", "Token Cleared")
    }

    fun fetchTasks() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val taskList = repository.getTasks()
                if (taskList != null) {
                    _tasks.value = taskList
                }
                Log.d("AppViewModel", "Fetched Tasks: $taskList")
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("AppViewModel", "Error fetching tasks: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val newTask = repository.addTask(task)
                Log.d("AppViewModel", "Added Task: $newTask")
                _tasks.value = _tasks.value.plus(newTask)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun updateTask(task: ToDoServerModel){
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val updatedTask = repository.updateTask(task._id!!,task)
                _tasks.value = _tasks.value.map { if (it._id == updatedTask._id) updatedTask else it }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteTask(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val success = repository.deleteTask(id)
                if (success) {
                    _tasks.value = _tasks.value!!.filterNot { it._id == id }
                } else {
                    _error.value = "Failed to delete task"
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getTaskById(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val task = repository.getTaskByID(id)
                _selectedTask.value = task
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
