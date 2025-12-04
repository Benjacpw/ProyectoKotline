package com.example.avance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.avance.viewmodel.OrdenesViewModel
import com.example.avance.viewmodel.ProductosViewModel
import com.example.avance.data.RetrofitInstance
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardAdminScreen(navController: NavController) {

    val ordenesViewModel: OrdenesViewModel = viewModel()
    val productosViewModel: ProductosViewModel = viewModel()

    val ordenes by ordenesViewModel.ordenes.collectAsState()
    val productos = productosViewModel.productos

    LaunchedEffect(Unit) {
        ordenesViewModel.cargarTodasOrdenes(RetrofitInstance.api)
        productosViewModel.cargarProductos()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("📊 Dashboard Admin") }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Text("Resumen General", style = MaterialTheme.typography.titleLarge)

            DashboardCard("💰 Ventas Totales", "$${ordenes.sumOf { it.total }}")

            DashboardCard("🕒 Pedidos Pendientes",
                ordenes.count { it.estado == "pendiente" }.toString()
            )

            DashboardCard("📦 Productos Activos", productos.count().toString())

            DashboardCard("🔔 Notificaciones", generarNotificaciones(ordenes))

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("ordenes") },
                modifier = Modifier.fillMaxWidth()
            ) { Text("📑 Ver Todas las Órdenes") }

            Button(
                onClick = { navController.navigate("productos") },
                modifier = Modifier.fillMaxWidth()
            ) { Text("🛠️ Gestionar Productos") }

            Button(
                onClick = { navController.navigate("usuarios_api") },
                modifier = Modifier.fillMaxWidth()
            ) { Text("👥 Gestionar Usuarios") }

            Button(
                onClick = { navController.navigate("categoria_api") },
                modifier = Modifier.fillMaxWidth()
            ) { Text("🏷️ Gestionar Categorías") }

            Spacer(modifier = Modifier.height(20.dp))

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



@Composable
fun DashboardCard(titulo: String, valor: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .padding(18.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = valor,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = 26.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}


fun generarNotificaciones(ordenes: List<com.example.avance.data.Orden>): String {
    val pendientes = ordenes.count { it.estado == "pendiente" }

    return when {
        pendientes > 5 -> "Hay muchos pedidos pendientes!"
        pendientes in 1..5 -> "Tienes pedidos por revisar"
        else -> "Todo está en orden ✔"
    }
}
