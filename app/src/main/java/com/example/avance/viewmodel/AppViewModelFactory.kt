package com.example.avance.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.avance.data.CarritoRepository
import com.example.avance.data.ProductRepository

class AppViewModelFactory(
    private val productRepo: ProductRepository,
    private val carritoRepo: CarritoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ProductosViewModel::class.java) ->
                ProductosViewModel(productRepo) as T
            modelClass.isAssignableFrom(CarritoViewModel::class.java) ->
                CarritoViewModel(carritoRepo) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
