package com.example.avance

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.avance.data.Producto
import com.example.avance.viewmodel.ProductosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(productosViewModel: ProductosViewModel, navController: NavController) {
    val productos by productosViewModel.productos.collectAsState(initial = emptyList())
    val grouped = productos.groupBy { it.categoria }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🛍 Catálogo de Productos") },
                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primaryContainer)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            grouped.forEach { (categoria, lista) ->
                item {
                    Text(
                        text = categoria,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(lista) { producto ->
                    ProductoCatalogoItem(
                        producto = producto,
                        onClick = { navController.navigate("detalle/${producto.id}") }
                    )
                }
            }
        }
    }
}

@Composable
fun ProductoCatalogoItem(producto: Producto, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
            Text("💰 Precio: \$${producto.precio}")
            Text("📂 Categoría: ${producto.categoria}")
            Text("📝 ${producto.descripcion}")
        }
    }
}
