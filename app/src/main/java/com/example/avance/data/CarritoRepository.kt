package com.example.avance.data

import kotlinx.coroutines.flow.Flow

class CarritoRepository(private val dao: CarritoDao) {
    val items: Flow<List<CarritoItem>> = dao.getAll()

    suspend fun agregar(item: CarritoItem) = dao.insert(item)
    suspend fun eliminar(item: CarritoItem) = dao.delete(item)
}

