package com.example.avance

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.avance.viewmodel.ProductosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, productosViewModel: ProductosViewModel = viewModel()) {

    val productos = productosViewModel.productos.take(6)

    LaunchedEffect(Unit) {
        productosViewModel.cargarProductos()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Menú Principal") })
        },
        bottomBar = {
            HomeBottomBar(navController)
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(10.dp))

            Text("Bienvenido a", style = MaterialTheme.typography.titleLarge)
            Text("ExtremeShop", style = MaterialTheme.typography.titleLarge)

            Spacer(Modifier.height(18.dp))
            Text("Productos Recomendados", style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(12.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(productos) { producto ->
                    ProductoRecomendadoCard(
                        producto = producto,
                        onClick = { id ->
                            navController.navigate("detalle/$id")
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun ProductoRecomendadoCard(
    producto: com.example.avance.data.Producto,
    onClick: (Long) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp)
            .clickable { onClick(producto.id) },
        elevation = CardDefaults.cardElevation(8.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AsyncImage(
                model = producto.imagen,
                contentDescription = producto.titulo,
                modifier = Modifier
                    .height(130.dp)
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = producto.titulo.take(22),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                maxLines = 1
            )

            Text("💰 $${producto.precio}", style = MaterialTheme.typography.bodySmall)
            Text(producto.categoria, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
    }
}

@Composable
fun HomeBottomBar(navController: NavController) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary
    ) {

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("catalogo") },
            icon = { Icon(Icons.Default.Store, contentDescription = "Productos", tint = Color.White) },
            label = { Text("Productos", color = Color.White) }
        )

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("carrito") },
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito", tint = Color.White) },
            label = { Text("Carrito", color = Color.White) }
        )

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("quienes_somos") },
            icon = { Icon(Icons.Default.People, contentDescription = "Nosotros", tint = Color.White) },
            label = { Text("Nosotros", color = Color.White) }
        )

        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate("login") {
                    popUpTo("home_user") { inclusive = true }
                }
            },
            icon = {
                Icon(
                    Icons.Filled.PowerSettingsNew,
                    contentDescription = "Cerrar sesión",
                    tint = Color.Red
                )
            },
            label = { Text("Salir", color = Color.Red) }
        )
    }
}
