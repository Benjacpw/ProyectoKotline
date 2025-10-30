package com.example.avance.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carrito")
data class CarritoItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productoId: Long,
    val cantidad: Int = 1
)
