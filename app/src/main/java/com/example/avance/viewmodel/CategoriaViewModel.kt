package com.example.avance.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avance.data.*
import kotlinx.coroutines.launch

class CategoriasViewModel : ViewModel() {

    var categorias by mutableStateOf<List<Categoria>>(emptyList())
    var cargando by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    fun cargarCategorias() {
        viewModelScope.launch {
            try {
                cargando = true
                categorias = RetrofitInstance.api.getCategorias()
                error = null
            } catch (e: Exception) {
                error = e.message
            } finally {
                cargando = false
            }
        }
    }

    fun crearCategoria(data: CategoriaCreate) {
        viewModelScope.launch {
            try {
                cargando = true
                RetrofitInstance.api.createCategoria(data)
                cargarCategorias()
            } catch (e: Exception) {
                error = e.message
            } finally {
                cargando = false
            }
        }
    }

    fun actualizarCategoria(id: Long, data: CategoriaCreate) {
        viewModelScope.launch {
            try {
                cargando = true
                RetrofitInstance.api.updateCategoria(id, data)
                cargarCategorias()
            } catch (e: Exception) {
                error = e.message
            } finally {
                cargando = false
            }
        }
    }

    fun eliminarCategoria(id: Long) {
        viewModelScope.launch {
            try {
                cargando = true
                RetrofitInstance.api.deleteCategoria(id)
                cargarCategorias()
            } catch (e: Exception) {
                error = e.message
            } finally {
                cargando = false
            }
        }
    }
}
