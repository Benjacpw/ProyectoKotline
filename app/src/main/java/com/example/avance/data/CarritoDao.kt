package com.example.avance.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CarritoDao {

    @Query("SELECT * FROM carrito")
    fun getAll(): Flow<List<CarritoItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CarritoItem)

    @Update
    suspend fun update(item: CarritoItem)

    @Delete
    suspend fun delete(item: CarritoItem)

    @Query("DELETE FROM carrito")
    suspend fun clear()
}

