package com.pabloat.hotelperikero.ui.views

//noinspection UsingMaterialAndMaterial3Libraries
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pabloat.hotelperikero.R
import com.pabloat.hotelperikero.data.local.entities.Habitacion
import com.pabloat.hotelperikero.navigation.Destinations
import com.pabloat.hotelperikero.viewmodel.HotelViewModel


@SuppressLint(
    "UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation",
    "UnusedMaterialScaffoldPaddingParameter"
)
@ExperimentalFoundationApi
@Composable
fun HabitacionesScreen(habitaciones: List<Habitacion>, navHostController: NavHostController, mainViewModel: HotelViewModel) {
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


    )
    {
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
                nav.navigate(Destinations.HabitacionDetalleScreen.route); mainViewModel.selectHabitacionId(
                habitacion.id
            )
            },
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
            Text(habitacion.tipo, style = MaterialTheme.typography.bodyLarge)
            Text(habitacion.descripcion, style = MaterialTheme.typography.bodySmall)
            Text("${habitacion.precio}€/Noche", style = MaterialTheme.typography.bodySmall)

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