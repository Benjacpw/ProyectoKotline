package com.example.avance.viewmodel

import androidx.lifecycle.ViewModel
import com.example.avance.data.CarritoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CarritoViewModel : ViewModel() {

    private val _carrito = MutableStateFlow<List<CarritoItem>>(emptyList())
    val carrito: StateFlow<List<CarritoItem>> = _carrito

    fun agregar(item: CarritoItem) {
        _carrito.value = _carrito.value + item
    }

    fun eliminar(item: CarritoItem) {
        _carrito.value = _carrito.value - item
    }

    fun limpiar() {
        _carrito.value = emptyList()
    }

    fun total(): Double {
        return carrito.value.sumOf { it.precio * it.cantidad }
    }

    fun subtotal(item: CarritoItem): Double {
        return item.precio * item.cantidad
    }
}
