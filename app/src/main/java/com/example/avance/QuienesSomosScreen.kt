package com.example.avance

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.avance.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuienesSomosScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quiénes Somos") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.skate),
                contentDescription = "Nuestros Deportes!",
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Somos un emprendimiento dedicado al deporte extremo. Nuestra misión es inspirar, motivar y equipar a quienes buscan aventura y superación.",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuienesSomosScreenPreview() {
    QuienesSomosScreen()
}
