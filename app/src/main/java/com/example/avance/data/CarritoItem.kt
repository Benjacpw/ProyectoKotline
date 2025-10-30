package com.example.avance.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carrito")
data class CarritoItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val idProducto: Long,
    val nombre: String,
    val precio: Double,
    var cantidad: Int = 1
)

