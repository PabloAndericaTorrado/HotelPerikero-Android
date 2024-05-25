package com.pabloat.hotelperikero.ui.views

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.pabloat.hotelperikero.data.local.entities.Espacio
import com.pabloat.hotelperikero.navigation.Destinations
import com.pabloat.hotelperikero.viewmodel.HotelViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@Composable
fun EspaciosScreen(navHostController: NavHostController, mainViewModel: HotelViewModel) {
    val espacios = mainViewModel.espacios.collectAsState().value
    androidx.compose.material.Scaffold(
        topBar = {
            androidx.compose.material.TopAppBar(
                backgroundColor = Color.Gray,
                title = {
                    androidx.compose.material.Text(
                        "¡Nuestros Espacios!",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
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
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxWidth()
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                EspacioList(
                    espacios = espacios,
                    navHostController = navHostController,
                    mainViewModel = mainViewModel
                )
            }
        }
    )
}

@Composable
fun EspacioList(
    espacios: List<Espacio>,
    navHostController: NavHostController,
    mainViewModel: HotelViewModel
) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(espacios) { espacio ->
            EspacioCard(
                espacio = espacio,
                navHostController = navHostController,
                mainViewModel = mainViewModel
            )
        }
    }
}

@Composable
fun EspacioCard(
    espacio: Espacio,
    navHostController: NavHostController,
    mainViewModel: HotelViewModel
) {
    ElevatedCard(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .clickable {
                mainViewModel.selectEspacioId(espacio.id)
                navHostController.navigate(Destinations.EspacioDetalleScreen.route)
            }
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = getEspacioImageUrl(espacio.id)),
                contentDescription = "Imagen del espacio ${espacio.nombre}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                espacio.nombre,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(espacio.descripcion, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(4.dp))
            Text("${espacio.precio}€ por hora", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            if (espacio.disponible == 1) {
                Button(
                    onClick = {
                        mainViewModel.selectEspacioId(espacio.id)
                        navHostController.navigate(Destinations.EspacioDetalleScreen.route)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Reservar", color = Color.White)
                }
            } else {
                Text("No disponible", color = Color.Red)
            }
        }
    }
}