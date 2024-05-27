package com.pabloat.hotelperikero.ui.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
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
import com.pabloat.hotelperikero.R
import com.pabloat.hotelperikero.data.local.entities.Reserva
import com.pabloat.hotelperikero.data.local.entities.Resenia
import com.pabloat.hotelperikero.navigation.Destinations
import com.pabloat.hotelperikero.viewmodel.HotelViewModel
import com.pabloat.hotelperikero.viewmodel.ReseniaViewModel
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navHostController: NavHostController, viewModel: HotelViewModel, reseniaViewModel: ReseniaViewModel) {
    val user by viewModel.userData.collectAsState()
    val pastReservas by viewModel.pastReservas.collectAsState(emptyList())
    val showDialog = remember { mutableStateOf(false) }
    val selectedReserva = remember { mutableStateOf<Reserva?>(null) }
    val showSuccessDialog = remember { mutableStateOf(false) }

    val reseniaCreada by reseniaViewModel.reseniaCreada.collectAsState()

    LaunchedEffect(user) {
        user?.let {
            val userId = it.optInt("id")
            if (userId != 0) {
                viewModel.loadPastReservasByUserId(userId)
            }
        }
    }

    LaunchedEffect(reseniaCreada) {
        reseniaCreada?.let {
            showSuccessDialog.value = true
            selectedReserva.value = null
            reseniaViewModel.resetReseniaCreada()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil de Usuario") },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                            Color.White
                        )
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            user?.let {
                UserProfile(user = it)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { showDialog.value = true },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Ver Reservas Pasadas", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                if (showDialog.value) {
                    PastReservasDialog(
                        pastReservas = pastReservas,
                        onDismiss = { showDialog.value = false },
                        onAddReviewClick = { reserva ->
                            selectedReserva.value = reserva
                            showDialog.value = false
                        }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        // Logout logic here
                        viewModel.logOut()
                        navHostController.navigate(Destinations.LoginScreen.route) {
                            popUpTo(navHostController.graph.startDestinationId) {
                                inclusive = true
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Log out", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                selectedReserva.value?.let { reserva ->
                    AddReviewDialog(
                        reserva = reserva,
                        onDismiss = { selectedReserva.value = null },
                        onSaveReview = { comentario, rating ->
                            val now = LocalDateTime.now()
                                .format(DateTimeFormatter.ISO_DATE_TIME)
                            val nuevaResenia = Resenia(
                                id = null,
                                reserva_id = reserva.habitacion_id,
                                usuario_id = user!!.optInt("id"),
                                calificacion = rating,
                                comentario = comentario,
                                created_at = now,
                                updated_at = now
                            )
                            reseniaViewModel.crearResenia(nuevaResenia)
                        }
                    )
                }
                if (showSuccessDialog.value) {
                    SuccessDialog(onDismiss = { showSuccessDialog.value = false })
                }
            } ?: run {
                Button(
                    onClick = {
                        navHostController.navigate(Destinations.LoginScreen.route)
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Iniciar sesión", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun UserProfile(user: JSONObject) {
    val name = user.optString("name", "Nombre no disponible")
    val email = user.optString("email", "Correo no disponible")
    val phone = user.optString("telefono", "Teléfono no disponible")
    val city = user.optString("ciudad", "Ciudad no disponible")
    val direccion = user.optString("direccion", "Dirección no disponible")
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = R.drawable.user),
                contentDescription = "Imagen de perfil",
                modifier = Modifier
                    .size(128.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            ProfileDetailItem(label = "Correo electrónico", value = email)
            ProfileDetailItem(label = "Teléfono", value = phone)
            ProfileDetailItem(label = "Ciudad", value = city)
            ProfileDetailItem(label = "Dirección", value = direccion)
        }
    }
}

@Composable
fun ProfileDetailItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun PastReservasDialog(pastReservas: List<Reserva>, onDismiss: () -> Unit, onAddReviewClick: (Reserva) -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Reservas Pasadas") },
        text = {
            LazyColumn(modifier = Modifier.height(400.dp)) {
                items(pastReservas) { reserva ->
                    PastReservaCard(reserva, onAddReviewClick)
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}

@Composable
fun PastReservaCard(reserva: Reserva, onAddReviewClick: (Reserva) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = getHabitacionImageUrl(reserva.habitacion_id)),
                contentDescription = "Imagen de la habitación ${reserva.habitacion_id}",
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Habitación: ${reserva.habitacion_id}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Fecha de estancia: ${reserva.check_in} - ${reserva.check_out}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Precio total: ${reserva.precio_total}€",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { onAddReviewClick(reserva) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Añadir Reseña")
            }
        }
    }
}

@Composable
fun AddReviewDialog(reserva: Reserva, onDismiss: () -> Unit, onSaveReview: (String, Int) -> Unit) {
    val rating = remember { mutableStateOf(0) }
    val comentario = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Añadir Reseña") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Calificación")
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    for (i in 1..5) {
                        Icon(
                            imageVector = if (i <= rating.value) Icons.Filled.Star else Icons.Filled.StarBorder,
                            contentDescription = null,
                            tint = if (i <= rating.value) Color.Yellow else Color.Gray,
                            modifier = Modifier
                                .size(32.dp)
                                .padding(4.dp)
                                .clickable { rating.value = i }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = comentario.value,
                    onValueChange = { comentario.value = it },
                    label = { Text("Comentario") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onSaveReview(comentario.value, rating.value)
                onDismiss()
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun SuccessDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Éxito") },
        text = { Text("La reseña se ha insertado correctamente.") },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Aceptar")
            }
        }
    )
}
