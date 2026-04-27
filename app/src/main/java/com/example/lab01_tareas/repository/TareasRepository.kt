package com.example.lab01_tareas.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.lab01_tareas.model.Tarea
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "tareas_prefs")

class TareasRepository(private val context: Context) {
    private val gson = Gson()
    private val TAREAS_KEY = stringPreferencesKey("tareas_list")
    private val THEME_KEY = booleanPreferencesKey("is_dark_theme")

    val tareasFlow: Flow<List<Tarea>> = context.dataStore.data.map { preferences ->
        val jsonString = preferences[TAREAS_KEY] ?: "[]"
        val type = object : TypeToken<List<Tarea>>() {}.type
        gson.fromJson(jsonString, type)
    }

    suspend fun saveTareas(tareas: List<Tarea>) {
        context.dataStore.edit { preferences ->
            val jsonString = gson.toJson(tareas)
            preferences[TAREAS_KEY] = jsonString
        }
    }

    val isDarkThemeFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[THEME_KEY] ?: false
    }

    suspend fun saveDarkTheme(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDark
        }
    }
}
