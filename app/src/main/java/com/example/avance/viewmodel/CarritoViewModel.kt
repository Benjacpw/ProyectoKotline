package com.example.avance.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avance.data.CarritoItem
import com.example.avance.data.CarritoRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CarritoViewModel(private val repo: CarritoRepository) : ViewModel() {
    val items: StateFlow<List<CarritoItem>> = repo.items.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun agregar(item: CarritoItem) = viewModelScope.launch { repo.agregar(item) }
    fun eliminar(item: CarritoItem) = viewModelScope.launch { repo.eliminar(item) }
}

