package com.pabloat.hotelperikero.ui.views

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
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
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.pabloat.hotelperikero.navigation.Destinations
import com.pabloat.hotelperikero.viewmodel.HotelViewModel

@OptIn(ExperimentalMaterial3Api::class)
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
                title = {
                    Text(
                        text = espacio?.nombre ?: "Espacio no disponible",
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
                espacio?.let { space ->
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
                                text = space.nombre,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2A4B8D)
                                ),
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = getEspacioImageUrl(space.id ?: -1)
                                ),
                                contentDescription = "Imagen detallada del espacio",
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
                                        "Precio: ${space.precio}€/Hora",
                                        style = MaterialTheme.typography.titleMedium.copy(color = Color.White)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            InfoEspacioRow(icon = Icons.Default.Info, label = "Características", value = space.descripcion.replace("_", " de "))
                            Spacer(modifier = Modifier.height(8.dp))
                            InfoEspacioRow(icon = Icons.Default.People, label = "Capacidad", value = "${space.capacidad_maxima} personas")
                            Spacer(modifier = Modifier.height(16.dp))
                            if (space.disponible == 1) {
                                if (userLoggedIn != null) {
                                    val userId = userLoggedIn!!.getInt("id")
                                    Button(
                                        onClick = {
                                            navHostController.navigate("reservationEspacio_form/${space.id}/$userId")
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
                        "Espacio no disponible",
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            item {
                ServiceEspacioSectionMain(
                    servicios = serviciosEventos,
                    navHostController = navHostController
                )
            }
        }
    }
}

@Composable
fun InfoEspacioRow(icon: ImageVector, label: String, value: String) {
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
