package com.example.avance

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.avance.data.RetrofitInstance
import com.example.avance.viewmodel.CarritoViewModel
import com.example.avance.viewmodel.ProductosViewModel
import com.example.avance.viewmodel.UsuariosViewModel
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val productosViewModel: ProductosViewModel = viewModel()
    val carritoViewModel: CarritoViewModel = viewModel()
    val usuariosViewModel: UsuariosViewModel = viewModel()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController, usuariosViewModel)
        }
        composable("register") {
            RegistroScreen(navController, usuariosViewModel)
        }
        composable("home_user") {
            HomeScreen(navController, productosViewModel)
        }

        composable("productos") {
            ProductosScreen(navController)
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
                navController = navController,
                apiService = RetrofitInstance.api
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
        composable("quienes_somos") {
            QuienesSomosScreen(navController)
        }
        composable("usuarios_api") {
            UsuariosApiScreen(navController)
        }
        composable("categoria_api") {
            ApiCategoriasScreen(navController)
        }

        composable("ordenes") {
            OrdenesScreen(navController = navController)
        }

        composable("dashboard_admin") {
            DashboardAdminScreen(navController)
        }
        composable("recuperar_contrasena") {
            RecuperarContrasenaScreen(navController)
        }

        composable("detalle/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toLong() ?: 0L
            DetalleProductoScreen(
                productoId = id,
                viewModel = productosViewModel,
                carritoViewModel = carritoViewModel,
                navController = navController
            )
        }
    }
}