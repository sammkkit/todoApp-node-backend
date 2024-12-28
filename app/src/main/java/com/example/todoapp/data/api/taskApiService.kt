package com.example.todoapp.data.api

//import com.example.todoapp.data.model.TaskServer
import com.example.todoapp.data.model.ToDoServerModel
import com.example.todoapp.domain.model.Task
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface taskApiService {

    @GET("api/tasks")
    suspend fun getTasks(): Response<List<ToDoServerModel>>

    @POST("api/tasks")
    suspend fun addTask(@Body task: Task): ToDoServerModel

    @DELETE("api/tasks/{id}")
    suspend fun deleteTask(@Path("id") id: String): Response<Unit>

    @GET("api/tasks/{id}")
    suspend fun getTaskByID(@Path("id") id: String): ToDoServerModel

    @PUT("api/tasks/{id}")
    suspend fun updateTask(@Path("id") id: String,@Body task: ToDoServerModel): ToDoServerModel
}