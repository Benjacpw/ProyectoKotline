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

            Button(
                onClick = {
                    if (isAdmin) {
                        navController.navigate("productos") // CRUD admin
                    } else {
                        navController.navigate("catalogo") // Catálogo usuario
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isAdmin) "📦 Gestionar Productos" else "🛍 Ver Productos")
            }

            if (!isAdmin) {
                Button(
                    onClick = { navController.navigate("carrito") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("🛒 Ver Carrito")
                }
            }

            Button(
                onClick = { navController.navigate("quienes_somos") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("👥 Quiénes Somos")
            }

            OutlinedButton(
                onClick = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("🚪 Cerrar Sesión")
            }
        }
    }
}
