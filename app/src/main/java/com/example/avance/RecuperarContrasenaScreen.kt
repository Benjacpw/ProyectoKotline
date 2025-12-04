package com.example.avance

import android.R.attr.navigationIcon
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.avance.data.RetrofitInstance
import com.example.avance.data.SolicitudActualizarClave
import kotlinx.coroutines.launch

@Composable
fun RecuperarContrasenaScreen(navController: NavController) {

    val contexto = LocalContext.current
    val scope = rememberCoroutineScope()

    var correo by remember { mutableStateOf("") }
    var nuevaClave by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(text = "Recuperar contraseña")

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo registrado") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = nuevaClave,
                onValueChange = { nuevaClave = it },
                label = { Text("Nueva contraseña") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (correo.isBlank() || nuevaClave.isBlank()) {
                        Toast.makeText(
                            contexto,
                            "Completa ambos campos",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }
                    cargando = true
                    scope.launch {
                        try {
                            val solicitud = SolicitudActualizarClave(
                                correo = correo,
                                nuevaClave = nuevaClave
                            )
                            val respuesta = RetrofitInstance.api.recuperarContrasena(solicitud)
                            cargando = false
                            Toast.makeText(
                                contexto,
                                respuesta.mensaje,
                                Toast.LENGTH_LONG
                            ).show()
                            if (respuesta.exito) {
                                navController.popBackStack()
                            }
                        } catch (e: Exception) {
                            cargando = false
                            Toast.makeText(
                                contexto,
                                "Error de conexión: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                },
                enabled = !cargando,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = if (cargando) "Actualizando..." else "Actualizar contraseña")
            }
            TextButton(
                onClick = { navController.navigate("login") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al Inicio de Sesión")
            }
        }
    }
}