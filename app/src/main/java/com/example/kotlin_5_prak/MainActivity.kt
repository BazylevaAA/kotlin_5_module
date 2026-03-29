package com.example.kotlin_5_prak

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.compose.material3.MaterialTheme

class MainActivity : ComponentActivity() {

    // список не перечитывается при повороте экрана
    private val viewModel: DiaryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Простая тема без лишних файлов
            MaterialTheme {
                DiaryApp(viewModel)
            }
        }
    }
}

@Composable
fun DiaryApp(viewModel: DiaryViewModel) {
    var currentScreen by remember { mutableStateOf<String?>(null) }

    if (currentScreen == null) {
        DiaryListScreen(
            viewModel = viewModel,
            onOpenEntry = { fileName ->
                // null означает новую запись, передаём маркер "new"
                currentScreen = fileName ?: "new"
            }
        )
    } else {
        // Экран редактирования
        DiaryEditScreen(
            viewModel = viewModel,
            fileName = if (currentScreen == "new") null else currentScreen,
            onBack = { currentScreen = null }
        )
    }
}