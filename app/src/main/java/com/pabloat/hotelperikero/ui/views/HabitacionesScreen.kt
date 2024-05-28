package com.pabloat.hotelperikero.ui.views

//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Scaffold
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.pabloat.hotelperikero.data.local.entities.Habitacion
import com.pabloat.hotelperikero.navigation.Destinations
import com.pabloat.hotelperikero.viewmodel.HotelViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HabitacionesScreen(
    navHostController: NavHostController,
    mainViewModel: HotelViewModel
) {
    val habitaciones by mainViewModel.habitaciones.collectAsState()

    var searchText by remember { mutableStateOf("") }
    var precioFiltro by remember { mutableIntStateOf(0) }
    var capacidadFiltro by remember { mutableStateOf<Int?>(null) }
    var caracteristicasFiltro by remember { mutableStateOf<List<String>>(emptyList()) }
    var isFilterExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color(0xFF2A4B8D),
                title = { Text("¡Nuestras Habitaciones!", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    label = { Text("Buscar habitaciones") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = Color(0xFF2A4B8D)
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF2A4B8D),
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Color(0xFF2A4B8D)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { isFilterExpanded = !isFilterExpanded },
                    modifier = Modifier
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2A4B8D),
                        contentColor = Color.White
                    ),
                ) {
                    Text(if (isFilterExpanded) "Cerrar Filtros" else "Abrir Filtros", color = Color.White)
                }
            }

            AnimatedVisibility(visible = isFilterExpanded) {
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
                valueRange = 0f..300f
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
            .shadow(8.dp, shape = RoundedCornerShape(16.dp))
            .clickable {
                nav.navigate(Destinations.HabitacionDetalleScreen.route)
                mainViewModel.selectHabitacionId(habitacion.id)
            },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = getHabitacionImageUrl(habitacion.id)),
                contentDescription = "Imagen de la habitación ${habitacion.descripcion}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                habitacion.tipo,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF2A4B8D)),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                habitacion.descripcion,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF333333)),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "${habitacion.precio}€/Noche",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF4A90E2)),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (habitacion.disponibilidad == 1) {
                Button(
                    onClick = {
                        nav.navigate(Destinations.HabitacionDetalleScreen.route)
                        mainViewModel.selectHabitacionId(habitacion.id)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A4B8D))
                ) {
                    Text("Ir a la Habitación", style = MaterialTheme.typography.bodyMedium.copy(color = Color.White))
                }
            } else {
                Text("No disponible", color = Color.Red, modifier = Modifier.padding(4.dp))
            }
        }
    }
}
