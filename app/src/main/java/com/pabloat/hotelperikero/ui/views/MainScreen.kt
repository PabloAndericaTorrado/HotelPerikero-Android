package com.pabloat.hotelperikero.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pabloat.hotelperikero.R
import com.pabloat.hotelperikero.data.local.entities.Espacio
import com.pabloat.hotelperikero.data.local.entities.Habitacion
import com.pabloat.hotelperikero.data.local.entities.Servicio
import com.pabloat.hotelperikero.navigation.Destinations
import com.pabloat.hotelperikero.viewmodel.HotelViewModel

@Composable
fun MainScreen(
    navHostController: NavHostController,
    mainViewModel: HotelViewModel
) {
    val rooms by mainViewModel.randomHabitaciones.collectAsState()
    val servicios by mainViewModel.servicios.collectAsState()
    val espacios by mainViewModel.espacios.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Image(painter = painterResource(id = R.drawable.fondo_oscurecido),
            contentDescription = "background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop)
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scrollState) // Agregar capacidad de desplazamiento vertical

        ) {
            WelcomeSection(navHostController)
            RoomSectionMain(
                rooms = rooms,
                navHostController = navHostController,
                mainViewModel = mainViewModel
            )
            Spacer(modifier = Modifier.height(16.dp))
            ServiceSectionMain(servicios = servicios,navHostController = navHostController)
            EspacioSectionMain(navHostController = navHostController, mainViewModel = mainViewModel,espacios = espacios)
            FooterSection(navHostController)  // Llamada al nuevo Composable para el pie de página

        }
    }
}

@Composable
fun ServiceSectionMain(servicios: List<Servicio>,navHostController: NavHostController) {
    val LightBlue = Color(173, 216, 230)  // Un ejemplo de light blue

    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("¡Todos Nuestros Servicios!", style = MaterialTheme.typography.headlineSmall, color = Color.White)
        Text("¡Toca Aquí Para Ver Más!", modifier = Modifier.clickable { navHostController.navigate(Destinations.ServiciosScreen.route) }, color = Color.White)
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
        Column(modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
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
        }
    }
}

fun getServiceImageResource(servicioid: Int?): Int {
    val imageIndex = ((servicioid?.minus(1))?.rem(10) ?: 1) + 1  // Esto calcula un índice del 1 al 10 repetido
    return when (imageIndex) {
        1 -> R.drawable.servicio_1
        2 -> R.drawable.servicio_2
        3 -> R.drawable.servicio_3
        4 -> R.drawable.servicio_4
        5 -> R.drawable.servicio_5
        6 -> R.drawable.servicio_6
        7 -> R.drawable.servicio_7
        8 -> R.drawable.servicio_8
        9 -> R.drawable.servicio_9
        10 -> R.drawable.servicio_10
        else -> R.drawable.servicio_1 // Un recurso por defecto, en caso de error en el cálculo
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Bienvenidos al Perikero Hotel",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
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
fun RoomSectionMain(
    rooms: List<Habitacion>,
    navHostController: NavHostController,
    mainViewModel: HotelViewModel
) {
    Column(modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("¡Nuestras Mejores Habitaciones!", style = MaterialTheme.typography.headlineSmall, color = Color.White)
        Text("¡Toca Aquí Para Ver Reseñas!", style = MaterialTheme.typography.bodySmall, modifier = Modifier.clickable { navHostController.navigate(Destinations.ReseniasScreen.route) }, color = Color.White)
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow {
            items(rooms) { room ->
                RoomCardMain(room, navHostController, mainViewModel = mainViewModel)
            }
        }
    }
}

@Composable
fun RoomCardMain(room: Habitacion, nav: NavHostController, mainViewModel: HotelViewModel) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .width(200.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = getHabitacionImageResource(room.id)),
                contentDescription = "Imagen de la habitación ${room.id}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
            )
            Text(room.tipo, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 4.dp), color = Color.Black)
            Text(" ${room.descripcion}", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(vertical = 4.dp),color = Color.Black)
            Text("Precio: ${room.precio}€/Noche", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(vertical = 4.dp),color = Color.Black)
            if (room.disponibilidad == 1) {
                Button(onClick = {
                    nav.navigate(Destinations.HabitacionDetalleScreen.route); mainViewModel.selectHabitacionId(
                    room.id
                )
                }, modifier = Modifier.fillMaxWidth()) {
                    Text("Ir a la Habitación")
                }
            } else {
                Text("No disponible", color = Color.Red, modifier = Modifier.padding(4.dp))
            }
        }
    }
}

@Composable
fun FooterSection(navHostController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Contacto", style = MaterialTheme.typography.titleMedium, color = Color.White)
        Text("Teléfono: +123 456 7890", style = MaterialTheme.typography.bodyMedium, color = Color.White)
        Text("Email: info@perikerohotel.com", style = MaterialTheme.typography.bodyMedium, color = Color.White)
        Text("Dirección: C. Estébanez Calderón, 10, Marbella", style = MaterialTheme.typography.bodyMedium, color = Color.White)
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = { navHostController.navigate(Destinations.Contacto.route) }, colors = ButtonDefaults.buttonColors()
        ){
            Text("Más Información", color = Color.White)
        }
    }
}

@Composable
fun EspacioSectionMain(espacios: List<Espacio>,navHostController: NavHostController, mainViewModel: HotelViewModel) {
    Column(modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("¡Celebra Con Nosotros!", style = MaterialTheme.typography.headlineSmall, color = Color.White)
        Text("¡Toca Aquí Para Reservar!", style = MaterialTheme.typography.bodySmall, modifier = Modifier.clickable { navHostController.navigate(Destinations.EspaciosScreen.route) }, color = Color.White)
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow {
            items(espacios) { espacio ->
                EsapcioCardMain(espacio)
            }
        }
    }
}

@Composable
fun EsapcioCardMain(espacio: Espacio) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .width(200.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = getEspacioImageResource(espacio.id)),
                contentDescription = "Imagen de la habitación ${espacio.id}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
            )
            Text(espacio.nombre, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 4.dp), color = Color.Black)
            Text("Precio: ${espacio.precio}€/Hora", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(vertical = 4.dp),color = Color.Black)
        }
    }
}

fun getEspacioImageResource(espacioId: Int?): Int {
    return when (espacioId) {
        1 -> R.drawable.espacio_1
        2 -> R.drawable.espacio_2
        3 -> R.drawable.espacio_3
        4 -> R.drawable.espacio_4
        5 -> R.drawable.espacio_5
        else -> R.drawable.espacio_1 // Un recurso por defecto, en caso de error en el cálculo
    }
}
