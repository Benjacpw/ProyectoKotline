package com.example.avance

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.avance.data.Producto
import com.example.avance.viewmodel.ProductosViewModel
import androidx.compose.runtime.getValue

@Composable
fun CatalogoScreen(productosViewModel: ProductosViewModel, navController: NavController) {
    val productos by productosViewModel.productos.collectAsState()
    val grouped = productos.groupBy { it.categoria }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        grouped.forEach { (categoria, lista) ->
            item {
                Text(categoria, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(vertical = 8.dp))
            }
            items(lista) { producto ->
                ProductoListItem(producto = producto, onClick = { navController.navigate("detalle/${producto.id}") })
            }
        }
    }
}
