package com.example.avance.data

data class Orden(
    val id: Long,
    val fecha: String,
    val total: Int,
    val nombre: String,
    val apellido: String,
    val correo: String,
    val calle: String,
    val departamento: String,
    val region: String,
    val comuna: String,
    val indicaciones: String,
    val usuarioId: Long,
    val estado: String,
    val productos: List<OrdenItem>
)