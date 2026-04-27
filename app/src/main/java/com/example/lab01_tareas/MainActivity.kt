package com.example.lab01_tareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.lab01_tareas.model.Tarea
import com.example.lab01_tareas.repository.TareasRepository
import com.example.lab01_tareas.ui.theme.LAB01TareasTheme
import com.example.lab01_tareas.viewmodel.TareaFiltro
import com.example.lab01_tareas.viewmodel.TareasViewModel
import com.example.lab01_tareas.viewmodel.TareasViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: TareasViewModel by viewModels {
        TareasViewModelFactory(TareasRepository(applicationContext))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isDarkTheme by viewModel.isDarkTheme.collectAsState()
            
            LAB01TareasTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppTareas(viewModel)
                }
            }
        }
    }
}

@Composable
fun AppTareas(viewModel: TareasViewModel) {
    val tareas by viewModel.tareasFiltradas.collectAsState()
    val filtroActual by viewModel.filtro.collectAsState()
    val isDarkTheme by viewModel.isDarkTheme.collectAsState()
    var texto by remember { mutableStateOf("") }
    
    // Estado para la edición
    var tareaAEditar by remember { mutableStateOf<Tarea?>(null) }
    var textoEdicion by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Cabecera con título y botón de tema
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Gestor de Tareas",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(if (isDarkTheme) "🌙" else "☀️", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = { viewModel.toggleTheme(it) }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        // Campo de texto y botón para agregar
        OutlinedTextField(
            value = texto,
            onValueChange = { texto = it },
            label = { Text("Nueva tarea") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                if (texto.isNotBlank()) {
                    viewModel.agregarTarea(texto)
                    texto = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar tarea")
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        // Filtros
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterButton(
                text = "Todas",
                selected = filtroActual == TareaFiltro.TODAS,
                onClick = { viewModel.setFiltro(TareaFiltro.TODAS) }
            )
            FilterButton(
                text = "Pendientes",
                selected = filtroActual == TareaFiltro.PENDIENTES,
                onClick = { viewModel.setFiltro(TareaFiltro.PENDIENTES) }
            )
            FilterButton(
                text = "Completadas",
                selected = filtroActual == TareaFiltro.COMPLETADAS,
                onClick = { viewModel.setFiltro(TareaFiltro.COMPLETADAS) }
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        // Lista de tareas
        LazyColumn {
            items(tareas) { tarea ->
                ItemTarea(
                    tarea = tarea,
                    onToggle = { viewModel.toggleTarea(tarea) },
                    onDelete = { viewModel.eliminarTarea(tarea) },
                    onEdit = {
                        tareaAEditar = tarea
                        textoEdicion = tarea.titulo
                    }
                )
            }
        }
    }

    // Diálogo de edición
    tareaAEditar?.let { tarea ->
        AlertDialog(
            onDismissRequest = { tareaAEditar = null },
            title = { Text("Editar Tarea") },
            text = {
                OutlinedTextField(
                    value = textoEdicion,
                    onValueChange = { textoEdicion = it },
                    label = { Text("Título de la tarea") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (textoEdicion.isNotBlank()) {
                        viewModel.editarTarea(tarea, textoEdicion)
                        tareaAEditar = null
                    }
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { tareaAEditar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun FilterButton(text: String, selected: Boolean, onClick: () -> Unit) {
    val containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
    val contentColor = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
    
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(50)
    ) {
        Text(text)
    }
}

@Composable
fun ItemTarea(
    tarea: Tarea,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (tarea.completada) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Checkbox(
                    checked = tarea.completada,
                    onCheckedChange = { onToggle() }
                )
                Text(
                    text = tarea.titulo,
                    modifier = Modifier.padding(start = 8.dp),
                    color = if (tarea.completada) Color.Gray else MaterialTheme.colorScheme.onSurface,
                    textDecoration = if (tarea.completada) TextDecoration.LineThrough else TextDecoration.None
                )
            }
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}