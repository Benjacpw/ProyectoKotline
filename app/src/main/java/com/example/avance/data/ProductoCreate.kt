package com.example.avance.data

data class ProductoCreate(
    val titulo: String,
    val descripcion: String,
    val precio: Double,
    val imagen: String,
    val qty: Int,
    val categoria: String
)
