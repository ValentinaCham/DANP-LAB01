package com.example.lab01_tareas.model

data class Tarea(
    val id: Int,
    val titulo: String,
    val completada: Boolean = false
)
