package com.example.avance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.avance.viewmodel.UsuariosViewModel

@Composable
fun LoginScreen(
    navController: NavHostController,
    usuariosViewModel: UsuariosViewModel
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }


    val usuariosCargados = usuariosViewModel.usuarios.isNotEmpty()


    LaunchedEffect(Unit) {
        usuariosViewModel.cargarUsuarios()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(32.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Iniciar Sesión", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Usuario (correo registrado)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {

                    if (!usuariosCargados) {
                        errorMessage = "Cargando usuarios... intenta en 1 segundo."
                        return@Button
                    }

                    when {
                        username == "admin" && password == "admin123" -> {
                            errorMessage = ""
                            navController.navigate("home_admin") {
                                popUpTo("login") { inclusive = true }
                            }
                        }

                        else -> {
                            val usuarioEncontrado =
                                usuariosViewModel.usuarios.find { u ->
                                    u.email == username && u.contrasena == password
                                }

                            if (usuarioEncontrado != null) {
                                errorMessage = ""
                                navController.navigate("home_user") {
                                    popUpTo("login") { inclusive = true }
                                }
                            } else {
                                errorMessage = "❌ Usuario o contraseña incorrectos"
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ingresar")
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = { navController.navigate("register") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("¿No tienes cuenta? Crear cuenta")
            }

            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val navController = rememberNavController()
    val fakeVm = UsuariosViewModel()
    LoginScreen(navController = navController, usuariosViewModel = fakeVm)
}
