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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.avance.data.CarritoItem
import com.example.avance.data.ApiService
import com.example.avance.data.UsuarioPreferences
import com.example.avance.data.UsuarioData
import com.example.avance.viewmodel.CarritoViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    viewModel: CarritoViewModel,
    navController: NavController,
    apiService: ApiService
) {
    val carrito by viewModel.carrito.collectAsState()

    val total = carrito.fold(0.0) { acc, item ->
        acc + (item.precio * item.cantidad)
    }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var mostrarCheckout by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val userPrefs = remember { UsuarioPreferences(context) }

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
                Text("Tu carrito está vacío 🛍️")
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
                                        snackbarHostState.showSnackbar("Producto eliminado")
                                    }
                                }
                            }
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))
                Text("💰 Total: $${"%.2f".format(total)}")

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
            userPrefs = userPrefs,
            onConfirm = { nombre, apellido, correo, direccion, comentarios ->
                viewModel.crearOrden(
                    nombre = nombre,
                    apellido = apellido,
                    correo = correo,
                    calle = direccion,
                    departamento = "",
                    region = "",
                    comuna = "",
                    indicaciones = comentarios,
                    apiService = apiService,
                    onSuccess = {
                        mostrarCheckout = false
                        scope.launch {
                            snackbarHostState.showSnackbar("Pedido creado correctamente")
                        }
                        viewModel.limpiar()
                    },
                    onError = { error ->
                        scope.launch {
                            snackbarHostState.showSnackbar("Error: $error")
                        }
                    }
                )
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
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.nombre)
                Text("Precio: $${item.precio}")
                Text("Subtotal: $${item.precio * item.cantidad}")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onRemove) {
                    Icon(Icons.Filled.Remove, contentDescription = null)
                }
                Text("${item.cantidad}")
                IconButton(onClick = onAdd) {
                    Icon(Icons.Filled.Add, contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun CheckoutDialog(
    total: Double,
    userPrefs: UsuarioPreferences,
    onConfirm: (String, String, String, String, String) -> Unit,
    onDismiss: () -> Unit
) {
    val usuario by userPrefs.usuarioFlow.collectAsState(
        initial = UsuarioData("", "", "", "")
    )

    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var comentarios by remember { mutableStateOf("") }

    LaunchedEffect(usuario) {
        nombre = usuario.nombre
        apellido = usuario.apellido
        correo = usuario.correo
        direccion = usuario.direccion
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Total a pagar: $${"%.2f".format(total)}") },
        text = {
            Column {
                OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre") })
                OutlinedTextField(apellido, { apellido = it }, label = { Text("Apellido") })
                OutlinedTextField(correo, { correo = it }, label = { Text("Correo") })
                OutlinedTextField(direccion, { direccion = it }, label = { Text("Dirección") })
                OutlinedTextField(comentarios, { comentarios = it }, label = { Text("Comentarios") })
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm(nombre, apellido, correo, direccion, comentarios)
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
