package com.example.avance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.avance.data.*
import com.example.avance.viewmodel.ProductosViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosScreen(navController: NavController) {

    val vm: ProductosViewModel = viewModel()

    var modo by remember { mutableStateOf("lista") }
    var seleccionado by remember { mutableStateOf<Producto?>(null) }

    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var imagen by remember { mutableStateOf("") }
    var qty by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun showMsg(msg: String) {
        scope.launch { snackbarHostState.showSnackbar(msg) }
    }

    LaunchedEffect(Unit) {
        vm.cargarProductos()
    }

    fun setCamposDesdeProducto(p: Producto?) {
        titulo = p?.titulo ?: ""
        descripcion = p?.descripcion ?: ""
        precio = p?.precio?.toString() ?: ""
        imagen = p?.imagen ?: ""
        qty = p?.qty?.toString() ?: ""
        categoria = p?.categoria ?: ""
    }

    fun limpiarCampos() = setCamposDesdeProducto(null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (modo) {
                            "lista" -> "Productos Registrados"
                            "crear" -> "Crear Producto"
                            "editar" -> "Editar Producto"
                            else -> ""
                        }
                    )
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
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
                            limpiarCampos()
                            modo = "crear"
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("➕ Crear Producto")
                    }

                    Spacer(Modifier.height(12.dp))

                    when {
                        vm.cargando -> Text("Cargando productos...")
                        vm.error != null -> Text(vm.error!!, color = MaterialTheme.colorScheme.error)
                        vm.productos.isEmpty() -> Text("No hay productos registrados.")
                        else -> {
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(vm.productos) { p ->

                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        elevation = CardDefaults.cardElevation(4.dp)
                                    ) {
                                        Column(Modifier.padding(12.dp)) {

                                            Text("📦 ${p.titulo}")
                                            Text("💲 ${p.precio}")
                                            Text("📦 Stock: ${p.qty}")
                                            Text("🏷️ Categoría: ${p.categoria}")  // <--- STRING

                                            Spacer(Modifier.height(8.dp))

                                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                                                Button(
                                                    onClick = {
                                                        seleccionado = p
                                                        setCamposDesdeProducto(p)
                                                        modo = "editar"
                                                    },
                                                    modifier = Modifier.weight(1f)
                                                ) {
                                                    Text("✏️Editar")
                                                }

                                                Button(
                                                    onClick = { vm.eliminarProducto(p.id!!) },
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

                    OutlinedTextField(titulo, { titulo = it }, label = { Text("Título") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(descripcion, { descripcion = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(precio, { precio = it }, label = { Text("Precio") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(imagen, { imagen = it }, label = { Text("URL Imagen") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(qty, { qty = it }, label = { Text("Stock") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(categoria, { categoria = it }, label = { Text("Categoría") }, modifier = Modifier.fillMaxWidth())

                    Spacer(Modifier.height(12.dp))

                    Button(
                        onClick = {

                            val precioDouble = precio.toDoubleOrNull()
                            val qtyInt = qty.toIntOrNull()

                            when {
                                titulo.isBlank() || descripcion.isBlank() || precio.isBlank() ||
                                        imagen.isBlank() || qty.isBlank() || categoria.isBlank() ->
                                    showMsg("⚠️ Todos los campos son obligatorios")

                                precioDouble == null || precioDouble <= 0 ->
                                    showMsg("⚠️ Precio inválido")

                                qtyInt == null || qtyInt < 0 ->
                                    showMsg("⚠️ Cantidad inválida")

                                else -> {
                                    vm.crearProducto(
                                        ProductoCreate(
                                            titulo = titulo,
                                            descripcion = descripcion,
                                            precio = precioDouble,
                                            imagen = imagen,
                                            qty = qtyInt,
                                            categoria = categoria
                                        )
                                    )
                                    showMsg("✅ Producto creado")
                                    modo = "lista"
                                }
                            }

                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Crear Producto") }

                    OutlinedButton(onClick = { modo = "lista" }, modifier = Modifier.fillMaxWidth()) {
                        Text("Cancelar")
                    }
                }
                "editar" -> {

                    OutlinedTextField(titulo, { titulo = it }, label = { Text("Título") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(descripcion, { descripcion = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(precio, { precio = it }, label = { Text("Precio") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(imagen, { imagen = it }, label = { Text("URL Imagen") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(qty, { qty = it }, label = { Text("Stock") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(categoria, { categoria = it }, label = { Text("Categoría") }, modifier = Modifier.fillMaxWidth())

                    Spacer(Modifier.height(12.dp))

                    Button(
                        onClick = {

                            val precioDouble = precio.toDoubleOrNull()
                            val qtyInt = qty.toIntOrNull()

                            when {
                                titulo.isBlank() || descripcion.isBlank() || precio.isBlank() ||
                                        imagen.isBlank() || qty.isBlank() || categoria.isBlank() ->
                                    showMsg("⚠️ Todos los campos son obligatorios")

                                precioDouble == null || precioDouble <= 0 ->
                                    showMsg("⚠️ Precio inválido")

                                qtyInt == null || qtyInt < 0 ->
                                    showMsg("⚠️ Stock inválido")

                                else -> {
                                    seleccionado?.let { p ->
                                        vm.actualizarProducto(
                                            id = p.id,
                                            ProductoCreate(
                                                titulo = titulo,
                                                descripcion = descripcion,
                                                precio = precioDouble,
                                                imagen = imagen,
                                                qty = qtyInt,
                                                categoria = categoria
                                            )
                                        )
                                    }
                                    showMsg("✅ Cambios guardados")
                                    modo = "lista"
                                }
                            }

                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Guardar Cambios") }

                    OutlinedButton(onClick = { modo = "lista" }, modifier = Modifier.fillMaxWidth()) {
                        Text("Cancelar")
                    }
                }
            }
        }
    }
}
