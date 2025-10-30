package com.example.avance.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.avance.data.CarritoItem
import com.example.avance.data.CarritoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CarritoViewModel(private val repo: CarritoRepository) : ViewModel() {
    val carrito: StateFlow<List<CarritoItem>> =
        repo.items.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun agregar(item: CarritoItem) = viewModelScope.launch {
        repo.agregar(item)
    }

    fun actualizar(item: CarritoItem) = viewModelScope.launch {
        repo.actualizar(item)
    }

    fun eliminar(item: CarritoItem) = viewModelScope.launch {
        repo.eliminar(item)
    }

    fun limpiar() = viewModelScope.launch {
        repo.limpiar()
    }

    fun total(): Double {
        return carrito.value.sumOf { it.precio * it.cantidad }
    }

    fun subtotal(item: CarritoItem): Double {
        return item.precio * item.cantidad
    }

    class Factory(private val repo: CarritoRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CarritoViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CarritoViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

