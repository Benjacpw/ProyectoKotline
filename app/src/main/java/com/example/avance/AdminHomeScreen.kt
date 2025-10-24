package com.example.avance

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomeScreen(navController: NavHostController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Inicio - Admin") }) }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Bienvenido, Administrador 🔑", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = { navController.popBackStack("login", inclusive = false) }) {
                    Text("Cerrar sesión")
                }
            }
        }
    }
}
