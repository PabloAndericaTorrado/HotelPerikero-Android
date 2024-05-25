package com.pabloat.hotelperikero.ui.views

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.pabloat.hotelperikero.data.local.entities.Habitacion
import com.pabloat.hotelperikero.navigation.Destinations
import com.pabloat.hotelperikero.viewmodel.HotelViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HabitacionesScreen(
    navHostController: NavHostController,
    mainViewModel: HotelViewModel
) {
    val habitaciones by mainViewModel.habitaciones.collectAsState()

    var searchText by remember { mutableStateOf("") }
    var precioFiltro by remember { mutableStateOf(0) }
    var capacidadFiltro by remember { mutableStateOf<Int?>(null) }
    var caracteristicasFiltro by remember { mutableStateOf<List<String>>(emptyList()) }
    var isFilterExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.Gray,
                title = { Text("¡Nuestras Habitaciones!", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) {
        Column {
            // Busqueda Asíncrona
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Buscar habitaciones") },
                modifier = Modifier.padding(16.dp)
            )

            // Abrir filtros
            Button(
                onClick = { isFilterExpanded = !isFilterExpanded },
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text(if (isFilterExpanded) "Cerrar Filtros" else "Abrir Filtros")
            }

            // Desplegable de filtros
            DropdownMenu(
                expanded = isFilterExpanded,
                onDismissRequest = { isFilterExpanded = false },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp),

                ) {
                SearchFilterSection(
                    precioFiltro = precioFiltro,
                    onPrecioFiltroChanged = { precioFiltro = it },
                    capacidadFiltro = capacidadFiltro,
                    onCapacidadFiltroChanged = { capacidadFiltro = it },
                    caracteristicasFiltro = caracteristicasFiltro,
                    onCaracteristicasFiltroChanged = { caracteristicasFiltro = it }
                )
            }

            RoomList(
                habitaciones = habitaciones.filter { habitacion ->
                    val precioFiltrado = if (precioFiltro > 0) precioFiltro else Int.MAX_VALUE
                    habitacion.tipo.contains(searchText, ignoreCase = true) &&
                            habitacion.precio <= precioFiltrado.toString() &&
                            (capacidadFiltro == null || habitacion.capacidad >= capacidadFiltro!!) &&
                            habitacion.caracteristicas.split(",").containsAll(caracteristicasFiltro)
                },
                nav = navHostController,
                mainViewModel = mainViewModel
            )
        }
    }
}


@Composable
fun SearchFilterSection(
    precioFiltro: Int,
    onPrecioFiltroChanged: (Int) -> Unit,
    capacidadFiltro: Int?,
    onCapacidadFiltroChanged: (Int?) -> Unit,
    caracteristicasFiltro: List<String>,
    onCaracteristicasFiltroChanged: (List<String>) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        // Slider para filtro de precio
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Precio máximo: $precioFiltro", modifier = Modifier.padding(end = 8.dp))
            Slider(
                value = precioFiltro.toFloat(),
                onValueChange = { onPrecioFiltroChanged(it.toInt()) },
                valueRange = 0f..300f,
                steps = 60
            )
        }


        // Slider para filtro de capacidad
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text(
                "Capacidad mínima: ${capacidadFiltro ?: "1"}",
                modifier = Modifier.padding(end = 8.dp)
            )
            Slider(
                value = capacidadFiltro?.toFloat() ?: 1f,
                onValueChange = { onCapacidadFiltroChanged(it.toInt()) },
                valueRange = 1f..4f,
                steps = 2
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(capacidadFiltro?.toString() ?: "Ninguno")
        }



        CheckboxList(
            opciones = caracteristicasFiltro,
            onCheckedChange = { onCaracteristicasFiltroChanged(it) }
        )
    }
}

@Composable
fun CheckboxList(
    opciones: List<String>,
    onCheckedChange: (List<String>) -> Unit
) {
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        Text("Características:")
        val caracteristicas = listOf(
            "vista_mar", "fumadores", "mascotas", "vista_interior",
            "balcon_privado", "vista_ciudad", "balcon_compartido"
        )
        caracteristicas.forEach { caracteristica ->
            val isChecked = opciones.contains(caracteristica)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { isChecked ->
                        val updatedList = if (isChecked) {
                            opciones + caracteristica
                        } else {
                            opciones - caracteristica
                        }
                        onCheckedChange(updatedList)
                    }
                )
                Text(caracteristica, modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}


@Composable
fun RoomList(
    habitaciones: List<Habitacion>,
    nav: NavHostController,
    mainViewModel: HotelViewModel
) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(habitaciones) { habitacion ->
            RoomCard(habitacion = habitacion, nav = nav, mainViewModel = mainViewModel)
        }
    }
}

@Composable
fun RoomCard(habitacion: Habitacion, nav: NavHostController, mainViewModel: HotelViewModel) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .clickable {
                nav.navigate(Destinations.HabitacionDetalleScreen.route)
                mainViewModel.selectHabitacionId(habitacion.id)
            },
    ) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(model= getHabitacionImageUrl(habitacion.id)),
                contentDescription = "Imagen de la habitación ${habitacion.descripcion}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
            )
            Text(habitacion.tipo, style = MaterialTheme.typography.bodyLarge)
            Text(habitacion.descripcion, style = MaterialTheme.typography.bodySmall)
            Text("${habitacion.precio}€/Noche", style = MaterialTheme.typography.bodySmall)
        }
    }
}

