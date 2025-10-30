package com.example.avance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.avance.data.Producto
import com.example.avance.viewmodel.ProductosViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosScreen(
    viewModel: ProductosViewModel = viewModel(),
    navController: NavController? = null
) {
    val productos by viewModel.productos.collectAsState(initial = emptyList())

    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }

    var editando by remember { mutableStateOf<Producto?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val categoriasValidas = listOf("Skate", "Roller", "BMX")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("📦 Panel de Productos (Admin)") },
                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primaryContainer)
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(if (editando == null) "Agregar" else "Guardar cambios") },
                icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },
                onClick = {
                    val precioDouble = precio.toDoubleOrNull()
                    when {
                        nombre.isBlank() || descripcion.isBlank() || categoria.isBlank() -> {
                            showSnackbar(scope, snackbarHostState, "⚠️ Todos los campos son obligatorios")
                        }
                        precioDouble == null || precioDouble <= 0 -> {
                            showSnackbar(scope, snackbarHostState, "⚠️ El precio debe ser un número mayor a 0")
                        }
                        categoria !in categoriasValidas -> {
                            showSnackbar(scope, snackbarHostState, "⚠️ Categoría inválida (usa: Skate, Roller o BMX)")
                        }
                        else -> {
                            if (editando == null) {
                                val nuevo = Producto(
                                    nombre = nombre.trim(),
                                    precio = precioDouble,
                                    descripcion = descripcion.trim(),
                                    categoria = categoria.trim()
                                )
                                viewModel.agregar(nuevo)
                                showSnackbar(scope, snackbarHostState, "✅ Producto creado correctamente")
                            } else {
                                val actualizado = editando!!.copy(
                                    nombre = nombre.trim(),
                                    precio = precioDouble,
                                    descripcion = descripcion.trim(),
                                    categoria = categoria.trim()
                                )
                                viewModel.actualizar(actualizado)
                                showSnackbar(scope, snackbarHostState, "✅ Producto actualizado correctamente")
                                editando = null
                            }

                            nombre = ""
                            precio = ""
                            descripcion = ""
                            categoria = ""
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        navController?.navigate("login") {
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
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = categoria,
                onValueChange = { categoria = it },
                label = { Text("Categoría (Skate, Roller o BMX)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(productos) { producto ->
                    ProductoItem(
                        producto = producto,
                        onDelete = {
                            viewModel.eliminar(producto)
                            showSnackbar(scope, snackbarHostState, "🗑 Producto eliminado")
                        },
                        onEdit = {
                            editando = producto
                            nombre = producto.nombre
                            precio = producto.precio.toString()
                            descripcion = producto.descripcion
                            categoria = producto.categoria
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ProductoItem(producto: Producto, onDelete: () -> Unit, onEdit: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                Text("💰 Precio: \$${producto.precio}")
                Text("📝 ${producto.descripcion}")
                Text("📂 ${producto.categoria}")
            }
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

fun showSnackbar(scope: CoroutineScope, snackbarHostState: SnackbarHostState, message: String) {
    scope.launch {
        snackbarHostState.showSnackbar(message)
    }
}
