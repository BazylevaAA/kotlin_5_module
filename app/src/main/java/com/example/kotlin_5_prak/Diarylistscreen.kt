package com.example.kotlin_5_prak

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryListScreen(
    viewModel: DiaryViewModel,
    onOpenEntry: (String?) -> Unit
) {
    val entries by viewModel.entries.collectAsState()

    // Запись, по которой показываем меню удаления
    var menuEntry by remember { mutableStateOf<DiaryEntry?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Мой дневник") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { onOpenEntry(null) }) {
                Icon(Icons.Default.Add, contentDescription = "Новая запись")
            }
        }
    ) { padding ->

        if (entries.isEmpty()) {
            // Пустой экран
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("У вас пока нет записей", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Нажмите +, чтобы создать первую", color = MaterialTheme.colorScheme.outline)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(entries, key = { it.fileName }) { entry ->
                    EntryCard(
                        entry = entry,
                        onClick = { onOpenEntry(entry.fileName) },
                        onLongClick = { menuEntry = entry }
                    )
                }
            }
        }

        // Диалог удаления по долгому нажатию
        menuEntry?.let { entry ->
            AlertDialog(
                onDismissRequest = { menuEntry = null },
                title = { Text("Удалить запись?") },
                text = { Text("«${entry.title}» будет удалена без возможности восстановления.") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.delete(entry.fileName)
                        menuEntry = null
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = null)
                        Spacer(Modifier.width(4.dp))
                        Text("Удалить")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { menuEntry = null }) { Text("Отмена") }
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun EntryCard(
    entry: DiaryEntry,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(entry.title, style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(4.dp))
            Text(entry.preview, color = MaterialTheme.colorScheme.outline, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(4.dp))
            Text(entry.date, color = MaterialTheme.colorScheme.outline, style = MaterialTheme.typography.labelSmall)
        }
    }
}