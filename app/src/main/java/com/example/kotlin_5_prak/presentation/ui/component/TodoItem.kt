package com.example.kotlin_5_prak.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.kotlin_5_prak.domain.model.Todo

@Composable
fun TodoItem(
    todo: Todo,
    highlightDone: Boolean,
    onToggle: () -> Unit,
    onEdit: (String) -> Unit,
    onDelete: () -> Unit
) {
    var editMode by remember { mutableStateOf(false) }
    var editText by remember(todo.title) { mutableStateOf(todo.title) }

    val bgColor = if (todo.isDone && highlightDone)
        Color(0xFFB2FFB2)
    else
        MaterialTheme.colorScheme.surface

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .background(bgColor)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = todo.isDone, onCheckedChange = { onToggle() })

            if (editMode) {
                //поле ввода
                OutlinedTextField(
                    value = editText,
                    onValueChange = { editText = it },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                IconButton(onClick = {
                    onEdit(editText)
                    editMode = false
                }) {
                    Icon(Icons.Default.Check, contentDescription = "Сохранить")
                }
            } else {
                //текст задачи
                Text(
                    text = todo.title,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (todo.isDone) Color.Gray else MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = { editMode = true }) {
                    Icon(Icons.Default.Edit, contentDescription = "Редактировать")
                }
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Удалить", tint = Color.Red)
            }
        }
    }
}