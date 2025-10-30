package com.example.avance.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avance.data.ProductRepository
import com.example.avance.data.Producto
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductosViewModel(private val repo: ProductRepository) : ViewModel() {

    val productos: StateFlow<List<Producto>> =
        repo.productos.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun agregar(producto: Producto) = viewModelScope.launch { repo.agregar(producto) }
    fun actualizar(producto: Producto) = viewModelScope.launch { repo.actualizar(producto) }
    fun eliminar(producto: Producto) = viewModelScope.launch { repo.eliminar(producto) }
    suspend fun obtener(id: Long): Producto? = repo.obtener(id)
}

