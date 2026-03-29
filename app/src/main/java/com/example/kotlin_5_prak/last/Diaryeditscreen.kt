package com.example.kotlin_5_prak.last

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryEditScreen(
    viewModel: DiaryViewModel,
    fileName: String?,
    onBack: () -> Unit
) {
    val (initialTitle, initialBody) = remember(fileName) {
        if (fileName != null) viewModel.readEntry(fileName)
        else Pair("", "")
    }

    var title by remember { mutableStateOf(initialTitle) }
    var body by remember { mutableStateOf(initialBody) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (fileName == null) "Новая запись" else "Редактировать") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Поле заголовка
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Заголовок (необязательно)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            // Основной текст
            OutlinedTextField(
                value = body,
                onValueChange = { body = it },
                label = { Text("Текст записи") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                minLines = 8
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) {
                    Text("Назад")
                }
                Button(
                    onClick = {
                        viewModel.save(fileName, title, body)
                        onBack()
                    },
                    modifier = Modifier.weight(1f),
                    enabled = body.isNotBlank()
                ) {
                    Text("Сохранить")
                }
            }
        }
    }
}