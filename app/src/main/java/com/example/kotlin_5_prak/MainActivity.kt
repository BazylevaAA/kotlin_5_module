package com.example.kotlin_5_prak

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import com.example.kotlin_5_prak.presentation.ui.screen.TodoListScreen
import com.example.kotlin_5_prak.presentation.viewmodel.TodoViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: TodoViewModel by viewModels()

    private val initialJson = """
        [
          {"title": "Купить продукты", "isDone": false},
          {"title": "Прочитать книгу", "isDone": false},
          {"title": "Сходить в спортзал", "isDone": true},
          {"title": "Позвонить другу", "isDone": false},
          {"title": "Сделать домашнее задание", "isDone": false}
        ]
    """.trimIndent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.importInitialData(initialJson)

        setContent {
            MaterialTheme {
                TodoListScreen(viewModel)
            }
        }
    }
}