package com.example.kotlin_5_prak.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    //как поток
    @Query("SELECT * FROM todos ORDER BY id DESC")
    fun getAll(): Flow<List<TodoEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(todo: TodoEntity)

    // Вставка списка используется для импорта из JSON
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(todos: List<TodoEntity>)

    @Update
    suspend fun update(todo: TodoEntity)

    @Delete
    suspend fun delete(todo: TodoEntity)

    @Query("SELECT COUNT(*) FROM todos")
    suspend fun count(): Int
}