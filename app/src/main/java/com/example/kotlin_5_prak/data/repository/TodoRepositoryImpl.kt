package com.example.kotlin_5_prak.data.repository

import com.example.kotlin_5_prak.data.local.TodoDao
import com.example.kotlin_5_prak.data.local.TodoEntity
import com.example.kotlin_5_prak.domain.model.Todo
import com.example.kotlin_5_prak.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray

class TodoRepositoryImpl(private val dao: TodoDao) : TodoRepository {

    // Конвертируем Entity в Domain при чтении
    override fun getAll(): Flow<List<Todo>> =
        dao.getAll().map { list -> list.map { it.toDomain() } }

    override suspend fun add(todo: Todo) = dao.insert(todo.toEntity())

    override suspend fun update(todo: Todo) = dao.update(todo.toEntity())

    override suspend fun delete(todo: Todo) = dao.delete(todo.toEntity())

    // Импорт из JSON только если БД пустая (разовая операция при первом запуске)
    override suspend fun importFromJsonIfEmpty(jsonString: String) {
        if (dao.count() > 0) return

        val array = JSONArray(jsonString)
        val entities = (0 until array.length()).map { i ->
            val obj = array.getJSONObject(i)
            TodoEntity(
                title = obj.getString("title"),
                isDone = obj.optBoolean("isDone", false)
            )
        }
        dao.insertAll(entities)
    }

    private fun TodoEntity.toDomain() = Todo(id = id, title = title, isDone = isDone)
    private fun Todo.toEntity() = TodoEntity(id = id, title = title, isDone = isDone)
}