package com.example.avance.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avance.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CarritoViewModel : ViewModel() {

    private val _carrito = MutableStateFlow<List<CarritoItem>>(emptyList())
    val carrito: StateFlow<List<CarritoItem>> = _carrito

    private val _ordenes = MutableStateFlow<List<Orden>>(emptyList())
    val ordenes: StateFlow<List<Orden>> = _ordenes

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
    fun crearOrden(
        nombre: String,
        apellido: String,
        correo: String,
        calle: String,
        departamento: String,
        region: String,
        comuna: String,
        indicaciones: String,
        apiService: ApiService,
        onSuccess: (Orden) -> Unit,
        onError: (String) -> Unit
    ) {
        val orden = Orden(
            id = 0L,
            fecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
            total = total().toInt(),
            nombre = nombre,
            apellido = apellido,
            correo = correo,
            calle = calle,
            departamento = departamento,
            region = region,
            comuna = comuna,
            indicaciones = indicaciones,
            usuarioId = 0L,
            estado = "pendiente",
            productos = carrito.value.map {
                OrdenItem(
                    id = 0L,
                    titulo = it.nombre,
                    qty = it.cantidad,
                    precio = it.precio.toInt()
                )
            }
        )


        viewModelScope.launch {
            try {
                val resultado = apiService.crearOrden(orden)
                _ordenes.value = _ordenes.value + resultado
                limpiar()
                onSuccess(resultado)
            } catch (e: Exception) {
                onError(e.message ?: "Error desconocido")
            }
        }
    }

    fun cargarTodasOrdenes(apiService: ApiService) {
        viewModelScope.launch {
            try {
                val lista = apiService.obtenerTodasOrdenes()
                _ordenes.value = lista
            } catch (e: Exception) {
            }
        }
    }

}
