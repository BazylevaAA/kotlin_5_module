package com.example.kotlin_5_prak.domain.model

data class Todo(
    val id: Int = 0,
    val title: String,
    val isDone: Boolean = false
)