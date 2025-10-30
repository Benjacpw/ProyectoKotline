package com.example.avance

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.avance.data.Producto
import com.example.avance.viewmodel.ProductosViewModel
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosScreen(viewModel: ProductosViewModel = viewModel()) {
    val productos by viewModel.productos.collectAsState(initial = emptyList())

    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") } // String
    var descripcion by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("📦 Productos") },
                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primaryContainer)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Agregar") },
                icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },
                onClick = {
                    if (nombre.isNotBlank() && precio.isNotBlank() && descripcion.isNotBlank() && categoria.isNotBlank()) {
                        val precioDouble = precio.toDoubleOrNull() ?: 0.0
                        viewModel.agregar(
                            Producto(
                                nombre = nombre.trim(),
                                precio = precioDouble,
                                descripcion = descripcion.trim(),
                                categoria = categoria.trim()
                            )
                        )
                        nombre = ""
                        precio = ""
                        descripcion = ""
                        categoria = ""
                    }
                }
            )
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = categoria,
                onValueChange = { categoria = it },
                label = { Text("Categoría") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            LazyColumn {
                items(productos) { producto ->
                    ProductoItem(producto) { viewModel.eliminar(producto) }
                }
            }
        }
    }
}

@Composable
fun ProductoItem(producto: Producto, onDelete: () -> Unit) {
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
            Column {
                Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                Text("Precio: \$${producto.precio}")
                Text("Descripción: ${producto.descripcion}")
                Text("Categoría: ${producto.categoria}")
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

@Preview(showBackground = true)
@Composable
fun ProductosScreenPreview() {
    val productosEjemplo = listOf(
        Producto(id = 1, nombre = "Tabla Skate A", precio = 150.0, descripcion = "Tabla profesional", categoria = "Skate"),
        Producto(id = 2, nombre = "Tabla Skate B", precio = 120.0, descripcion = "Tabla para principiantes", categoria = "Skate"),
        Producto(id = 3, nombre = "Roller Pro", precio = 200.0, descripcion = "Roller de competición", categoria = "Roller"),
        Producto(id = 4, nombre = "Roller Básico", precio = 80.0, descripcion = "Roller para recreación", categoria = "Roller"),
        Producto(id = 5, nombre = "BMX Xtreme", precio = 350.0, descripcion = "BMX para trucos", categoria = "BMX"),
        Producto(id = 6, nombre = "BMX Urban", precio = 300.0, descripcion = "BMX urbana", categoria = "BMX")
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Text("📦 Productos (Preview)", style = MaterialTheme.typography.titleLarge)
        LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
            items(productosEjemplo) { producto ->
                ProductoItem(producto = producto, onDelete = {})
            }
        }
    }
}

