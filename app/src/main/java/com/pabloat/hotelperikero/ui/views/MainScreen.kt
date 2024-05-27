package com.pabloat.hotelperikero.ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.pabloat.hotelperikero.R
import com.pabloat.hotelperikero.data.local.entities.Espacio
import com.pabloat.hotelperikero.data.local.entities.Habitacion
import com.pabloat.hotelperikero.data.local.entities.Servicio
import com.pabloat.hotelperikero.data.local.entities.ServicioEvento
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
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White
    ) { padding ->
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

@Composable
fun WelcomeSection(navHostController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo1),
            contentDescription = "background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.5f),
                            Color.Transparent
                        )
                    )
                )
        ) {
            Text(
                "Bienvenidos a Hotel Perikero",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = Color.White
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { navHostController.navigate(Destinations.HabitacionesScreen.route) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90E2))
            ) {
                Text(text = "¡Reserva aquí!", style = MaterialTheme.typography.titleLarge.copy(color = Color.White))
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
        Text(
            "¡Nuestras mejores habitaciones!",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontSize = 24.sp,
                color = Color(0xFF2A4B8D),
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "¡Toca aquí para ver reseñas!",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .clickable { navHostController.navigate(Destinations.ReseniasScreen.route) }
                .padding(horizontal = 16.dp),
            color = Color(0xFF4A90E2),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow {
            items(rooms) { room ->
                AnimatedVisibility(
                    visible = true,
                    enter = slideInVertically() + fadeIn(),
                    exit = slideOutVertically() + fadeOut()
                ) {
                    RoomCardMain(room, navHostController, mainViewModel = mainViewModel)
                }
            }
        }
    }
}

@Composable
fun RoomCardMain(room: Habitacion, nav: NavHostController, mainViewModel: HotelViewModel) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .width(300.dp)
            .shadow(8.dp, shape = RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                room.tipo,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF2A4B8D)),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                room.descripcion,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF333333)),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Precio: ${room.precio}€/Noche",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF4A90E2)),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (room.disponibilidad == 1) {
                Button(
                    onClick = {
                        nav.navigate(Destinations.HabitacionDetalleScreen.route)
                        mainViewModel.selectHabitacionId(room.id)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A4B8D))
                ) {
                    Text("Ir a la habitación", style = MaterialTheme.typography.bodyMedium.copy(color = Color.White))
                }
            } else {
                Text("No disponible", color = Color.Red, modifier = Modifier.padding(4.dp))
            }
        }
    }
}

@Composable
fun ServiceSectionMain(servicios: List<Servicio>, navHostController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "¡Todos nuestros servicios!",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontSize = 24.sp,
                color = Color(0xFF2A4B8D),
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "¡Toca aquí para ver más!",
            modifier = Modifier
                .clickable { navHostController.navigate(Destinations.ServiciosScreen.route) }
                .padding(horizontal = 16.dp),
            color = Color(0xFF4A90E2),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
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
            .width(300.dp)
            .shadow(8.dp, shape = RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
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
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                servicio.nombre,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF2A4B8D)),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )
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
        Text(
            "¡Celebra con nosotros!",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontSize = 24.sp,
                color = Color(0xFF2A4B8D),
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "¡Toca aquí para reservar!",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .clickable { navHostController.navigate(Destinations.EspaciosScreen.route) }
                .padding(horizontal = 16.dp),
            color = Color(0xFF4A90E2),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
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
            .width(300.dp)
            .shadow(8.dp, shape = RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                espacio.nombre,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF2A4B8D)),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Precio: ${espacio.precio}€/Hora",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF4A90E2)),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
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
        Text(
            "Contacto",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF2A4B8D)),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            "Teléfono: +123 456 7890",
            style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF333333)),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            "Email: info@perikerohotel.com",
            style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF333333)),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            "Dirección: C. Estébanez Calderón, 10, Marbella",
            style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF333333)),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navHostController.navigate(Destinations.Contacto.route) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90E2))
        ) {
            Text("Más Información", color = Color.White)
        }
    }
}

fun getServiceImageUrl(servicioId: Int?): String {
    val imageUrls = mapOf(
        1 to "https://estaticosgn-cdn.deia.eus/clip/3c84d28d-e825-4262-81cd-ae0dfe0af667_16-9-aspect-ratio_default_0.jpg",
        2 to "https://theobjective.com/wp-content/uploads/2023/07/Surtido-de-panes-en-el-desayuno-de-hotel-1024x576.webp",
        3 to "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcQyyZWVev_nRM_o5PIpT5cB3fzmIoyvzQNxj2dtIBTnJd3wtTG",
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

    return imageUrls[((habitacionId?.minus(1))?.rem(10) ?: 1) + 1] ?: "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcQvfiexSpDCqSXw3QvxeGLc_642uhdT3oS-09cijzF-XmEQD-hk"
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

fun getServiceEventoImageUrl(servicioEventoId: Int?): String {
    val imageUrls = mapOf(
        1 to "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRm3c0r1syUmIcmMzV8qhf2IAfWTABXpnHYbhpp1OlsgLzaYnYY",
        2 to "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcREZe56xJe5VLsfrDSEyXBV4Jn2T8hrrZjUFeg1VxJQclfUqJvY",
        3 to "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcSZ47U2HyP8K1kjS9heZAFqlMtGszRi3P9sEKEvp5WNbotd84ed",
        4 to "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTIWax5kfzHqFRn29t273VInfxUnfNCi-Sq7XtqKeFhYtJFK9ee",
        5 to "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTTt7tOl3foEwKXD6owwZqNEc90lpDY19V6wGPy2BG3i0i_qqDF",
        6 to "https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcTMcB2YAStYXAt5unrMqmHnrA3TZb2t7TT3K9Qzm3jrQdKUsCU5",
        7 to "https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcQ_tVERH2a4bY7ONnFq9-j3EUx1h76ShdeeGG9fgrl1JVnAV8aa",
    )

    return imageUrls[servicioEventoId]
        ?: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRm3c0r1syUmIcmMzV8qhf2IAfWTABXpnHYbhpp1OlsgLzaYnYY"
}

@Composable
fun ServiceEspacioSectionMain(
    servicios: List<ServicioEvento>,
    navHostController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "¡Todos Nuestros Servicios!",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontSize = 24.sp,
                color = Color(0xFF2A4B8D),
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center
        )
        Text(
            "¡Toca Aquí Para Ver Más!",
            modifier = Modifier
                .clickable { navHostController.navigate(Destinations.ServiciosEventosScreen.route) }
                .padding(horizontal = 16.dp),
            color = Color(0xFFD70000),
            textAlign = TextAlign.Center
        )
        LazyRow {
            items(servicios) { servicio ->
                ServiceEspacioCardMain(servicio)
            }
        }
    }
}

@Composable
fun ServiceEspacioCardMain(servicio: ServicioEvento) {
    val painter = rememberAsyncImagePainter(model = getServiceEventoImageUrl(servicio.id))
    Card(
        modifier = Modifier
            .padding(10.dp)
            .width(300.dp)
            .shadow(8.dp, shape = RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
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
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                servicio.nombre,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF2A4B8D)),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
