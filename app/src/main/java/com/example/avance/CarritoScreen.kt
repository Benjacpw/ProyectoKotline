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
    val carrito by viewModel.carrito.collectAsState()
    val total = carrito.sumOf { it.precio * it.cantidad }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var mostrarCheckout by remember { mutableStateOf(false) }

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
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(carrito) { item ->
                        CarritoItemView(
                            item = item,
                            onAdd = {
                                val actualizado = item.copy(cantidad = item.cantidad + 1)
                                viewModel.eliminar(item)
                                viewModel.agregar(actualizado)
                            },
                            onRemove = {
                                if (item.cantidad > 1) {
                                    val actualizado = item.copy(cantidad = item.cantidad - 1)
                                    viewModel.eliminar(item)
                                    viewModel.agregar(actualizado)
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

                Divider()
                Spacer(Modifier.height(8.dp))

                Text(
                    "💰 Total: $${"%.2f".format(total)}",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = { mostrarCheckout = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Finalizar Compra")
                }
            }
        }
    }

    if (mostrarCheckout) {
        CheckoutDialog(
            total = total,
            onConfirm = { retiroEnTienda, direccion, telefono, comentarios ->
                val numeroPedido = generarNumeroPedidoAleatorio()


                viewModel.limpiar()
                mostrarCheckout = false


                scope.launch {
                    snackbarHostState.showSnackbar(
                        "✅ Tu número de pedido es $numeroPedido. ¡Gracias por elegirnos!"
                    )
                }
            },
            onDismiss = { mostrarCheckout = false }
        )
    }
}


@Composable
fun CarritoItemView(
    item: CarritoItem,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
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


@Composable
fun CheckoutDialog(
    total: Double,
    onConfirm: (retiroEnTienda: Boolean, direccion: String, telefono: String, comentarios: String) -> Unit,
    onDismiss: () -> Unit
) {
    var retiroEnTienda by remember { mutableStateOf(true) }
    var direccion by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var comentarios by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Datos de envío / retiro") },
        text = {
            Column {
                Text("Total a pagar: $${"%.0f".format(total)}")

                Spacer(Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Retiro en tienda")
                    Spacer(Modifier.width(8.dp))
                    Switch(
                        checked = retiroEnTienda,
                        onCheckedChange = { retiroEnTienda = it }
                    )
                }

                if (retiroEnTienda) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "📍 Solo disponible en Mall Plaza Vespucio",
                        color = MaterialTheme.colorScheme.primary
                    )
                }


                if (!retiroEnTienda) {
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = direccion,
                        onValueChange = { direccion = it },
                        label = { Text("Dirección de envío") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    label = { Text("Teléfono (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = comentarios,
                    onValueChange = { comentarios = it },
                    label = { Text("Comentarios (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm(retiroEnTienda, direccion, telefono, comentarios)
            }) {
                Text("Confirmar pedido")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}


fun generarNumeroPedidoAleatorio(): String {
    val numero = (100000..999999).random()
    return "PED-$numero"
}