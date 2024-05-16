package com.pabloat.hotelperikero.ui.views

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pabloat.hotelperikero.data.local.entities.Reserva
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
    if (userLoggedIn != null) {
        val userId = userLoggedIn!!.getInt("id")
        viewModel.loadUserReservations(userId)

        Scaffold(

        ) {
            Column {
                UserReservationList(
                    userReservations = userReservations,
                    navHostController = navHostController
                )
            }
        }
    } else {
        Text(
            "Necesitas Iniciar Sesión",
            color = Color.Red,
            modifier = Modifier
                .clickable { navHostController.navigate(Destinations.LoginScreen.route) }
        )
    }
}

@Composable
fun UserReservationList(
    userReservations: List<Reserva>,
    navHostController: NavHostController
) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(userReservations) { reserva ->
            UserReservationCard(reserva = reserva, navHostController = navHostController)
        }
    }
}

@Composable
fun UserReservationCard(
    reserva: Reserva,
    navHostController: NavHostController
) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .clickable {
                showDialog = true
            }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                "Habitación ID: ${reserva.habitacion_id}",
                style = MaterialTheme.typography.titleMedium
            )
            Text("Check-In: ${reserva.check_in}", style = MaterialTheme.typography.bodyMedium)
            Text("Check-Out: ${reserva.check_out}", style = MaterialTheme.typography.bodyMedium)
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Detalles de la reserva") },
            text = {
                Column {
                    Text("Habitación ID: ${reserva.habitacion_id}")
                    Text("Check-In: ${reserva.check_in}")
                    Text("Check-Out: ${reserva.check_out}")
                    Text("Precio: ${reserva.precio_total}")
                    Text("Número de huespedes: ${reserva.numero_personas}")
                }
            },
            confirmButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Cerrar")
                }
            }
        )
    }
}
