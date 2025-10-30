package com.example.avance.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(entities = [Producto::class, CarritoItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productoDao(): ProductoDao
    abstract fun carritoDao(): CarritoDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context, scope: CoroutineScope = GlobalScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .addCallback(DatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(private val scope: CoroutineScope) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val productoDao = database.productoDao()
                    val ejemplos = listOf(
                        Producto(nombre = "Tabla Skate A", precio = 150.0, descripcion = "Tabla profesional", categoria = "Skate"),
                        Producto(nombre = "Tabla Skate B", precio = 120.0, descripcion = "Tabla para principiantes", categoria = "Skate"),
                        Producto(nombre = "Roller Pro", precio = 200.0, descripcion = "Roller de competición", categoria = "Roller"),
                        Producto(nombre = "Roller Básico", precio = 80.0, descripcion = "Roller para recreación", categoria = "Roller"),

                        Producto(nombre = "BMX Xtreme", precio = 350.0, descripcion = "BMX para trucos", categoria = "BMX"),
                        Producto(nombre = "BMX Urban", precio = 300.0, descripcion = "BMX urbana", categoria = "BMX")
                    )
                    ejemplos.forEach { productoDao.insert(it) }
                }
            }
        }
    }
}

