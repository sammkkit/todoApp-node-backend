package com.example.todoapp.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.todoapp.data.model.ToDoServerModel
import com.example.todoapp.domain.model.Task

@Composable
fun TaskListItem(
    task: ToDoServerModel,
    onCheck: (ToDoServerModel) -> Unit = {},
    onClick: (String?) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var checked by remember {
        mutableStateOf(task.completed)
    }
    Box(contentAlignment = Alignment.Center) {
        Card(
            modifier = modifier
                .padding(bottom = 16.dp)
                .clickable { onClick(task._id) },
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp),
            border = BorderStroke(1.dp, Color.Black)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = checked, onCheckedChange = {
                    checked = !checked
                    onCheck(task)
                })
                Spacer(modifier = Modifier.width(25.dp))
                Column {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = if (checked) Color.LightGray else Color.Black
                    )
                    Text(
                        text = task.description, style = MaterialTheme.typography.labelMedium,
                        color = if (checked) Color.LightGray else Color.Black
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = checked,
            enter = slideIn(
                animationSpec = tween(durationMillis = 500),
                initialOffset = { _ ->
                    IntOffset(0, 0)
                })
        ) {
            Divider()
        }
    }
}
