package com.example.avance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.avance.data.Categoria
import com.example.avance.data.CategoriaCreate
import com.example.avance.viewmodel.CategoriasViewModel
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiCategoriasScreen(navController: NavController) {

    val vm: CategoriasViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

    var modo by remember { mutableStateOf("lista") }
    var seleccionado by remember { mutableStateOf<Categoria?>(null) }
    var nombre by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun showMsg(msg: String) {
        scope.launch { snackbarHostState.showSnackbar(msg) }
    }

    LaunchedEffect(Unit) {
        vm.cargarCategorias()
    }

    fun setCampos(cat: Categoria?) {
        nombre = cat?.nombre ?: ""
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (modo) {
                            "lista" -> "Categorías Registradas"
                            "crear" -> "Crear Categoría"
                            "editar" -> "Editar Categoría"
                            else -> ""
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("dashboard_admin") {
                            popUpTo("dashboard_admin") { inclusive = true }
                        }
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
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

            when (modo) {

                "lista" -> {

                    Button(
                        onClick = {
                            setCampos(null)
                            modo = "crear"
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("➕ Crear Categoría")
                    }

                    Spacer(Modifier.height(12.dp))

                    when {
                        vm.cargando -> Text("Cargando categorías")
                        vm.error != null -> Text(vm.error!!, color = MaterialTheme.colorScheme.error)
                        vm.categorias.isEmpty() -> Text("No hay categorías registradas.")
                        else -> {
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(vm.categorias) { cat ->

                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        elevation = CardDefaults.cardElevation(4.dp)
                                    ) {
                                        Column(Modifier.padding(12.dp)) {

                                            Text("🏷️ ${cat.nombre}")

                                            Spacer(Modifier.height(8.dp))

                                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                                                Button(
                                                    onClick = {
                                                        seleccionado = cat
                                                        setCampos(cat)
                                                        modo = "editar"
                                                    },
                                                    modifier = Modifier.weight(1f)
                                                ) {
                                                    Text("✏️Editar")
                                                }

                                                Button(
                                                    onClick = {
                                                        vm.eliminarCategoria(cat.id)
                                                    },
                                                    modifier = Modifier.weight(1f),
                                                    colors = ButtonDefaults.buttonColors(
                                                        MaterialTheme.colorScheme.error
                                                    )
                                                ) {
                                                    Text("🗑️Eliminar")
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                "crear" -> {

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre Categoría") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(12.dp))

                    Button(
                        onClick = {
                            when {
                                nombre.isBlank() -> showMsg("⚠️ Nombre obligatorio")
                                else -> {
                                    vm.crearCategoria(CategoriaCreate(nombre))
                                    showMsg("✅ Categoría creada")
                                    modo = "lista"
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Crear Categoría")
                    }

                    OutlinedButton(
                        onClick = { modo = "lista" },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cancelar")
                    }
                }

                "editar" -> {

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre Categoría") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(12.dp))

                    Button(
                        onClick = {
                            seleccionado?.let { cat ->
                                vm.actualizarCategoria(cat.id, CategoriaCreate(nombre))
                            }
                            showMsg("✅ Cambios guardados")
                            modo = "lista"
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Guardar Cambios")
                    }

                    OutlinedButton(
                        onClick = { modo = "lista" },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cancelar")
                    }
                }
            }
        }
    }
}
