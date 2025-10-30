package com.example.avance

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.avance.data.CarritoItem
import com.example.avance.viewmodel.CarritoViewModel
import com.example.avance.viewmodel.ProductosViewModel
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(
    productoId: Long,
    viewModel: ProductosViewModel,
    carritoViewModel: CarritoViewModel,
    navController: NavController
) {
    val productos by viewModel.productos.collectAsState(initial = emptyList())
    val producto = productos.find { it.id == productoId }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🛒 Detalle del Producto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        producto?.let {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(24.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(it.nombre, style = MaterialTheme.typography.headlineMedium)
                Text("💰 Precio: \$${it.precio}", style = MaterialTheme.typography.bodyLarge)
                Text("📂 Categoría: ${it.categoria}")
                Text("📝 Descripción: ${it.descripcion}")

                Spacer(Modifier.height(32.dp))

                Button(
                    onClick = {
                        scope.launch {
                            val nuevoItem = CarritoItem(
                                idProducto = it.id,
                                nombre = it.nombre,
                                precio = it.precio,
                                cantidad = 1
                            )

                            carritoViewModel.agregar(nuevoItem)
                            snackbarHostState.showSnackbar("✅ ${it.nombre} agregado al carrito")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("🛍 Agregar al carrito")
                }
            }
        } ?: Text(
            "Producto no encontrado",
            modifier = Modifier.padding(24.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

