package com.pabloat.hotelperikero.ui.views

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Scaffold
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.pabloat.hotelperikero.navigation.Destinations
import com.pabloat.hotelperikero.viewmodel.HotelViewModel

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
                backgroundColor = Color(0xFF2A4B8D),
                title = {
                    if (habitacion != null) {
                        Text(
                            "Detalle de la Habitación ${habitacion.numero_habitacion}",
                            color = Color.White
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            habitacion?.let { room ->
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = room.tipo,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2A4B8D)
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
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
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Precio: ${room.precio}€/Noche",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF4A90E2)),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(8.dp))
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
                                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                            )
                        }
                    } else {
                        Text(
                            "Necesitas Iniciar Sesión",
                            color = Color.Red,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .clickable { navHostController.navigate(Destinations.LoginScreen.route) }
                        )
                    }
                } else {
                    Text(
                        "No disponible",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            } ?: run {
                Text(
                    "Habitación no disponible",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            ServiceSectionMain(servicios = servicios, navHostController = navHostController)
        }
    }
}
