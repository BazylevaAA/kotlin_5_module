package com.example.kotlin_5_prak.presentation.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kotlin_5_prak.presentation.ui.component.TodoItem
import com.example.kotlin_5_prak.presentation.viewmodel.TodoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(viewModel: TodoViewModel) {
    val todos by viewModel.todos.collectAsState()
    val highlightDone by viewModel.highlightDone.collectAsState()

    var newTaskText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Todo List") },
                actions = {
                    Text("Цвет завершённых", style = MaterialTheme.typography.labelSmall)
                    Spacer(Modifier.width(4.dp))
                    Switch(
                        checked = highlightDone,
                        onCheckedChange = { viewModel.toggleHighlight() }
                    )
                    Spacer(Modifier.width(8.dp))
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {

            // Поле добавления новой задачи
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newTaskText,
                    onValueChange = { newTaskText = it },
                    label = { Text("Новая задача") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                Spacer(Modifier.width(8.dp))
                FloatingActionButton(
                    onClick = {
                        viewModel.addTodo(newTaskText)
                        newTaskText = ""
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Добавить")
                }
            }

            if (todos.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Нет задач. Добавьте первую!")
                }
            } else {
                LazyColumn {
                    items(todos, key = { it.id }) { todo ->
                        TodoItem(
                            todo = todo,
                            highlightDone = highlightDone,
                            onToggle = { viewModel.toggleDone(todo) },
                            onEdit = { newTitle -> viewModel.updateTitle(todo, newTitle) },
                            onDelete = { viewModel.deleteTodo(todo) }
                        )
                    }
                }
            }
        }
    }
}