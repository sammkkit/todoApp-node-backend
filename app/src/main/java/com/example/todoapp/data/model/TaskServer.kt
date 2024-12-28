package com.example.todoapp.data.model

import com.example.todoapp.domain.model.Task
import kotlinx.serialization.SerialName

data class ToDoServerModel(
    @SerialName("_id") val _id: String? = null,
    val title: String,
    val description: String,
    val completed: Boolean = false,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    @SerialName("__v") val _v: Int?
)
//
//fun Task.toServerModel(): ToDoServerModel = ToDoServerModel(
//    id=id,
//    title = title,
//    description = description,
//    completed = completed
//)
//fun ToDoServerModel.toDomainModel(): Task = Task(
//    id = id,
//    title = title,
//    description = description,
//    completed = completed,
//    createdAt = createdAt,
//    updatedAt = updatedAt
//)
