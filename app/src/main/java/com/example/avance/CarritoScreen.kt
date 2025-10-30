package com.example.avance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.avance.data.CarritoItem
import com.example.avance.viewmodel.CarritoViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    viewModel: CarritoViewModel,
    navController: NavController
) {
    val carrito by viewModel.carrito.collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val total = carrito.sumOf { it.precio * it.cantidad }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🛒 Carrito de Compras") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        if (carrito.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Tu carrito está vacío 🛍️", style = MaterialTheme.typography.titleMedium)
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(carrito) { item ->
                        CarritoItemView(
                            item = item,
                            onAdd = {
                                val actualizado = item.copy(cantidad = item.cantidad + 1)
                                viewModel.actualizar(actualizado)
                            },
                            onRemove = {
                                if (item.cantidad > 1) {
                                    val actualizado = item.copy(cantidad = item.cantidad - 1)
                                    viewModel.actualizar(actualizado)
                                } else {
                                    viewModel.eliminar(item)
                                    scope.launch {
                                        snackbarHostState.showSnackbar("🗑 Producto eliminado del carrito")
                                    }
                                }
                            }
                        )
                    }
                }

                Divider(thickness = 1.dp)
                Spacer(Modifier.height(8.dp))
                Text("💰 Total: $${"%.2f".format(total)}", style = MaterialTheme.typography.titleLarge)

                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = {
                        viewModel.limpiar()
                        scope.launch {
                            snackbarHostState.showSnackbar("✅ Compra finalizada correctamente")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Finalizar Compra")
                }
            }
        }
    }
}

@Composable
fun CarritoItemView(item: CarritoItem, onAdd: () -> Unit, onRemove: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.nombre, style = MaterialTheme.typography.titleMedium)
                Text("Precio unitario: $${item.precio}")
                Text("Subtotal: $${item.cantidad * item.precio}")
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(onClick = onRemove) {
                    Icon(Icons.Filled.Remove, contentDescription = "Quitar uno")
                }
                Text("${item.cantidad}")
                IconButton(onClick = onAdd) {
                    Icon(Icons.Filled.Add, contentDescription = "Agregar uno")
                }
            }
        }
    }
}
