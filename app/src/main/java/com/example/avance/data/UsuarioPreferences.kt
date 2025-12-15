package com.example.avance.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map



private val Context.dataStore by preferencesDataStore("usuario_prefs")

class UsuarioPreferences(private val context: Context) {

    companion object {
        val NOMBRE = stringPreferencesKey("nombre")
        val APELLIDO = stringPreferencesKey("apellido")
        val CORREO = stringPreferencesKey("correo")
        val DIRECCION = stringPreferencesKey("direccion")
    }

    suspend fun guardarUsuario(
        nombre: String,
        apellido: String,
        correo: String,
        direccion: String
    ) {
        context.dataStore.edit { prefs ->
            prefs[NOMBRE] = nombre
            prefs[APELLIDO] = apellido
            prefs[CORREO] = correo
            prefs[DIRECCION] = direccion
        }
    }

    val usuarioFlow = context.dataStore.data.map { prefs ->
        UsuarioData(
            nombre = prefs[NOMBRE] ?: "",
            apellido = prefs[APELLIDO] ?: "",
            correo = prefs[CORREO] ?: "",
            direccion = prefs[DIRECCION] ?: ""
        )
    }
}

data class UsuarioData(
    val nombre: String,
    val apellido: String,
    val correo: String,
    val direccion: String
)
