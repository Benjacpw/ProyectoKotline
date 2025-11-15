package com.example.avance.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avance.data.*
import kotlinx.coroutines.launch

class ProductosViewModel : ViewModel() {

    var productos by mutableStateOf<List<Producto>>(emptyList())
    var cargando by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    fun cargarProductos() {
        viewModelScope.launch {
            try {
                cargando = true
                productos = RetrofitInstance.api.getProductos()
                error = null
            } catch (e: Exception) {
                error = e.message
            } finally {
                cargando = false
            }
        }
    }

    fun crearProducto(data: ProductoCreate) {
        viewModelScope.launch {
            try {
                cargando = true
                RetrofitInstance.api.createProducto(data)
                cargarProductos()
            } catch (e: Exception) {
                error = e.message
            } finally { cargando = false }
        }
    }

    fun actualizarProducto(id: Long, data: ProductoCreate) {
        viewModelScope.launch {
            try {
                cargando = true
                RetrofitInstance.api.updateProducto(id, data)
                cargarProductos()
            } catch (e: Exception) {
                error = e.message
            } finally { cargando = false }
        }
    }

    fun eliminarProducto(id: Long) {
        viewModelScope.launch {
            try {
                cargando = true
                RetrofitInstance.api.deleteProducto(id)
                cargarProductos()
            } catch (e: Exception) {
                error = e.message
            } finally { cargando = false }
        }
    }
}
