package com.example.avance.data;

data class UsuarioApi(
    val id: Long,
    val nombre: String,
    val email: String,
    val contrasena: String,
    val telefono: Int,
    val region: String,
    val comuna: String
)

