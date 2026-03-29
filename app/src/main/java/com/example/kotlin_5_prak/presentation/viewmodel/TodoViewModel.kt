package com.example.kotlin_5_prak.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_5_prak.data.local.TodoDatabase
import com.example.kotlin_5_prak.data.preferences.TodoPreferencesRepository
import com.example.kotlin_5_prak.data.preferences.dataStore
import com.example.kotlin_5_prak.data.repository.TodoRepositoryImpl
import com.example.kotlin_5_prak.domain.model.Todo
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TodoViewModel(app: Application) : AndroidViewModel(app) {

    private val dao = TodoDatabase.getDatabase(app).todoDao()
    private val repository = TodoRepositoryImpl(dao)
    private val prefsRepo = TodoPreferencesRepository(app.dataStore)

    val todos: StateFlow<List<Todo>> = repository.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val highlightDone: StateFlow<Boolean> = prefsRepo.highlightDone
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    // Импортируем задачи из JSON при первом запуске
    fun importInitialData(json: String) {
        viewModelScope.launch {
            repository.importFromJsonIfEmpty(json)
        }
    }

    fun addTodo(title: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            repository.add(Todo(title = title.trim()))
        }
    }

    fun toggleDone(todo: Todo) {
        viewModelScope.launch {
            repository.update(todo.copy(isDone = !todo.isDone))
        }
    }

    fun updateTitle(todo: Todo, newTitle: String) {
        if (newTitle.isBlank()) return
        viewModelScope.launch {
            repository.update(todo.copy(title = newTitle.trim()))
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            repository.delete(todo)
        }
    }

    // Переключаем настройку цвета выполненных и сохраняем в DataStore
    fun toggleHighlight() {
        viewModelScope.launch {
            prefsRepo.setHighlightDone(!highlightDone.value)
        }
    }
}