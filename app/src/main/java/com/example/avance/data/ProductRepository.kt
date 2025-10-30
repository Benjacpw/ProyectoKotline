package com.example.avance.data

import kotlinx.coroutines.flow.Flow

class ProductRepository(private val dao: ProductoDao) {

    val productos: Flow<List<Producto>> = dao.getAll()

    suspend fun agregar(producto: Producto) = dao.insert(producto)

    suspend fun actualizar(producto: Producto) = dao.update(producto)

    suspend fun eliminar(producto: Producto) = dao.delete(producto)

    suspend fun obtener(id: Long): Producto? = dao.findById(id)
}
