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
import com.example.avance.data.UsuarioCreate
import com.example.avance.data.UsuarioApi
import com.example.avance.viewmodel.UsuariosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuariosApiScreen(navController: NavController) {

    val vm: UsuariosViewModel = viewModel()

    var modo by remember { mutableStateOf("lista") }
    var usuarioSeleccionado by remember { mutableStateOf<UsuarioApi?>(null) }

    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("") }
    var comuna by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        vm.cargarUsuarios()
    }

    fun setCamposDesdeUsuario(u: UsuarioApi?) {
        nombre = u?.nombre ?: ""
        email = u?.email ?: ""
        contrasena = u?.contrasena ?: ""
        telefono = u?.telefono?.toString() ?: ""
        region = u?.region ?: ""
        comuna = u?.comuna ?: ""
    }

    fun limpiarCampos() {
        setCamposDesdeUsuario(null)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (modo) {
                            "lista" -> "Usuarios registrados"
                            "crear" -> "Crear Usuario"
                            "editar" -> "Editar Usuario"
                            else -> ""
                        }
                    )
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
                            limpiarCampos()
                            modo = "crear"
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("➕ Crear Usuario")
                    }

                    Spacer(Modifier.height(10.dp))

                    when {
                        vm.cargando -> Text("Cargando usuarios...")
                        vm.error != null -> Text(vm.error!!, color = MaterialTheme.colorScheme.error)
                        vm.usuarios.isEmpty() -> Text("No hay usuarios registrados.")

                        else -> {
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(vm.usuarios) { u ->
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        elevation = CardDefaults.cardElevation(4.dp)
                                    ) {
                                        Column(Modifier.padding(12.dp)) {

                                            Text("👤 ${u.nombre ?: "Sin nombre"}")
                                            Text("📧 ${u.email ?: "Sin email"}")
                                            Text("📱 ${u.telefono ?: 0}")
                                            Text("🌍 ${(u.region ?: "") + ", " + (u.comuna ?: "")}")

                                            Spacer(Modifier.height(8.dp))

                                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                                                Button(
                                                    onClick = {
                                                        usuarioSeleccionado = u
                                                        setCamposDesdeUsuario(u)
                                                        modo = "editar"
                                                    },
                                                    modifier = Modifier.weight(1f)
                                                ) {
                                                    Text("✏ Editar")
                                                }

                                                Button(
                                                    onClick = {
                                                        vm.eliminarUsuario(u.id)
                                                    },
                                                    modifier = Modifier.weight(1f),
                                                    colors = ButtonDefaults.buttonColors(
                                                        MaterialTheme.colorScheme.error
                                                    )
                                                ) {
                                                    Text("🗑 Eliminar")
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
                        onValueChange = { nombre = it ?: "" },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it ?: "" },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = contrasena,
                        onValueChange = { contrasena = it ?: "" },
                        label = { Text("Contraseña") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = telefono,
                        onValueChange = { telefono = it ?: "" },
                        label = { Text("Teléfono") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = region,
                        onValueChange = { region = it ?: "" },
                        label = { Text("Región") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = comuna,
                        onValueChange = { comuna = it ?: "" },
                        label = { Text("Comuna") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(10.dp))

                    Button(
                        onClick = {
                            vm.crearUsuario(
                                UsuarioCreate(
                                    nombre = nombre,
                                    email = email,
                                    contrasena = contrasena,
                                    telefono = telefono.toIntOrNull() ?: 0,
                                    region = region,
                                    comuna = comuna
                                )
                            )
                            modo = "lista"
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Crear Usuario")
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
                        onValueChange = { nombre = it ?: "" },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it ?: "" },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = contrasena,
                        onValueChange = { contrasena = it ?: "" },
                        label = { Text("Contraseña") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = telefono,
                        onValueChange = { telefono = it ?: "" },
                        label = { Text("Teléfono") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = region,
                        onValueChange = { region = it ?: "" },
                        label = { Text("Región") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = comuna,
                        onValueChange = { comuna = it ?: "" },
                        label = { Text("Comuna") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(10.dp))

                    Button(
                        onClick = {
                            usuarioSeleccionado?.let { u ->
                                vm.actualizarUsuario(
                                    id = u.id,
                                    UsuarioCreate(
                                        nombre = nombre,
                                        email = email,
                                        contrasena = contrasena,
                                        telefono = telefono.toIntOrNull() ?: 0,
                                        region = region,
                                        comuna = comuna
                                    )
                                )
                            }
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
