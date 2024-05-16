package com.pabloat.hotelperikero.ui.views

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.pabloat.hotelperikero.navigation.Destinations
import com.pabloat.hotelperikero.viewmodel.HotelViewModel

@Composable
fun EspacioDetalleScreen(
    espacioId: Int?,
    navHostController: NavHostController,
    viewModel: HotelViewModel
) {
    Log.d("MainNavigation", "espacioIdScreen: $espacioId")
    val espacio = viewModel.getEspacioById(espacioId)
    val userLoggedIn by viewModel.userData.collectAsState()
    val serviciosEventos by viewModel.serviciosEventos.collectAsState()


    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.Gray,
                title = {
                    Text(
                        text = espacio?.nombre ?: "Espacio no disponible",
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(
                            Icons.Filled.ArrowBack,
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
            espacio?.let { space ->
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = space.nombre,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally), color = Color.White
                )
                Image(
                    painter = rememberAsyncImagePainter(
                        model = getEspacioImageUrl(
                            space.id ?: -1
                        )
                    ),
                    contentDescription = "Imagen detallada del espacio",
                    modifier = Modifier
                        .height(240.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    space.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally), color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Capacidad máxima: ${space.capacidad_maxima} personas",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally), color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Precio: ${space.precio}€/Hora",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (space.disponible == 1) {
                    if (userLoggedIn != null) {
                        val userId = userLoggedIn!!.getInt("id")
                        Button(
                            onClick = {
                                navHostController.navigate("reservationEspacio_form/${space.id}/$userId")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                "Reservar Ahora",
                                modifier = Modifier.padding(vertical = 8.dp),
                                color = Color.White
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
                    "Espacio no disponible",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            Spacer(modifier = Modifier.padding(16.dp))
            ServiceEspacioSectionMain(
                servicios = serviciosEventos,
                navHostController = navHostController
            )
        }
    }
}


