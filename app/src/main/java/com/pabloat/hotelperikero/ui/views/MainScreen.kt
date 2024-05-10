package com.pabloat.hotelperikero.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pabloat.hotelperikero.R
import com.pabloat.hotelperikero.data.local.Habitacion
import com.pabloat.hotelperikero.viewmodel.HotelViewModel

@Composable
fun MainScreen(
    navHostController: NavHostController,
    mainViewModel: HotelViewModel
) {
    val rooms by mainViewModel.randomHabitaciones.collectAsState()
    Scaffold(
        topBar = { TopBarMain() }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
            RoomSectionMain(rooms = rooms)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarMain() {
    CenterAlignedTopAppBar(
        title = { Text("Perikero Hotel", color = Color.Blue) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Black,
            titleContentColor = Color.White
        )
    )
}

@Composable
fun RoomSectionMain(rooms: List<Habitacion>) {
    Column(modifier = Modifier.padding(16.dp)) {
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
            Text("Descripción: ${room.descripcion}", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(vertical = 4.dp))
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