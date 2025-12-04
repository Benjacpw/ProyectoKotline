package com.example.avance

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.avance.data.Producto
import com.example.avance.viewmodel.ProductosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(productosViewModel: ProductosViewModel, navController: NavController) {

    val productos = productosViewModel.productos

    val grouped = productos.groupBy { it.categoria }
    LaunchedEffect(Unit) {
        productosViewModel.cargarProductos()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🛍️ Catálogo de Productos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            grouped.forEach { (categoriaNombre, productosLista) ->
                item {
                    Text(
                        text = categoriaNombre,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                items(productosLista) { producto ->
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
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {

        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = producto.imagen,
                contentDescription = producto.titulo,
                modifier = Modifier
                    .width(80.dp)
                    .height(100.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.weight(1f)) {

                Text(producto.titulo, style = MaterialTheme.typography.titleMedium)
                Text("💰 $${producto.precio}")
                Text("📂 ${producto.categoria}")
                Text("📝 ${producto.descripcion}")
            }
        }
    }
}
