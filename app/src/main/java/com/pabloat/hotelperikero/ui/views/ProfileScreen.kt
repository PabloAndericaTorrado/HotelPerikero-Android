package com.pabloat.hotelperikero.ui.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.pabloat.hotelperikero.R
import com.pabloat.hotelperikero.data.local.entities.Resenia
import com.pabloat.hotelperikero.data.local.entities.Reserva
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
                title = { Text("Perfil de Usuario", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2A4B8D))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    color = Color.White
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            user?.let {
                UserProfile(user = it)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { showDialog.value = true },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A4B8D), contentColor = Color.White)
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
                        viewModel.logOut()
                        navHostController.navigate(Destinations.LoginScreen.route) {
                            popUpTo(navHostController.graph.startDestinationId) {
                                inclusive = true
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Color.White)
                ) {
                    Text("Log out", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                selectedReserva.value?.let { reserva ->
                    AddReviewDialog(
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
                Column (
                    modifier = Modifier.fillMaxSize().align(Alignment.CenterHorizontally),
                    Arrangement.Center
                ){
                    Button(
                        onClick = {
                            navHostController.navigate(Destinations.LoginScreen.route)
                        },
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2A4B8D),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Iniciar sesión", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
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
            containerColor = Color(0xFFF5F5F5)
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
                color = Color(0xFF2A4B8D)
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
            color = Color(0xFF2A4B8D)
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF333333)
        )
    }
}

@Composable
fun PastReservasDialog(pastReservas: List<Reserva>, onDismiss: () -> Unit, onAddReviewClick: (Reserva) -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = { Text("Reservas Pasadas") },
        text = {
            LazyColumn(modifier = Modifier.height(400.dp)) {
                items(pastReservas) { reserva ->
                    PastReservaCard(reserva, onAddReviewClick)
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A4B8D), contentColor = Color.White)) {
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
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
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
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2A4B8D)
            )
            Text(
                text = "Fecha de estancia: ${reserva.check_in} - ${reserva.check_out}",
                fontSize = 14.sp,
                color = Color(0xFF333333)
            )
            Text(
                text = "Precio total: ${reserva.precio_total}€",
                fontSize = 14.sp,
                color = Color(0xFF333333)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { onAddReviewClick(reserva) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A4B8D), contentColor = Color.White)
            ) {
                Text("Añadir Reseña")
            }
        }
    }
}

@Composable
fun AddReviewDialog(onDismiss: () -> Unit, onSaveReview: (String, Int) -> Unit) {
    val rating = remember { mutableIntStateOf(0) }
    val comentario = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = { Text("Añadir Reseña") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Calificación")
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    for (i in 1..5) {
                        Icon(
                            imageVector = if (i <= rating.intValue) Icons.Filled.Star else Icons.Filled.StarBorder,
                            contentDescription = null,
                            tint = if (i <= rating.intValue) Color.Yellow else Color.Gray,
                            modifier = Modifier
                                .size(32.dp)
                                .padding(4.dp)
                                .clickable { rating.intValue = i }
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
                onSaveReview(comentario.value, rating.intValue)
                onDismiss()
            }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A4B8D), contentColor = Color.White)) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Color.White)) {
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
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A4B8D), contentColor = Color.White)) {
                Text("Aceptar")
            }
        }
    )
}
