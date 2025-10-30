package com.example.avance

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, isAdmin: Boolean) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isAdmin) "Panel Administrador" else "Menú Principal") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Bienvenido a Avance",
                style = MaterialTheme.typography.titleLarge,
                fontSize = 22.sp
            )

            // Botón Productos
            Button(
                onClick = { navController.navigate("productos") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("🛍️ Ver Productos")
            }

            // Botón Quiénes Somos
            Button(
                onClick = { navController.navigate("quienes_somos") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("ℹ️ Quiénes Somos")
            }

            // Solo admins
            if (isAdmin) {
                Button(
                    onClick = { navController.navigate("home_admin") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("⚙️ Panel de Administración")
                }
            }

            // Cerrar sesión
            OutlinedButton(
                onClick = { navController.navigate("login") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("🚪 Cerrar Sesión")
            }
        }
    }
}
