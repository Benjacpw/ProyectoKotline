package com.example.avance

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.avance.data.*
import com.example.avance.viewmodel.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val database = AppDatabase.getInstance(context)
    val productRepo = ProductRepository(database.productoDao())
    val carritoRepo = CarritoRepository(database.carritoDao())

    val factory = AppViewModelFactory(productRepo, carritoRepo)

    val productosViewModel: ProductosViewModel = viewModel(factory = factory)
    val carritoViewModel: CarritoViewModel = viewModel(factory = factory)

    NavHost(navController, startDestination = "login") {

        composable("login") { LoginScreen(navController) }

        composable("home_admin") { HomeScreen(navController, isAdmin = true) }
        composable("home_user") { HomeScreen(navController, isAdmin = false) }

        composable("productos") {
            ProductosScreen(viewModel = productosViewModel, navController = navController)
        }

        composable("catalogo") {
            CatalogoScreen(
                productosViewModel = productosViewModel,
                navController = navController
            )
        }

        composable("carrito") {
            CarritoScreen(
                viewModel = carritoViewModel,
                navController = navController
            )
        }
        composable("detalle/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toLongOrNull() ?: 0L
            DetalleProductoScreen(
                productoId = id,
                viewModel = productosViewModel,
                carritoViewModel = carritoViewModel,
                navController = navController
            )
        }

        composable("quienes_somos") { QuienesSomosScreen(navController) }
    }
}
