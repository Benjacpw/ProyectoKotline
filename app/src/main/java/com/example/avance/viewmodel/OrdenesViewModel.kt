package com.example.avance.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avance.data.ApiService
import com.example.avance.data.Orden
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.avance.data.RetrofitInstance

class OrdenesViewModel : ViewModel() {

    private val _ordenes = MutableStateFlow<List<Orden>>(emptyList())
    val ordenes: StateFlow<List<Orden>> = _ordenes

    fun cargarTodasOrdenes(apiService: ApiService) {
        viewModelScope.launch {
            try {
                val lista = apiService.obtenerTodasOrdenes()
                _ordenes.value = lista
            } catch (e: Exception) {
            }
        }
    }
    fun totalVentas(): Int {
        return _ordenes.value.size
    }

    fun actualizarEstadoOrden(api: ApiService, id: Long, nuevoEstado: String) {
        viewModelScope.launch {
            try {
                api.actualizarEstado(id, nuevoEstado)
                cargarTodasOrdenes(api)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}




