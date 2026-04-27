# Gestor de Tareas - Android Studio (LAB 01)

Este es un proyecto de ejemplo desarrollado para el laboratorio de sistemas, consistente en una aplicación moderna de gestión de tareas utilizando **Jetpack Compose** y arquitectura **MVVM**.

## 🚀 Características Principales

1.  **Gestión de Tareas:** Permite añadir, editar, completar y eliminar tareas de forma intuitiva.
2.  **Arquitectura MVVM:** Implementación robusta separando la lógica de negocio (ViewModel), los datos (Repository) y la interfaz de usuario (Compose).
3.  **Persistencia Local:** Uso de **Jetpack DataStore** junto con **Gson** para guardar las tareas y preferencias del usuario de forma permanente en el dispositivo.
4.  **Sistema de Filtros:** Visualización dinámica de tareas según su estado: *Todas*, *Pendientes* o *Hechas*.
5.  **Tema Dinámico:** Soporte completo para **Tema Claro y Oscuro**, con un interruptor persistente en la interfaz.
6.  **Diseño Premium:** Interfaz basada en **Material 3**, con tarjetas personalizadas, elevaciones y feedback visual de estados.

## 🛠️ Tecnologías Utilizadas

*   **Lenguaje:** Kotlin
*   **UI Framework:** Jetpack Compose (Material 3)
*   **Persistencia:** Preferences DataStore
*   **Serialización:** Gson
*   **Gestión de Estado:** Flow & StateFlow
*   **Arquitectura:** MVVM (Model-View-ViewModel)

## 📋 Requisitos

*   Android Studio Ladybug o superior.
*   JDK 11 o superior.
*   Dispositivo Android o Emulador con API 24 (Android 7.0) o superior.

## ⚙️ Cómo Ejecutar el Proyecto

1.  **Clonar o abrir el proyecto:** Abre la carpeta raíz en Android Studio.
2.  **Sincronización de Gradle:** Espera a que Android Studio descargue las dependencias (DataStore, Gson, etc.) y sincronice el proyecto automáticamente.
3.  **Configuración del SDK:** Asegúrate de tener instalado el SDK de Android 34/35 (o el especificado en `build.gradle`).
4.  **Ejecución:** 
    *   Conecta un dispositivo físico o inicia un emulador.
    *   Haz clic en el botón **Run** (índice verde `play`) en la barra superior.

## 📂 Estructura del Proyecto (MVVM)

```text
com.example.lab01_tareas/
├── model/          # Definición de la entidad Tarea
├── repository/     # Lógica de persistencia (DataStore)
├── viewmodel/      # Lógica de negocio y estados de UI
├── ui/theme/       # Definiciones de colores, temas y tipografía
└── MainActivity.kt # Punto de entrada y componentes UI
```
