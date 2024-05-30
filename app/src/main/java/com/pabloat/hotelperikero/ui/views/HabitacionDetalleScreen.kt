package com.pabloat.hotelperikero.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.RoomService
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.navigation.NavHostController
import com.pabloat.hotelperikero.navigation.Destinations
import com.pabloat.hotelperikero.viewmodel.HotelViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitacionDetalleScreen(
    habitacionId: Int?,
    navHostController: NavHostController,
    viewModel: HotelViewModel
) {
    val habitacion = viewModel.getHabitacionById(habitacionId)
    val servicios by viewModel.servicios.collectAsState()
    val userLoggedIn by viewModel.userData.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detalle de la Habitación ${habitacion?.numero_habitacion ?: ""}",
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2A4B8D)
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                habitacion?.let { room ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = room.tipo,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2A4B8D)
                                ),
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = getHabitacionImageUrl(room.id ?: -1)
                                ),
                                contentDescription = "Imagen detallada de la habitación",
                                modifier = Modifier
                                    .height(240.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Box(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF2A4B8D))
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.AttachMoney,
                                        contentDescription = "Precio",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        "Precio: ${room.precio}€/Noche",
                                        style = MaterialTheme.typography.titleMedium.copy(color = Color.White)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            InfoRow(icon = Icons.Default.RoomService, label = "Descripción", value = room.descripcion)
                            Spacer(modifier = Modifier.height(8.dp))
                            InfoRow(icon = Icons.Default.Info, label = "Características", value = room.caracteristicas.replace("_", " "))
                            Spacer(modifier = Modifier.height(8.dp))
                            InfoRow(icon = Icons.Default.People, label = "Capacidad", value = "${room.capacidad} personas")
                            Spacer(modifier = Modifier.height(16.dp))
                            if (room.disponibilidad == 1) {
                                if (userLoggedIn != null) {
                                    val userId = userLoggedIn!!.getInt("id")
                                    Button(
                                        onClick = {
                                            navHostController.navigate("reservation_form/${room.id}/$userId")
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(20.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF2A4B8D),
                                            contentColor = Color.White
                                        )
                                    ) {
                                        Text(
                                            "Reservar Ahora",
                                            modifier = Modifier.padding(vertical = 8.dp),
                                            style = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
                                        )
                                    }
                                } else {
                                    Text(
                                        "Necesitas Iniciar Sesión",
                                        color = Color.Red,
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                            .clickable { navHostController.navigate(Destinations.LoginScreen.route) }
                                            .padding(vertical = 8.dp)
                                    )
                                }
                            } else {
                                Text(
                                    "No disponible",
                                    color = Color.Red,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            }
                        }
                    }
                } ?: run {
                    Text(
                        "Habitación no disponible",
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
            }

            item {
                ServiceSectionMain(servicios = servicios, navHostController = navHostController)
            }
        }
    }
}

@Composable
fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Icon(icon, contentDescription = label, tint = Color(0xFF2A4B8D), modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
