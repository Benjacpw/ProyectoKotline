package com.example.avance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
fun HomeScreen(navController: NavController) {

    val productosViewModel: ProductosViewModel = viewModel()
    val productos = productosViewModel.productos.take(6)

    LaunchedEffect(Unit) {
        productosViewModel.cargarProductos()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Menú Principal") })
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

            Text(
                text = "Bienvenido a",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Text(
                text = "ExtremeShop",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(Modifier.height(18.dp))

            Text(
                "Productos Recomendados",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(Modifier.height(12.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.height(370.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(productos) { producto ->
                    ProductoRecomendadoCard(producto)
                }
            }

            Spacer(Modifier.height(20.dp))

            HomeBoton("🛍️ Ver Productos") {
                navController.navigate("catalogo")
            }

            Spacer(Modifier.height(10.dp))

            HomeBoton("🛒 Ver Carrito") {
                navController.navigate("carrito")
            }

            Spacer(Modifier.height(10.dp))

            HomeBoton("👥 Quienes Somos") {
                navController.navigate("quienes_somos")
            }

            Spacer(Modifier.height(16.dp))

            OutlinedButton(
                onClick = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("🚪 Cerrar Sesión", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun ProductoRecomendadoCard(producto: com.example.avance.data.Producto) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp),
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

            Text(
                text = "💰 $${producto.precio}",
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = producto.categoria,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun HomeBoton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(text, fontSize = 17.sp)
    }
}
