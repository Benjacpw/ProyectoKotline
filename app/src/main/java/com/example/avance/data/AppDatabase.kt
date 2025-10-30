package com.example.avance.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Producto::class, CarritoItem::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productoDao(): ProductoDao
    abstract fun carritoDao(): CarritoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database2"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            // ⚡ Importante: Usar una corrutina IO directa, sin depender de INSTANCE
                            CoroutineScope(Dispatchers.IO).launch {
                                val database = getInstance(context)
                                val productoDao = database.productoDao()

                                val ejemplos = listOf(
                                    Producto(nombre = "Tabla Skate A", precio = 15000.0, descripcion = "Tabla profesional", categoria = "Skate"),
                                    Producto(nombre = "Tabla Skate B", precio = 12000.0, descripcion = "Tabla para principiantes", categoria = "Skate"),
                                    Producto(nombre = "Roller Pro", precio = 20000.0, descripcion = "Roller de competición", categoria = "Roller"),
                                    Producto(nombre = "Roller Básico", precio = 8000.0, descripcion = "Roller para recreación", categoria = "Roller"),
                                    Producto(nombre = "BMX Xtreme", precio = 35000.0, descripcion = "BMX para trucos", categoria = "BMX"),
                                    Producto(nombre = "BMX Urban", precio = 30000.0, descripcion = "BMX urbana", categoria = "BMX")
                                )

                                ejemplos.forEach { productoDao.insert(it) }
                            }
                        }
                    })
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
