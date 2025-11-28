package com.example.avance

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.avance.data.CarritoItem
import com.example.avance.viewmodel.CarritoViewModel
import com.example.avance.viewmodel.ProductosViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(
    productoId: Long,
    viewModel: ProductosViewModel,
    carritoViewModel: CarritoViewModel,
    navController: NavController
) {
    val productos = viewModel.productos
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
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        producto?.let { p ->

            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(24.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = p.imagen,
                    contentDescription = p.titulo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )

                Text(p.titulo, style = MaterialTheme.typography.headlineMedium)
                Text("💰 Precio: $${p.precio}", style = MaterialTheme.typography.titleLarge)
                Text("📂 Categoría: ${p.categoria}")
                Text("📝 ${p.descripcion}")

                Spacer(Modifier.height(20.dp))

                Button(
                    onClick = {
                        scope.launch {
                            carritoViewModel.agregar(
                                CarritoItem(
                                    idProducto = p.id,
                                    nombre = p.titulo,
                                    precio = p.precio,
                                    cantidad = 1
                                )
                            )
                            snackbarHostState.showSnackbar("🛒 ${p.titulo} agregado al carrito")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("🛍 Agregar al carrito")
                }
            }

        } ?: Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("❌ Producto no encontrado", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
