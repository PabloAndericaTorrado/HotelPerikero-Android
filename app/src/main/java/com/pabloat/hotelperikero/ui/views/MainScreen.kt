package com.pabloat.hotelperikero.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pabloat.hotelperikero.R
import com.pabloat.hotelperikero.data.local.Habitacion
import com.pabloat.hotelperikero.data.local.Servicio
import com.pabloat.hotelperikero.navigation.Destinations
import com.pabloat.hotelperikero.viewmodel.HotelViewModel

@Composable
fun MainScreen(
    navHostController: NavHostController,
    mainViewModel: HotelViewModel
) {
    val rooms by mainViewModel.randomHabitaciones.collectAsState()
    val servicios by mainViewModel.servicios.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scrollState) // Agregar capacidad de desplazamiento vertical

        ) {
            WelcomeSection(navHostController)
            RoomSectionMain(rooms = rooms)
            Spacer(modifier = Modifier.height(16.dp))
            ServiceSectionMain(servicios = servicios)

        }
    }
}

@Composable
fun ServiceSectionMain(servicios: List<Servicio>) {
    Column(modifier = Modifier.padding(16.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("Todos Nuestros Servicios", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow {
            items(servicios) { servicio ->
                ServiceCardMain(servicio)
            }
        }
    }
}

@Composable
fun ServiceCardMain(servicio: Servicio) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .width(200.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column {
            Image(
                painter = painterResource(id = getServiceImageResource(servicio.id)), // Necesitas implementar esta función
                contentDescription = "Imagen del servicio ${servicio.descripcion}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
            )
            Text(
                servicio.nombre,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                "Descripción: ${servicio.descripcion}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Text(
                "Precio: ${servicio.precio}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Button(onClick = { /* Acción */ }, modifier = Modifier.fillMaxWidth()) {
                Text("Más información")
            }
        }
    }
}

fun getServiceImageResource(servicioid: Int?): Int {
    val imageIndex = ((servicioid?.minus(1))?.rem(10) ?: 1) + 1  // Esto calcula un índice del 1 al 10 repetido
    return when (imageIndex) {
        1 -> R.drawable.habitacion_1
        2 -> R.drawable.habitacion_2
        3 -> R.drawable.habitacion_3
        4 -> R.drawable.habitacion_4
        5 -> R.drawable.habitacion_5
        6 -> R.drawable.habitacion_6
        7 -> R.drawable.habitacion_7
        8 -> R.drawable.habitacion_8
        9 -> R.drawable.habitacion_9
        10 -> R.drawable.habitacion_10
        else -> R.drawable.habitacion_1 // Un recurso por defecto, en caso de error en el cálculo
    }
}


@Composable
fun WelcomeSection(navHostController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo1), // Asume que 'fondo1' es el ID de tu recurso
            contentDescription = null, // No es necesario describir visualmente el fondo
            contentScale = ContentScale.Crop, // Ajusta la imagen para que llene el fondo
            modifier = Modifier.matchParentSize() // Asegura que la imagen llene el Box
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Bienvenidos al Perikero Hotel",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontWeight = FontWeight.Bold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Reserva Aquí!", style = MaterialTheme.typography.titleLarge, color = Color.Red , modifier = Modifier.clickable {
                    navHostController.navigate(Destinations.HabitacionesScreen.route)
                })
            }
        }
    }
}

@Composable
fun RoomSectionMain(rooms: List<Habitacion>) {
    Column(modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("Nuestras habitaciones mejor valoradas", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow {
            items(rooms) { room ->
                RoomCardMain(room)
            }
        }
    }
}

@Composable
fun RoomCardMain(room: Habitacion) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .width(200.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column {
            Image(
                painter = painterResource(id = getHabitacionImageResource(room.id)),
                contentDescription = "Imagen de la habitación ${room.descripcion}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
            )
            Text(room.tipo, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 4.dp))
            Text(" ${room.descripcion}", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(vertical = 4.dp))
            Text("Precio: ${room.precio}€/Noche", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(vertical = 4.dp))
            if (room.disponibilidad == 1) {
                Button(onClick = { /* Acción */ }, modifier = Modifier.fillMaxWidth()) {
                    Text("Más información")
                }
            } else {
                Text("No disponible", color = Color.Red, modifier = Modifier.padding(4.dp))
            }
        }
    }
}

