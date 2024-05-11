package com.pabloat.hotelperikero.ui.views

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pabloat.hotelperikero.R
import com.pabloat.hotelperikero.data.local.entities.Habitacion
import com.pabloat.hotelperikero.viewmodel.HotelViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@ExperimentalFoundationApi
@Composable
fun HabitacionesScreen(habitaciones: List<Habitacion>, navHostController: NavHostController, mainViewModel: HotelViewModel) {
    Scaffold(
        content = { padding ->
            Column(modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
                .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                androidx.compose.material3.Text(
                    "¡Nuestras Habitaciones!",
                    style = MaterialTheme.typography.headlineSmall
                )
                RoomList(habitaciones = habitaciones)
            }
        }
    )
}

@Composable
fun RoomList(habitaciones: List<Habitacion>) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(habitaciones) { habitacion ->
            RoomCard(habitacion = habitacion)
        }
    }
}


@Composable
fun RoomCard(habitacion: Habitacion) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        Column(Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = getHabitacionImageResource(habitacion.id)), // Usa un ID de recurso adecuado
                contentDescription = "Imagen de la habitación ${habitacion.descripcion}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
            )
            Text("Habitación: ${habitacion.tipo}", style = MaterialTheme.typography.bodyLarge)
            Text("Descripción: ${habitacion.descripcion}", style = MaterialTheme.typography.bodySmall)
            Text("Precio: ${habitacion.precio}€", style = MaterialTheme.typography.bodySmall)
            if (habitacion.disponibilidad == 1) {
                Button(onClick = { /* Acción, por ejemplo, navegar a detalles */ }) {
                    Text("Reservar")
                }
            } else {
                Text("No disponible", color = Color.Red)
            }
        }
    }
}

fun getHabitacionImageResource(habitacionId: Int?): Int {
    val imageIndex = ((habitacionId?.minus(1))?.rem(10) ?: 1) + 1  // Esto calcula un índice del 1 al 10 repetido
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




/*
@Composable
fun ServiceSection(services: List<Service>) { // Asume que Service es una clase con los datos necesarios
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Nuestros servicios mejor valorados", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            services.forEach { service ->
                ServiceCard(service)
            }
        }
    }
}

@Composable
fun ServiceCard(service: Service) {
    Card(
        elevation = 10.dp,
        modifier = Modifier.padding(10.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.placeholder), // Sustituye esto por tus imágenes
                contentDescription = "Imagen servicio",
                contentScale = ContentScale.Crop,
                modifier = Modifier.height(180.dp).fillMaxWidth()
            )
            Text(service.name, style = MaterialTheme.typography.h6)
            Button(onClick = { /* Acción */ }) {
                Text("Más información")
            }
        }
    }
}

 */