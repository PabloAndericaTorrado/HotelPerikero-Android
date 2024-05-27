package com.pabloat.hotelperikero.ui.views

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.pabloat.hotelperikero.data.local.entities.Reserva
import com.pabloat.hotelperikero.data.local.entities.ReservaEventos
import com.pabloat.hotelperikero.navigation.Destinations
import com.pabloat.hotelperikero.viewmodel.HotelViewModel

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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
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
                        containerColor = if (selectedTab == ReservationTab.Rooms) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                ) {
                    Text("Habitaciones", color = Color.White)
                }
                Button(
                    onClick = { selectedTab = ReservationTab.Events },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTab == ReservationTab.Events) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                ) {
                    Text("Eventos", color = Color.White)
                }
            }

            if (selectedTab == ReservationTab.Rooms) {
                Text(
                    "Reservas de Habitaciones",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(userReservations) { reserva ->
                        UserReservationCard(reserva = reserva)
                    }
                }
            } else {
                Text(
                    "Reservas de Eventos",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(userEventReservations) { reservaEvento ->
                        UserEventReservationCard(reserva = reservaEvento)
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
                    .clickable { navHostController.navigate(Destinations.LoginScreen.route) }
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
    var showDialog by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .clickable { showDialog = true },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = getHabitacionImageUrl(reserva.habitacion_id)),
                contentDescription = "Imagen de la habitación ${reserva.habitacion_id}",
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Habitación ${reserva.habitacion_id}",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text("Check-In: ${reserva.check_in}", style = MaterialTheme.typography.bodyLarge)
                Text("Check-Out: ${reserva.check_out}", style = MaterialTheme.typography.bodyLarge)
                Text("Precio Total: ${reserva.precio_total}€", style = MaterialTheme.typography.bodyLarge)
                Text("Huéspedes: ${reserva.numero_personas}", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }

    if (showDialog) {
        ReservationDetailDialog(
            title = "Detalles de la reserva",
            imageUrl = getHabitacionImageUrl(reserva.habitacion_id),
            details = listOf(
                "Habitación ID" to reserva.habitacion_id.toString(),
                "Check-In" to reserva.check_in,
                "Check-Out" to reserva.check_out,
                "Precio Total" to "${reserva.precio_total} €",
                "Número de huéspedes" to reserva.numero_personas.toString()
            ),
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
fun UserEventReservationCard(
    reserva: ReservaEventos
) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .clickable { showDialog = true },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = getEspacioImageUrl(reserva.id)),
                contentDescription = "Imagen del evento ${reserva.id}",
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Evento ${reserva.id}",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text("Check-In: ${reserva.check_in}", style = MaterialTheme.typography.bodyLarge)
                Text("Check-Out: ${reserva.check_out}", style = MaterialTheme.typography.bodyLarge)
                Text("Precio Total: ${reserva.precio_total}€", style = MaterialTheme.typography.bodyLarge)
                Text("Personas: ${reserva.cantidad_personas}", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }

    if (showDialog) {
        ReservationDetailDialog(
            title = "Detalles de la reserva de evento",
            imageUrl = getEspacioImageUrl(reserva.id),
            details = listOf(
                "Reserva de Evento ID" to reserva.id.toString(),
                "Check-In" to reserva.check_in,
                "Check-Out" to reserva.check_out,
                "Precio Total" to "${reserva.precio_total} €",
                "Cantidad de Personas" to reserva.cantidad_personas.toString()
            ),
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
fun ReservationDetailDialog(
    title: String,
    details: List<Pair<String, String>>,
    imageUrl: String? = null,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(title, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)) },
        text = {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                imageUrl?.let {
                    Image(
                        painter = rememberAsyncImagePainter(model = it),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(180.dp)
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .clip(MaterialTheme.shapes.medium)
                    )
                }
                details.forEach { detail ->
                    DetailRow(label = detail.first, value = detail.second)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onDismiss() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Cerrar")
            }
        }
    )
}

@Composable
fun DetailRow(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 2.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
    }
}