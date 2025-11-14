package com.example.avance.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avance.data.*
import kotlinx.coroutines.launch
import androidx.compose.runtime.*

class UsuariosViewModel : ViewModel() {

    var usuarios by mutableStateOf<List<UsuarioApi>>(emptyList())
    var seleccionado by mutableStateOf<UsuarioApi?>(null)
    var cargando by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    fun cargarUsuarios() {
        viewModelScope.launch {
            try {
                cargando = true
                usuarios = RetrofitInstance.api.getUsuarios()
            } catch (e: Exception) {
                error = e.message
            } finally {
                cargando = false
            }
        }
    }

    fun crearUsuario(data: UsuarioCreate) {
        viewModelScope.launch {
            RetrofitInstance.api.createUsuario(data)
            cargarUsuarios()
        }
    }

    fun actualizarUsuario(id: Long, data: UsuarioCreate) {
        viewModelScope.launch {
            RetrofitInstance.api.updateUsuario(id, data)
            cargarUsuarios()
        }
    }

    fun eliminarUsuario(id: Long) {
        viewModelScope.launch {
            RetrofitInstance.api.deleteUsuario(id)
            cargarUsuarios()
        }
    }
}
