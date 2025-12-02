package com.example.avance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.avance.data.RetrofitInstance
import com.example.avance.viewmodel.OrdenesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdenesScreen(navController: NavController) {
    val viewModel: OrdenesViewModel = viewModel()
    val ordenes by viewModel.ordenes.collectAsState()
    val apiService = RetrofitInstance.api

    LaunchedEffect(Unit) {
        viewModel.cargarTodasOrdenes(apiService)
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("📦 Todas las Órdenes") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->

        if (ordenes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("No hay órdenes aún.", style = MaterialTheme.typography.titleMedium)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(ordenes) { orden ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("🆔 Pedido ID: ${orden.id}", style = MaterialTheme.typography.titleMedium)
                            Text("📅 Fecha: ${orden.fecha}")
                            Text("💰 Total: $${orden.total}")
                            Text("📋 Productos:", style = MaterialTheme.typography.titleSmall)
                            orden.productos.forEach { item ->
                                Text("- ${item.titulo} x${item.qty} = $${item.precio * item.qty}")
                            }
                        }
                    }
                }
            }
        }
    }
}
