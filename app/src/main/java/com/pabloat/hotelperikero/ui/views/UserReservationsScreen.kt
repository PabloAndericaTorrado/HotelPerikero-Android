package com.pabloat.hotelperikero.ui.views

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.pabloat.hotelperikero.data.local.entities.Reserva
import com.pabloat.hotelperikero.data.local.entities.ReservaEventos
import com.pabloat.hotelperikero.navigation.Destinations.LoginScreen
import com.pabloat.hotelperikero.viewmodel.HotelViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UserReservationsScreen(
    navHostController: NavHostController,
    viewModel: HotelViewModel
) {
    val userLoggedIn by viewModel.userData.collectAsState()
    val userReservations by viewModel.userReservations.collectAsState()
    val userEventReservations by viewModel.userEventReservations.collectAsState()
    var selectedTab by remember { mutableStateOf(ReservationTab.Rooms) }

    if (userLoggedIn != null) {
        val userId = userLoggedIn!!.getInt("id")
        viewModel.loadUserReservations(userId)
        viewModel.loadUserEventReservations(userId)

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Mis Reservas",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { selectedTab = ReservationTab.Rooms },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedTab == ReservationTab.Rooms) Color(0xFF2A4B8D) else Color.Gray
                        )
                    ) {
                        Text("Habitaciones", color = Color.White)
                    }
                    Button(
                        onClick = { selectedTab = ReservationTab.Events },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedTab == ReservationTab.Events) Color(0xFF2A4B8D) else Color.Gray
                        )
                    ) {
                        Text("Eventos", color = Color.White)
                    }
                }

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    if (selectedTab == ReservationTab.Rooms) {
                        item {
                            Text(
                                "Reservas de Habitaciones",
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        items(userReservations) { reserva ->
                            UserReservationCard(reserva = reserva)
                        }
                    } else {
                        item {
                            Text(
                                "Reservas de Eventos",
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        items(userEventReservations) { reservaEvento ->
                            UserEventReservationCard(reserva = reservaEvento)
                        }
                    }
                }
            }
        }
    } else {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                "Necesitas Iniciar Sesión",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .clickable { navHostController.navigate(LoginScreen.route) }
                    .padding(16.dp),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

enum class ReservationTab {
    Rooms, Events
}

@Composable
fun UserReservationCard(
    reserva: Reserva
) {

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = getHabitacionImageUrl(reserva.habitacion_id)),
                contentDescription = "Imagen de la habitación ${reserva.habitacion_id}",
                modifier = Modifier
                    .fillMaxHeight()
                    .width(150.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Habitación ${reserva.habitacion_id}",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2A4B8D),
                        fontSize = 20.sp
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                DetailRow(Icons.Default.CalendarToday, "Check-In", reserva.check_in, Color(0xFF2A4B8D))
                DetailRow(Icons.Default.CalendarToday, "Check-Out", reserva.check_out, Color(0xFF2A4B8D))
                DetailRow(Icons.Default.People, "Huéspedes",
                    reserva.numero_personas.toString(), Color(0xFF2A4B8D))
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF2A4B8D))
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = "Precio Total: ${reserva.precio_total}€",
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun UserEventReservationCard(
    reserva: ReservaEventos
) {

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = getEspacioImageUrl(reserva.id)),
                contentDescription = "Imagen del evento ${reserva.id}",
                modifier = Modifier
                    .fillMaxHeight()
                    .width(150.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Evento ${reserva.id}",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2A4B8D),
                        fontSize = 20.sp
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                DetailRow(Icons.Default.CalendarToday, "Check-In", reserva.check_in, Color(0xFF2A4B8D))
                DetailRow(Icons.Default.CalendarToday, "Check-Out", reserva.check_out, Color(0xFF2A4B8D))
                DetailRow(Icons.Default.People, "Personas",
                    reserva.cantidad_personas.toString(), Color(0xFF2A4B8D))
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF2A4B8D))
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = "Precio Total: ${reserva.precio_total}€",
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun DetailRow(icon: ImageVector, label: String, value: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "$label: $value",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 4.dp)
        )
    }
}
