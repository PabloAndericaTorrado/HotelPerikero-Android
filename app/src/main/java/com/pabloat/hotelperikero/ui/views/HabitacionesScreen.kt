package com.pabloat.hotelperikero.ui.views

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.pabloat.hotelperikero.R
import com.pabloat.hotelperikero.data.local.entities.Habitacion
import com.pabloat.hotelperikero.navigation.Destinations
import com.pabloat.hotelperikero.viewmodel.HotelViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation", "UnusedMaterialScaffoldPaddingParameter")
@ExperimentalFoundationApi
@Composable
fun HabitacionesScreen(
    navHostController: NavHostController,
    mainViewModel: HotelViewModel
) {
    val habitaciones by mainViewModel.habitaciones.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.Gray,
                title = {
                    Text("¡Nuestras Habitaciones!", color = Color.White)
                },
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
        RoomList(
            habitaciones = habitaciones,
            nav = navHostController,
            mainViewModel = mainViewModel
        )
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

