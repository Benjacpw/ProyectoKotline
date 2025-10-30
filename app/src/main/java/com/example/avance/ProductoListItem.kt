package com.example.avance

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.avance.data.Producto

@Composable
fun ProductoListItem(producto: Producto, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = producto.nombre, style = MaterialTheme.typography.titleMedium)
            Text(text = "Precio: \$${producto.precio}")
            Text(text = "Descripción: ${producto.descripcion}")
            Text(text = "Categoría: ${producto.categoria}")
        }
    }
}
