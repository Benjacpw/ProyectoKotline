package com.example.avance

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.avance.viewmodel.ProductosViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val productosViewModel: ProductosViewModel = viewModel()

    NavHost(navController, startDestination = "login") {

        composable("login") { LoginScreen(navController) }

        composable("menu/admin") { MenuScreen(navController, isAdmin = true) }

        composable("menu/user") { MenuScreen(navController, isAdmin = false) }

        composable("quienes_somos") { QuienesSomosScreen() }

        composable("productos") {
            ProductosScreen(viewModel = productosViewModel)
        }
    }
}

