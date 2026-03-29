package com.example.kotlin_5_prak.domain.repository

import com.example.kotlin_5_prak.domain.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getAll(): Flow<List<Todo>>
    suspend fun add(todo: Todo)
    suspend fun update(todo: Todo)
    suspend fun delete(todo: Todo)
    suspend fun importFromJsonIfEmpty(jsonString: String)
}