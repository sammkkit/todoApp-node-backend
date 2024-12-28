package com.example.todoapp.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.time.LocalDate
import java.time.LocalTime
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.text.font.FontWeight
import com.example.todoapp.domain.model.Task

@Composable
fun AddTaskDialog(
    modifier: Modifier = Modifier,
    onSave: (Task) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember {
        mutableStateOf("")
    }
    var description by remember {
        mutableStateOf("")
    }
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = modifier
                .width(400.dp)
                .height(500.dp),
            border = BorderStroke(1.dp, Color.Black)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                InputTextField(value = title, label = "Title", onValueChange = { title = it })
                Spacer(modifier = Modifier.height(30.dp))
                InputTextField(
                    value = description,
                    label = "Description (Optional)",
                    onValueChange = { description = it }
                )

                Spacer(modifier = Modifier.height(50.dp))
                ToDoButton(text = "Save", onClick = {
                    if (title.trim().isNotEmpty()) {
                        (if (description.trim()
                                .isEmpty()
                        ) null else description)?.let {
                            Task(
                                title = title,
                                description = it,
                            )
                        }?.let {
                            onSave(
                                it
                            )
                        }
                    }
                    onDismiss()
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputTextField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        modifier = modifier.padding(16.dp),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = Color.White,
        ),
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
        value = value,
        label = { Text(text = label) },
        onValueChange = onValueChange
    )
}
@Composable
fun ToDoButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {

    Button(
        modifier = modifier.padding(16.dp),
        onClick = onClick,
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(1.dp, Color.LightGray),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        Text(text = text, fontWeight = FontWeight.Bold, color = Color.Black)
    }

}