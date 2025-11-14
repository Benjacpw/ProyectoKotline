package com.example.avance.data

data class UsuarioCreate(
    val nombre: String,
    val email: String,
    val contrasena: String,
    val telefono: Int,
    val region: String,
    val comuna: String
)
