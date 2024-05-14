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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
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
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.fondo_oscurecido),
                contentDescription = "background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                WelcomeSection(navHostController)
                RoomSectionMain(
                    rooms = rooms,
                    navHostController = navHostController,
                    mainViewModel = mainViewModel
                )
                Spacer(modifier = Modifier.height(16.dp))
                ServiceSectionMain(servicios = servicios, navHostController = navHostController)
                EspacioSectionMain(espacios = espacios, navHostController = navHostController)
                FooterSection(navHostController)
            }
        }
    }
}

@Composable
fun ServiceSectionMain(servicios: List<Servicio>, navHostController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
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
    val painter = rememberAsyncImagePainter(model = getServiceImageUrl(servicio.id))
    Card(
        modifier = Modifier
            .padding(10.dp)
            .width(200.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painter,
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

fun getServiceImageUrl(servicioId: Int?): String {
    val imageUrls = mapOf(
        1 to "https://estaticosgn-cdn.deia.eus/clip/3c84d28d-e825-4262-81cd-ae0dfe0af667_16-9-aspect-ratio_default_0.jpg",
        2 to "https://theobjective.com/wp-content/uploads/2023/07/Surtido-de-panes-en-el-desayuno-de-hotel-1024x576.webp",
        3 to "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcQyyZWVev_nRM_o5PIpT5cB3lO95NIQZMSnzmfe_75Lr95nu609",
        4 to "https://www.macrun.es/wp-content/uploads/2021/02/macrun-turismo1-480x480.jpg",
        5 to "https://img.freepik.com/foto-gratis/piscina-cubierta-grandes-ventanales-que-permiten-que-luz-natural-ilumine-agua-azul-clara_1268-31068.jpg?t=st=1715725869~exp=1715729469~hmac=e317a4d4ac422c8226564322712ae8f09f721c9ee31700e2903b7ad255f7995b&w=1480",
        6 to "https://demo.phlox.pro/gym/wp-content/uploads/sites/83/2018/12/image-from-rawpixel-id-378161-jpeg@2x-1-1000x1000.jpg",
        7 to "https://cdn.getyourguide.com/img/tour/6418454ae310c.jpeg/98.jpg",
        8 to "https://imagenes.20minutos.es/files/image_640_360/files/fp/uploads/imagenes/2023/07/03/mujer-esperando-aeropuerto.r_d.1511-1179-2133.jpeg",
        9 to "https://www.baikoonthai-bilbao.es/wp-content/uploads/2024/03/plate-with-some-inventory-massage_144627-24245-1024x683.jpg",
        10 to "https://www.brokersdoc.com/wp-content/uploads/2023/06/Grupo_excusionistas_con_guia.jpg"
    )

    return imageUrls[servicioId] ?: "https://example.com/default_service.jpg"
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
                Text(text = "Reserva Aquí!", style = MaterialTheme.typography.titleLarge, color = Color.Red, modifier = Modifier.clickable {
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
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
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
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = getHabitacionImageUrl(room.id)),
                contentDescription = "Imagen de la habitación ${room.id}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
            )
            Text(room.tipo, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 4.dp), color = Color.Black)
            Text(" ${room.descripcion}", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(vertical = 4.dp), color = Color.Black)
            Text("Precio: ${room.precio}€/Noche", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(vertical = 4.dp), color = Color.Black)
            if (room.disponibilidad == 1) {
                Button(onClick = {
                    nav.navigate(Destinations.HabitacionDetalleScreen.route)
                    mainViewModel.selectHabitacionId(room.id)
                }, modifier = Modifier.fillMaxWidth()) {
                    Text("Ir a la Habitación")
                }
            } else {
                Text("No disponible", color = Color.Red, modifier = Modifier.padding(4.dp))
            }
        }
    }
}

fun getHabitacionImageUrl(habitacionId: Int?): String {
    val imageUrls = mapOf(
        1 to "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQyUG2CquISf_UIK3TsoUhBtHHtelaaA_vbRUJNIU_va-DnHM58",
        2 to "https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcQDyV390hJ863VWY3la7qYSRaeKnb_ZB4_6RuQ8O15Y6f2erkDZ",
        3 to "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTmI7JsOt5mz20CTE4Ib6cPzl3F4Evo7jjSokS8WlprhQGw1Ytw",
        4 to "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcThXs1VVRuPdLYIAJyqZZbUDOV2gxhCeVI-YjrFHIrS8rO3X3Kt",
        5 to "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSxehlmV82_r7LFnyBqR86Bs3fzmIoyvzQNxj2dtIBTnJd3wtTG",
        6 to "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcR7zftyiFbfJS3NLfolNyWXM4FyFMHVrI-0-uKAC3VFreqgQGR3",
        7 to "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSPuzfsjzv1klH-OW9EPcEWse-pNGXg6fLqdwcqawrwhpR6Wpmj",
        8 to "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcQvfiexSpDCqSXw3QvxeGLc_642uhdT3oS-09cijzF-XmEQD-hk",
        9 to "https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcR4b_PijXTwNC3CEUsBkdjPxBzu6gELZ-ceWz5S4wb2bKvhfkSr",
        10 to "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQjQidgZGLaqBGB-RLcXMZVp1h4u_l88_YRGWEn6aTfaWrLm28B"
    )

    return imageUrls[((habitacionId?.minus(1))?.rem(10) ?: 1) + 1 ] ?: "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcQvfiexSpDCqSXw3QvxeGLc_642uhdT3oS-09cijzF-XmEQD-hk"
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
        Button(onClick = { navHostController.navigate(Destinations.Contacto.route) }, colors = ButtonDefaults.buttonColors()) {
            Text("Más Información", color = Color.White)
        }
    }
}

@Composable
fun EspacioSectionMain(espacios: List<Espacio>, navHostController: NavHostController) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("¡Celebra Con Nosotros!", style = MaterialTheme.typography.headlineSmall, color = Color.White)
        Text("¡Toca Aquí Para Reservar!", style = MaterialTheme.typography.bodySmall, modifier = Modifier.clickable { navHostController.navigate(Destinations.EspaciosScreen.route) }, color = Color.White)
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow {
            items(espacios) { espacio ->
                EspacioCardMain(espacio)
            }
        }
    }
}

@Composable
fun EspacioCardMain(espacio: Espacio) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .width(200.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = getEspacioImageUrl(espacio.id)),
                contentDescription = "Imagen del espacio ${espacio.id}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
            )
            Text(espacio.nombre, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 4.dp), color = Color.Black)
            Text("Precio: ${espacio.precio}€/Hora", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(vertical = 4.dp), color = Color.Black)
        }
    }
}

fun getEspacioImageUrl(espacioId: Int?): String {
    val imageUrls = mapOf(
        1 to "https://www.theimperialbcn.com/wp-content/uploads/2016/05/salon-imperial-hztl.jpg",
        2 to "https://i.pinimg.com/564x/d3/3e/2e/d33e2e2f8eb61f5dbf7a2e5ec3a9c110.jpg",
        3 to "https://assets.hyatt.com/content/dam/hyatt/hyattdam/images/2021/06/08/0511/BCNRB-P0078-Auditorium.jpg/BCNRB-P0078-Auditorium.16x9.jpg?imwidth=1920",
        4 to "https://www.galleryhotel.com/_images/salon/4/4941_salon.jpg",
        5 to "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcSPa1rLGdRFPJXEnmf694Aa5_yWESeVgJTBvWU50TN82q5Jr4mu"
    )

    return imageUrls[espacioId] ?: "https://www.theimperialbcn.com/wp-content/uploads/2016/05/salon-imperial-hztl.jpg"
}
