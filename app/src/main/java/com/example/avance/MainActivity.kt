package com.example.avance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.*
import com.example.avance.ui.theme.AvanceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AvanceTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("menu/admin") { MenuScreen(navController, isAdmin = true) }
        composable("menu/user") { MenuScreen(navController, isAdmin = false) }
        composable("quienes_somos") { QuienesSomosScreen() }
    }
}

@Preview(showBackground = true)
@Composable
fun MyAppPreview() {
    AvanceTheme {
        MyApp()
    }
}
