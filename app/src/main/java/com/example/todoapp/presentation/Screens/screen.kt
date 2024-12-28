package com.example.todoapp.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.todoapp.data.model.ToDoServerModel
import com.example.todoapp.domain.model.Task
import com.example.todoapp.presentation.components.InputTextField
import com.example.todoapp.presentation.components.TaskListItem
import com.example.todoapp.presentation.components.ToDoButton

//import org.koin.androidx.compose
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    taskViewModel:AppViewModel,
    onNavigateToLogin: () -> Unit
) {
    val tasks by taskViewModel.tasks.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var selectedTask by remember { mutableStateOf<ToDoServerModel?>(null) }
    // UI to display tasks
    val loading by taskViewModel.isLoading.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Task List") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showDialog = true
                    selectedTask = null
                }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Task")
            }
        }
    ){padding->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            when {
                loading -> {
                    CircularProgressIndicator(color = Color.Blue)
                }
                tasks.isEmpty() -> {
                    Text(
                        text = "No tasks available",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                else -> {
                    LazyColumn {
                        items(
                            items = tasks,
                            key = { it._id!! }
                        ) { task ->
                            TaskListItem(
                                task,
                                onCheck = {
                                    taskViewModel.updateTask(
                                        task.copy(completed = !task.completed)
                                    )
                                },
                                onClick = {
                                    // Show the dialog with the selected task for editing
                                    selectedTask = task
                                    showDialog = true
                                }
                            )
                        }
                    }
                }
            }

            Button(
                onClick = { taskViewModel.logout(onNavigateToLogin) },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Log Out")
            }
        }

    }
    if (showDialog) {
        if (selectedTask == null) {
            // Show dialog for adding a new task
            addTaskDetailDialog(
                onSave = { task ->
                    taskViewModel.addTask(task) // Add the new task
                    showDialog = false // Close the dialog
                },
                onDismiss = {
                    showDialog = false // Close the dialog
                }
            )
        } else {
            // Show dialog for editing an existing task
            selectedTaskDetailDialog(
                task = selectedTask,
                onUpdate = { task ->
                    taskViewModel.updateTask(task) // Update the existing task
                    showDialog = false // Close the dialog
                },
                onDismiss = {
                    showDialog = false // Close the dialog
                },
                onDelete = {
                    taskViewModel.deleteTask(selectedTask!!._id!!)
                    showDialog = false
                }
            )
        }
    }
}
@Composable
private fun selectedTaskDetailDialog(
    modifier: Modifier = Modifier,
    task:ToDoServerModel?,
    onUpdate: (ToDoServerModel) -> Unit,
    onDismiss: () -> Unit,
    onDelete:()->Unit
) {
    var title by remember { mutableStateOf(task?.title ?: "") }
    var description by remember { mutableStateOf( task?.description ?: "") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = modifier
                .width(400.dp)
                .height(370.dp),
            border = BorderStroke(1.dp, Color.Black)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                InputTextField(
                    value = title,
                    label = "Title",
                    onValueChange = {
                        title = it
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
                InputTextField(
                    value = description,
                    label = "Description (Optional)",
                    onValueChange = {
                        description = it
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row {

                    ToDoButton(text = "Save", onClick = {
                        if (task != null) {
                            onUpdate(
                                task.copy(
                                    title = title,
                                    description = description
                                )
                            )
                        }
                        onDismiss()
                    })

                    Spacer(modifier = Modifier.width(10.dp))
                    ToDoButton(text = "Delete", onClick = {
                        if (task != null) {
                            onDelete()
                        }
                    })
                }

            }
        }
    }
}
@Composable
private fun addTaskDetailDialog(
    modifier: Modifier = Modifier,
    onSave: (Task) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf( "") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = modifier
                .width(400.dp)
                .height(330.dp),
            border = BorderStroke(1.dp, Color.Black)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                InputTextField(
                    value = title,
                    label = "Title",
                    onValueChange = {
                        title = it
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
                InputTextField(
                    value = description,
                    label = "Description (Optional)",
                    onValueChange = {
                        description = it
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
                ToDoButton(text = "Save", onClick = {
                    onSave(
                        Task(
                            title = title,
                            description = description
                        )
                    )
                    onDismiss()
                })
            }
        }
    }
}