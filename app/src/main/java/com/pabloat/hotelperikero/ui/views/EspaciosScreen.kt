package com.pabloat.hotelperikero.ui.views

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.pabloat.hotelperikero.data.local.entities.Espacio
import com.pabloat.hotelperikero.navigation.Destinations
import com.pabloat.hotelperikero.viewmodel.HotelViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@Composable
fun EspaciosScreen(navHostController: NavHostController, mainViewModel: HotelViewModel) {
    val espacios = mainViewModel.espacios.collectAsState().value
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.Gray,
                title = {
                    Text(
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
            Column(modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
                .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
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
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = getEspacioImageUrl(espacio.id)), // Implementar esta función para obtener recursos de imagen
                contentDescription = "Imagen del espacio ${espacio.nombre}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
            )
            Text(espacio.nombre, style = MaterialTheme.typography.bodyLarge)
            Text(espacio.descripcion, style = MaterialTheme.typography.bodySmall)
            Text("${espacio.precio}€ por hora", style = MaterialTheme.typography.bodySmall)
            if (espacio.disponible == 1) {
                Button(onClick = {
                    mainViewModel.selectEspacioId(espacio.id);
                    navHostController.navigate(Destinations.EspacioDetalleScreen.route);
                    Log.d("MainNavigation", "Espacio seleccionado: ${espacio.id}")

                }) {
                    Text("Reservar")
                }
            } else {
                Text("No disponible", color = Color.Red)
            }
        }
    }
}
