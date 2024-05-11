package com.pabloat.hotelperikero.ui.views

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pabloat.hotelperikero.R
import com.pabloat.hotelperikero.data.local.entities.Servicio
import com.pabloat.hotelperikero.viewmodel.HotelViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@ExperimentalFoundationApi
@Composable
fun ServiciosScreen(
    navHostController: NavHostController,
    mainViewModel: HotelViewModel
) {
    val servicios by mainViewModel.servicios.collectAsState()

    androidx.compose.material.Scaffold(
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxWidth()
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "¡Nuestros Servicios!",
                    style = MaterialTheme.typography.headlineSmall
                )
ServiciosList(servicios = servicios)            }
        }
    )
}


@Composable
fun ServiciosList(servicios: List<Servicio>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        items(servicios) { servicio ->
            ServicioCard(servicio)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ServicioCard(servicio: Servicio) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = getServiceImageResource(servicio.id)),
                contentDescription = "Imagen del servicio ${servicio.nombre}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(servicio.nombre, style = MaterialTheme.typography.headlineSmall, color = Color.Black)
            Text("Precio: ${servicio.precio}€", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
            Text(servicio.descripcion, style = MaterialTheme.typography.bodyMedium, color = Color.DarkGray)
        }
    }
}


