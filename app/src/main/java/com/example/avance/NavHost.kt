package com.example.avance

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.avance.data.ProductRepository
import com.example.avance.data.AppDatabase
import com.example.avance.viewmodel.ProductosViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val database = AppDatabase.getInstance(context)
    val repo = ProductRepository(database.productoDao())

    val productosViewModel: ProductosViewModel =
        viewModel(factory = ProductosViewModel.Factory(repo))

    NavHost(navController, startDestination = "login") {

        composable("login") { LoginScreen(navController) }

        composable("home_admin") { HomeScreen(navController, isAdmin = true) }
        composable("home_user") { HomeScreen(navController, isAdmin = false) }

        composable("quienes_somos") { QuienesSomosScreen() }
        composable("productos") { ProductosScreen(viewModel = productosViewModel) }
    }
}



