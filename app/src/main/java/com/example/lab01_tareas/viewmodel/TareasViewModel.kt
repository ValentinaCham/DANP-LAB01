package com.example.lab01_tareas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModelProvider
import com.example.lab01_tareas.model.Tarea
import com.example.lab01_tareas.repository.TareasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class TareaFiltro {
    TODAS, PENDIENTES, COMPLETADAS
}

class TareasViewModel(private val repository: TareasRepository) : ViewModel() {

    private val allTareas = repository.tareasFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _filtro = MutableStateFlow(TareaFiltro.TODAS)
    val filtro: StateFlow<TareaFiltro> = _filtro

    val isDarkTheme: StateFlow<Boolean> = repository.isDarkThemeFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val tareasFiltradas: StateFlow<List<Tarea>> = combine(allTareas, _filtro) { lista, filtroActual ->
        when (filtroActual) {
            TareaFiltro.TODAS -> lista
            TareaFiltro.PENDIENTES -> lista.filter { !it.completada }
            TareaFiltro.COMPLETADAS -> lista.filter { it.completada }
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun toggleTheme(isDark: Boolean) {
        viewModelScope.launch {
            repository.saveDarkTheme(isDark)
        }
    }

    fun setFiltro(nuevoFiltro: TareaFiltro) {
        _filtro.value = nuevoFiltro
    }

    fun agregarTarea(titulo: String) {
        val actuales = allTareas.value
        val nuevaId = (actuales.maxOfOrNull { it.id } ?: 0) + 1
        val nuevaLista = actuales + Tarea(id = nuevaId, titulo = titulo)
        viewModelScope.launch { repository.saveTareas(nuevaLista) }
    }

    fun toggleTarea(tarea: Tarea) {
        val actuales = allTareas.value
        val nuevaLista = actuales.map {
            if (it.id == tarea.id) it.copy(completada = !it.completada) else it
        }
        viewModelScope.launch { repository.saveTareas(nuevaLista) }
    }

    fun eliminarTarea(tarea: Tarea) {
        val actuales = allTareas.value
        val nuevaLista = actuales.filter { it.id != tarea.id }
        viewModelScope.launch { repository.saveTareas(nuevaLista) }
    }

    fun editarTarea(tarea: Tarea, nuevoTitulo: String) {
        val actuales = allTareas.value
        val nuevaLista = actuales.map {
            if (it.id == tarea.id) it.copy(titulo = nuevoTitulo) else it
        }
        viewModelScope.launch { repository.saveTareas(nuevaLista) }
    }
}

class TareasViewModelFactory(private val repository: TareasRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TareasViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TareasViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
